import java.sql.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

class ConnectTest extends JFrame implements ActionListener {
	Editor editor;
	private JTextField tf;
	private JPasswordField pf;
	private JButton login, register;
	JLabel loginText = new JLabel();
	private boolean isLogin = false;
	static String fail="failedfailed";
	static String res = "failedfailed";
	static String result = "";
	
	public ConnectTest() {
		JPanel idPanel = new JPanel();
		JPanel passPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel msgPanel = new JPanel();
		JPanel mainPanel = new JPanel();
		tf = new JTextField(12);
		pf = new JPasswordField(10);
		loginText.setForeground(Color.RED);

		JLabel idLabel = new JLabel("ID : ");
		JLabel passLabel = new JLabel("PASSWORD : ");
		login = new JButton("LOGIN");
		register = new JButton("REGISTER");
		
		login.addActionListener(this);
		register.addActionListener(this);

		idPanel.add(idLabel);
		idPanel.add(tf);

		passPanel.add(passLabel);
		passPanel.add(pf);
		
		buttonPanel.add(login);
		buttonPanel.add(register);
		msgPanel.add(loginText);
		
		
		this.add(mainPanel);
		mainPanel.add(idPanel);
		mainPanel.add(passPanel);
		mainPanel.add(buttonPanel);
		mainPanel.add(msgPanel);
/**
		this.add(idPanel);
		this.add(passPanel);
		this.add(buttonPanel);
		this.add(msgPanel);
**/
		mainPanel.setLayout(new GridLayout(4,1));
		mainPanel.setBackground(Color.BLUE);
		setTitle("LOGIN");
		setSize(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == login) {
			try {
	            res = connectt(tf.getText(), pf.getText());
	            System.out.println("in action Performed res : "+res);
	            if(res.startsWith("already")){
	            	loginText.setText("�̹� �α��� �� ID�Դϴ�.");
	            }
	            else if (res.equals(fail)) {
	               loginText.setText("ID �Ǵ� password�� �߸��Ǿ����ϴ�.");
	            }else if(res.isEmpty()){
	               loginText.setText("������ �߻��Ͽ����ϴ�.");
	            }else {
	               String hello = "�ݰ����ϴ�,  "+result;
	               loginText.setText(hello);
	               
	               this.setVisible(false);
	              
	               editor= new Editor(tf.getText());
	               editor.jname.setText(" " + tf.getText()+" �� ");
	               
	            }
			} catch (Exception e1) {
				loginText.setText("������ �߻��Ͽ����ϴ�.");
				System.out.println(res);

			}
		}
		if(e.getSource()==register){
			//this.setVisible(false);
			//new RegisterTest();
			
		}
		
	}

	public static String connectt(String _id, String _pw) {
		String sql_origin1 = "SELECT id, pw, name FROM user WHERE id = \"";
		String id = _id; // ������
		String sql_origin2 = "\" and pw = \"";
		String pw = _pw;
		String sql_origin3 = "\";";
		String sql = sql_origin1 + id + sql_origin2 + pw + sql_origin3;
		// sql ������ ��
		try (Socket client = new Socket()) {
			// Ŭ���̾�Ʈ �ʱ�ȭ
			InetSocketAddress ipep = new InetSocketAddress("127.0.0.1",9994);  //��Ʈ��Ʈ
			// ����
			client.connect(ipep);

			// send,receiver ��Ʈ�� �޾ƿ���
			// �ڵ� close
			try (OutputStream sender = client.getOutputStream(); InputStream receiver = client.getInputStream();) {

				// ������ ������ ������
				byte[] data = sql.getBytes();
				sender.write(data, 0, data.length);
				
				System.out.println("C.send data is "+data);
				
				// �����κ��� ������ �ޱ�
				data = new byte[12];
				receiver.read(data, 0, 12);
				result= new String(data);

				String out = String.format("C.receive result - %s", result);
				System.out.println(out);
				
			}
			//client.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}

		System.out.println("\n\n- MySQL Connection Close");
		System.out.println("In connectt result : "+result);
		
		return result;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConnectTest a= new ConnectTest();
		
		
		

	}

}