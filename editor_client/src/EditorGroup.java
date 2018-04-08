

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.Vector;


public class EditorGroup extends EditorShape {
	private static final int TYPE = Editor.GROUP;
	private Vector<EditorShape> groups  = new Vector<EditorShape>();
	
	public EditorGroup(){
		
	}

	public void add(EditorShape g){
		groups.addElement(g);
	}
	public void move(int xD, int yD, int typeWS){
		for (EditorShape shape : groups) {	
			shape.move(xD, yD, typeWS);
		}
	}
	@Override
	public void paint(Graphics g) {
		for (EditorShape shape : groups) {	
			shape.paint(g);
		}
	}


	@Override
	public void resize(int x, int y, int type) {
		for (EditorShape shape : groups) {	
			shape.resize(x, y, type);
		}

		
	}

	@Override
	public int forcused(int x, int y) {
		int i = 0;
		
		for (EditorShape shape : groups) {
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
		for (EditorShape shape : groups) {
			shape.setSelected(b);
		}
		
		
	}

	@Override
	public void settingColor(Color color) {
		for (EditorShape shape : groups) {	
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

	@Override
	public void setId(String sid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setX(int nx){
		x=nx;
	}

	

}
