package Client;

public class RoomController {
	
	private ClientController u_Event = new ClientController();
	

	public RoomController() {

	}
	
	public String makeMsg(String args) {
		return UserController.getInstance().getRoomId() + "|" + UserController.getInstance().getId() + "|" + args;
	}
	
	public void tryEnterRoom(){
		String msg = makeMsg("");
		u_Event.sendDummyEvent("17", msg);
	}
	public void enterRoom(String roomId) {
		UserController.getInstance().enterRoom(roomId);
	}
	public void exitRoom() {
		UserController.getInstance().exitRoom();
	}
	public void showUserList() {
		u_Event.getSessionMember();//session member 가져와서 보여주기
	}
	
}
