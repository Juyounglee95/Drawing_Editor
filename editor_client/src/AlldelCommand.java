
import java.awt.Color;

import javax.swing.JMenuItem;
public class AlldelCommand extends JMenuItem implements Command {
	private Editor e;
	private String text;
	
	public AlldelCommand(String t, Editor e) {
		super(t);
		text = t;
		this.e=e;

	}
	public void execute() {
		e.space.clear();
		EditorShape es = new EditorLine("aa", -999,-999,-999,-999,Color.black);
		e.sendMsg((EditorShape)es);
	}
}
