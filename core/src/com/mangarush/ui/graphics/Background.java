package com.mangarush.ui.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

/**
 * An actor that draw a background scrolling and repeating itself. This class is
 * simpler than using Image or other actors. We just want to draw a texture with
 * an offset
 */
public class Background extends BaseDrawable {
	private final Texture texture;
	private float x, y;

	public Background(final Texture texture) {
		this.texture = texture;
		texture.setWrap(TextureWrap.Repeat, TextureWrap.ClampToEdge);

		x = y = 0f;
	}

	@Override
	public void draw(Batch batch, float originX, float originY, float width, float height) {
		// Call this method with null batch
		batch.draw(texture, x, y, (int) originX, (int) originY, (int) width, (int) height);
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
