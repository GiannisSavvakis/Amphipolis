package model;

import java.awt.Color;
import java.util.ArrayList;

public abstract class Character {
	/**
	 * Indicates the state of the <code>Character</code>.
	 */
	private boolean used;
	
	
	/**
	 * A reference to the <code>Player</code> Object that owns this <code>Character</code>
	 * <p>
	 * <code>Character</code> class needs this field in order to be able to know
	 * the last placing area used by its owner.
	 * 
	 * @see Player#getLastUsedArea()
	 */
	private Player owner;
	
	
	private Color color;	// to evala mono epeidh to leei h ekfwnhsh - den vlepw pou xrhsimeuei!
	
	
	public Character(Player owner) {
		this.owner = owner;
		this.color = this.getOwner().getColor();
		this.used = false;
	}
	
	
	public boolean isUsed() {
		return used;
	}
	
	
	public Player getOwner() {
		return owner;
	}
	
	
	/**
	 * The only way a <code>Character</code> can be used is via <code>use</code> function.<br>
	 * 
	 * This function guarantees that the {@link #used state} of the
	 * <code>Character</code> is handled correctly.
	 * 
	 * @return Whatever <code>functionality</code> function return.
	 * @throws CharacterIsAlreadyUsedException If <code>use</code> had been called
	 * at least once in the past.
	 * <p>
	 * @see {@link #functionality()}
	 */
	public ArrayList<Integer> use() throws CharacterIsAlreadyUsedException {
		if(this.isUsed()) {
			throw new CharacterIsAlreadyUsedException();
		}
		
		used = true;
		return functionality();
	}
	
	
	/**
	 * A child of <code>Character</code> class must implement this
	 * function depending on the functionality that offers to its <code>owner</code>.
	 * 
	 * @return An <code>ArrayList</code> with <code>Integer</code> Objects.<br>
	 * Each Object specifies how many tiles from the corresponding area can the
	 * <code>owner</code> take by using this <code>Character</code>.
	 * <p>
	 * @see #owner
	 */
	protected abstract ArrayList<Integer> functionality();
}
