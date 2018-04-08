
import javax.swing.JMenuItem;
public class DeleteCommand extends JMenuItem implements Command {
	private Editor e;
	private String text;
	
	public DeleteCommand(String t, Editor e) {
		super(t);
		text = t;
		this.e=e;

	}
	public void execute() {
		e.space.delete();
	}
}
