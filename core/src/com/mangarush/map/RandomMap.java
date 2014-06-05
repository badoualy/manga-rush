package com.mangarush.map;

import java.util.Random;

import com.badlogic.gdx.physics.box2d.World;
import com.mangarush.utils.MRVars;

/**
 * Map that auto-grows and store only chunksId and update B2DWorld if set.
 * Constructor grow map automatically
 */
public class RandomMap {
	private static final int STORED_CHUNKS = 15; // Chunks stored 

	// Map-related
	private final short[] map;
	private int currChunk; // Index of the first currently used chunk
	private int end; // Last chunk generated

	// Chunks available
	private MapChunk[] chunks;

	// UI-related
	private final World world;
	private final int tileSize;

	// Random chuck generator : instanciate once
	private Random random;

	public RandomMap(World world, int tileSize) {
		map = new short[STORED_CHUNKS];
		currChunk = end = 0;

		chunks = MRVars.getChunks();

		this.world = world;
		this.tileSize = tileSize;

		random = new Random();

		// Grow map first time
		growMap();
	}

	/** Generate random chunks to grow map if needed */
	private void growMap() {
		short chunkId;

		// Fill until full
		while (end % STORED_CHUNKS != currChunk % STORED_CHUNKS || end == 0) {
			chunkId = (short) random.nextInt(chunks.length);
			map[end % STORED_CHUNKS] = chunkId;
			chunks[chunkId].setupToWorld(world, end * MapChunk.WIDTH * tileSize, 0);
			end++;
		}
	}

	/** Get a tile on the map */
	public short getMap(int x, int y) {
		return chunks[map[(x / MapChunk.WIDTH) % STORED_CHUNKS]].getMap(x % MapChunk.WIDTH, y);
	}

	/** Set current position in the world and grows map if needed */
	public void setWorldPosition(int worldPosition) {
		currChunk = worldPosition / MapChunk.WIDTH;
		growMap();
	}

	/** Inner-class with all map-related constants */
	public static class MapConstants {
		// Tile types
		public static final short EMPTY = -1;
		public static final short GRASS_LEFT = 0;
		public static final short GRASS_MID = 1;
		public static final short GRASS_RIGHT = 2;
		public static final short GRASS_FILL = 3;
		public static final short GRASS_HALF_LEFT = 4;
		public static final short GRASS_HALF_MID = 5;
		public static final short GRASS_HALF_RIGHT = 6;
		public static final short GRASS_HALF_WATER_LEFT = 7;
		public static final short GRASS_HALF_WATER_MID = 8;
		public static final short GRASS_HALF_WATER_RIGHT = 9;
		public static final short WATER_FILL = 10;
		public static final short WATER_TOP = 11;
		public static final short SIGN_RIGHT = 12;

		// Tiles type count
		public static final short TYPE_COUNT = 13;
	}
}