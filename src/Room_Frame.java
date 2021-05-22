
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

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
	
    public Room_Frame() {
    	Init();
    }
    
    private void Init() {
    	setTitle("마피아 게임");
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
    	
    	Clock_Label = new JLabel("100초");
    	Clock_Label.setForeground(Color.red);
    	
    	Msg_Textbox = new JTextField();
    	Msg_Textbox.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent arg0) {
    			String msg = Msg_Textbox.getText();
    			sendMsg(msg);
    			Msg_Textbox.setText("");
    			Msg_Textbox.requestFocus();
    		}
    	});
    	
    	Msg_Send_Button = new JButton("보내기");
    	Msg_Send_Button.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent arg0) {
    			String msg = Msg_Textbox.getText();
    			sendMsg(msg);    			
    			Msg_Textbox.setText("");
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
    
    private void sendMsg(String msg) {
    	Chat_Area.append("test:"+msg+"\n");
    }
    
    private void setNetWorking() {
    	//thread create
    	if(thread == null) {
        	thread = new Thread(new msgReader());
        	thread.start();
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
    		//시계
    		
    		//메시지
    		//Chat_Area.append(msg+"\n");
    	}
    }
}
