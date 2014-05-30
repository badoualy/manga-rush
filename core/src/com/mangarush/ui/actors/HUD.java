package com.mangarush.ui.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mangarush.utils.B2DVars;
import com.mangarush.utils.GDXVars;

/** HUD-actor : draw HUD on screen */
public class HUD extends Actor {
	private Player player;
	private BitmapFont font;
	private Batch batch;

	public HUD(Player player) {
		this.player = player;

		// Load font and batch
		font = GDXVars.font;
		batch = GDXVars.hudBatch;
	}

	@Override
	public void draw(Batch stageBatch, float parentAlpha) {
		// We draw on custom batch : stop drawing stageBatch
		stageBatch.end();
		batch.begin();

		// Draw position (= score) in the top-right corner
		String str = "" + (int) (player.getX() / B2DVars.PPM);
		TextBounds bounds = font.getBounds(str);
		font.draw(batch, str, getWidth() - bounds.width - 5, getHeight() + bounds.height / 2);

		batch.end();
		stageBatch.begin();
	}
}
