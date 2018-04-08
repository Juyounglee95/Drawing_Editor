import javax.swing.*;
public class LineButton extends JButton implements Command {
	private Editor e;
	private String text;
	
	public LineButton(String t, Editor e) {
		super(t);
		text = t;
		this.e=e;

	}
	public void execute() {
		e.mode=e.LINE;
	}
}
