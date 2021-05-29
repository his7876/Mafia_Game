package Client;

import java.util.Iterator;

import kr.ac.konkuk.ccslab.cm.entity.CMSessionInfo;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

public class ClientControllerEventHandler implements CMAppEventHandler {

	private CMClientStub m_clientStub;
	private ClientController m_client;
	private GameController gameController;
	private RoomController roomController;
	
	//Dummy info type 
	public class DummyType{
		
		//0. 게임 시작 
		public static final String opcode0 = "0";
		//1. 직업 관련 
		public static final String opcode1 = "1";
		//2. 유저 선택
		public static final String opcode2 = "2";
		//3. 채팅 송수신
		public static final String opcode3 = "3";
		//4-5. 채팅 허가 
		public static final String opcode4 = "4";		 
		public static final String opcode5 = "5";	
		//6-7 찬반 선택
		public static final String opcode6 = "6";
		public static final String opcode7 = "7";
		//8. 사망자
		public static final String opcode8 = "8";
		//9. 유저 선택 요청
		public static final String opcode9 = "9";
		//10. 유저 선택 종료
		public static final String opcode10 = "10";
		//11-13. 게임 결과 (시민/마피아/진행불가)
		public static final String opcode11 = "11";
		public static final String opcode12 = "12";
		public static final String opcode13 = "13";
		//14-15. 찬반 투표 요청
		public static final String opcode14 = "14";
		public static final String opcode15 = "15";
		//16. 방 나가기
		public static final String opcode16 = "16";
		//17. 방 들어가기 
		public static final String opcode17 = "17";
		//18. 방 들어가기 성공
		public static final String opcode18 = "18";
		//19. ? 
		public static final String opcode19 = "19";
		//20. 방 유저 정보 
		public static final String opcode20 = "20";
		//21. 투표 제일 많이 받은 유저 
		public static final String opcode21 = "21";
		//22-26. 게임 턴 전달 
		public static final String opcode22 = "22";
		public static final String opcode23 = "23";
		public static final String opcode24 = "24";
		public static final String opcode25 = "25";
		public static final String opcode26 = "26";
			
	}
	
	
	public ClientControllerEventHandler(CMClientStub stub, ClientController client) {
		m_clientStub  = stub;
		m_client  = client;		
		gameController = new GameController();
		roomController = new RoomController();
	}
	
	
	@Override
	public void processEvent(CMEvent cme) {
		switch(cme.getType())
		{
		case CMInfo.CM_DUMMY_EVENT:
			processDummyEvent(cme);
			break;
		case CMInfo.CM_SESSION_EVENT:
			processSessionEvent(cme);
			break;
		default:
			break;
		}
		
	}
	
	
	
	private void processSessionEvent(CMEvent cme)
	{
		CMSessionEvent se = (CMSessionEvent)cme;
		switch(se.getID())
		{
		// 로그인 확인 
		case CMSessionEvent.LOGIN_ACK:
			if (se.isValidUser() == 0) {
				System.out.println("This client fails authentication by the default server!\n");
				m_client.terminateCM(); 
			}
			
			else if(se.isValidUser()== -1) {
				System.out.println("This client is already in the login-user list!\n");
			}
			
			else {
				System.out.println("This client successfully logs in to the default server.\n");
				
			}
			break;
			
		case CMSessionEvent.RESPONSE_SESSION_INFO:
			processRESPONSE_SESSION_INFO(se);
			break;
		
		
		case CMSessionEvent.REGISTER_USER_ACK:			
			if( se.getReturnCode() == 1 )
			{
				// user registration succeeded
				System.out.println("User["+se.getUserName()+"] successfully registered at time["
							+se.getCreationTime()+"].");
			}
			else
			{
				// user registration failed
				System.out.println("User["+se.getUserName()+"] failed to register!");
			}
			break;
		}
	}

	
	
	private void processRESPONSE_SESSION_INFO(CMSessionEvent se)
	{
		Iterator<CMSessionInfo> iter = se.getSessionInfoList().iterator();

		System.out.format("%-60s%n", "------------------------------------------------------------");
		System.out.format("%-20s%-20s%-10s%-10s%n", "name", "address", "port", "user num");
		System.out.format("%-60s%n", "------------------------------------------------------------");

		while(iter.hasNext())
		{
			CMSessionInfo tInfo = iter.next();
			System.out.format("%-20s%-20s%-10d%-10d%n", tInfo.getSessionName(), tInfo.getAddress(), 
					tInfo.getPort(), tInfo.getUserNum());
		}
	}

	

	// Server로 부터 오는 DummyEvent 처리 
	private void processDummyEvent(CMEvent cme)
	{
		CMDummyEvent due = (CMDummyEvent) cme;
		String msg = due.getDummyInfo();
		System.out.println("total message for sender : " +msg);
		String[] arrMsg = msg.split("|");
		String opcode = arrMsg[0];
		String roomId = arrMsg[1];
		String userName = arrMsg[2];
		String args = arrMsg[3];
		
		switch(opcode) {
		
		case DummyType.opcode0:
			gameController.gameStart();
			break;
			
		case DummyType.opcode1:
			gameController.setUserRole(args);
			break;
			
		case DummyType.opcode3:
			
			break;
			
		case DummyType.opcode4:
			
			break;
			
		case DummyType.opcode5:
			
			break;
			
		case DummyType.opcode8:
			gameController.broadcastDeadUser(args);
			break;
			
		case DummyType.opcode9:
			//투표 시작
			gameController.voteUser(args);
			break;	
			
		case DummyType.opcode10:
			//유저 선택종료하는 통신 
			break;	
			
		case DummyType.opcode11:
			gameController.citizenWin();
			break;	
			
		case DummyType.opcode12:
			gameController.mafiaWin();
			break;	
			
		case DummyType.opcode13:
			//게임 진행 불가 오류
			break;
			
		case DummyType.opcode14:
			gameController.voteProsCons();
			break;
			
		case DummyType.opcode15:
			
			//찬반 종료 통신
			break;
			
		case DummyType.opcode16:
			roomController.exitRoom();
			break;	
			
		case DummyType.opcode18:
			roomController.enterRoom(args);
			break;	
			
		case DummyType.opcode20:
			
			break;	
			
		case DummyType.opcode21:
			gameController.showMostSelectedUser(args);
			break;	
			
		case DummyType.opcode22:
			gameController.setDay(0);
			break;	
			
		case DummyType.opcode23:
			gameController.setDay(1);
			break;	
			
		case DummyType.opcode24:
			gameController.mafiaTurn(args);
			break;	
		case DummyType.opcode25:
			gameController.policeTurn(args);
			break;	
		case DummyType.opcode26:
			gameController.doctorTurn(args);
			break;	

		default:
			break;
		}
		return;
	}
	
	
	

	
}
