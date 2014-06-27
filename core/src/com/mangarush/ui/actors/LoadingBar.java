package com.mangarush.ui.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mangarush.constants.Paths;
import com.mangarush.ui.Game;

/**
 * Loading bar to add on stage, need an atlas with regions : <br/>
 * loading-bar<br/>
 * loading-frame<br/>
 * loading-bar-hidden
 */
public class LoadingBar extends Actor {
	// Progress bar assets
	private final TextureRegion barRegion, hiddenRegion, frameRegion;
	private final int barMaxWidth;

	// Progress text
	private final BitmapFont font;
	private String message;

	private float progress;

	/**
	 * Need an atlas with regions : <br/>
	 * loading-bar<br/>
	 * loading-frame<br/>
	 * loading-bar-hidden
	 */
	public LoadingBar(final TextureAtlas atlas) {
		barRegion = atlas.findRegion("loading-bar");
		frameRegion = atlas.findRegion("loading-frame");
		hiddenRegion = atlas.findRegion("loading-bar-hidden");

		font = Game.GDXVars().getFont(Paths.defaultFont);
		message = "";

		progress = 0f;
		barMaxWidth = barRegion.getRegionWidth();

		setBounds(0, 0, frameRegion.getRegionWidth(), frameRegion.getRegionHeight());

		setProgress(1f);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(barRegion, getX(), getY());
		batch.draw(frameRegion, getX(), getY());
		if (progress < 1)
			batch.draw(hiddenRegion, getX() + barRegion.getRegionWidth() - hiddenRegion.getRegionWidth(), getY());

		if (message.length() > 0) {
			// Write progress message
			font.setScale(0.8f);
			TextBounds bounds = font.getBounds(message);
			font.draw(batch, message, getX() + getWidth() / 2 - bounds.width / 2, getY() + 65);
		}
	}

	/** Set current progress in percent (<= 1) */
	public void setProgress(float percent) {
		progress = percent;
		barRegion.setRegionWidth((int) (barMaxWidth * progress));
	}

	/** Set message to write in progress bar */
	public void setMessage(String message) {
		this.message = message;
	}
}
