package Client;

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
	private roomDialog dialog;
   
	private RoomController roomController;
	private UserController userController;
	private JLabel label;
    private JList Rooms_List;
    private JList Friends_List;
    private DefaultListModel room_model;
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
        
//        userController = new UserController(Username, -1);//메인화면에 있을때 roomid -> -1
        roomController = new RoomController();//
        JPanel pnl = new JPanel();
        setDisplay(pnl);
        
        add(pnl);
        setVisible(true);
    }
    
    private void setDisplay(JPanel pnl) {
    	pnl.setLayout(null);
    	//����� id
    	User_Info_Text = new JLabel(Username);
    	
    	Room_Create_Button = new JButton("�� ����");
    	dialog = new roomDialog(this, "�� ����", true);
    	Room_Create_Button.addActionListener(new ActionListener() {
    		
    		@Override
    		public void actionPerformed(ActionEvent arg0) {
    			roomController.showQuickstart();
    		}
    	});
    

    	Rooms_List = new JList(new DefaultListModel());
    	Rooms_List.addMouseListener(new MouseAdapter() {
    		
    		@Override
    		public void mouseClicked(MouseEvent arg0) {
    			if(arg0.getClickCount()==2) {
    				
    				
    			}
    		}
    		
    	});
    	
//    	room_model = (DefaultListModel)Rooms_List.getModel();
//    	room_model.addElement("�� 1");
//    	room_model.addElement("�� 2");
//    	room_model.addElement("�� 3");
//    	room_model.addElement("�� 4");
//    	scrollpane1 = new JScrollPane(Rooms_List);
//    	scrollpane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	
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
    	//scrollpane1.setBounds(4, 34, 340, 337);
    	scrollpane2.setBounds(4, 36, 239, 337);
    	
    	pnl.add(User_Info_Text);
    	pnl.add(Room_Create_Button);
    	//pnl.add(scrollpane1);
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


class roomDialog extends JDialog {
	JLabel lb = new JLabel("�� ����");
	JTextField tf = new JTextField(20);
	JButton okbtn = new JButton("Ok");
	
	public roomDialog(JFrame frame, String title, boolean modal) {
		super(frame, title, modal);
		setLayout(null);
		setSize(250,90);
		setLocationRelativeTo(frame);
		setResizable(false);
		
		lb.setBounds(10, 4, 60, 25);
		tf.setBounds(60,4,170,25);
		okbtn.setBounds(95, 32, 60, 25);
		
		add(lb);
		add(tf);
		add(okbtn);
		
		tf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
		okbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//�����
				setVisible(false);
			}
		});
	}
}
