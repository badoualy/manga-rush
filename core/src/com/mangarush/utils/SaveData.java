package com.mangarush.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/** Utils class to store datas to be saved */
public class SaveData {
	private static final String SAVE_PREFERENCE_NAME = "save_data_preferences";
	private final Preferences prefs;
	public int highScore; // Highest score
	public long timeSpend; // Time spend playing

	/** Create a new save data, with all default value */
	public SaveData() {
		prefs = Gdx.app.getPreferences(SAVE_PREFERENCE_NAME);
		highScore = prefs.getInteger("highScore", 0);
		timeSpend = prefs.getLong("timeSpend", 0);
		highScore = 0;
	}

	/** Save datas (flush Preferences) */
	public void save() {
		prefs.putInteger("highScore", highScore);
		prefs.putLong("timeSpend", timeSpend);
		prefs.flush();
	}
}
