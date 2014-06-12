package com.mangarush.map;

import java.util.Random;

import com.mangarush.ui.stages.SurvivorStage;
import com.mangarush.utils.MRVars;

/**
 * Map that auto-grows and store only chunksId and update B2DWorld if set.
 * Constructor grow map automatically
 */
public class RandomMap {
	public static final short EMPTY = -1;
	private static final int STORED_CHUNKS = 15; // Chunks stored 

	// Map-related
	private final short[] map;
	private int currChunk; // Index of the first currently used chunk
	private int end; // Last chunk generated

	// Chunks available
	private MapChunk[] chunks;

	// UI-related
	private final SurvivorStage stage;
	private final int tileSize;

	// Random chuck generator : instanciate once
	private Random random;

	public RandomMap(SurvivorStage stage, int tileSize) {
		map = new short[STORED_CHUNKS];
		currChunk = end = 0;

		chunks = MRVars.getChunks();

		this.stage = stage;
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
			chunks[chunkId].setupToStage(stage, end * MapChunk.WIDTH * tileSize, 0);
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
}