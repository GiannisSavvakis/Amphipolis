package view;

import java.awt.Image;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;

/**
 * Implements the tile-placing area of this project's GUI.
 * 
 * @see #Area(int, int)
 */
public class Area extends JLayeredPane {
	private int w, h;
	private ArrayList<JButtonWithIconPath> tiles;
	
	/**
	 * Is a reference to the one ActionListener implementation
	 * that will be added to every tile in the area (common for all tiles).
	 * 
	 * @see #setActionListener(ActionListener)
	 */
	private ActionListener l;
	
	private ClassLoader cl;
	
	
	
	/**
	 * Creates a new instance of class <code>Area</code>
	 * <code>w</code> tiles long and <code>h</code> tiles tall.
	 * 
	 * @param w specifies the areas' width (in tiles)
	 * @param h specifies the areas' height (in tiles)
	 */
	public Area(int w, int h) {
		super();
		
		this.w = w;
		this.h = h;
		tiles = new ArrayList<>();
		l = null;
		cl = this.getClass().getClassLoader();
	}
	
	
	/**
	 * Is necessary because {@link java.awt.GridLayout} just did not work (!),
	 * so I had to manually draw the tiles...
	 */
	public void repaint() {
		this.removeAll();
		super.repaint();
		
		for(int i = 0; i < tiles.size(); i++) {
			int x = (i%w) * ViewConstants.AREA_W_H/w;
			int y = (i/h) * ViewConstants.AREA_W_H/h;
			tiles.get(i).setBounds(x, y, ViewConstants.AREA_W_H/w, ViewConstants.AREA_W_H/h);
			this.add(tiles.get(i));
		}
	}
	
	
	private void placeTile(String iconPath) {
		if(iconPath == null)	return;
		
		JButtonWithIconPath b = new JButtonWithIconPath(iconPath);
		URL url = cl.getResource(iconPath);
		ImageIcon imageIcon = new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(ViewConstants.AREA_W_H/w, ViewConstants.AREA_W_H/h, Image.SCALE_SMOOTH));
		b.setIcon(imageIcon);
		b.addActionListener(l);
		tiles.add(b);
	}
	
	
	/**
	 * Places tiles to the the area.
	 * <p>
	 * <b>Postcondition:</b> Must be called after
	 * <code>setActionListener</code> is called, or else
	 * the new tiles will not be able to perform any action.
	 * 
	 * @param iconPaths Each of the new tile's icon's path.
	 * <p>
	 * @see #setActionListener(ActionListener)
	 */
	public void placeTiles(ArrayList<String> iconPaths) {
		try {
			while(iconPaths.size() > 0) {
				placeTile(iconPaths.remove(0));
			}
			this.repaint();
		}
		catch(NullPointerException npe) {
			System.err.println("There was a problem!");
		}
	}
	
	
	public void removeTile(String iconPath) {
		for(int i = 0; i < tiles.size(); i++) {
			if(tiles.get(i).getIconPath().equals(iconPath)) {
				tiles.remove(i);
				break;
			}
		}
		
		repaint();
	}
	
	
	/**
	 * Sets the local variable <code>l</code>.
	 * <p>
	 * <b>Precondition:</b> Must be called before <code>placeTiles</code>
	 * is called.
	 * 
	 * @param l the value for <code>l</code> to be set.
	 * <p>
	 * @see #l
	 * @see #placeTiles(ArrayList)
	 */
	public void setActionListener(ActionListener l) {
		this.l = l;
	}
}
