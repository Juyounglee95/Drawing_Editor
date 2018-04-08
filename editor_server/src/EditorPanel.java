
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;

public class EditorPanel extends Canvas implements Serializable {
	protected Vector elements; // 그려진 도형을 저장
	protected EditorM selectedOne; // 선택된 도형
	protected int focusType; // 어떻게 선택되었는지 저장
	protected EditorM continueOne; // 계속 그려지는 중인 도형 저장
	protected Vector<EditorM> groupSelect;
	protected EditorGroup group;
	boolean grouping;
	protected EditorM selected;
	boolean copyKey = false;
	// static int settings;
	boolean groupKey = false;
	Color color = Color.BLACK;
	public int xNew, yNew, xStart, yStart;
	public int mode;
	public boolean drag = false;

	public EditorPanel() {
		super();
		elements = new Vector(10, 10); // 10개 크기, 모자르면 10개 추가
		continueOne = null;
		groupSelect = new Vector(10, 10);
		selectedOne = null;
		focusType = EditorShape.NONE;
		addMouseMotionListener(new MouseMotionHandler());
		addMouseListener(new MouseHandler());
	}

	public void group() {
		EditorM gd = null;
		group = new EditorGroup();
		if (groupSelect != null) {
			for (EditorM shape : groupSelect) {
				group.add(shape);
				elements.remove(shape);
			}
			gd = group;
		}
		if (gd != null) { // 생성된 객체가 있으면 벡터에 추가
			add(gd);
		}

		// if (selectedOne != null){ // 선택된 객체가 있으면 선택해제
		// selectedOne.setSelected(false);
		// }
		// if (continueOne == null) {
		// if(xStart-xNew >= 0 && yStart-yNew >= 0){
		// focusType = EditorSquare.UPPER_LEFT;
		// } else if(xStart-xNew > 0 && yStart-yNew < 0){
		// focusType = EditorSquare.BOTTOM_LEFT;
		// } else if(xStart-xNew <= 0 && yStart-yNew > 0){
		// focusType = EditorSquare.UPPER_RIGHT;
		// } else{
		// focusType = EditorSquare.BOTTOM_RIGHT;
		// }
		// selectedOne = d; // 객체 선택
		// selectedOne.setSelected(true);
		// }
		Editor.setMode(Editor.NONE);

	}

	public void delete() {
		if (!groupSelect.isEmpty()) {
			for (int p = 0; p < groupSelect.size(); p++)
				elements.remove(groupSelect.get(p));
			repaint();

			System.out.println("group deleted");
		} else if (selectedOne != null) {
			elements.remove(selectedOne);// 벡터에서 삭제
			selectedOne = null;
			repaint();
		}

	}

	public void clear() { // 벡터의 모든 객체 삭제
		elements.removeAllElements();
		selectedOne = null;
		// groupSelect = null;
		repaint();
	}

	public void paint(Graphics g) {
		int n = elements.size(); // 벡터의 크기

		for (int i = 0; i < n; i++) { // 벡터에 저장된 모든 객체를 그림
			EditorM d = (EditorM) elements.elementAt(i);
			d.paint(g);
		}

	}

	public void add(EditorM d) {
		elements.addElement(d); // 벡터에 추가
	}

