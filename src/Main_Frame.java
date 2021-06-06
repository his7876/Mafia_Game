import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.awt.event.MouseAdapter;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;
import java.util.ArrayList;

public class Main_Frame extends JFrame{

 
	private Dimension frameSize, screenSize;
	ClientController controller;
	private JLabel label;
    private JList Friends_List;
    private DefaultListModel friend_model;
    private JScrollPane scrollpane1;
    private JScrollPane scrollpane2;
    private JLabel User_Info_Text;
    private JButton Room_Create_Button;
    
    private RoomController roomController;
    
    private JTextField inputText;
    private String Username;
    
    public Main_Frame(String user, ClientController controller) {
    	this.controller = controller;
    	Username = user;
    	roomController = new RoomController(controller.getClientStub());
    	Init();
    }
    
    private void Init() {
        setTitle("마피아 게임");
        setSize(252,405);
        screenSizeLocation();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        
        JPanel pnl = new JPanel();
        setDisplay(pnl);
        
        add(pnl);
        setVisible(true);
    }
    
    private void setDisplay(JPanel pnl) {
    	pnl.setLayout(null);
    	User_Info_Text = new JLabel(Username);
    	
    	Room_Create_Button = new JButton("방 입장");
    	Room_Create_Button.addActionListener(new ActionListener() {
    		
    		@Override
    		public void actionPerformed(ActionEvent arg0) {
    			roomController.tryEnterRoom();
    			try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
    			//main.showRoomFrame();
    		}
    	});
    

    	Friends_List = new JList(new DefaultListModel());
    	friend_model = (DefaultListModel)Friends_List.getModel();
    	
    	
    	
    	ArrayList<String> members = controller.getSessionMember();
    	
    	for(int i = 0; i < members.size(); i++) {
   		friend_model.addElement(members.get(i));
    	}
    	scrollpane2 = new JScrollPane(Friends_List);
    	scrollpane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	
    	User_Info_Text.setBounds(4,8,131,25);
    	Room_Create_Button.setBounds(156,5,80,25);
    	scrollpane2.setBounds(4, 36, 239, 337);
    	
    	pnl.add(User_Info_Text);
    	pnl.add(Room_Create_Button);
    	pnl.add(scrollpane2);
    	
    }
    
    
    
    public void screenSizeLocation() {
    	frameSize = getSize();
    	screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
    }
    
//    public void setMain(MainProcess main) {
 //       this.main = main;
  //  }
    
    public void restart_main() {
    	
    }
    
}