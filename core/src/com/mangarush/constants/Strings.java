package com.mangarush.constants;

/** Abstract class containing strings used in game (easier to translate) */
public abstract class Strings {
	/** HUD MESSAGES */
	public static final String HIGHSCORE_MESSAGE = "New highscore !";
	public static final String GAMEOVER_MESSAGE = "Game Over !";
	public static final String UNLOCKED_MESSAGE = "New character : ";

	/** Unlocking message */
	public static final String GET_SCORE = "Get score equal to ";
	public static final String WITH_CHARACTER = "With ";

	/** Playable characters names */
	public static final String[] characterNames = new String[] {
			"naruto",
			"misaka",
			"shana",
			"luffy",
			"yoh",
			"ueki"
	};

	/**
	 * @param character
	 *            characterName (must be in characterNames
	 * @return
	 *         the index i with characterNames[i] = character, -1 if not in
	 *         array
	 */
	public static int indexOf(String character) {
		for (int i = 0; i < characterNames.length; i++)
			if (characterNames[i].equalsIgnoreCase(character))
				return i;

		return -1;
	}
}
