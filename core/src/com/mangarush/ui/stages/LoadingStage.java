package com.mangarush.ui.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mangarush.ui.Game;
import com.mangarush.ui.actors.LoadingBar;
import com.mangarush.ui.screens.SurvivorScreen;
import com.mangarush.utils.GDXVars;

/** A stage for loading screen */
public class LoadingStage extends Stage {
	private final Game game;

	private Image background;
	private LoadingBar loadingBar;
	private BitmapFont font;

	private float progress;

	public LoadingStage(Game game) {
		// Set the game
		this.game = game;

		// Get our textureatlas from the manager, then get assets
		TextureAtlas atlas = GDXVars.assetManager.get("atlases/loading.pack", TextureAtlas.class);
		font = GDXVars.assetManager.get(GDXVars.defaultFont, BitmapFont.class);

		// Create actors
		background = new Image(GDXVars.assetManager.get("backgrounds/splash.png", Texture.class));
		loadingBar = new LoadingBar(atlas);

		// Add actors
		addActor(background);
		addActor(loadingBar);

		// Setup bounds, loading bar center-bottom screen
		background.setSize(getWidth(), getHeight());
		loadingBar.setPosition(getWidth() / 2 - loadingBar.getWidth() / 2, loadingBar.getHeight());

		progress = 0f;

		// Start the work
		startLoadingWork();
	}

	/** Start the work needed to be done */
	private void startLoadingWork() {
		GDXVars.loadSurvivorStageAssets();
	}

	@Override
	public void draw() {
		// Check if done
		if (GDXVars.assetManager.update()) { // Load some, will return true if done loading
			if (Gdx.input.isTouched()) { // If the screen is touched after done loading
				game.setScreen(new SurvivorScreen());
				return;
			}
		}

		// Draw actors
		super.draw();

		// Write title
		getBatch().begin();
		font.draw(getBatch(), Game.TITLE, getWidth() / 2 - font.getBounds(Game.TITLE).width / 2, getHeight() - 10);
		getBatch().end();
	}

	@Override
	public void act(float delta) {
		progress = GDXVars.assetManager.getProgress();
		loadingBar.setProgress(progress);
		loadingBar.setMessage((progress == 1) ? "Tap to continue" : "Textures ...");
	}
}
