package com.mangarush.ui.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import com.mangarush.core.RandomMap;
import com.mangarush.core.RandomMap.MapConstants;
import com.mangarush.ui.Game;
import com.mangarush.ui.actions.CleanWorldRunnable;

public class RandomMapActor extends com.badlogic.gdx.scenes.scene2d.Actor {
	// Number of tiles drawn and tileSize
	private final int viewportWidth, viewportHeight;
	private final int tileSize;

	// Map and position
	private RandomMap map;
	private int worldPosition; // Current position in the world (in tiles)

	// Tasks
	private CleanWorldRunnable cleanWorldRunnable;

	public RandomMapActor(final World world, final int viewportWidth, final int viewportHeight, final int tileSize) {
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.tileSize = tileSize;
		// Bounds = screen
		setBounds(0, 0, viewportWidth * tileSize, viewportHeight * tileSize);

		map = new RandomMap(world, tileSize);
		worldPosition = 0;

		cleanWorldRunnable = new CleanWorldRunnable();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// Drawing from i = position to avoid useless calls (because out of screen
		// Drawing to +1 because position is rounded down, so we start a bit too far
		for (int i = worldPosition; i < worldPosition + viewportWidth + 1; i++) {
			for (int j = 0; j < viewportHeight; j++) {
				short id = map.getMap(i, j);
				if (id != MapConstants.EMPTY) {
					batch.draw(Game.GDXVars().getMapTileTexture(id), i * tileSize, j * tileSize);
				}
			}
		}
	}

	@Override
	public void act(float delta) {
		// Faster to do this directly instead of using this.addAction()
		// Don't call it too often
		/* if (worldPosition > cleanWorldRunnable.getLastPosition() +
		 * getWidth())
		 * MRVars.executor.execute(cleanWorldRunnable); */
	}

	public void setPosition(float position) {
		cleanWorldRunnable.setPosition(position);

		// Cast in tiles (rounded down)
		worldPosition = (int) position / tileSize;
		map.setWorldPosition(worldPosition);
	}
}
