import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javax.swing.JOptionPane;

import kr.ac.konkuk.ccslab.cm.entity.CMGroup;
import kr.ac.konkuk.ccslab.cm.entity.CMGroupInfo;
import kr.ac.konkuk.ccslab.cm.entity.CMList;
import kr.ac.konkuk.ccslab.cm.entity.CMMember;
import kr.ac.konkuk.ccslab.cm.entity.CMMessage;
import kr.ac.konkuk.ccslab.cm.entity.CMPosition;
import kr.ac.konkuk.ccslab.cm.entity.CMRecvFileInfo;
import kr.ac.konkuk.ccslab.cm.entity.CMSendFileInfo;
import kr.ac.konkuk.ccslab.cm.entity.CMServer;
import kr.ac.konkuk.ccslab.cm.entity.CMSession;
import kr.ac.konkuk.ccslab.cm.entity.CMSessionInfo;
import kr.ac.konkuk.ccslab.cm.entity.CMUser;
import kr.ac.konkuk.ccslab.cm.event.CMBlockingEventQueue;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMFileEvent;
import kr.ac.konkuk.ccslab.cm.event.CMInterestEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.CMUserEvent;
import kr.ac.konkuk.ccslab.cm.info.CMCommInfo;
import kr.ac.konkuk.ccslab.cm.info.CMConfigurationInfo;
import kr.ac.konkuk.ccslab.cm.info.CMFileTransferInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInteractionInfo;
import kr.ac.konkuk.ccslab.cm.manager.CMConfigurator;
import kr.ac.konkuk.ccslab.cm.manager.CMFileTransferManager;
import kr.ac.konkuk.ccslab.cm.manager.CMMqttManager;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;
import kr.ac.konkuk.ccslab.cm.util.CMUtil;

// 구현 볼륨 및, 기간상의 문제로, 많은 부분은 스케일러블을 고려하지 않고, 하드코딩 되었음.
// 하드코딩된 부분은 주석으로 표시했기 때문에, 경우에 따라, 스케일러블한 로직으로 고쳐주면 된다.


/*
 * 동기화 관련
 * 
 * 동기화 문제를 쉽게 해결하기 위해서, 인터럽트라는 개념을 새로 정의하였다.
 * 
 * 이 클래스 입장에서, 스레드는 총 2가지라고 느낄 수 있다. 
 * 하나는 게임 스레드이고
 * 나머지 하나는, main함수 로직이다(CM 이벤트 핸들러)
 * 
 * 이때, 인터럽트는, main함수로, CM핸들링 요청이 들어옴을 의미한다.
 * 외부에서 이 클래스의 값을 바꾸는 방식은 setter뿐인데, 이 getter, setter를 인터럽트라고 정의한다.
 * 인터럽트를 disable하면, 이 setter가 실행되더라도, 이 클래스에 영향을 줄 수 없다.
 * 
 * 따라서, 인터럽트가 disable된 상태에서는, 자식 스레드만 살아있는 것 처럼 생각하고 프로그래밍 해도 상관이 없다.
 * setter는 항상, 인터럽트 활성/비활성 함수와의 동기화를 고려해야 한다.
 * 
 * */

// 전반적인 게임의 모든 로직을 처리하는 클래스
public class GameLogicController implements Runnable{
	// Field
	private GameUser[] users = new GameUser[5]; // 이 부분은 하드코딩 되어있습니다. 이에 따라서, 유저 수의 스케일러블은 고려대상이 아닙니다.
	private GameCMConnector connector;
	private int alive = 5; // 현재 생존 상태인 유저의 수
	private volatile int voteCount; // 이 변수는 캐쉬에 적재되면 안된다. 캐쉬 동기화 이슈를 피하기 위함이다.
	private volatile boolean disabledInturrupt = false; // 동기화를 위해, 메모리에 적제한다. 동기화를 위한 state-variable이다.
	private int targetUser = -1;
	private int mafia, police, doctor;
	private UserSelectedBehaviour currentAction; // 유저를 선택 했을때의 request 핸들러
	private boolean exitCondition = false; // 게임 종료 조건이 달성되었는가?
	private GameSessionCallback collectResource; //
	private int gameExitStatus = -1; // 게임 종료 이유
	private int roomid = 0;
	
