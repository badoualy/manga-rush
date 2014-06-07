package com.mangarush.ui.stages;

import static com.mangarush.ui.Game.V_HEIGHT;
import static com.mangarush.ui.Game.V_WIDTH;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mangarush.ui.Game;
import com.mangarush.ui.actors.CharactersSelector;
import com.mangarush.ui.actors.LoadingBar;
import com.mangarush.utils.MRVars;

/** A stage for loading screen */
public class LoadingStage extends Stage {
	private Image background;
	private BitmapFont font;
	private LoadingBar loadingBar;
	private CharactersSelector selector;

	private float progress;

	public LoadingStage() {
		initViewport();

		// Get our textureatlas from the manager, then get assets
		TextureAtlas atlas = Game.GDXVars().getTextureAtlas(MRVars.loadingPack);
		font = Game.GDXVars().getFont(MRVars.defaultFont);

		// Create actors
		background = new Image(Game.GDXVars().getTexture(MRVars.splashBackground));
		background.setSize(getWidth(), getHeight());
		loadingBar = new LoadingBar(atlas);
		loadingBar.setPosition(getWidth() / 2 - loadingBar.getWidth() / 2, loadingBar.getHeight() / 2f);

		// Add actors
		addActor(background);
		addActor(loadingBar);

		// Selection group
		selector = new CharactersSelector(Game.GDXVars().getTextureAtlas(MRVars.charactersPack));
		addActor(selector);
		selector.setPosition(getWidth() / 2f - selector.getWidth() / 2f, 110);

		progress = 0f;

		// Start the work
		startLoadingWork();
	}

	/** Load cameras and set up viewport */
	private void initViewport() {
		getViewport().setWorldSize(V_WIDTH, V_HEIGHT);

		// Default cam : don't need BoundedCamera
		OrthographicCamera cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		getViewport().setCamera(cam); // Set stage default camera	
	}

	@Override
	public void draw() {
		// Check if done

		// Draw actors
		super.draw();

		// Write title
		getBatch().begin();
		font.draw(getBatch(), Game.TITLE, getWidth() / 2f - font.getBounds(Game.TITLE).width / 2f, getHeight() - 10f);
		getBatch().end();
	}

	@Override
	public void act(float delta) {
		Game.GDXVars().assetManager.update();
		progress = Game.GDXVars().assetManager.getProgress();
		loadingBar.setProgress(progress);
		loadingBar.setMessage((progress == 1) ? "Tap to continue" : "Textures ...");
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (!super.touchUp(screenX, screenY, pointer, button)) {
			if (Game.GDXVars().assetManager.update()) { // Load some, will return true if done loading
				Game.startSurvivorGame(selector.getSelectionIndex());
				return true;
			}
		}

		return false;
	}

	/** Start the work needed to be done */
	private void startLoadingWork() {
		Game.GDXVars().loadSurvivorStageAssets();
	}
}