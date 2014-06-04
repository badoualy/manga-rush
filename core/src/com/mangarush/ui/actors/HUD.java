package com.mangarush.ui.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mangarush.ui.Game;
import com.mangarush.utils.B2DVars;
import com.mangarush.utils.MRVars;

/** HUD-actor : draw HUD on screen */
public class HUD extends Actor {
	private Player player;
	private BitmapFont font;
	private Batch batch;

	public HUD(Player player, Batch batch) {
		this.player = player;
		this.batch = batch;

		// Load font
		font = Game.GDXVars().getFont(MRVars.defaultFont);
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
