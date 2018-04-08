
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class Editor extends JFrame implements ActionListener, Runnable {

	public static final int NONE = 0;
	public static final int LINE = 1;
	public static final int SQUARE = 2;
	public static final int GROUP = 3;
	public static final int COLOR = 4;
	// public static final int FILLCOLOR = 5;
	public static final int CIRCLE = 5;
	public static final int PEN = 6;
	public static int mode = NONE;

	JMenu file, edit;
	JMenuItem save, load, undo, redo;
	JButton lineB, squareB, circleB, ColorB, penB, select;
	JMenuItem groupB, delete, alldel;
	JLabel jname;
	
	Thread t;
	Socket s;
	ObjectInputStream vois;
	static ObjectOutputStream voos;
	InputStream vis;
	OutputStream vout;	
	
	

	public EditorPanel space;
	public static int colormode;

	private EditorLine line;
	public String sid;
	public Editor(String sid) {
		
		super("Drawing Editor");
		this.sid = sid;
		JMenuBar bar = new JMenuBar();
		JToolBar tool = new JToolBar("Object Menu");
		tool.setBackground(Color.white);
		file = new JMenu("File");
		edit = new JMenu("Edit");
		save = new JMenuItem("Save");
		load = new JMenuItem("Open");
		// undo = new JMenuItem("실행 취소");
		// redo = new JMenuItem("되돌리기");

		save.addActionListener(this);
		load.addActionListener(this);
		// undo.addActionListener(this);
		// redo.addActionListener(this);

		file.add(save);
		file.add(load);
		// edit.add(undo);
		// edit.add(redo);

		bar.add(file);
		bar.add(edit);
		setJMenuBar(bar);

		setLayout(new BorderLayout());
		// JPanel buttonP = new JPanel();
		// add(buttonP, BorderLayout.SOUTH);

		edit.add(delete = new DeleteCommand("Delete", this));
		edit.add(alldel = new AlldelCommand("Clear",this));
		//tool.setLayout(new BorderLayout());
		tool.add(lineB = new LineButton("Line",this));
		tool.add(squareB = new SquareButton("Rectangle",this));
		tool.add(circleB = new CircleButton("Circle",this));
		tool.add(penB = new PenButton("Pen",this));
		tool.add(ColorB = new ColorButton("Color",this));
		tool.add(select = new SelectButton("Select",this));
		tool.add(jname=new JLabel("123123"));

		edit.add(groupB = new JMenuItem("Group"));

		alldel.addActionListener(this);
		delete.addActionListener(this);
		select.addActionListener(this);
		lineB.addActionListener(this); // 이벤트 핸들러 추가
		squareB.addActionListener(this);
		ColorB.addActionListener(this);
		groupB.addActionListener(this);
		penB.addActionListener(this);
		circleB.addActionListener(this);

		// add("South", buttonP);

		add(tool, BorderLayout.NORTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setSize(1000, 1000);
		//this.sid = sid;
		space = new EditorPanel();
		space.setId(sid);
		add(space, BorderLayout.CENTER);

		setVisible(true);

		load.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String path = ".";
				JFileChooser jfc = new JFileChooser(path);

				int cmd = jfc.showOpenDialog(space);
				if (cmd == JFileChooser.APPROVE_OPTION) {

					FileInputStream fis = null;
					File select_file = jfc.getSelectedFile();

					try {
						fis = new FileInputStream(select_file);
						ObjectInputStream ois = new ObjectInputStream(fis);

						try {
							space.elements = (Vector) ois.readObject();
							space.repaint();

							fis.close();
							ois.close();

						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}

					} catch (IOException | ClassNotFoundException e1) {
						e1.printStackTrace();
					}

				}

			}

		});

		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String path = ".";
				JFileChooser jfc = new JFileChooser(path);

				int cmd = jfc.showSaveDialog(space);
				if (cmd == JFileChooser.APPROVE_OPTION) {
					FileOutputStream fos = null;
					File saved_file = jfc.getSelectedFile();

					try {
						fos = new FileOutputStream(saved_file);

						ObjectOutputStream oos = new ObjectOutputStream(fos);

						oos.writeObject(space.elements);

						fos.close();
						oos.close();

					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}

			}

		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (s != null) { // 접속이 된 상태
					try{
						s.close();
					} catch (Exception e2) {
					}
				} else { // 접속하지 않은 상태
					try {
						s.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					t.stop(); t.interrupt();
					System.exit(0);
				}
			}
		});
		
		connected();
	}

	public static int getMode() { // mode를 받는다
		return mode;
	}

	public static void setMode(int m) { // mode 설정
		mode = m;
	}

	public void actionPerformed(ActionEvent e) { // 버튼 클릭이벤트
		Command command = (Command) e. getSource();
		command.execute();
	}

