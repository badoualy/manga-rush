package com.mangarush.characters.unlockers;

import com.mangarush.constants.Strings;
import com.mangarush.ui.actors.Player;

public class UekiUnlocker extends Unlocker {
	public UekiUnlocker() {
		super(5);
	}

	@Override
	public boolean hasUnlock(Player player) {
		if (player.getScore() >= 300 && player.getCharacterId() == 3)
			return true;
		return false;
	}

	/**
	 * @return
	 *         Short description of how to unlock character
	 */
	public static String getDescription() {
		return Strings.GET_SCORE + "300 " + Strings.WITH_CHARACTER + " Luffy";
	}
}