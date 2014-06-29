package com.mangarush.characters.unlockers;

import com.mangarush.constants.Strings;
import com.mangarush.ui.actors.Player;

/** Class to check if player has unlock a character */
public abstract class Unlocker {
	private final int id;

	public Unlocker(final int id) {
		this.id = id;
	}

	/**
	 * @param player
	 * @return
	 *         return true if player has unlock the character
	 */
	public abstract boolean hasUnlock(Player player);

	/**
	 * @return
	 *         Id of the character to unlock
	 */
	public final int getId() {
		return id;
	}

	/**
	 * @return
	 *         Name of the character to unlock
	 */
	public final String getName() {
		return Strings.characterNames[id];
	}
}
