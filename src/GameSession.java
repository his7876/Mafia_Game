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

public class GameSession{
	private GameSessionCallback callback;
	private GameCMConnector connector;
	private int roomID;
	private Thread thread;
	private GameLogicController gameLogic;
	
	public GameSession(GameSessionCallback callback, int id) {
		this.callback = callback;
		connector = new GameCMConnector();
		roomID = id;
	}
	
	public boolean tryAddUser(String user) {
		if(canAddUser()) {
			connector.tryAddUser(user);
			ServerLogger.printLog("[방 :" + roomID + "] 유저 참여함 : " + ", 현재 인원수 : " + connector.currentUsers.size());
			if(connector.currentUsers.size() == 5) {
				gameLogic = new GameLogicController(connector,new GameSessionCallback() {					
					@Override
					public void requestCollectGameThread(int roomid, int status) {
						// TODO Auto-generated method stub
						callback.requestCollectGameThread(roomid, status);
						connector.currentUsers = new ArrayList<String>();
					}
				}, roomID);
				thread = new Thread(gameLogic);
				thread.start();
				ServerLogger.printLog("[방 :" + roomID + "] : 게임 시작 조건 달성, 게임 시작" );
			}
		}
		return false;
	}
	
	public boolean canAddUser() {
		return connector.currentUsers.size() < 5 ? true : false;
	}
	
	public boolean selectUser(String subject, String target) {
		gameLogic.processUserSelected(subject, target);
		return true;
	}
	
	public boolean fowardChatEvent(String subject, String target) {
		connector.broadcastChatData("[" + subject + "] : " + target);
		return true;
	}
	
	public boolean voteProsCons(String subject, boolean pros) {
		gameLogic.adjustVoteCount(subject, pros);
		return true;
	}
	
	public List<String> getUsers(){
		return connector.currentUsers;
	}
}
