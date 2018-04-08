



import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

public class Client_list extends Thread {
	Socket s;
	EditorServer server;
	ObjectOutputStream out;
	ObjectInputStream in;
	String id;

	public Client_list() {
	}

	public Client_list(Socket s, EditorServer server, String id) {
		this.id=id; 
		this.s = s;
		this.server = server;
		try {
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
		} catch (Exception e) {
		}
	}

	@Override
	public void run() {
		//Object ob= null;
		EditorM elements = null;
		bk: while (true) {
			System.out.println("!23123");
			try {
				//ob = in.readObject();
//				System.out.println("asdasd:"+in.readObject().toString());
//				System.out.println(ob);
//				elements = (Vector)ob;
				elements =  (EditorM)in.readObject();
				System.out.println("kokokokokokoko  "+elements+"123");
//				switch (elements.getCmd()) {
//				case 1:	// 종료
//					out.writeObject(p);
//					out.flush();
//					server.del(this);
//					break;
//				case 2:	// 그리기
//					server.sendMsg(p);
//					break;
//				}
				server.sendMsg(elements, id);
				System.out.println("("+id+") sendMSG  :   " + elements+" x : "+ elements.getX()+" color : "+ elements.getColor()+" width : " + elements.getWidth());
			} catch (Exception e){
				System.out.println("execption");
				e.printStackTrace();
			}

		}
	}
}
