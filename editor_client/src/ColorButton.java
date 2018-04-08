import javax.swing.JButton;
public class ColorButton extends JButton implements Command {
	private Editor e;
	private String text;
	
	public ColorButton(String t, Editor e) {
		super(t);
		text = t;
		this.e=e;

	}
	public void execute() {
		e.space.setColor();
	}
}
