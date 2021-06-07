import com.sun.xml.internal.stream.events.DummyEvent;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class MafiaClientEventHandler  implements CMAppEventHandler {

	
	private DummyEventHandler dummyManager;
	private CMClientStub stub;
	
	public MafiaClientEventHandler(CMClientStub stub ) {
		this.stub = stub;
		System.out.println("[Mafia Game] : Client Logic Start");
	}
	
	
	@Override
	public void processEvent(CMEvent cme) {
		UserGameEventInfo msg = new UserGameEventInfo();
		// TODO Auto-generated method stub
		switch(cme.getType())
		{
		case CMInfo.CM_DUMMY_EVENT:
			msg.fromString(((CMDummyEvent)cme).getDummyInfo());
			System.out.println("[클라이언트] 받은 이벤트 : " + GameCMConnector.OPCODE_INFO[msg.opcode] + ", 인자 : " + msg.args);
			break;
		default:
			return;
		}
	}
	
}