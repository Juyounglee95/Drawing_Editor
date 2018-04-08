import javax.swing.JButton;
public class SquareButton extends JButton implements Command {
	private Editor e;
	private String text;
	
	public SquareButton(String t, Editor e) {
		super(t);
		text = t;
		this.e=e;

	}
	public void execute() {
		e.mode=e.SQUARE;
	}
}