//	public static void main(String[] args) {
//		ConnectTest login= new ConnectTest();
//		
//		new Editor();
//	}

//	public void sendMsg(int x, int y, int h, int w, int type, Color c){
//		ShapeProtocol p = new ShapeProtocol(x, y, h, w, type, c);
//		try {
//			oos.writeObject(p);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			
//			e.printStackTrace();
//			System.out.println("msg 에러");
//		}
//		
//	}
	
	public static void sendMsg(EditorShape s_elements){
		try{
	
		voos.writeObject(s_elements);
		voos.flush();
		voos.reset();
		s_elements.setSelected(false);
		System.out.println("editor sendmsg: "+s_elements);
		System.out.println("sendmsg:"+s_elements.getColor()+":"+s_elements.getX()+":"+s_elements.getY()+":"+s_elements.getHeight());

		}catch(IOException e){
			e.printStackTrace();
			System.out.println("msg 에러");
		}
	}

	public void connected(){
		try {
			System.out.println("conn");
			s= new Socket("127.0.0.1", 9995);   //  포트포트포트
			System.out.println("Connected");
			vis= s.getInputStream();
			vout = s.getOutputStream();
			vois = new ObjectInputStream(vis);
			voos = new ObjectOutputStream(vout);
			t= new Thread(this);
			t.start();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void run() {
		//Object ob = null;
		//Vector s_elements =null;
		EditorShape ed=null;
		bk:while(true){
			try {
				ed = (EditorShape)vois.readObject();
				//s_elements = (Vector)ob;
				//System.out.println("s_element !!!! : "+s_elements);
//				for(int i=0; i<s_elements.size(); i++){
//					System.out.println("   "+i+"   " +s_elements.elementAt(i).toString());
//					space.elements =  (Vector) s_elements.elementAt(i); ////
//					space.repaint();
//				}
				
				if(ed.getX()!=-999){
					
					String ed_name = ed.getId();
				
					System.out.println("ed_name : "+ed_name);
					if(space.elements.size()!=0){
						int cnt=0;
						for(int i=0 ; i<space.elements.size(); i++){
							EditorShape sem = (EditorShape)space.elements.elementAt(i);
							System.out.println("debug 1122"+sem.getId() +" :  "+ed_name);

							if(sem.getId().equals(ed_name)){							
								cnt=1;
								System.out.println("space change"+sem.getId()+"&&&&"+ed_name);
								System.out.println("sem: "+sem.getColor()+":"+sem.getX()+":"+sem.getY()+":"+sem.getHeight());
								System.out.println("ed:"+ed.getColor()+":"+ed.getX()+":"+ed.getY()+":"+ed.getHeight());


								space.elements.set(i, ed);
								EditorShape dsem= (EditorShape)space.elements.elementAt(i);
								System.out.println("dsem:"+dsem.getColor()+":"+dsem.getX()+":"+dsem.getY()+":"+dsem.getHeight());
								space.repaint();
								break;
								}
							}						
						if(cnt==0){
						space.elements.add(ed);}
					}else{
					space.elements.add(ed);
					}
					ed.setSelected(false);
					space.repaint();
					
				}else if(ed.getX()==-898){
					space.elements.remove(ed);
				}
				else{
					space.clear();
				}
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}