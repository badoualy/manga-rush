package com.mangarush.utils;

import com.mangarush.constants.Paths;
import com.mangarush.map.MapChunk;

/** Bunch of globals non-ui related */
public abstract class MRVars {
	/** Map's chunks */
	public static MapChunk[] chunks = null;

	/** Instanciate chunks if needed and return it */
	public static MapChunk[] getChunks() {
		if (chunks == null) {
			// First call : instanciate
			// Get all chunks' filepath
			String[] chunksPath = Paths.mapChunksTmx;

			// Instanciate chunks
			chunks = new MapChunk[chunksPath.length];
			for (int i = 0; i < chunks.length; i++)
				chunks[i] = new MapChunk(chunksPath[i]);
		}

		return chunks;
	}
}
