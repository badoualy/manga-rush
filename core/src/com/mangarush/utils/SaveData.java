package com.mangarush.utils;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/** Utils class to store datas to be saved */
public class SaveData {
	private static final String SAVE_PREFERENCE_NAME = "save_data_preferences";

	private final Preferences prefs; // Where to save

	public int highScore; // Highest score
	public long timeSpend; // Time spend playing

	public HashMap<String, Boolean> characters; // List of available characters

	/** Create a new save data, with all default value */
	public SaveData() {
		prefs = Gdx.app.getPreferences(SAVE_PREFERENCE_NAME);
		highScore = prefs.getInteger("highScore", 0);
		timeSpend = prefs.getLong("timeSpend", 0);

		characters = new HashMap<>(10);
		characters.put("luffy", prefs.getBoolean("luffy", false));
	}

	/** Save datas (flush Preferences) */
	public void save() {
		prefs.putInteger("highScore", highScore);
		prefs.putLong("timeSpend", timeSpend);
		prefs.put(characters);

		prefs.flush();
	}
}