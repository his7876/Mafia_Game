import java.util.Timer;
import java.util.TimerTask;
import kr.ac.konkuk.ccslab.cm.stub.*;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;

import java.util.ArrayList;
import java.util.List;

public class GameController extends Thread{
	static int count = 1;
	private int day = 0;// �궙 - 0, 諛� - 1
	CMClientStub clientStub;
//	private ClientController clientController;
//	private ClientControllerEventHandler clientHandler = new ClientControllerEventHandler( , clientController);// stub �뿉 萸� ?

	public GameController() {
		clientStub = new CMClientStub();
	}
	
	public void sendDummyEvent(String opcode, String msg) {
		System.out.println("====== DummyEvent send to default server");
		CMDummyEvent due = new CMDummyEvent();
		due.setSender(clientStub.getCMInfo().getInteractionInfo().getMyself().getName());
		due.setDummyInfo(opcode+"|"+msg);
		clientStub.send(due, "SERVER");
		System.out.println(due.getDummyInfo());
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
	//opcode 1濡� 諛쏆븘�삩 param string�쑝濡� 吏곸뾽�쓣 set�빐 (server -> client)
	
	//opcode 22 �븘移� (server -> client)
	//諛섎났
	//opcode 3 �넗濡� �떆�옉.. (�긽�샇�옉�슜 )
	//opcode 9 �닾�몴 �떆�옉.. (sever ->client) // opcode2 �닾�몴 �늻援ы븷吏� (client -> server) // �닾�몴�떆 �븘臾대룄 �꽑�깮 �븞�븯�뒗 寃�
	//opcode 10 �닾�몴 醫낅즺 (server -> client)
	//opcode 21 �닾�몴�뿉�꽌 �몴瑜� 媛��옣 留롮씠 諛쏆� �궗�엺 (server->client)
	//opcode 14(李�) 15(諛�) 李щ컲�닾�몴 �떆�옉.. (client -> server)
	//opcode 8 �궗留앹옄媛� �엳�뿀�쓣�븣 �궗留앹옄瑜� �븣由щ뒗 �넻�떊 (Server-> client)
	//opcode 23 諛� (server -> client)
	//opcode 24 留덊뵾�븘 �궡�씤  -> opcode 2 �늻援� 二쎌씪吏� �꽑�깮 (client -> server)
	//opcode 25 寃쎌같 �솗�씤 -> opcode 2  �늻援� 蹂쇱� (client -> server)
	//opcode 26 �쓽�궗 �늻援� �궡由댁� -> opcode2 �늻援� �궡由댁� (client -> server)
	//�븘移�
	//opcode 8 諛ㅼ뿉 �궗留앹옄媛� �엳�쑝硫� �븣由щ뒗 �넻�떊 .. �뾾�쑝硫� �뾾�떎怨� 留먰빐
	// if �듅由щ�� �뻽�떎硫� �듅由щ�� �븣由щ뒗 �넻�떊 opcode 11 �떆誘� �듅由� opcode 12 留덊뵾�븘 �듅由�  opcode 13 寃뚯엫吏꾪뻾遺�
			//寃뚯엫 醫낅즺 �떆 opcode 16
	
	public void gameStart() {// 諛⑹뿉 �궗�엺 �떎 �뱾�뼱�삤硫� -> �꽌踰꾩뿉�꽌�뒗 �떆�옉 議곌굔�씠 �셿猷뚮맂嫄� �븣�젮二쇰㈃ �겢�씪�씠�뼵�듃�뿉�꽌 5珥� 
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				if(count <= 5){ //count媛믪씠 5蹂대떎 �옉嫄곕굹 媛숈쓣�븣源뚯� �닔�뻾
					System.out.println("[移댁슫�듃�떎�슫 : "+count+"]");
					count++; //�떎�뻾�슏�닔 利앷� 
				}
				else{
					timer.cancel(); //���씠癒� 醫낅즺
					count = 1;
					System.out.println("[移댁슫�듃�떎�슫 : 醫낅즺]");
				}
			}
		};
		timer.schedule(task, 1000, 1000);//task �떎�뻾 , 1珥덈쭏�떎 諛섎났
	//諛⑹뿉�꽌 �씤�썝�씠 苑� 李쇰떎怨� 諛쏆븘�삤湲�
	//-> 洹몃븣遺��꽣 移댁슫�듃 (移댁슫�듃瑜� �꽌踰꾩뿉�꽌 �븷吏� �겢�씪�씠�뼵�듃�뿉�꽌 �븷吏�)
	//寃뚯엫�떆�옉 -> opcode 0
	}
	
	public void setUserRole(String parm) {
		UserController.getInstance().setUserRole(parm);
	}
	
	public void voteUser(String parm) {
		//諛쏆븘��
		String[] aliveUserList = parm.split("/");
		//ui
		// String selectUser �꽑�깮 諛쏆�嫄� 媛��졇���꽌
		String selectUser = " ";// �꽑�깮諛쏆�id
		String msg = makeMsg(selectUser);
		sendDummyEvent("2", msg);
	}
	//李쎌쓣 �쓣�슦�뒗 �븿�닔瑜� 留뚮뱾怨� �쓣�뼱吏� 李쎌쓣 �뼱�뼸寃� �떕�븘..
	public void showMostSelectedUser(String userid) {
		//
	}
	
	public void chatPermit() {
		
	}
	
	public void voteProsCons() {
		int num = 0;//李щ컲 �꽑�깮 �뿬湲곗뿉 ���옣 
		String msg = makeMsg("");
		if(num == 0) {
			sendDummyEvent("6", msg);
		}
		else if(num == 1) {
			sendDummyEvent("7", msg);
		}
	}
	
	public void broadcastDeadUser(String userId) {
		if(userId == UserController.getInstance().getId()) {
			UserController.getInstance().setIsDead("0");
		}
		//二쎌뿀�떎 ui show
	}
	
	
	public void mafiaTurn(String userId) {
		if(userId != UserController.getInstance().getId()) {
			//留덊뵾�븘 �븘�땶 �쑀���븳�뀒 ui 泥섎━ 
		}
		else {
			
		}
	}
	
	public void policeTurn(String userId) {
		if(userId != UserController.getInstance().getId()) {
			//寃쎌같 �븘�땶 �쑀���븳�뀒 ui泥�
		}
	}
	
	public void doctorTurn(String userId) {
		if(userId != UserController.getInstance().getId()) {
			//�쓽�궗 �븘�땶 �쑀���븳�뀒 ui泥�
		}
	}
	
	public void mafiaWin() {
		//ui 泥섎━
		UserController.getInstance().exitRoom();
	}
	
	public void citizenWin() {
		//ui 泥섎━
		UserController.getInstance().exitRoom();
	}

}
