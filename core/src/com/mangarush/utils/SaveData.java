package com.mangarush.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mangarush.constants.Strings;

/** Utils class to store datas to be saved */
public class SaveData {
	private static final String SAVE_PREFERENCE_NAME = "save_data_preferences";

	private final Preferences prefs; // Where to save

	public int highScore; // Highest score
	public long timeSpend; // Time spend playing

	public boolean[] characters; // List of available characters

	/** Create a new save data, with all default value */
	public SaveData() {
		prefs = Gdx.app.getPreferences(SAVE_PREFERENCE_NAME);
		highScore = prefs.getInteger("highScore", 0);
		timeSpend = prefs.getLong("timeSpend", 0);

		// Check availability of characters
		characters = new boolean[Strings.characterNames.length];
		for (int i = 0; i < characters.length; i++) {
			if (i < 3) // First 3 : free
				characters[i] = true;
			else
				characters[i] = prefs.getBoolean(Strings.characterNames[i], true);
		}
	}

	/** Save datas (flush Preferences) */
	public void save() {
		prefs.putInteger("highScore", highScore);
		prefs.putLong("timeSpend", timeSpend);
		for (int i = 0; i < characters.length; i++)
			prefs.putBoolean(Strings.characterNames[i], characters[i]);

		prefs.flush();
	}
}