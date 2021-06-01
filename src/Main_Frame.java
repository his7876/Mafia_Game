
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;


public class Main_Frame extends JFrame{

    private MainProcess main;
	private Dimension frameSize, screenSize;
	
	private JLabel label;
    private JList Friends_List;
    private DefaultListModel friend_model;
    private JScrollPane scrollpane1;
    private JScrollPane scrollpane2;
    private JLabel User_Info_Text;
    private JButton Room_Create_Button;
    
    private JTextField inputText;
    private String Username;
    
    public Main_Frame(String user) {
    	Username = user;
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
    			main.showRoomFrame(Username);
    		}
    	});
    

    	Friends_List = new JList(new DefaultListModel());
    	friend_model = (DefaultListModel)Friends_List.getModel();
    	friend_model.addElement("User 1");
    	friend_model.addElement("User 2");
    	friend_model.addElement("User 3");
    	friend_model.addElement("User 4");
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
    
    public void setMain(MainProcess main) {
        this.main = main;
    }
    
}