	public class MouseMotionHandler extends MouseMotionAdapter implements
			KeyListener {

		public void mouseDragged(MouseEvent e) { // 마우스 드래그
			// 커서의 위치를 저장
			drag = true;
			if (continueOne != null) { // 그려지고 있는 도형이 있으면 종료
				return;
			}

			xNew = e.getX();
			yNew = e.getY();
			int n = elements.size();
			System.out.println("X:" + xNew + "y:" + yNew);

			EditorM d = null;
			if (selectedOne == null) { // 선택된 도형이 없으면
				for (int i = 0; i < n; i++) {
					d = (EditorM) elements.elementAt(i);
					focusType = d.forcused(xNew, yNew); // 커서의 위치에 선택되어질 도형이 있나
														// 확인
					if (focusType > 0) { // 선택될 도형이 있으면
						if (groupKey == true) {
							groupSelect.add(d);
						} else {
							selectedOne = d;
						}
						break;

					}
				}
			}

			if (selectedOne != null) { // 선택된 도형이 있으면
				switch (focusType) { // 포커스 타입에 따라 이동, 리사이즈 결정
				case EditorShape.NONE:
					// case EditorShape.MOVE:
					// System.out.printf("xnew : %d  x : %d   ynew = %d   y= %d   ",
					// xNew, xStart, yNew, yStart);
					// selectedOne.move(xStart-xNew, yStart-yNew, focusType);
					// repaint(); break;
				case EditorShape.UPPER_LEFT:
				case EditorShape.UPPER_RIGHT:
				case EditorShape.BOTTOM_LEFT:
				case EditorShape.BOTTOM_RIGHT:
					selectedOne.resize(xNew, yNew, focusType);
					//Editor.sendMsg(elements);
					repaint();
					break;
				}
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == e.VK_CONTROL)
				groupKey = true;

		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == e.VK_CONTROL)
				groupKey = false;

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

	public class MouseHandler extends MouseAdapter implements KeyListener {
		MouseHandler() {
			addKeyListener(this);
		}

		public void mousePressed(MouseEvent e) { // 마우스 버튼이 눌리면
			// 눌려진 위치 저장
			xStart = e.getX();
			yStart = e.getY();
			System.out.println(xStart);
			int n = elements.size();
			EditorM d = null;
			int mode = Editor.getMode();

			if (continueOne != null) {
				Editor.setMode(Editor.NONE);
				return;
			}
			if (mode != Editor.NONE) {
				switch (mode) {
				case Editor.SQUARE:
					d = new EditorSquare(xStart, yStart, 0, 0); // 사각형 생성
					break;
				case Editor.LINE:
					d = new EditorLine(xStart, yStart, 0, 0); // 직선 생성
					break;
				case Editor.CIRCLE:
					d= new EditorCircle(xStart, yStart, 0,0);
					break;
				case Editor.PEN:
					
					break;
				case Editor.COLOR:
					mode = 1;
					repaint();
					break;

			//	case Editor.LINECOLOR:
				//	mode = 2;
					//repaint();
					//break;
				// case Editor.GROUP:
				// group = new EditorGroup();
				// if(groupSelect != null){
				// for(EditorM shape : groupSelect){
				// group.add(shape);
				// elements.remove(shape);
				// }
				// d = group;
				// } break;
				default:
					return;
				}
				// Editor.setMode(Editor.NONE); // 그리기를 완료했으므로 모드변경

				if (d != null ) { // 생성된 객체가 있으면 벡터에 추가
					add(d);
//					Editor.sendMsg(elements);
					System.out.println("asdasdsad  : "+elements);
				}

				if (selectedOne != null) { // 선택된 객체가 있으면 선택해제
					selectedOne.setSelected(false);
				}

				if (continueOne == null) {
					if (xStart - xNew >= 0 && yStart - yNew >= 0) {
						focusType = EditorSquare.UPPER_LEFT;
					} else if (xStart - xNew > 0 && yStart - yNew < 0) {
						focusType = EditorSquare.BOTTOM_LEFT;
					} else if (xStart - xNew <= 0 && yStart - yNew > 0) {
						focusType = EditorSquare.UPPER_RIGHT;
					} else {
						focusType = EditorSquare.BOTTOM_RIGHT;
					}
					selectedOne = d; // 객체 선택
					selectedOne.setSelected(true);

				}

				repaint();
				return;
			}

			for (int i = 0; i < n; i++) { // 벡터의 모든 객체를 검사해서 선택위치에 있으면 선택
				d = (EditorM) elements.elementAt(i);
				focusType = d.forcused(xStart, yStart);

				if (focusType > 0) {
					if (groupSelect.isEmpty()) {
						for (int k = 0; k < groupSelect.size(); k++) {
							groupSelect.get(k).setSelected(false);
							groupSelect.remove(k);
						}
					}
					if (selectedOne != null) {
						selectedOne.setSelected(false);
					}

					selectedOne = d;
					if (groupKey == true) {
						groupSelect.add(d);
					} else {
						selectedOne.setSelected(true);
					}
					for (int j = 0; j < groupSelect.size(); j++) {
						groupSelect.get(j).setSelected(true);
					}
					repaint();
					return;
				}
			}

			if (selectedOne != null) {
				selectedOne.setSelected(false);
				for (int j = 0; j < groupSelect.size(); j++) {
					groupSelect.get(j).setSelected(false);
					groupSelect.remove(j);
				}
				repaint();
			}
			for (int j = 0; j < groupSelect.size(); j++) {
				groupSelect.get(j).setSelected(false);
				groupSelect.remove(j);
			}
			selectedOne = null;
		}

		public void mouseReleased(MouseEvent e) { // 마우스 버튼이 올라올 때

			if (selectedOne != null) {
				switch (focusType) {
				case EditorShape.MOVE:
					System.out.printf(
							"xnew : %d  x : %d   ynew = %d   y= %d   ", xNew,
							xStart, yNew, yStart);
					if (drag) {
						selectedOne.move(xStart - xNew, yStart - yNew,
								focusType);
						repaint();
					}
					break;
				}

				selectedOne.setFirst(true); // 이동, 리사이즈 종료되었으므로 first를 true로 설정
			}
			if (!groupSelect.isEmpty()) {
				for (int i = 0; i < groupSelect.size(); i++) {
					groupSelect.get(i).setFirst(true);
				}
			}
			focusType = EditorShape.NONE;
			drag = false;

			/*
			 * endX = e.getX(); endY = e.getY();
			 */}

		boolean cv = false;
		EditorM cvd = null;
		Color cv_color;

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
//			if (e.getKeyCode() == e.VK_CONTROL){
//				System.out.println("asd");
//				groupKey = true;
				if (e.getKeyCode() == e.VK_C && e.isControlDown()){
					groupKey = true;
					System.out.println("aaaaaaaa");
					if (selectedOne != null) {
						System.out.println(selectedOne.toString());
						// System.out.println(selectedOne.toString().startsWith("EditorSquare"));
						// System.out.println("selected");
						if (selectedOne.toString().startsWith(
								"test2.EditorSquare")) { //have to change package name : xxx.EditorSquare
							cv = true;
							cvd = new EditorSquare(30, 30,
									selectedOne.getWidth(),
									selectedOne.getHeight());
							cv_color = selectedOne.getColor();
							// System.out.println("color");
							System.out.println(selectedOne.getColor());

							System.out.println("yayayayyayaayya");
						} else if (selectedOne.toString().startsWith(
								"test2.EditorLine")) { //have to change package name : xxx.EditorLine
							cv = true;
							cvd = new EditorLine(30, 30,
									selectedOne.getWidth(),
									selectedOne.getHeight());
							cv_color = selectedOne.getColor();
						}
					}
				}
				if (e.getKeyCode() == e.VK_V && e.isControlDown()) {
					groupKey = true;
					System.out.println("vvvvvvv");
					if (selectedOne != null && cv == true) {
						System.out.println(selectedOne.toString());
						// d = new EditorSquare(30, 30,
						// selected.getWidth(),selected.getHeight());
						cvd.settingColor(cv_color);
						System.out.println(cv_color);
						if (cvd != null) { // 생성된 객체가 있으면 벡터에 추가
							add(cvd);
						}

						if (selectedOne != null) { // 선택된 객체가 있으면 선택해제
							selectedOne.setSelected(false);
						}

						if (continueOne == null) {
							if (xStart - xNew >= 0 && yStart - yNew >= 0) {
								focusType = EditorSquare.UPPER_LEFT;
							} else if (xStart - xNew > 0 && yStart - yNew < 0) {
								focusType = EditorSquare.BOTTOM_LEFT;
							} else if (xStart - xNew <= 0 && yStart - yNew > 0) {
								focusType = EditorSquare.UPPER_RIGHT;
							} else {
								focusType = EditorSquare.BOTTOM_RIGHT;
							}
							selectedOne = cvd; // 객체 선택
							selectedOne.setSelected(true);
						}
						cv = false;
						repaint();

					}
				}
				if (e.getKeyCode() == e.VK_G && e.isControlDown())
					groupKey = true;
					group();
			}
		//}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

	}

	public void setColor() {
		if (!groupSelect.isEmpty()) {
			color = JColorChooser.showDialog(this, "Color", color);
			if (color != null) {
				for (int i = 0; i < groupSelect.size(); i++) {
					groupSelect.get(i).settingColor(color);
				}
			}
		} else {

			color = JColorChooser.showDialog(this, "Color", color);
			if (color != null) {
				selectedOne.settingColor(color);
			}
		}
		repaint();

	}

}
