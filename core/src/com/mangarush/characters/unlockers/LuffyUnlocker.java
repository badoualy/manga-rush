package com.mangarush.characters.unlockers;

import com.mangarush.constants.Strings;
import com.mangarush.ui.actors.Player;

public class LuffyUnlocker extends Unlocker {
	public LuffyUnlocker() {
		super(3);
	}

	@Override
	public boolean hasUnlock(Player player) {
		if (player.getScore() > 100)
			return true;
		return false;
	}

	/**
	 * @return
	 *         Short description of how to unlock character
	 */
	public static String getDescription() {
		return Strings.GET_SCORE + "100";
	}
}
