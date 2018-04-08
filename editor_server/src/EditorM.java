import java.awt.Color;
import java.awt.Graphics;


public interface EditorM {

	void paint(Graphics g); // 도형을 그림
	
	void resize(int x, int y, int typeWS);	// 리사이즈
	int  forcused(int x, int y);	// 선택될지 검사
	void setFirst(boolean first);	 // first 설정
	void setSelected(boolean b);	// selected 설정
	void settingColor(Color color);
	void move(int x, int y, int typeWS);

	
	int getX();
	int getY();
	int getType();
	int getWidth();
	int getHeight();
	Color getColor();

}
