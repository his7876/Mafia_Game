
public class FrameController {
	public Room_Frame room_frame;
	public Main_Frame main_frame;
	
	public FrameController() {
		
	}
	private static FrameController f = null;
	
	public static FrameController getInstance() {
		if(f == null) {
			f = new FrameController();
		}
		return f;
	}
	public void init_room_frame(String userName) {
		room_frame = new Room_Frame(userName);
	}
	
	public void init_main_frame(String userName , ClientController client) {
		main_frame = new Main_Frame(userName, client);
	}
	
}
