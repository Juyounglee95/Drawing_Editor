

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.Vector;


public class EditorGroup implements EditorM, Serializable{
	private static final int TYPE = Editor.GROUP;
	private Vector<EditorM> group  = new Vector<EditorM>();
	
	public EditorGroup(){
		
	}

	public void add(EditorM g){
		group.addElement(g);
	}
	public void move(int xD, int yD, int typeWS){
		for (EditorM shape : group) {	
			shape.move(xD, yD, typeWS);
		}
	}
	@Override
	public void paint(Graphics g) {
		for (EditorM shape : group) {	
			shape.paint(g);
		}
	}


	@Override
	public void resize(int x, int y, int type) {
		for (EditorM shape : group) {	
			shape.resize(x, y, type);
		}

		
	}

	@Override
	public int forcused(int x, int y) {
		int i = 0;
		
		for (EditorM shape : group) {
			i = shape.forcused(x, y);
			if(i != 0){
				break;
			}
		}
		return i;
	}

	@Override
	public void setFirst(boolean first) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSelected(boolean b) {
		for (EditorM shape : group) {
			shape.setSelected(b);
		}
		
		
	}

	@Override
	public void settingColor(Color color) {
		for (EditorM shape : group) {	
			shape.settingColor(color);
		}		
	}
	


	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}
	public Color getColor(){
		return Color.BLACK;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}
	public int getX(){
		return 0;
	}
	public int getY(){
		return 0;
	}
	public int getType(){
		return TYPE;
	}
	

	

}
