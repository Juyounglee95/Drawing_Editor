import java.awt.Color;
import java.awt.Graphics;


public interface EditorM {

	void paint(Graphics g); // ������ �׸�
	
	void resize(int x, int y, int typeWS);	// ��������
	int  forcused(int x, int y);	// ���õ��� �˻�
	void setFirst(boolean first);	 // first ����
	void setSelected(boolean b);	// selected ����
	void settingColor(Color color);
	void move(int x, int y, int typeWS);

	
	int getX();
	int getY();
	int getType();
	int getWidth();
	int getHeight();
	Color getColor();

}
