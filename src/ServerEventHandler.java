import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javax.swing.JOptionPane;

import kr.ac.konkuk.ccslab.cm.entity.CMGroup;
import kr.ac.konkuk.ccslab.cm.entity.CMGroupInfo;
import kr.ac.konkuk.ccslab.cm.entity.CMList;
import kr.ac.konkuk.ccslab.cm.entity.CMMember;
import kr.ac.konkuk.ccslab.cm.entity.CMMessage;
import kr.ac.konkuk.ccslab.cm.entity.CMPosition;
import kr.ac.konkuk.ccslab.cm.entity.CMRecvFileInfo;
import kr.ac.konkuk.ccslab.cm.entity.CMSendFileInfo;
import kr.ac.konkuk.ccslab.cm.entity.CMServer;
import kr.ac.konkuk.ccslab.cm.entity.CMSession;
import kr.ac.konkuk.ccslab.cm.entity.CMSessionInfo;
import kr.ac.konkuk.ccslab.cm.entity.CMUser;
import kr.ac.konkuk.ccslab.cm.event.CMBlockingEventQueue;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMFileEvent;
import kr.ac.konkuk.ccslab.cm.event.CMInterestEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.CMUserEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMCommInfo;
import kr.ac.konkuk.ccslab.cm.info.CMConfigurationInfo;
import kr.ac.konkuk.ccslab.cm.info.CMFileTransferInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInteractionInfo;
import kr.ac.konkuk.ccslab.cm.manager.CMConfigurator;
import kr.ac.konkuk.ccslab.cm.manager.CMFileTransferManager;
import kr.ac.konkuk.ccslab.cm.manager.CMMqttManager;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;
import kr.ac.konkuk.ccslab.cm.util.CMUtil;

public class ServerEventHandler  implements CMAppEventHandler {

	
	private DummyEventHandler dummyManager;
	private CMServerStub stub;
	
	public ServerEventHandler(CMServerStub stub ) {
		this.stub = stub;
		CMGameGateway.getInstance().registerCM(stub);
		dummyManager = new DummyEventHandler();
		System.out.println("[Mafia Game] : Server Logic Start");
	}
	
	
	@Override
	public void processEvent(CMEvent cme) {
		// TODO Auto-generated method stub
		switch(cme.getType())
		{
		case CMInfo.CM_DUMMY_EVENT:
			dummyManager.handleGameEvent(cme);
			break;
		case CMInfo.CM_SESSION_EVENT:
			processSessionEvent(cme);
		default:
			return;
		}
	}
	
	//DB 내 로그인 정보 검증 과정 추가 
	public void processSessionEvent(CMEvent cme) {
		CMConfigurationInfo confInfo = stub.getCMInfo().getConfigurationInfo();
		CMSessionEvent se = (CMSessionEvent) cme;
		switch (se.getID()) {
		case CMSessionEvent.LOGIN:
			printMessage("[" + se.getUserName() + "] requests login.\n");
			if(confInfo.isLoginScheme())
			{
				// user authentication...
				// CM DB must be used in the following authentication..
				
				String strQuery = "select * from user_table where userName='"+se.getUserName()
						+"' and password=PASSWORD('"+se.getPassword()+"');";
				System.out.println(strQuery);
				boolean ret = CMDBManager.authenticateUser(se.getUserName(), se.getPassword(), 
						stub.getCMInfo());
				if(!ret)
				{
					printMessage("["+se.getUserName()+"] authentication fails!\n");
					stub.replyEvent(cme, 0);
				}
				else
				{
					printMessage("["+se.getUserName()+"] authentication succeeded.\n");
					stub.replyEvent(cme, 1);
					
				}
				
				
			}
						break;
		case CMSessionEvent.LOGOUT:
			printMessage("[" + se.getUserName() + "] logs out.\n");
			break;
			
		case CMSessionEvent.REGISTER_USER:
			printMessage("User registration requested by user[" + se.getUserName() + "].\n");
			break;
		default:
			return;
	}
	
}


	private void printMessage(String strText) {
		System.out.println("printMessage: " + strText);
	}
}
