import java.util.Timer;
import java.util.TimerTask;
import kr.ac.konkuk.ccslab.cm.stub.*;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameController extends Thread{
	static int count = 1;
	private int day = 0;// 낮 - 0, 밤 - 1
//	CMClientStub clientStub;
//	private ClientController clientController;
//	private ClientControllerEventHandler clientHandler = new ClientControllerEventHandler( , clientController);// stub 占쎈퓠 �맱占� ?

	public GameController() {
		
	}
	
	public void sendDummyEvent(String opcode, String msg) {
		System.out.println("====== DummyEvent send to default server");
		CMDummyEvent due = new CMDummyEvent();
		due.setSender(CMClientStub.getInstance().getCMInfo().getInteractionInfo().getMyself().getName());
		due.setDummyInfo(opcode+"|"+msg);
		CMClientStub.getInstance().send(due, "SERVER");
		System.out.println(due.getDummyInfo());
	}
	
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
		if(day == 0) {
			FrameController.getInstance().room_frame.sendMsg("admin","day");
		}
		else {
			FrameController.getInstance().room_frame.sendMsg("admin", "night");
		}	
	}
	
	public String makeMsg(String args) {
		return UserController.getInstance().getRoomId() + "|" + UserController.getInstance().getId() + "|" + args;
	}
	
	//GameStart
		//assign Job..
		//opcode 1로 받아온 param string으로 직업을 set해 (server -> client)
		
		//opcode 22 아침 (server -> client)
		//반복
		//opcode 3 토론 시작.. (상호작용 )
		//opcode 9 투표 시작.. (sever ->client) // opcode2 투표 누구할지 (client -> server) // 투표시 아무도 선택 안하는 경
		//opcode 10 투표 종료 (server -> client)
		//opcode 21 투표에서 표를 가장 많이 받은 사람 (server->client)
		//opcode 14(찬) 15(반) 찬반투표 시작.. (client -> server)
		//opcode 8 사망자가 있었을때 사망자를 알리는 통신 (Server-> client)
		//opcode 23 밤 (server -> client)
		//opcode 24 마피아 살인  -> opcode 2 누구 죽일지 선택 (client -> server)
		//opcode 25 경찰 확인 -> opcode 2  누구 볼지 (client -> server)
		//opcode 26 의사 누구 살릴지 -> opcode2 누구 살릴지 (client -> server)
		//아침
		//opcode 8 밤에 사망자가 있으면 알리는 통신 .. 없으면 없다고 말해
		// if 승리를 했다면 승리를 알리는 통신 opcode 11 시민 승리 opcode 12 마피아 승리  opcode 13 게임진행불
				//게임 종료 시 opcode 16
	
	public void gameStart() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				if(count <= 5){ //count값이 5보다 작거나 같을때까지 수행
					System.out.println("[카운트다운 : "+count+"]");
					count++; //실행횟수 증가 
				}
				else{
					timer.cancel(); //타이머 종료
					count = 1;
					System.out.println("[카운트다운 : 종료]");
				}
			}
		};
		timer.schedule(task, 1000, 1000);//task 실행 , 1초마다 반복
	//방에서 인원이 꽉 찼다고 받아오기
	//-> 그때부터 카운트 (카운트를 서버에서 할지 클라이언트에서 할지)
	//게임시작 -> opcode 0
	}
	
	
	public void setUserRole(String parm) {
		UserController.getInstance().setUserRole(parm);
	}
	
	public void voteUser(String parm) {
		HashMap<String, Boolean>hm = new HashMap<>();
		
		//獄쏆룇釉섓옙占�
		String[] aliveUserList = parm.split("/");
		//ui
		
		for(int i = 0; i < aliveUserList.length; i++) {
			hm.put(aliveUserList[i], true);
		}
		FrameController.getInstance().room_frame.dl = new Choose_Dialog(FrameController.getInstance().room_frame, "Vote", hm);
		
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				if(count <= 60){ //count값이 60보다 작거나 같을때까지 수행
					System.out.println("[카운트다운 : "+count+"]");
					count++; //실행횟수 증가 
				}
				else{
					timer.cancel(); //타이머 종료
					count = 1;
					System.out.println("[카운트다운 : 종료]");
				}
			}
		};
		timer.schedule(task, 1000, 1000);//task 실행 , 1초마다 반복
