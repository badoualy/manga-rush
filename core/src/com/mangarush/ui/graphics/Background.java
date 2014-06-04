package com.mangarush.ui.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

/** An actor that draw a background scrolling and repeating itself */
public class Background extends BaseDrawable {
	private Texture texture;
	private Batch batch;

	public Background(Texture texture, Batch batch) {
		this.texture = texture;
		texture.setWrap(TextureWrap.Repeat, TextureWrap.ClampToEdge);

		// Use custom batch
		this.batch = batch;
	}

	@Override
	public void draw(Batch uBatch, float x, float y, float width, float height) {
		// Call this method with null batch
		batch.begin();
		batch.draw(texture, 0f, 0f, (int) x, (int) y, (int) width, (int) height);
		batch.end();
	}
}
