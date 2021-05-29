package Client;

public class UserController{
	
	private String id;
	private String password;
	private String userRole;
	private String roomNum;
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
	
	public void enterRoom(String roomNumber) { //방 입장
		this.roomNum = roomNumber;
		//
	}
	
	public void exitRoom() { //방에서 나오기
		this.roomNum = "0";
		//
	}
	
	public void votePerson(int person) { //게임 내에서 투표 지목
		
	}
	
	public void amIDead(String deadOrLive) { //자신이 죽었는지 확인하는 함수
		/*
		 * 0 : 죽음
		 * 1 : 삶
		 */
		this.setIsDead(deadOrLive);
		//
	}

	public void voteAgainst(boolean trueOrFalse) { //죽일지 말지 찬반 투표
		
	}
	
	public void chat(int session) { //게임 내에서 채팅하기 1:n
		
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
		 * 0 : 게임에 입장 하지 않았을 때
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
