package Client;

import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.List;

public class GameController extends Thread{
	static int count = 1;
	private int day = 0;// 낮 - 0, 밤 - 1
	private ClientController clientController = new ClientController();
//	private ClientControllerEventHandler clientHandler = new ClientControllerEventHandler( , clientController);// stub 에 뭘 ?

	public GameController() {
		
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
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
	
	public void gameStart() {// 방에 사람 다 들어오면 -> 서버에서는 시작 조건이 완료된거 알려주면 클라이언트에서 5초 
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
		//받아와
		String[] aliveUserList = parm.split("/");
		//ui
		// String selectUser 선택 받은거 가져와서
		String selectUser = " ";// 선택받은id
		String msg = makeMsg(selectUser);
		clientController.sendDummyEvent("2", msg);
	}
	
	public void showMostSelectedUser(String userid) {
		//
	}
	
	public void chatPermit() {
		
	}
	
	public void voteProsCons() {
		int num = 0;//찬반 선택 여기에 저장 
		String msg = makeMsg("");
		if(num == 0) {
			clientController.sendDummyEvent("6", msg);
		}
		else if(num == 1) {
			clientController.sendDummyEvent("7", msg);
		}
	}
	
	public void broadcastDeadUser(String userId) {
		if(userId == UserController.getInstance().getId()) {
			UserController.getInstance().setIsDead("0");
		}
		//죽었다 ui show
	}
	
	
	public void mafiaTurn(String userId) {
		if(userId != UserController.getInstance().getId()) {
			//마피아 아닌 유저한테 ui 처리 
		}
		else {
			
		}
	}
	
	public void policeTurn(String userId) {
		if(userId != UserController.getInstance().getId()) {
			//경찰 아닌 유저한테 ui처
		}
	}
	
	public void doctorTurn(String userId) {
		if(userId != UserController.getInstance().getId()) {
			//의사 아닌 유저한테 ui처
		}
	}
	
	public void mafiaWin() {
		//ui 처리
		UserController.getInstance().exitRoom();
	}
	
	public void citizenWin() {
		//ui 처리
		UserController.getInstance().exitRoom();
	}

}
