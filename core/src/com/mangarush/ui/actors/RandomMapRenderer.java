package com.mangarush.ui.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import com.mangarush.map.RandomMap;
import com.mangarush.map.RandomMap.MapConstants;
import com.mangarush.ui.Game;

public class RandomMapRenderer extends com.badlogic.gdx.scenes.scene2d.Actor {
	// Number of tiles drawn and tileSize
	private final int viewportWidth, viewportHeight;
	private final int tileSize;

	// Map and position
	private final RandomMap map;
	private int worldPosition; // Current position in the world (in tiles)

	public RandomMapRenderer(final World world, final int viewportWidth, final int viewportHeight, final int tileSize) {
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.tileSize = tileSize;
		// Bounds = screen
		setBounds(0, 0, viewportWidth * tileSize, viewportHeight * tileSize);

		map = new RandomMap(world, tileSize);
		worldPosition = 0;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// Drawing from i = position to avoid useless calls (because out of screen
		// Drawing to +2 because position is rounded down, so we start a bit too far
		for (int i = worldPosition; i < worldPosition + viewportWidth + 2; i++) {
			for (int j = 0; j < viewportHeight; j++) {
				short id = map.getMap(i, j);
				if (id != MapConstants.EMPTY) {
					batch.draw(Game.GDXVars().getTileTexture(id), i * tileSize, j * tileSize);
				}
			}
		}
	}

	public void setPosition(float position) {
		// Cast in tiles (rounded down)
		worldPosition = (int) position / tileSize;
		map.setWorldPosition(worldPosition);
	}
}
