import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SimpleServer extends Thread {
	public InputStream receiver = null;
	public OutputStream sender = null;
	public final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	public final String DB_URL = "jdbc:mysql://127.0.0.1:3306/test1?autoReconnect=true&useSSL=false";
	public final String USER_ID = "root";
	public final String USER_PASSWORD = "1234";
	private final String select = "SELECT";
	private final String insert = "INSERT";
	public static String verify;
//	public static EditorServer editorServer;
	String PW;
	String ID;
	String NAME=" ";
	private String result = "failedfailed";
	public static EditorServer editorServer;
	public SimpleServer(InputStream receiver, OutputStream sender) {
		this.receiver = receiver;
		this.sender = sender;
	}

	public SimpleServer() {
		
	}

	@Override
	public void run() {
		Connection conn = null;
		Statement stmt = null;
		//ResultSet rs = null;
		try {

			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER_ID, USER_PASSWORD);
			System.out.println("\n- MySQL Connection");
			stmt = conn.createStatement();

			// 클라이언트로부터 메시지 대기
			byte[] data = new byte[100];

			receiver.read(data, 0, 100);
			String sql = new String(data);
			sql.replace("\0", "");
			String out = String.format("S.receive - %s", sql);
			System.out.println(out);
			
			verify = sql.substring(0, 6);
			
			
			if (verify.equals(select)) {
				System.out.println("verify condition is select");
				ResultSet rs = stmt.executeQuery(sql);
				try {
					while (rs.next()) {
						ID = rs.getString("id");
						PW = rs.getString("pw");
						NAME = rs.getString("name");
					}
					if (ID.isEmpty()) {
						System.out.println("result is failedfailed");
					}
					
					if(editorServer.useridv.contains(ID)){
						result = "already";
						System.out.println("alredy logined ID");
					}
					else {
						System.out.println("result is name");
						result = NAME;
						//EditorServer editorServer= new EditorServer();
						editorServer.userid=ID;
						editorServer.useridv.addElement(ID);
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					rs.close();
				}
			}
			
			
			else if(verify.equals(insert)) {
				System.out.println("verify condition is insert");
				try {
					stmt.executeUpdate(sql);
					System.out.println("SQL Query is updated");
					result = "true";
				} catch (Exception e) {
					System.out.println("INSERT Exception occur");
					e.printStackTrace();
					result = "fail";
				}
			}				
			
			
			
			System.out.println("Result.toString is "+result.toString());
			data = result.getBytes();

			this.sender.write(data);

			System.out.println("S.send data is " + data);

			
			stmt.close();
			conn.close();

		} catch (SQLException se1) {
			se1.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
			this.close();
		}
		System.out.println("\n\n- MySQL Connection Close");
	}

	public void close() {
		try {
			this.receiver.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		try {
			this.sender.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}


//	public void openlogin() {
	public static void main(String[] args){
		editorServer= new EditorServer();
		System.out.println(editorServer.toString());
		System.out.println("      "  + editorServer.userid);
		System.out.println("null :   " + editorServer.useridv.isEmpty());
		// 자동 close
		try (ServerSocket server = new ServerSocket()) {
			// 서버 초기화
			InetSocketAddress ipep = new InetSocketAddress(9865); //포트포트
			server.bind(ipep);
			System.out.println("Initialize complete");

			while (true) {
				// LISTEN 대기
				Socket client = server.accept();
				System.out.println("Connection");

				// Stream을 쓰레드로 넘기기
				SimpleServer serverThread = new SimpleServer(client.getInputStream(), client.getOutputStream());
				// 쓰레드 시작(run을 호출한다.)
				serverThread.start();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}try{
		//editorServer= new EditorServer();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}