
import java.io.IOException;
import java.util.Vector;

import kr.ac.konkuk.ccslab.cm.entity.CMMember;
import kr.ac.konkuk.ccslab.cm.entity.CMUser;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

public class ClientController {
	
	private CMClientStub clientStub;
	private ClientControllerEventHandler clientEventHandler;
	
	private boolean m_bRun;
	// GameController gc;
	// RoomController rc;
	// UserController uc;
			
	public ClientController() {
		
		clientStub = new CMClientStub();
		clientEventHandler = new ClientControllerEventHandler(clientStub,this);
	
		//CM 초기화 및 시작  
		m_bRun = clientStub.startCM(); 
		if(!m_bRun) {

			System.err.println("CM initialization error!");
			return;
		}else {
			System.out.println("CM initialization success!");
		}
		
	}
	
	public CMClientStub getClientStub() {
		return clientStub;
	}
	
	public ClientControllerEventHandler getClientEventHandler() {
		return clientEventHandler;
	}
	
		

	 /* Methods */

  /*  Functionality :
      CM 종료 함수  
      
  *   Parameters : void
  *   Return values : void
  * */
	public void terminateCM() {
		clientStub.terminateCM();
		m_bRun = false;
	}
	
	
	
	
	 /* Methods */

    /*  Functionality : 
        로그인 함수
          
    *   Parameters : void
    *   Return values : void
    * */	
	public boolean loginCM(String strUserID,  String strPassword) throws IOException {
		
		//String strUserID = "null";
		//String strPassword = "null";
	
		boolean bRequestResult = false;
		
		//Object[] message = {"User Name: ", id, "Password", pw};
		
		 		
		if(strUserID.equals("")) {
			System.out.println("Please enter your ID");

 		}else if(strPassword.equals("")){
 			System.out.println("Please enter your PassWord ");
		}
		
		
		
		bRequestResult = clientStub.loginCM(strUserID, strPassword);
			
		if(bRequestResult) {
			System.out.println("successfully sent the login request.");
		}
		else {
			System.err.println("failed the login request!");
		}
			
		return bRequestResult;
			
}
	
		
	

	 /* Methods */

   /*  Functionality :
         회원 가입 함수
         ID, PWD , 확인용 PWD 입력 받음 
         
         
   *   Parameters : void
   *   Return values : void
   * */		
	public void singUpCM(String strUserID,String strPassword, String strRePassword) throws IOException {

		//String strUserID = "null";
		//String strPassword = "null";
		
		//Object[] message = {"User Name: ", id, "Password", pw, "RePassWord" , repw};
		
		if(strUserID.equals("")) {
			System.out.println("Please enter your ID");

 		}else if(strPassword.equals("")){
 			System.out.println("Please enter your PassWord ");
		}else if(strRePassword.equals("")) {
 			System.out.println("Please enter your RePassWord ");
		}
		
		if(!strPassword.equals(strRePassword)) {
			System.err.println("Password input error");
			return;
		}
		
		clientStub.registerUser(strUserID, strPassword);
		
}


	 /* Methods */

  /*  Functionality :
      로그아웃 함수
  *   Parameters : void
  *   Return values : void
  * */
	public void logoutCM() {
		
		boolean bRequestResult = false;
		System.out.println("====== logout from default server");
		bRequestResult = clientStub.logoutCM();
		if(bRequestResult)
			System.out.println("successfully sent the logout request.");
		else
			System.err.println("failed the logout request!");
		System.out.println("======");
	
	}
	
	
	

	 /* Methods */

   /*  Functionality :
       현재 세션에 존재하는 사람 리스트 조회 함수 

   *   Parameters : void
   *   Return values : void
   * */
	public void getSessionMember() {
		System.out.print("====== print group members\n");
		CMMember groupMembers = clientStub.getGroupMembers();
		if(groupMembers == null || groupMembers.isEmpty())
		{
			System.err.println("No group member yet!");
			return;
		}
		System.out.print(groupMembers.toString()+"\n");
	}
	

	
	
	
	 /* Methods */

  /*  Functionality :
      DummyEvent 전송 함수
      
      InfoType :  "opcode|roomID|userName|args" 
      
      
      
  *   Parameters : void
  *   Return values : void
  * */
	
	public void sendDummyEvent(String opcode, String msg) {
		System.out.println("====== DummyEvent send to default server");
		CMDummyEvent due = new CMDummyEvent();
		due.setSender(clientStub.getCMInfo().getInteractionInfo().getMyself().getName());
		due.setDummyInfo(opcode+"|"+msg);
		clientStub.send(due, "SERVER");
		System.out.println(due.getDummyInfo());
	}

	

	
	 /* Methods */

 /*  Functionality :
     세션 정보 출력 함수
     
 *   Parameters : void
 *   Return values : void
 * */
	
	public void SessionInfo()
	{
		boolean bRequestResult = false;
		System.out.println("====== request session info from default server");
		bRequestResult = clientStub.requestSessionInfo();
		if(bRequestResult)
			System.out.println("successfully sent the session-info request.");
		else
			System.err.println("failed the session-info request!");
		System.out.println("======");
	}
	
	
	
	
	public static void main(String[] args) {	
		ClientController client = new ClientController();
		client.clientStub.setAppEventHandler(client.clientEventHandler);
	
				
	}
	
}