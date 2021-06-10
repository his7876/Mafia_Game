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
		
		//0. 野껊슣�뿫 占쎈뻻占쎌삂 
		public static final String opcode0 = "0";
		// 1. 筌욊낯毓� �꽴占쏙옙�졃
		public static final String opcode1 = "1";
		// 2. 占쎌�占쏙옙 占쎄퐨占쎄문
		public static final String opcode2 = "2";
		// 3. 筌�袁る샒 占쎈꽊占쎈땾占쎈뻿
		public static final String opcode3 = "3";
		// 4-5. 筌�袁る샒 占쎈�揶쏉옙
		public static final String opcode4 = "4";
		public static final String opcode5 = "5";
		// 6-7 筌⊥됱뺘 占쎄퐨占쎄문
		public static final String opcode6 = "6";
		public static final String opcode7 = "7";
		// 8. 占쎄텢筌띿빘�쁽
		public static final String opcode8 = "8";
		// 9. 占쎌�占쏙옙 占쎄퐨占쎄문 占쎌뒄筌ｏ옙
		public static final String opcode9 = "9";
		// 10. 占쎌�占쏙옙 占쎄퐨占쎄문 �넫�굝利�
		public static final String opcode10 = "10";
		// 11-13. 野껊슣�뿫 野껉퀗�궢 (占쎈뻻沃섓옙/筌띾뜇逾억옙釉�/筌욊쑵六얗겫�뜃占�)
		public static final String opcode11 = "11";
		public static final String opcode12 = "12";
		public static final String opcode13 = "13";
		// 14-15. 筌⊥됱뺘 占쎈떮占쎈ご 占쎌뒄筌ｏ옙
		public static final String opcode14 = "14";
		public static final String opcode15 = "15";
		// 16. 獄쏉옙 占쎄돌揶쏉옙疫뀐옙
		public static final String opcode16 = "16";
		// 17. 獄쏉옙 占쎈굶占쎈선揶쏉옙疫뀐옙
		public static final String opcode17 = "17";
		// 18. 獄쏉옙 占쎈굶占쎈선揶쏉옙疫뀐옙 占쎄쉐�⑨옙
		public static final String opcode18 = "18";
		// 19. ?
		public static final String opcode19 = "19";
		// 20. 獄쏉옙 占쎌�占쏙옙 占쎌젟癰귨옙
		public static final String opcode20 = "20";
		// 21. 占쎈떮占쎈ご 占쎌젫占쎌뵬 筌띾‘�뵠 獄쏆룇占� 占쎌�占쏙옙
		public static final String opcode21 = "21";
		// 22-26. 野껊슣�뿫 占쎄쉘 占쎌읈占쎈뼎
		public static final String opcode22 = "22";
		public static final String opcode23 = "23";
		public static final String opcode24 = "24";
		public static final String opcode25 = "25";
		public static final String opcode26 = "26";
		public static final String opcode27 = "27";

	}

	public ClientControllerEventHandler(CMClientStub stub, ClientController client) {
		m_clientStub = stub;
		m_client = client;
		gameController = new GameController();
		roomController = new RoomController();
	}

	@Override
	public void processEvent(CMEvent cme) {
		switch (cme.getType()) {
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

	private void processSessionEvent(CMEvent cme) {
		CMSessionEvent se = (CMSessionEvent) cme;
		switch (se.getID()) {
		// 嚥≪뮄�젃占쎌뵥 占쎌넇占쎌뵥
		case CMSessionEvent.LOGIN_ACK:
			if (se.isValidUser() == 0) {
				System.out.println("This client fails authentication by the default server!\n");
				m_client.terminateCM();
			}

			else if (se.isValidUser() == -1) {
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
			if (se.getReturnCode() == 1) {
				// user registration succeeded
				System.out.println("User[" + se.getUserName() + "] successfully registered at time["
						+ se.getCreationTime() + "].");
			} else {
				// user registration failed
				System.out.println("User[" + se.getUserName() + "] failed to register!");
			}
			break;
		}
	}

	private void processRESPONSE_SESSION_INFO(CMSessionEvent se) {
		Iterator<CMSessionInfo> iter = se.getSessionInfoList().iterator();

		System.out.format("%-60s%n", "------------------------------------------------------------");
		System.out.format("%-20s%-20s%-10s%-10s%n", "name", "address", "port", "user num");
		System.out.format("%-60s%n", "------------------------------------------------------------");

		while (iter.hasNext()) {
			CMSessionInfo tInfo = iter.next();
			System.out.format("%-20s%-20s%-10d%-10d%n", tInfo.getSessionName(), tInfo.getAddress(), tInfo.getPort(),
					tInfo.getUserNum());
		}
	}

	// Server嚥∽옙 �겫占쏙옙苑� 占쎌궎占쎈뮉 DummyEvent 筌ｌ꼶�봺
	private void processDummyEvent(CMEvent cme) {
		CMDummyEvent due = (CMDummyEvent) cme;
		String msg = due.getDummyInfo();
		System.out.println("total message for sender : " + msg);
		String[] arrMsg = msg.split("\\|");
		String opcode = arrMsg[0];
		String roomId = arrMsg[1];
		String userName = arrMsg[2];
		String args = arrMsg[3];
		String[] job = {"시민", "마피아","경찰","의사"};
		switch (opcode) {

		case DummyType.opcode0:
			gameController.gameStart();
			FrameController.getInstance().room_frame.sendMsg(userName,  "게임을 시작합니다.");
			break;

		case DummyType.opcode1:
			gameController.setUserRole(args);
			FrameController.getInstance().room_frame.sendMsg(userName,  job[Integer.parseInt(args)]);
			break;

		case DummyType.opcode3:
			FrameController.getInstance().room_frame.sendMsg(userName,args);
			break;

		case DummyType.opcode4:
			break;

		case DummyType.opcode5:
			
			break;

		case DummyType.opcode8:
			gameController.broadcastDeadUser(args);
			break;

		case DummyType.opcode9:
			// 占쎈떮占쎈ご 占쎈뻻占쎌삂
			gameController.voteUser(args);
			FrameController.getInstance().room_frame.sendMsg(userName,"투표를 시작합니다");
			FrameController.getInstance().room_frame.sendMsg(userName, "choose");
			break;

		case DummyType.opcode10:
			// 占쎌�占쏙옙 占쎄퐨占쎄문�넫�굝利븝옙釉�占쎈뮉 占쎈꽰占쎈뻿
			break;

		case DummyType.opcode11:
			gameController.citizenWin();
			break;

		case DummyType.opcode12:
			gameController.mafiaWin();
			break;

		case DummyType.opcode13:
			FrameController.getInstance().room_frame.dispose();
			break;

		case DummyType.opcode14:
			// gameController.voteProsCons();
			FrameController.getInstance().room_frame.sendMsg(userName,"찬반 투표를 시작합니다(20초)");
			FrameController.getInstance().room_frame.sendMsg(args , "kill");
			break;

		case DummyType.opcode15:

			// 筌⊥됱뺘 �넫�굝利� 占쎈꽰占쎈뻿
			break;

		case DummyType.opcode16:
			roomController.exitRoom();
			break;

		case DummyType.opcode18:
			System.out.println(userName);
			roomController.enterRoom(userName, args);
			break;

		case DummyType.opcode20:

			break;

		case DummyType.opcode21:
			gameController.showMostSelectedUser(args);
			break;

		case DummyType.opcode22:
			FrameController.getInstance().room_frame.sendMsg(userName,"낮이 되었습니다");
			gameController.setDay(0);
			break;

		case DummyType.opcode23:
			FrameController.getInstance().room_frame.sendMsg(userName,"밤이 되었습니다");
			gameController.setDay(1);
			break;

		case DummyType.opcode24:
			FrameController.getInstance().room_frame.sendMsg(userName,"마피아 턴 입니다");
			gameController.mafiaTurn(args);
			break;	
		case DummyType.opcode25:
			FrameController.getInstance().room_frame.sendMsg(userName,"경찰 턴 입니다");
			gameController.policeTurn(args);
			break;	
		case DummyType.opcode26:
			FrameController.getInstance().room_frame.sendMsg(userName,"의사 턴 입니다");
			gameController.doctorTurn(args);
			break;	
		case DummyType.opcode27:
			FrameController.getInstance().room_frame.sendMsg(userName,"토론을 시작합니다(60초)");
			break;	

		default:
			break;
		}
		return;
	}
	
	
	

	
}
