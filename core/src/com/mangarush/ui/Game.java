package com.mangarush.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;
import com.mangarush.ui.screens.SplashScreen;
import com.mangarush.utils.GDXVars;

/** Main class, application listener, extends Game */
public class Game extends com.badlogic.gdx.Game {
	public static final String TITLE = "Manga Rush";
	public static int V_WIDTH = 800; // Default value, need tweak on android
	public static final int V_HEIGHT = 480;
	public static final float STEP = 1.0f / 60.0f;

	// Res manager
	public GDXVars gdxVars;

	// Screens
	private Screen loadingScreen;

	// Fixed timestep
	private double accumulator = 0;
	private double currentTime = 0;

	@Override
	public void create() {
		// Res manager (static : bug)
		gdxVars = new GDXVars();
		Texture.setAssetManager(gdxVars.assetManager);

		// Adjust width to keep good ratio
		V_WIDTH = (V_HEIGHT * Gdx.graphics.getWidth()) / Gdx.graphics.getHeight();

		loadingScreen = new SplashScreen();

		// Set splash screen
		setScreen(loadingScreen);
	}

	@Override
	public void render() {
		double newTime = TimeUtils.millis() / 1000.0;
		double frameTime = Math.min(newTime - currentTime, 0.25);

		currentTime = newTime;
		accumulator += frameTime;

		while (accumulator >= STEP) {
			super.render();
			accumulator -= STEP;
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		Game.GDXVars().assetManager.dispose();
	}

	@Override
	public void resume() {
		// Have to create new instance
		setScreen(loadingScreen);
	}

	public static GDXVars GDXVars() {
		return ((Game) Gdx.app.getApplicationListener()).gdxVars;
	}
}
