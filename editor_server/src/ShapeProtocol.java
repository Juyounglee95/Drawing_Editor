

import java.awt.Color;
import java.io.Serializable;

public class ShapeProtocol implements Serializable{
	public static final long serialversionUid = 2L;
	private int x, y, w, h, type;
	private Color color;
	public ShapeProtocol() {}
	public ShapeProtocol(int x, int y, int w, int h, int type, Color color) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.type = type;
		this.color = color;
	}
	public int getX() {		return x;	}
	public void setX(int x) {		this.x = x;	}
	public int getY() {		return y;	}
	public void setY(int y) {		this.y = y;	}
	public int getW() {		return w;	}
	public void setW(int w) {		this.w = w;	}
	public int geth() {		return h;	}
	public void seth(int h) {		this.h = h;	}
	public int getType() {		return type;	}
	public void setType(int type) {		this.type = type;	}
	public Color getColor() {		return color;	}
	public void setColor(Color color) {		this.color = color;	}
	
}
