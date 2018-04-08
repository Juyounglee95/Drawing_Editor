import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;



public class EditorCircle  extends EditorShape {
	
	private static final int TYPE = Editor.CIRCLE;
	
	public EditorCircle(String sid, int x, int y, int w, int h, Color color) {
		this.sid = sid;
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.color = color;
		xDiff = yDiff = 0;
		first = true;
		
		
	}
	


	public void paint(Graphics g) {
//		super.paint(g);
		
		
	//	System.out.println(width);
		g.setColor(color);
		
		g.fillOval(x, y, Math.min(width,  height), Math.min(width,  height));
		
		g.drawOval(x, y, Math.min(width,  height), Math.min(width,  height));
		
			
		
		ul = new Rectangle(x - hotspot, y - hotspot, 2 * hotspot, 2 * hotspot); 
		ur = new Rectangle(x + Math.min(width,  height) - hotspot, y - hotspot, 2 * hotspot,
				2 * hotspot);
		bl = new Rectangle(x - hotspot, y + Math.min(width,  height) - hotspot, 2 * hotspot,
				2 * hotspot);
		br = new Rectangle(x + Math.min(width,  height) - hotspot, y + Math.min(width,  height) - hotspot,
				2 * hotspot, 2 * hotspot);
		
		Color color = new Color(0xEDEDED);
		if (selectedd) {
			g.setColor(Color.blue);
			g.fillRect(ul.x, ul.y, ul.width, ul.height);
			g.fillRect(ur.x, ur.y, ur.width, ur.height);
			g.fillRect(bl.x, bl.y, bl.width, bl.height);
			g.fillRect(br.x, br.y, br.width, br.height);
			
		}
		
	}
	
	@Override
	public void setSelected(boolean b) {
		selectedd = b;
	}


	public void setFirst(boolean first) {
		this.first = first;
	}



	public void resize(int xMouse, int yMouse, int type) {
		if (first) {
			first = false;
		}
		
		int ul_w = x - xMouse;
		int ul_h = y - yMouse;

		
			switch (type) {	 
			case  UPPER_LEFT:
				width = Math.abs(width + x - xMouse);
				height = Math.abs(height + y - yMouse);
				//x = xMouse;
				//y = yMouse;
				
				break;
			case  UPPER_RIGHT:
				width = Math.abs(width + xMouse - (x+width));
				height = Math.abs(height + y - yMouse);
				//y = yMouse;
				break;
			case  BOTTOM_LEFT:
				width = Math.abs(width + x - xMouse);
				height = Math.abs(height + yMouse - (y+height));
				//x = xMouse ;
		
				break;
			case  BOTTOM_RIGHT:
				width = Math.abs(xMouse - x);
				height = Math.abs(height + yMouse - (y+height));
			
				break;
			}
		
	}
	public void move(int xD, int yD,  int typeWS){
		x = x -xD; 
		y= y- yD;
		}
	
	public int forcused(int xMouse, int yMouse) {	// 선택한 위치에 따른 모드 반환
		if(ul != null && ul.contains(xMouse, yMouse)) {
			return UPPER_LEFT;
		} else if(ur != null && ur.contains(xMouse, yMouse)) {
			return UPPER_RIGHT;
		} else if(bl != null && bl.contains(xMouse, yMouse)) {
			return BOTTOM_LEFT;
		} else if(br != null && br.contains(xMouse, yMouse)) {
			return BOTTOM_RIGHT;
		} else if(x<=xMouse && xMouse<=x+width && 
			y<=yMouse&&yMouse<=y+height) {
			return MOVE;
		}
		return NONE;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	public Color getColor(){
		return color;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getType(){
		return TYPE;
	}
	
	
	@Override
	public void settingColor(Color color) {
		this.color = color;
	}
//	public void setId(char[] sid){
//		this.sid = sid;
//	}
//	public char[] getId(){
//		return sid;
//	}
//	



	@Override
	public void setId(String sid) {
		// TODO Auto-generated method stub
		this.sid = sid;
	}



	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return sid;
	}
	public void setX(int nx){
		x=nx;
	}


}

		

