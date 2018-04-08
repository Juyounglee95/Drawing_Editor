import javax.swing.JButton;
public class PenButton extends JButton implements Command {
	private Editor e;
	private String text;
	
	public PenButton(String t, Editor e) {
		super(t);
		text = t;
		this.e=e;

	}
	public void execute() {
		e.mode=e.PEN;
	}
}
