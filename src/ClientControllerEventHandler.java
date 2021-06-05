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
		
		//0. 寃뚯엫 �떆�옉 
		public static final String opcode0 = "0";
		//1. 吏곸뾽 愿��젴 
		public static final String opcode1 = "1";
		//2. �쑀�� �꽑�깮
		public static final String opcode2 = "2";
		//3. 梨꾪똿 �넚�닔�떊
		public static final String opcode3 = "3";
		//4-5. 梨꾪똿 �뿀媛� 
		public static final String opcode4 = "4";		 
		public static final String opcode5 = "5";	
		//6-7 李щ컲 �꽑�깮
		public static final String opcode6 = "6";
		public static final String opcode7 = "7";
		//8. �궗留앹옄
		public static final String opcode8 = "8";
		//9. �쑀�� �꽑�깮 �슂泥�
		public static final String opcode9 = "9";
		//10. �쑀�� �꽑�깮 醫낅즺
		public static final String opcode10 = "10";
		//11-13. 寃뚯엫 寃곌낵 (�떆誘�/留덊뵾�븘/吏꾪뻾遺덇�)
		public static final String opcode11 = "11";
		public static final String opcode12 = "12";
		public static final String opcode13 = "13";
		//14-15. 李щ컲 �닾�몴 �슂泥�
		public static final String opcode14 = "14";
		public static final String opcode15 = "15";
		//16. 諛� �굹媛�湲�
		public static final String opcode16 = "16";
		//17. 諛� �뱾�뼱媛�湲� 
		public static final String opcode17 = "17";
		//18. 諛� �뱾�뼱媛�湲� �꽦怨�
		public static final String opcode18 = "18";
		//19. ? 
		public static final String opcode19 = "19";
		//20. 諛� �쑀�� �젙蹂� 
		public static final String opcode20 = "20";
		//21. �닾�몴 �젣�씪 留롮씠 諛쏆� �쑀�� 
		public static final String opcode21 = "21";
		//22-26. 寃뚯엫 �꽩 �쟾�떖 
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
		// 濡쒓렇�씤 �솗�씤 
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

	

	// Server濡� 遺��꽣 �삤�뒗 DummyEvent 泥섎━ 
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
			//�닾�몴 �떆�옉
			gameController.voteUser(args);
			break;
			
		case DummyType.opcode10:
			//�쑀�� �꽑�깮醫낅즺�븯�뒗 �넻�떊 
			break;	
			
		case DummyType.opcode11:
			gameController.citizenWin();
			break;	
			
		case DummyType.opcode12:
			gameController.mafiaWin();
			break;	
			
		case DummyType.opcode13:
			//寃뚯엫 吏꾪뻾 遺덇� �삤瑜�
			break;
			
		case DummyType.opcode14:
			gameController.voteProsCons();
			break;
			
		case DummyType.opcode15:
			
			//李щ컲 醫낅즺 �넻�떊
			break;
			
		case DummyType.opcode16:
			roomController.exitRoom();
			break;	
			
		case DummyType.opcode18:
			roomController.enterRoom(userName, args);
			
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
