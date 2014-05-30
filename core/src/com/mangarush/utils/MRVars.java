package com.mangarush.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.mangarush.core.MapChunk;

/** Bunch of constants core-related */
public class MRVars {
	// An executor to run some async tasks
	public static Executor executor = Executors.newSingleThreadExecutor();

	// Map chunks
	private static MapChunk[] chunks = null;

	// Instanciate chunks if needed and return it
	public static MapChunk[] getChunks() {
		if (chunks == null) {
			// First call : instanciate
			// Get all chunks' filepath
			String[] chunksPath = GDXVars.chunksPath;

			// Instanciate chunks
			chunks = new MapChunk[chunksPath.length];
			for (int i = 0; i < chunks.length; i++)
				chunks[i] = new MapChunk(chunksPath[i]);
		}

		return chunks;
	}
}
