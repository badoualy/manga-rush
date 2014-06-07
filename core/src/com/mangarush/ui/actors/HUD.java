package com.mangarush.ui.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mangarush.ui.Game;
import com.mangarush.utils.MRVars;

/** HUD-actor : draw HUD on screen */
public class HUD extends Group {
	// Datas
	private final Player player;
	private final BitmapFont font;

	// Actors
	private final Label scoreLabel;
	private final ImageButton replayButton, menuButton;

	// Message
	private String message; // message to show on screen
	private float messageDuration; // How long to show message
	private float messageState; // Time elapsed

	public HUD(final Player player) {
		this.player = player;

		// Load font
		font = Game.GDXVars().getFont(MRVars.defaultFont);

		// Create score label
		scoreLabel = new Label("0000", new LabelStyle(font, Color.WHITE));

		// Buttons
		TextureAtlas atlas = Game.GDXVars().getTextureAtlas(MRVars.uiPack);
		menuButton = new ImageButton(new TextureRegionDrawable(atlas.findRegion("menuButton")));
		replayButton = new ImageButton(new TextureRegionDrawable(atlas.findRegion("replayButton")));

		// Add actors
		addActor(scoreLabel);
		addActor(menuButton);
		addActor(replayButton);

		// Message related
		message = "";
		messageState = 0f;

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
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// Draw added actors
		super.draw(batch, 1);

		if (!message.isEmpty()) {
			// We have a message to show
			TextBounds bounds = font.getBounds(message);
			font.draw(batch, message, getX() + getWidth() / 2f - bounds.width / 2f, getHeight() / 1.2f);
		}
	}

	@Override
	public void act(float delta) {
		messageState += delta;
		if (messageState >= messageDuration && messageDuration != MRVars.INFINITE_DURATION)
			message = "";

		// Update scoreLabel value
		scoreLabel.setText(String.format("%04d", player.getScore()));

		// Act buttons etc
		super.act(delta);
	}

	@Override
	protected void sizeChanged() {
		// Update actors position : super.sizeChanged() is empty
		scoreLabel.setPosition(getWidth() - scoreLabel.getWidth() - 5f, getHeight() - scoreLabel.getHeight() / 2f);
		replayButton.setPosition(scoreLabel.getX() - replayButton.getWidth() - 20f,
				getHeight() - replayButton.getHeight());
		menuButton.setPosition(replayButton.getX() - menuButton.getWidth() - 20f, replayButton.getY());
	}

	/** Print a message on screen for a fixed duration (may be infinite -1) */
	public void showMessage(String message, float duration) {
		this.message = message;
		messageDuration = duration;
		messageState = 0f;
	}

	/**
	 * Grow up buttons and put them at the center of the screen : call this when
	 * game is over
	 */
	public void growUp() {
		// Menu button
		menuButton
				.addAction(Actions.moveTo(getWidth() / 2 - menuButton.getWidth() * 2 - 50f, getHeight() / 2.2f, 0.20f));
		menuButton.getImage().addAction(Actions.scaleTo(2f, 2f, 0.20f));

		// Replat Button
		replayButton.addAction(Actions.moveTo(getWidth() / 2 + 50f, getHeight() / 2.2f, 0.20f));
		replayButton.getImage().addAction(Actions.scaleTo(2f, 2f, 0.20f));
	}
}
