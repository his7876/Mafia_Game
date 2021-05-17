
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class Sign_Up_Frame extends JFrame {
    private MainProcess main;
    
    private JLabel lb1;
    private JLabel lb2;
    private JLabel lb3;
    private JTextField Id_Textbox;
    private JPasswordField Password_Textbox;
    private JPasswordField Password_Check_Textbox;
    private JButton Signup_Button;
    private boolean bPasswordCheck;

    public Sign_Up_Frame(){
        Init();
    }

    private void Init(){
        setTitle("Sign Up");
        setSize(280,190);
        setLocation(800,450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel pnl = new JPanel();
        setDisplay(pnl);

        add(pnl);

        setVisible(true);
    }

    private void setDisplay(JPanel pnl){
    	pnl.setLayout(null);
    	
        lb1 = new JLabel("Id");        
        lb2 = new JLabel("Pass");        
        lb3 = new JLabel("PassCheck");       
        Id_Textbox = new JTextField(20);        
        Password_Textbox = new JPasswordField(20);
       
        Password_Check_Textbox = new JPasswordField(20);
        Password_Check_Textbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isCorrectinput();
            }
        });

        
        Signup_Button = new JButton("Sign Up");
        Signup_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });
        
        lb1.setBounds(10, 10, 80, 25);
        lb2.setBounds(10, 40, 80, 25);
        lb3.setBounds(10, 70, 80, 25);
        Id_Textbox.setBounds(100,10,160,25);
        Password_Textbox.setBounds(100,40,160,25);
        Password_Check_Textbox.setBounds(100,70,160,25);
        Signup_Button.setBounds(160, 110, 100, 25);
        
        pnl.add(lb1);
        pnl.add(lb2);
        pnl.add(lb3);
        pnl.add(Id_Textbox);
        pnl.add(Password_Textbox);
        pnl.add(Password_Check_Textbox);
        pnl.add(Signup_Button);
       
    }
    
    private void isCorrectinput() {
    	//비밀번호 check
    }
    
    private void signUp() {
    	//sign up
    	if(Id_Textbox.getText().equals("") && new String(Password_Textbox.getPassword()).equals("")){
            JOptionPane.showMessageDialog(this, "회원가입 성공", "회원가입", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        }else{
            JOptionPane.showMessageDialog(this, "회원가입 실패", "회원가입", JOptionPane.ERROR_MESSAGE);
        }
    	
    }

}
