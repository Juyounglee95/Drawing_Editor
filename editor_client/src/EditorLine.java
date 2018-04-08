import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;


public class EditorLine extends EditorShape {

	private static final int TYPE = Editor.LINE;
	
	public EditorLine(String sid, int x, int y, int w, int h, Color color) {
		this.sid = sid;
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.color = color;
		xDiff = yDiff = 0;
		first = true;
//
	}

	public void paint(Graphics g) {
		Color fg = g.getColor();
		g.setColor(color);
//		super.paint(g);
		g.drawLine(x, y, x + width, y + height); // 직선을 그린다

		// 선택영역 설정
		ul = new Rectangle(x - hotspot, y - hotspot, 2 * hotspot, 2 * hotspot);
		
		br = new Rectangle(x + width - hotspot, y + height - hotspot,	2 * hotspot, 2 * hotspot);

		
		if (selectedd) {
/*			g.setColor(color); // ur, bl은 안보임
			g.fillRect(ur.x, ur.y, ur.width, ur.height);
			g.fillRect(bl.x, bl.y, bl.width, bl.height);
*/
			g.setColor(Color.ORANGE); // ul, br은 보임
			g.fillRect(ul.x, ul.y, ul.width, ul.height);
			g.fillRect(br.x, br.y, br.width, br.height);
		}

	}

	public boolean select(int x, int y) {
		return distToSegment(x, y, ul.x, ul.y, br.x, br.y) < 5;
	}

	public double sqr(double x) {
		return x * x;
	}

	public double dist2(int ulx, int uly, int brx, int bry) {
		return sqr(ulx - brx) + sqr(uly - bry);
	}

	public double distToSegmentSquared(int x, int y, int ulx, int uly, int brx, int bry) {
		double l2 = dist2(ulx, uly, brx, bry);
		if (l2 == 0) {
			return dist2(x, y, ulx, uly);
		}
		double t = ((x - ulx) * (brx - ulx) + (y - uly) * (bry - uly)) / l2;
		if (t < 0) {
			return dist2(x,y , ulx, uly);
		}
		if (t > 1) {
			return dist2(x,y , brx, bry);
		}

		return dist2(x, y, (int) (ulx + t * (brx - ulx)), (int) (uly + t * (bry - uly)));
	}

	public double distToSegment(int x, int y, int ulx, int uly, int brx, int bry) {
		return Math.sqrt(distToSegmentSquared(x, y, ulx, uly, brx, bry));
	}
	
	public int forcused(int xMouse, int yMouse) {
		if (ul != null && ul.contains(xMouse, yMouse)) { // ul, br만 선택가능
			return UPPER_LEFT;
		} else if (br != null && br.contains(xMouse, yMouse)) {
			return BOTTOM_RIGHT;
		} else if (select(xMouse, yMouse)){
			return MOVE;
		}
		
		
		return NONE;
	}

	public void resize(int xMouse, int yMouse, int type) {
		if (first) {
			first = false;
		}

		switch (type) { // ul, br만 리사이즈 가능
	      case UPPER_LEFT:
	         width = (width + x - xMouse);
	         height = (height + y - yMouse);
	         x = xMouse;
	         y = yMouse;
	         break;
	      case  UPPER_RIGHT:
	         width = Math.abs(width + xMouse - (x+width));
	         height = Math.abs(height + y - yMouse);
	         y = yMouse;
	         break;
	         
	      case  BOTTOM_LEFT:
	         width = Math.abs(width + x - xMouse);
	         height = Math.abs(height + yMouse - (y+height));
	         x = xMouse ;
	   
	         break;
	      case BOTTOM_RIGHT:
	         width = (width + xMouse - (x + width));
	         height = (height + yMouse - (y + height));
	         break;
	      }
	}
	
	public void move(int xD, int yD, int typeWS){
		x = x -xD; 
		y= y- yD;
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


	@Override
	public void setSelected(boolean b) {
		selectedd = b;
	}


	public void setFirst(boolean first) {
		this.first = first;
	}
//	public void setId(char[] sid){
//		this.sid = sid;
//	}
//	public char[] getId(){
//		return sid;
//	}

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
