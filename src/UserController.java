public class UserController{
	
	private String id;
	private String password;
	private String userRole;
	private String roomNum = "0";
	private String isDead;
	
	public UserController() {
		
	}
	
	private static UserController u = null;
	
//	private UserController(String id, String roomNum) {
//		this.id = id;
////		this.password = password;
//		this.userRole = "0";
//		this.roomNum = roomNum;
//	}
//	
	public static UserController getInstance() {
		if (u == null) {
			u = new UserController();
		}
		return u;
	}
	
	public void enterRoom(String roomNumber) { //諛� �엯�옣
		this.roomNum = roomNumber;
		//
	}
	
	public void exitRoom() { //諛⑹뿉�꽌 �굹�삤湲�
		this.roomNum = "0";
		//
	}
	
	public void votePerson(int person) { //寃뚯엫 �궡�뿉�꽌 �닾�몴 吏�紐�
		
	}
	
	public void amIDead(String deadOrLive) { //�옄�떊�씠 二쎌뿀�뒗吏� �솗�씤�븯�뒗 �븿�닔
		/*
		 * 0 : 二쎌쓬
		 * 1 : �궣
		 */
		this.setIsDead(deadOrLive);
		//
	}

	public void voteAgainst(boolean trueOrFalse) { //二쎌씪吏� 留먯� 李щ컲 �닾�몴
		
	}
	
	public void chat(int session) { //寃뚯엫 �궡�뿉�꽌 梨꾪똿�븯湲� 1:n
		
	}

	public void deleteRole() {
		this.userRole = "0";
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		/*
		 * 0 : 寃뚯엫�뿉 �엯�옣 �븯吏� �븡�븯�쓣 �븣
		 * 1 : citizen
		 * 2 : mafia
		 * 3 : police
		 * 4 : doctor
		 */
		this.userRole = userRole;
	}
	
	public void skill() {
		switch(userRole) {
		case "1":
			break;
		case "2":
			//kill
			break;
		case "3":
			//detect
			break;
		case "4":
			//care
			break;
		default:
			break;
		}
	}

	public String getRoomId() {
		return roomNum;
	}
	
	public String getIsDead() {
		return isDead;
	}

	public void setIsDead(String isDead) {
		this.isDead = isDead;
	}
	
}