	private static final int CHAT_VOTE_TIME = 60; // 초기 투표 시간
	private static final int PROS_CONS_TIME = 20; // 찬반 투표 시간
	private static final int NIGHT_JOB_TIME = 20; // 밤 시간에 각 직업이 선택하는 시간
	
	
	private static final int CIVIL_WIN = 0; // 시민 승
	private static final int MAFIA_WIN = 1; // 마피아 승
	private static final int SOMEONE_EXIT = 2; // 누군가 나가서, 게임 진행이 불가능
	
	
	// Constructor
	public GameLogicController(GameCMConnector cm, GameSessionCallback callback, int roomID) {
		connector = cm;
		collectResource = callback;
		roomid = roomID;
		for(int i = 0; i < 5; i ++) {
			users[i] = new GameUser(i);
			users[i].userName = connector.currentUsers.get(i);
		}
		connector.broadcastGameStart(getUserList(0));
	}
	
	
	
	//Methods
	
	
	// 방에 참여한 유저들에게 직업을 할당한다.
	private void assignJob() {
		int[] jobs;
		Randomizer randomizer = new Randomizer();
		// 역시 유저 처리 부분은 하드 코딩으로 대체한다.(시민 : 2, 마피아 : 1, 경찰 : 1, 의사 : 1)
		randomizer.addNumber(0);
		randomizer.addNumber(0);
		randomizer.addNumber(1);
		randomizer.addNumber(2);
		randomizer.addNumber(3);
		jobs = randomizer.getNums();
		
		// 무작위로 할당받은 직업을 배정
		for(int i = 0; i < users.length; i ++) {
			users[i].job = jobs[i];
			connector.castJobInfo(users[i].userName, users[i].job); // 각 유저에게 해당 유저의 직업을 방송
			switch(i) {
			case 1 :
				mafia = i;
				break;
			case 2 : 
				police = i;
				break;
			case 3:
				doctor = i;
				break;
			default:
			}
		}
		
	}
	
	// 방에 참여한 유저들의 상태를 확인한다.
	private void initForNextTurn() {
		for(int i = 0; i < users.length; i ++) {
			users[i].cleanState();
		}
		targetUser = -1;
	}
	
	// 동기화 함수, CM인터럽트를 무시한다.
	private synchronized void disableInturrupt() {
		disabledInturrupt = true;
	}
	
	// 동기화 함수, CM인터럽트를 받아들인다.
	private synchronized void enableInturrupt() {
		disabledInturrupt = false;
	}
	
	
	// 시민 투표를 시작하는 함수
	private void civilVotePrepare() {
		connector.controllChatFunctionAll(true);
		connector.controllUserSelectFunctionAll(true);
	}
	
	
	// 시민 투표를 종료하는 함수
	private void civilVoteClose() {
		connector.controllUserSelectFunctionAll(false);
	}
	
	// 시민 투표 결과를 집계하는 함수
	private void civilVoteResult() {
		int who, score;
		score = 0;
		who = -1; // 아무도 투표를 하지 않았다면, 찬반 투표를 생략
		// 각 유저를 순회하며, 최다 득표를 한 유저를 탐색
		for(int i = 0 ; i < users.length; i ++) {
			if(users[i].voteCount > score) {
				score = users[i].voteCount;
				who = i;
			}
		}
		this.targetUser = who;
	}
	
	
	// 시민 투표를 처리하는 함수
	private void civilVoteProcess() throws InterruptedException {
		connector.broadcastGameStage(0, getUserList(1));
		civilVotePrepare();
		enableInturrupt();
		Thread.sleep(1000 * CHAT_VOTE_TIME);
		disableInturrupt();
		civilVoteClose();
		civilVoteResult();
	}
	
	
	// 찬반 투표를 시작하는 함수
	private void civilProsConsPrepare() {
		connector.controllVoteFunctionAll(true, users[targetUser].userName);
	}
	
	
	// 찬반 투표를 종료하는 함수
	private void civilProsConsClose() {
		connector.controllVoteFunctionAll(false, "END");
		connector.controllChatFunctionAll(false);
	}
	
	// 찬반 투표 결과를 집계하는 함수
	private void civilProsConsResult() {
		if(voteCount > alive / 2) {
			killPlayer(targetUser);
		}
	}
	
