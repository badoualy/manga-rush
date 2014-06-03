package com.mangarush.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mangarush.ui.screens.SplashScreen;
import com.mangarush.utils.GDXVars;

/** Main class, application listener, extends Game */
public class Game extends com.badlogic.gdx.Game {
	public static final String TITLE = "Manga Rush";
	public static int V_WIDTH = 800; // Need tweak on android
	public static final int V_HEIGHT = 480;
	public static final float STEP = 1 / 60f;

	// Res manager
	public GDXVars gdxVars;

	// FPS fixer
	private float accum;

	@Override
	public void create() {
		// Res manager (static : bug)
		gdxVars = new GDXVars();
		Texture.setAssetManager(gdxVars.assetManager);

		// Constants
		accum = STEP; // So we draw at start
		// Adjust width to keep good ratio
		V_WIDTH = (V_HEIGHT * Gdx.graphics.getWidth()) / Gdx.graphics.getHeight();

		// Set splash screen
		setScreen(new SplashScreen(this));
	}

	@Override
	public void render() {
		// Use accum to fix fps
		accum += Gdx.graphics.getDeltaTime();
		while (accum >= STEP) {
			super.render();
			accum -= STEP;
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		Game.GDXVars().assetManager.dispose();
	}

	public static GDXVars GDXVars() {
		return ((Game) Gdx.app.getApplicationListener()).gdxVars;
	}
}
