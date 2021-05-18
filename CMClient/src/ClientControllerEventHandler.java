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

	
	//Dummy info type 
	public class DummyType{
		
		//1. 게임 시작 
		public static final String GameStart = "GameStart";
		//2. 직업 관련 
		public static final String SetRole = "SetRole";
		//3. 채팅 관련
		public static final String Chat = "Chat";
		//4. 게임 결과 관련
		public static final String Result = "Result";
		//5. 투표 관련 
		public static final String Voting = "Voting";
		//6. 방 관련 
		public static final String Room = "Room";
		//7. 게임 진행 관련
		public static final String GameTurn = "GameTurn";
		
	}
	
	
	public ClientControllerEventHandler(CMClientStub stub, ClientController client) {
		m_clientStub  = stub;
		m_client  = client;		

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
			}else if(se.isValidUser()== -1) {
				System.out.println("This client is already in the login-user list!\n");
			}else {
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

	

	// DummyEvent 처리 
	private void processDummyEvent(CMEvent cme)
	{
		CMDummyEvent due = (CMDummyEvent) cme;
		String msg = due.getDummyInfo();
		System.out.println("total message for sender : " +msg);
		String[] arrMsg = msg.split("#");
		String type = arrMsg[0];
		
		switch(type) {
		case DummyType.GameStart:
			break;
		case DummyType.SetRole:
			break;
		case DummyType.Chat:
			break;
		case DummyType.Result:
			break;
		case DummyType.Voting:
			break;
		case DummyType.Room:
			break;
		case DummyType.GameTurn:
			break;						
		default:
			break;
		}
		return;
	}
	
	
	

	
}
