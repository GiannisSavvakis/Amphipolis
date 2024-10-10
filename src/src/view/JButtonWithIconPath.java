package view;

import javax.swing.JButton;


/**
 * <code>JButtonWithIconPath</code> is an extension of
 * {@link javax.swing.JButton}
 * that has the ability to store a <code>String</code> and return it to the caller
 * with the call of the <code>getIconPath</code> function.
 * <p>
 * In this project's GUI, <code>JButtonWithIconPath</code> instances are used
 * by the <code>Area</code> class to implement the tiles that have been placed
 * up to the board. Each instance stores its icon's path.
 * <p>
 * Because having a new implementation of the {@link java.awt.event.ActionListener} interface
 * for every single tile of the board was not at all practical,
 * <code>JButtonWithIconPath</code> was created in order for the <code>Controller</code> class
 * to know which tile did the player clicked, so which tile has to be removed from the board.
 * 
 * @see #JButtonWithIconPath(String)
 * @see #getIconPath()
 * @see Area
 * @see Controller
 */
public class JButtonWithIconPath extends JButton {
	private final String iconPath;
	
	
	/**
	 * Constructor - Creates an instance of <code>JButtonWithIconPath</code>.
	 * 
	 * @param iconPath is the <code>String</code> Object that <code>this</code> instance
	 * of <code>JButtonWithIconPath</code> will store.
	 * <p>
	 * @see JButtonWithIconPath
	 */
	public JButtonWithIconPath(String iconPath) {
		super();
		this.iconPath = iconPath;
	}
	
	
	public String getIconPath() {
		return this.iconPath;
	}
}
