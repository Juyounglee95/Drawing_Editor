import javax.swing.JButton;
public class CircleButton extends JButton implements Command {
	private Editor e;
	private String text;
	
	public CircleButton(String t, Editor e) {
		super(t);
		text = t;
		this.e=e;

	}
	public void execute() {
		e.mode=e.CIRCLE;
	}
}
