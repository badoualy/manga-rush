package com.mangarush.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.mangarush.ui.screens.SplashScreen;
import com.mangarush.utils.GDXVars;
import com.mangarush.utils.SaveData;

/** Main class, application listener, extends Game */
public class Game extends com.badlogic.gdx.Game {
	public static final String TITLE = "Manga Rush";
	public static int V_WIDTH = 800; // Default value, need tweak on android
	public static final int V_HEIGHT = 480;
	public static final float FIXED_FPS = 1.0f / 60.0f;

	// Res manager
	private GDXVars gdxVars;

	// Save data
	private SaveData save;

	// Screens
	private Screen loadingScreen;

	@Override
	public void create() {
		// Res manager (static : bug)
		gdxVars = new GDXVars();
		Texture.setAssetManager(gdxVars.assetManager);

		// Save data
		save = new SaveData();

		// Adjust width to keep good ratio
		V_WIDTH = (V_HEIGHT * Gdx.graphics.getWidth()) / Gdx.graphics.getHeight();

		loadingScreen = new SplashScreen();

		// Set splash screen
		setScreen(loadingScreen);
	}

	@Override
	public void dispose() {
		super.dispose();
		Game.GDXVars().assetManager.dispose();
		save.save();
	}

	/** Return current instance of GDXVars */
	public static GDXVars GDXVars() {
		return ((Game) Gdx.app.getApplicationListener()).gdxVars;
	}

	/** Return current save datas */
	public static SaveData Save() {
		return ((Game) Gdx.app.getApplicationListener()).save;
	}
}
