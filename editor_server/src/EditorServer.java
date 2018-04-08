

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;



public class EditorServer implements Runnable{
	ServerSocket ss;
	Socket s;
	Thread t;
	ArrayList<Client_list> list = new ArrayList<Client_list>();
	String userid;
	Vector<String> useridv;
	
	
	public EditorServer() {
		useridv= new Vector(10,10);
		try {
			ss= new ServerSocket(10005); //포트포트
			t = new Thread(this);
			t.start();
			
			System.out.println("서버 대기중....");
		} catch (Exception e) {
		}
	}
	
//		public static void main(String[] args) {
//		new EditorServer();
//		SimpleServer simpleserver = new SimpleServer();
//		simpleserver.openlogin();
//		simpleserver.start();
//	}

	public void sendMsg(EditorM p, String id){
		
		try {
			
			Iterator<Client_list> it = list.iterator();
			/*
			for(Client_list k: list){
				System.out.println("쏜다");
				k.out.writeObject(p);
				k.out.flush();
			}*/
			
			while(it.hasNext()){
				Client_list k= it.next();
				System.out.println(k);
				if(!k.id.equals(id)){
				k.out.writeObject(p);
				k.out.flush();
				System.out.println(k.getName()+" "+k.server+" "+k.s+" ");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
	
	public void del(Client_list p){
		useridv.remove(p.id);
		list.remove(p);
	}

	@Override
	public void run() {
		while(true){
			try {
				System.out.println("대기대기");
				s = ss.accept();
				System.out.println("ss accepted");
				Client_list c_list = new Client_list(s, this, userid);
				list.add(c_list);	
				c_list.start();
			} catch (Exception e) {
			}
		}
	}
}