	// 찬반 투표를 진행하는 함수
	private void civilProsConsProcess() throws InterruptedException {
		if(targetUser == -1){
			return;
		}
		civilProsConsPrepare();
		enableInturrupt();
		Thread.sleep(1000 * PROS_CONS_TIME);
		disableInturrupt();
		civilProsConsClose();
		civilProsConsResult();
	}
	
	
	private int checkAlivePlayer() {
		int ret = 0;
		for(int i = 0; i < users.length; i ++) {
			if(users[i].alive && users[i].job != 1) {
				ret ++;
			}
		}
		return ret;
	}
	
	// 해당 id의 플레이어를 사망 처리하는 함수
	private void killPlayer(int id) {
		if(!users[id].isProtected) {
			users[id].alive = false;
			connector.markUser(id, false);
			connector.controllChatFunction(users[id].userName, false);
			connector.broadcastUserDie(users[id].userName, null);
		}
		if(id == mafia) {
			gameExitStatus = CIVIL_WIN;
			exitCondition = true;
			connector.broadcastGameEnd(0);
		}
		if(checkAlivePlayer() < 2) {
			gameExitStatus = MAFIA_WIN;
			exitCondition = true;
			connector.broadcastGameEnd(1);
		}
	}
	
	private void nightAction() throws InterruptedException{
		connector.broadcastGameStage(1, getUserList(1));
		doMafiaJob();
		doPoliceJob();
		doDoctorJob();
		civilVoteResult();
		if(targetUser != -1) {
			if(!users[targetUser].isProtected) {
				killPlayer(targetUser);
			}
		}
	}
	
	private void doMafiaJob() throws InterruptedException{
		connector.broadcastGameStage(2, getUserList(1));
		connector.controllUserSelectFunction(users[mafia].userName, true);
		currentAction = new MafiaSelectBehaviour();
		enableInturrupt();
		Thread.sleep(1000 * NIGHT_JOB_TIME);
		disableInturrupt();
		connector.controllUserSelectFunction(users[mafia].userName, false);
	}
	
	private void doDoctorJob() throws InterruptedException{
		connector.broadcastGameStage(3, getUserList(1));
		connector.controllUserSelectFunction(users[doctor].userName, true);
		currentAction = new DoctorSelectBehaviour();
		enableInturrupt();
		Thread.sleep(1000 * NIGHT_JOB_TIME);
		disableInturrupt();
		connector.controllUserSelectFunction(users[doctor].userName, false);
	}
	
	private void doPoliceJob() throws InterruptedException{
		connector.broadcastGameStage(4, getUserList(1));
		connector.controllUserSelectFunction(users[police].userName, true);
		currentAction = new PoliceSelectBehaviour();
		enableInturrupt();
		Thread.sleep(1000 * NIGHT_JOB_TIME);
		disableInturrupt();
		connector.controllUserSelectFunction(users[police].userName, false);
	}
	
	
	private int tryFindUser(String user) {
		for(int i = 0; i < users.length; i ++) {
			if(users[i].userName.equals(user)) {
				return i;
			}
		}
		return -1;
	}
	
	public synchronized void adjustVoteCount(String user, boolean pros) {
		if(!disabledInturrupt) {
			int subject = tryFindUser(user);
			if(subject == -1) {
				return;
			}
			if(!users[subject].isVoted) {
				users[subject].isVoted = true;
				if(pros) {
					voteCount ++;
				}
			}
		}
	}
	
	public synchronized void processUserSelected(String who, String target) {
		if(!disabledInturrupt) {
			currentAction.whenUserSelected(tryFindUser(target), tryFindUser(who), users, connector);
		}
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		assignJob();
		try {
			while (!exitCondition) {
				civilVoteProcess();
				if(exitCondition) {continue;}
				
				civilProsConsProcess();
				if(exitCondition) {continue;}
				
				nightAction();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		collectResource.requestCollectGameThread(-1, gameExitStatus);
		
	}
	
	private String getUserList(int type) {
		String ret = "";
		switch(type) {
		case 0:
			for(int i = 0; i < 5; i ++) {
				ret += users[i].userName;
				ret += "/";
			}
			return ret;
		case 1:
			for(int i = 0; i < 5; i ++) {
				if(users[i].alive) {
					ret += users[i].userName;
					ret += "/";
				}

			}
			return ret;
		}
		return ret;
	}
}
