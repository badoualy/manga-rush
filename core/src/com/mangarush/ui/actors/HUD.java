package com.mangarush.ui.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mangarush.ui.Game;
import com.mangarush.utils.MRVars;

/** HUD-actor : draw HUD on screen */
public class HUD extends Actor {
	private static final float HIGHSCORE_DURATION = 2f; // How long should show the message

	private Player player;
	private BitmapFont font;
	private Batch batch;

	private boolean showHighScored; // Show new highscore message
	private float highScoreState; // Time elapsed

	public HUD(Player player, Batch batch) {
		this.player = player;
		this.batch = batch;

		// Load font
		font = Game.GDXVars().getFont(MRVars.defaultFont);

		showHighScored = false;
		highScoreState = 0f;
	}

	@Override
	public void draw(Batch stageBatch, float parentAlpha) {
		// We draw on custom batch : stop drawing stageBatch
		stageBatch.end();
		batch.begin();

		// Draw score (= position) in the top-right corner
		String str = String.format("%04d", player.getScore());
		TextBounds bounds = font.getBounds(str);
		font.draw(batch, str, getWidth() - bounds.width - 5, getHeight() + bounds.height / 2);

		// Check if need to show highscore message
		if (showHighScored && highScoreState < HIGHSCORE_DURATION) {
			str = "New high score !";
			bounds = font.getBounds(str);
			font.draw(batch, str, getWidth() / 2 - bounds.width / 2, getHeight() / 1.2f);
		}

		batch.end();
		stageBatch.begin();
	}

	@Override
	public void act(float delta) {
		if (player.hasHighScored() && !showHighScored) {
			showHighScored = true;
		} else if (showHighScored) {
			highScoreState += delta;
		}
	}
}
