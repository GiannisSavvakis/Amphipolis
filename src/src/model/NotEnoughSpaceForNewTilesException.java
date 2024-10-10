package model;

/**
 * Is being thrown by <code>placeTiles</code> function of
 * class <code>Board</code>.
 * 
 * @see Board#placeTiles(Tile[])
 */
public class NotEnoughSpaceForNewTilesException extends Exception {
	public NotEnoughSpaceForNewTilesException() {
		super();
	}
}
