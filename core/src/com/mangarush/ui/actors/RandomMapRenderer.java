package com.mangarush.ui.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mangarush.map.MapConstants;
import com.mangarush.map.RandomMap;
import com.mangarush.ui.Game;
import com.mangarush.ui.stages.SurvivorStage;

public class RandomMapRenderer extends Actor {
	// Number of tiles drawn and tileSize
	private final int viewportWidth, viewportHeight;
	private final int tileSize;

	// Map and position
	private final RandomMap map;
	private int worldPosition; // Current position in the world (in tiles)

	public RandomMapRenderer(final SurvivorStage stage, final int viewportWidth, final int viewportHeight,
			final int tileSize) {
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.tileSize = tileSize;
		// Bounds = screen
		setBounds(0, 0, viewportWidth * tileSize, viewportHeight * tileSize);

		map = new RandomMap(stage, tileSize);
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
