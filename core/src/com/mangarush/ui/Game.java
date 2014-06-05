package com.mangarush.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.mangarush.ui.screens.FixedFpsScreen;
import com.mangarush.ui.screens.SurvivorScreen;
import com.mangarush.ui.stages.LoadingStage;
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
	private Screen mainScreen;

	@Override
	public void create() {
		// Res manager (static : bug)
		gdxVars = new GDXVars();
		Texture.setAssetManager(gdxVars.assetManager);

		// Save data
		save = new SaveData();

		// Adjust width to keep good ratio
		V_WIDTH = (V_HEIGHT * Gdx.graphics.getWidth()) / Gdx.graphics.getHeight();

		// Load assets needed for the main screen and wait until it's over
		Game.GDXVars().loadSplashAssets();
		Game.GDXVars().assetManager.finishLoading();

		// Create main screen
		mainScreen = new FixedFpsScreen(new LoadingStage());

		// MainScreen
		showMainScreen();
	}

	@Override
	public void dispose() {
		super.dispose();
		Game.GDXVars().assetManager.dispose();
		save.save();
	}

	/** Back to the main screen */
	public static void showMainScreen() {
		((Game) Gdx.app.getApplicationListener()).setScreen(((Game) Gdx.app.getApplicationListener()).mainScreen);
	}

	/** Starts a new survivor game with the given character */
	public static void startSurvivorGame(int characterId) {
		((Game) Gdx.app.getApplicationListener()).setScreen(new SurvivorScreen(characterId));
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