//	//방에서 인원이 꽉 찼다고 받아오기
//	//-> 그때부터 카운트 (카운트를 서버에서 할지 클라이언트에서 할지)
//	//게임시작 -> opcode 0
		// String selectUser 占쎄퐨占쎄문 獄쏆룇占썲쳞占� 揶쏉옙占쎌죬占쏙옙占쎄퐣
		String selectUser = " ";// 占쎄퐨占쎄문獄쏆룇占퐄d
		String msg = makeMsg(selectUser);
		sendDummyEvent("2", msg);
	}
	//筌≪럩�뱽 占쎌뱽占쎌뒭占쎈뮉 占쎈맙占쎈땾�몴占� 筌띾슢諭얏�⑨옙 占쎌뱽占쎈선筌욑옙 筌≪럩�뱽 占쎈선占쎈섯野껓옙 占쎈뼍占쎈툡..
	public void showMostSelectedUser(String userid) {
		//
	}
	
	public void chatPermit() {
		
	}
	/*
	public void voteProsCons() {
		int num = 0;//筌⊥됱뺘 占쎄퐨占쎄문 占쎈연疫꿸퀣肉� 占쏙옙占쎌삢 
		String msg = makeMsg("");
		if(num == 0) {
			sendDummyEvent("6", msg);
		}
		else if(num == 1) {
			sendDummyEvent("7", msg);
		}
	}
	*/
	public void broadcastDeadUser(String userId) {
		if(userId == UserController.getInstance().getId()) {
			UserController.getInstance().setIsDead("0");
			FrameController.getInstance().room_frame.sendMsg("Host", userId+" is Dead");
		}
		else {
			FrameController.getInstance().room_frame.sendMsg("Host", userId+" is Dead");
		}
		//죽으면 채팅창에 show
	}
	
	
	public void mafiaTurn(String parm) {
		if(UserController.getInstance().getUserRole() == "2") {
			HashMap<String, Boolean>hm = new HashMap<>();
			
			//MAFIA 죽일사람 리스트
			String[] aliveUserList = parm.split("/");
			//ui
			
			for(int i = 0; i < aliveUserList.length; i++) {
				hm.put(aliveUserList[i], true);
			}
			FrameController.getInstance().room_frame.dl = new Choose_Dialog(FrameController.getInstance().room_frame, "Vote", hm);
			
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				public void run() {
					if(count <= 60){ //count값이 60보다 작거나 같을때까지 수행
						System.out.println("[카운트다운 : "+count+"]");
						count++; //실행횟수 증가 
					}
					else{
						timer.cancel(); //타이머 종료
						count = 1;
						System.out.println("[카운트다운 : 종료]");
					}
				}
			};
			timer.schedule(task, 1000, 1000);//task 실행 , 1초마다 반복
		}
	}
	
	public void policeTurn(String parm) {
		//경찰일때
		if(UserController.getInstance().getUserRole() == "3") {
			HashMap<String, Boolean>hm = new HashMap<>();
			
			//경찰 볼사람 리스트
			String[] aliveUserList = parm.split("/");
			//ui
			
			for(int i = 0; i < aliveUserList.length; i++) {
				hm.put(aliveUserList[i], true);
			}
			FrameController.getInstance().room_frame.dl = new Choose_Dialog(FrameController.getInstance().room_frame, "Vote", hm);
			
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				public void run() {
					if(count <= 20){ //count값이 60보다 작거나 같을때까지 수행
						System.out.println("[카운트다운 : "+count+"]");
						count++; //실행횟수 증가 
					}
					else{
						timer.cancel(); //타이머 종료
						count = 1;
						System.out.println("[카운트다운 : 종료]");
					}
				}
			};
			timer.schedule(task, 1000, 1000);//task 실행 , 1초마다 반복
		}
	}
	
	public void doctorTurn(String parm) {
		if(UserController.getInstance().getUserRole() == "4") {
			HashMap<String, Boolean>hm = new HashMap<>();
			
			//의사 살릴사람 리스트
			String[] aliveUserList = parm.split("/");
			//ui
			
			for(int i = 0; i < aliveUserList.length; i++) {
				hm.put(aliveUserList[i], true);
			}
			FrameController.getInstance().room_frame.dl = new Choose_Dialog(FrameController.getInstance().room_frame, "Vote", hm);
			
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				public void run() {
					if(count <= 20){ //count값이 60보다 작거나 같을때까지 수행
						System.out.println("[카운트다운 : "+count+"]");
						count++; //실행횟수 증가 
					}
					else{
						timer.cancel(); //타이머 종료
						count = 1;
						System.out.println("[카운트다운 : 종료]");
					}
				}
			};
			timer.schedule(task, 1000, 1000);//task 실행 , 1초마다 반복
		}
	}
	
	public void mafiaWin() {
		if(UserController.getInstance().getUserRole() == "2") {
			FrameController.getInstance().room_frame.victory();
		}
		else {
			FrameController.getInstance().room_frame.defeat();
		}
		
		UserController.getInstance().exitRoom();
	}
	
	public void citizenWin() {
		if(UserController.getInstance().getUserRole() != "2") {
			FrameController.getInstance().room_frame.victory();
		}
		else {
			FrameController.getInstance().room_frame.defeat();
		}
		UserController.getInstance().exitRoom();
	}

}
