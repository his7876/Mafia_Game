import kr.ac.konkuk.ccslab.cm.entity.CMUser;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.info.CMInteractionInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class CMGameGateway {
	private CMServerStub cmStub;
	private CMInteractionInfo interInfo;
	private CMUser myself;
	
	private static CMGameGateway instance = null;
	
	private CMGameGateway() {

	}
	
	public static CMGameGateway getInstance() {
		if(instance == null) {
			instance = new CMGameGateway();
		}
		return instance;
	}
	
	public void registerCM(CMServerStub stub) {
		cmStub = stub;
		interInfo = cmStub.getCMInfo().getInteractionInfo();
		myself = interInfo.getMyself();
	}
	
	public void sendGameEvent(UserGameEventInfo info, String target) {
		CMDummyEvent due = new CMDummyEvent();
		due.setHandlerSession(myself.getCurrentSession());
		due.setHandlerGroup(myself.getCurrentGroup());
		due.setDummyInfo(info.toString());
		cmStub.send(due, target);
	}
}
