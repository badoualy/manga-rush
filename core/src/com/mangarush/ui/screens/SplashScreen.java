package com.mangarush.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.mangarush.ui.Game;
import com.mangarush.ui.stages.LoadingStage;
import com.mangarush.utils.GDXVars;

/** Splash screen showing loading progress */
public class SplashScreen extends ScreenAdapter {
	private Game game;
	private LoadingStage stage;

	public SplashScreen(Game game) {
		this.game = game;
	}

	@Override
	public void show() {
		// Load assets needed for the stage
		GDXVars.loadSplashAssets();
		// Wait until they are finished loading
		GDXVars.assetManager.finishLoading();

		// Initialize the stage where we will place everything
		stage = new LoadingStage(game);
	}

	@Override
	public void hide() {
		// Dispose the loading assets as we no longer need them
		GDXVars.assetManager.unload("atlases/loading.pack");
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
