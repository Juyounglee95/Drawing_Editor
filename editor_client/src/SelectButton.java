import javax.swing.JButton;
public class SelectButton extends JButton implements Command {
	private Editor e;
	private String text;
	
	public SelectButton(String t, Editor e) {
		super(t);
		text = t;
		this.e=e;

	}
	public void execute() {
		e.mode=e.NONE;
	}
}
