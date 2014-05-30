package com.mangarush.ui;

import com.badlogic.gdx.Gdx;
import com.mangarush.ui.screens.SurvivorScreen;

/** Main class, application listener, extends Game */
public class Game extends com.badlogic.gdx.Game {
	public static final String TITLE = "Manga Rush";
	public static final int V_WIDTH = 800;
	public static final int V_HEIGHT = 480;
	public static final float STEP = 1 / 60f;

	// FPS fixer
	private float accum;

	@Override
	public void create() {
		accum = STEP; // So we draw at start
		setScreen(new SurvivorScreen());
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
}
