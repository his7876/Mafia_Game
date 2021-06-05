import kr.ac.konkuk.ccslab.cm.*;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub; 

public class MafiaServerApp { 
	private CMServerStub m_serverStub; 
	private ServerEventHandler m_eventHandler; 
	
	public MafiaServerApp() 
	{ 
		m_serverStub = new CMServerStub(); 
		m_eventHandler = new ServerEventHandler(m_serverStub); 
	}
	
	public CMServerStub getServerStub() 
	{ 
		return m_serverStub; 
	}
	
	public ServerEventHandler getServerEventHandler() 
	{ 
		return m_eventHandler; 
	}
	
	public static void main(String[] args) { 
		MafiaServerApp server = new MafiaServerApp(); 
		CMServerStub cmStub = server.getServerStub(); 
		cmStub.setAppEventHandler(server.getServerEventHandler()); 
		cmStub.startCM();
		System.out.println("[Mafia Game] : Server Start");
	} 
}
