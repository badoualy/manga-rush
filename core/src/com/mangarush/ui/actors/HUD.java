package com.mangarush.ui.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mangarush.ui.Game;
import com.mangarush.utils.MRVars;

/** HUD-actor : draw HUD on screen */
public class HUD extends Group {
	private static final float HIGHSCORE_DURATION = 2f; // How long should show the message

	private final Player player;
	private final BitmapFont font;

	// Actors
	private final Label scoreLabel;
	private final ImageButton replayButton, menuButton;

	private boolean showHighScored; // Show new highscore message
	private float highScoreState; // Time elapsed

	public HUD(final Player player) {
		this.player = player;

		// Load font
		font = Game.GDXVars().getFont(MRVars.defaultFont);

		// Create score label
		scoreLabel = new Label("0000", new LabelStyle(font, Color.WHITE));

		// Buttons
		TextureAtlas atlas = Game.GDXVars().getTextureAtlas(MRVars.uiPack);
		menuButton = new ImageButton(new TextureRegionDrawable(atlas.findRegion("menuButton")));
		menuButton.setSize(50, 50);
		replayButton = new ImageButton(new TextureRegionDrawable(atlas.findRegion("replayButton")));
		replayButton.setSize(50, 50);

		// Add actors
		addActor(scoreLabel);
		addActor(menuButton);
		addActor(replayButton);

		// Click listeners
		replayButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Game.startSurvivorGame(player.getCharacterId());
			}
		});

		menuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Game.showMainScreen();
			}
		});

		showHighScored = false;
		highScoreState = 0f;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// Check if need to show highscore message
		if (showHighScored && highScoreState < HIGHSCORE_DURATION) {
			String str = "New high score !";
			TextBounds bounds = font.getBounds(str);
			font.draw(batch, str, getWidth() / 2 - bounds.width / 2, getHeight() / 1.2f);
		}

		// Draw added actors
		super.draw(batch, 1);
	}

	@Override
	public void act(float delta) {
		if (player.hasHighScored() && !showHighScored) {
			showHighScored = true;
		} else if (showHighScored) {
			highScoreState += delta;
		}

		scoreLabel.setText(String.format("%04d", player.getScore()));
	}

	@Override
	protected void sizeChanged() {
		scoreLabel.setPosition(getWidth() - scoreLabel.getWidth() - 5f, getHeight() - scoreLabel.getHeight() / 2f);
		replayButton.setPosition(scoreLabel.getX() - replayButton.getWidth() - 10f,
				getHeight() - replayButton.getHeight());
		menuButton.setPosition(replayButton.getX() - menuButton.getWidth() - 10f, replayButton.getY());
	}

	public boolean isInReplayButton(int x, int y) {
		if ((x >= replayButton.getX() && x <= replayButton.getX() + replayButton.getWidth())
				&& (y >= replayButton.getY() && y <= replayButton.getY() + replayButton.getHeight())) {
			return true;
		}

		return false;
	}
}
