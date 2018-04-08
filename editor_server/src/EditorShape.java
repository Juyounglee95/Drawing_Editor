import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;


public class EditorShape extends EditorPanel implements EditorM{
	
	static final int	 NONE = 0;
	static final int	 MOVE = 1;
	static final int	 UPPER_LEFT = 2;
	static final int	 UPPER_RIGHT = 3;
	static final int	 BOTTOM_LEFT = 4;
	static final int	 BOTTOM_RIGHT = 5;
	private static final int TYPE = Editor.NONE;
	protected boolean group;
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected int xDiff, yDiff;	// x, y와 클릭한 위치의 차이
	protected boolean first, selectedd ;	// 드래그시작인지, 선택되었는지
	protected    Rectangle   ul, ur, bl, br; // 선택되었을 때 나타나는 표시
	public  static   int     hotspot = 3; // 선택표시의 크기
	public int bor = 0;
	public int xNew, yNew;

	public EditorShape(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		xDiff = yDiff = 0;
		first = true;
	}
	

	

	public EditorShape() {
		this(25,25,25,15);
	}

	public void paint(Graphics g) {
		Color fg = g.getColor();
		//selectTrue(this);
		
		
		ul = new Rectangle(x - hotspot, y - hotspot, 2 * hotspot, 2 * hotspot);
		ur = new Rectangle(x + Math.min(width,  height) - hotspot, y - hotspot, 2 * hotspot,
				2 * hotspot);
		bl = new Rectangle(x - hotspot, y + Math.min(width,  height) - hotspot, 2 * hotspot,
				2 * hotspot);
		br = new Rectangle(x + Math.min(width,  height) - hotspot, y + Math.min(width,  height) - hotspot,
				2 * hotspot, 2 * hotspot);

		
		if (selectedd) {
			Color color =new Color(0xEDEDED);
			g.setColor(color);
			g.fillRect(ul.x, ul.y, ul.width, ul.height);
			g.fillRect(ur.x, ur.y, ur.width, ur.height);
			g.fillRect(bl.x, bl.y, bl.width, bl.height);
			g.fillRect(br.x, br.y, br.width, br.height);
		}

		g.setColor(fg);
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
		super.color = color;
	}





	
}
