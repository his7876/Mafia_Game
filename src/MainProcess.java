
import javax.swing.JFrame;

public class MainProcess {
	
    Login_Frame login_frame;
    Sign_Up_Frame signup_frame;
    Main_Frame main_frame;
    Room_Frame room_frame;
    String username;
    public static void main(String[] args){
        MainProcess main = new MainProcess();
        
        main.login_frame = new Login_Frame();
        main.login_frame.setMain(main);
//        main.room_frame = new Room_Frame();
//        main.room_frame.setMain(main);
    	
    }
    
    public void showSignUpFrame() {
    	this.signup_frame = new Sign_Up_Frame();
    	
    }
    
    public void showMainFrame(String user) {
    	username = user;
    	login_frame.dispose();
    	
    	this.main_frame = new Main_Frame(user);
    	this.main_frame.setMain(this);
    }
    
    public void showRoomFrame(String user) {
    	this.room_frame = new Room_Frame(user);
    }
}
