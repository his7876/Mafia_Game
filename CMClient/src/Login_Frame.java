
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Login_Frame extends JFrame{
	
	ClientController controller;
    //private MainProcess main;
	private Dimension frameSize, screenSize;
	
    private JLabel userLabel;
    private JLabel passLabel;
    private JTextField Id_Textbox;
    private JPasswordField Password_Textbox;
    private JButton Login_Button;
    private JButton Signup_Button;
    private boolean bLoginCheck;

    public Login_Frame(ClientController controller){
    	this.controller = controller;
        Init();
    }

    private void Init(){
        setTitle("Login Page");
        setSize(280,150);
        screenSizeLocation();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel pnl = new JPanel();
        setDisplay(pnl);

        add(pnl);

        setVisible(true);
    }

    private void setDisplay(JPanel pnl){
        pnl.setLayout(null);
        
        userLabel = new JLabel("Id");
        passLabel = new JLabel("Password");      
        Id_Textbox = new JTextField(20);
        
        Password_Textbox = new JPasswordField(20);
        Password_Textbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isLoginCheck();
            }
        });

        Login_Button = new JButton("Login");
        Login_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isLoginCheck();
            }
        });

        Signup_Button = new JButton("Sign Up");
        Signup_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	Sign_Up_Frame sign_up_frame = new Sign_Up_Frame(controller);
  //              main.showSignUpFrame();
            }
        });
        
        userLabel.setBounds(10, 10, 80, 25);
        passLabel.setBounds(10, 40, 80, 25);
        Id_Textbox.setBounds(100,10,160,25);
        Password_Textbox.setBounds(100,40,160,25);
        Login_Button.setBounds(160, 80, 100, 25);
        Signup_Button.setBounds(10, 80, 100, 25);
        
        pnl.add(userLabel);
        pnl.add(passLabel);
        pnl.add(Id_Textbox);
        pnl.add(Password_Textbox);
        pnl.add(Login_Button);
        pnl.add(Signup_Button);
        

    }

    public void isLoginCheck(){
   
    	String uname = Id_Textbox.getText();
    	String upwd = new String(Password_Textbox.getPassword());
    	
    	if(controller.loginCM(uname, upwd)) {
    		 JOptionPane.showMessageDialog(this, "Login Success");
             bLoginCheck = true;

             if(isLogin()){
            	   Main_Frame main_frame = new Main_Frame((String)Id_Textbox.getText(),controller);                   
    //             main.showMainFrame((String)Id_Textbox.getText());
            	   
             }
    	}
    	else {
            JOptionPane.showMessageDialog(this, "Login Faild", "Fail", JOptionPane.ERROR_MESSAGE);

    	}
    	
    	/**
        if(Id_Textbox.getText().equals("test") && new String(Password_Textbox.getPassword()).equals("1234")){
            JOptionPane.showMessageDialog(this, "Login Success");
            bLoginCheck = true;

            if(isLogin()){
                main.showMainFrame((String)Id_Textbox.getText());
            }
        }else{
            JOptionPane.showMessageDialog(this, "Login Faild", "Fail", JOptionPane.ERROR_MESSAGE);
        }
        
        **/
    }

    public void screenSizeLocation() {
    	frameSize = getSize();
    	screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
    }
    
//    public void setMain(MainProcess main) {
 //       this.main = main;
 //   }

    public boolean isLogin() {
        return bLoginCheck;
    }


}
