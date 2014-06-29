package com.mangarush.ui.stages;

import static com.mangarush.ui.Game.V_HEIGHT;
import static com.mangarush.ui.Game.V_WIDTH;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mangarush.constants.Paths;
import com.mangarush.ui.Game;
import com.mangarush.ui.actors.AtlasSelector;
import com.mangarush.ui.actors.LoadingBar;

/** A stage for loading screen */
public class LoadingStage extends Stage {
	// GDX-assets
	private Image background;
	private LoadingBar loadingBar;
	private BitmapFont font;

	// Selector for characters
	private AtlasSelector selector;

	private float progress;

	public LoadingStage() {
		initViewport();

		// Get our textureatlas from the manager, then get assets
		TextureAtlas atlas = Game.GDXVars().getTextureAtlas(Paths.loadingPack);
		font = Game.GDXVars().getFont(Paths.defaultFont);

		// Create actors
		background = new Image(Game.GDXVars().getTexture(Paths.splashBackground));
		background.setSize(getWidth(), getHeight());
		loadingBar = new LoadingBar(atlas);
		loadingBar.setPosition(getWidth() / 2 - loadingBar.getWidth() / 2, loadingBar.getHeight() / 2f);

		// Add actors
		addActor(background);
		addActor(loadingBar);

		// Selection group
		selector = new AtlasSelector(Game.GDXVars().getTextureAtlas(Paths.charactersPack), Game.Save().characters);
		addActor(selector);
		selector.setPosition(getWidth() / 2f - selector.getWidth() / 2f, 130);

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
		// Draw actors
		super.draw();

		// Write title
		getBatch().begin();
		font.setScale(1f);
		font.draw(getBatch(), Game.TITLE, getWidth() / 2f - font.getBounds(Game.TITLE).width / 2f, getHeight() - 10f);
		font.setScale(0.5f);
		TextBounds bounds = font.getBounds("Highscore : " + Game.Save().highScore);
		font.draw(getBatch(), "Highscore : " + Game.Save().highScore, getWidth() - bounds.width, getHeight());
		getBatch().end();
	}

	@Override
	public void act(float delta) {
		Game.GDXVars().assetManager.update();
		progress = Game.GDXVars().assetManager.getProgress();
		loadingBar.setProgress(progress);
		loadingBar.setMessage((progress == 1) ? "Tap to continue..." : "Loading textures ...");
		selector.setSelectable(Game.Save().characters);

		super.act(delta);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (!super.touchUp(screenX, screenY, pointer, button)) {
			if (Game.GDXVars().assetManager.update()) { // Load some, will return true if done loading
				if (Game.Save().characters[selector.getSelectedIndex()]) {
					Game.startSurvivorGame(selector.getSelectedIndex());
					return true;
				}
			}
		}

		return false;
	}

	/** Start the work needed to be done */
	private void startLoadingWork() {
		Game.GDXVars().loadSurvivorStageAssets();
	}
}
