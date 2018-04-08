import javax.swing.JButton;
public class GroupButton extends JButton implements Command {
	private Editor e;
	private String text;
	
	public GroupButton(String t, Editor e) {
		super(t);
		text = t;
		this.e=e;

	}
	public void execute() {
		e.mode=e.GROUP;
	}
}
