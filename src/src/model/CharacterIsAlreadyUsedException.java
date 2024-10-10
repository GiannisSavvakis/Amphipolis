package model;

/**
 * Is being thrown by <code>use</code> function of
 * abstract class <code>Character</code>.
 * 
 * @see Character#use()
 */
public class CharacterIsAlreadyUsedException extends Exception {
	public CharacterIsAlreadyUsedException() {
		super();
	}
}
