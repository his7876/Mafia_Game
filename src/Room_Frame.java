
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.*;

class Choose_Dialog extends JDialog{
	private JPanel pnl;
	private Dimension frameSize, screenSize;
	private JButton[] user_btn;
	
	public Choose_Dialog(JFrame frame, String title, HashMap<String, Boolean> user) {
		super(frame, title);
		pnl = new JPanel();
		pnl.setLayout(new GridLayout(2,4,5,5));
		
		user_btn = new JButton[user.size()];
		HashMap<String, Boolean>hm = user;
		Set set = hm.keySet();
		Iterator it = set.iterator();
		int i = 0;
		ActionListener eventHandler =  new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {                        
              	System.out.println(((JButton)e.getSource()).getText());
              	
              	setVisible(false);
                }
			};
                
        for(Map.Entry ent : user.entrySet()) {
        	user_btn[i] = new JButton((String)ent.getKey());
			if((Boolean)ent.getValue() == false) {
				user_btn[i].setBackground(Color.gray);
				user_btn[i].setEnabled(false);
				
			}
			user_btn[i].addActionListener(eventHandler);
			
			pnl.add(user_btn[i]);
			i++;
        }
      
		add(pnl);
		screenSizeLocation();
		setSize(325,100);
		
	}
	
    public void screenSizeLocation() {
    	frameSize = getSize();
    	screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
    }
    
    
}

public class Room_Frame extends JFrame{

    private MainProcess main;
	private Dimension frameSize, screenSize;
//	private BufferedReader reader;
//	private BufferedWriter writer;
//	private Socket sock;
	private Thread thread;
	
	private JTextArea Chat_Area;
	private JLabel Clock_Label;
    private JTextField Msg_Textbox;
    private JButton Msg_Send_Button;
    private JScrollPane scrollpane;
	
    private String Username;

    private Choose_Dialog dl;
    private HashMap<String, Boolean>hm = new HashMap<>();

    public Room_Frame(String user) {
    	Username = user;
    	Init();
    }
    
    private void Init() {
    	setTitle("留덊뵾�븘 寃뚯엫");
        setSize(325 ,408);
        screenSizeLocation();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);     
        setNetWorking();
        JPanel pnl = new JPanel();
        setDisplay(pnl);
        add(pnl);
        
        setVisible(true);
    }
    
    private void setDisplay(JPanel pnl) {
    	pnl.setLayout(null);
    	
    	Chat_Area = new JTextArea();
    	Chat_Area.setEditable(false);
    	Chat_Area.setLineWrap(true);
    	Chat_Area.setWrapStyleWord(true);
    	scrollpane = new JScrollPane(Chat_Area);
    	scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	
    	Clock_Label = new JLabel("100珥�");
    	Clock_Label.setForeground(Color.red);
    	
    	daynightchange(true);
    	
    	hm.put("111", true);
    	hm.put("222", true);
    	hm.put("333", true);
    	hm.put("444", false);
    	hm.put("555", true);
    	hm.put("666", false);

    	dl = new Choose_Dialog(this, "�궗�엺", hm);
    
    	
    	Msg_Textbox = new JTextField();
    	//Msg_Textbox.setEnabled(false);
    	Msg_Textbox.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent arg0) {
    			String msg = Msg_Textbox.getText();
    			if(!msg.trim().isEmpty()) {
    				sendMsg(msg);
    				
    				Msg_Textbox.setText("");
        			
    			}
    			Msg_Textbox.requestFocus();
    		}
    	});
    	
    	Msg_Send_Button = new JButton("蹂대궡湲�");
    	//Msg_Send_Button.setEnabled(false);
    	Msg_Send_Button.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent arg0) {
    			String msg = Msg_Textbox.getText();
    			if(!msg.trim().isEmpty()) {
    				sendMsg(msg);
    				Msg_Textbox.setText("");
        			
    			}
    			Msg_Textbox.requestFocus();
    		}
    	});
    	
    	scrollpane.setBounds(5,5,300,300);
    	Clock_Label.setBounds(135,310,300,25);
    	Msg_Textbox.setBounds(5,340,220,25);
    	Msg_Send_Button.setBounds(225, 340, 80, 25);

    	
    	pnl.add(scrollpane);
    	pnl.add(Clock_Label);
    	pnl.add(Msg_Textbox);
    	pnl.add(Msg_Send_Button);
    	
    }
    
    private void makekilldialog(String name) {
    	String[] answer = {"李ъ꽦", "諛섎�"};
    	int ans = JOptionPane.showOptionDialog(this, name + " �떂�쓣 二쎌씠�떆寃좎뒿�땲源�?", "Kill", JOptionPane.YES_NO_OPTION , JOptionPane.PLAIN_MESSAGE ,  null, answer, answer[0]);
    	System.out.println("ans : "+ans);
    }
    
    private void sendMsg(String msg) {
    	if(msg.equals("choose")) {
    		dl.setVisible(true);
		}
    	if(msg.equals("kill")) {
			makekilldialog(Username);
		}
		if(msg.equals("day")) {
			Chat_Area.setText("");
			daynightchange(true);
		}
		if(msg.equals("night")) {
			Chat_Area.setText("");
			daynightchange(false);
		}
		else {
			Chat_Area.append(Username + ":"+msg+"\n");
		}
    }
    
    private void setNetWorking() {
    	//thread create
    	if(thread == null) {
        	thread = new Thread(new msgReader());
        	thread.start();
        }
    }
    
    private void daynightchange(Boolean day) {
    	if(day) {
    		Chat_Area.setBackground(Color.white);
    		Chat_Area.setForeground(Color.black);
    	} else {
    		Chat_Area.setBackground(Color.black);
    		Chat_Area.setForeground(Color.red);
    	}
    }

    
    public void screenSizeLocation() {
    	frameSize = getSize();
    	screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
    }
    
    public void setMain(MainProcess main) {
        this.main = main;
    }
    
    public class msgReader implements Runnable{
    	public void run() {
    		//�떆怨�
    		
    		//硫붿떆吏�
    		//Chat_Area.append(msg+"\n");
    	}
    }
   
    
    
    
}
