package com.mangarush.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.mangarush.ui.Game;
import com.mangarush.ui.stages.LoadingStage;

/** Splash screen showing loading progress */
public class SplashScreen extends ScreenAdapter {
	private LoadingStage stage;

	public SplashScreen() {
		// Load assets needed for the stage
		Game.GDXVars().loadSplashAssets();

		// Wait until they are finished loading
		Game.GDXVars().assetManager.finishLoading();

		// Initialize the stage where we will place everything
		stage = new LoadingStage((Game) Gdx.app.getApplicationListener());
	}

	@Override
	public void render(float delta) {
		// Clear the screen
		Gdx.gl.glClearColor(0.09f, 0.09f, 0.09f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Show the stage
		stage.act();
		stage.draw();
	}
}
