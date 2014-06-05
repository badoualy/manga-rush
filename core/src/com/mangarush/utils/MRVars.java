package com.mangarush.utils;

import com.mangarush.map.MapChunk;

/** Bunch of constants core-related */
public abstract class MRVars {
	/** Some HUD constants */
	public static final float HIGHSCORE_DURATION = 2f;
	public static final float INFINITE_DURATION = -1f;
	public static final String HIGHSCORE_MESSAGE = "New highscore !";
	public static final String GAMEOVER_MESSAGE = "Game Over !";

	/** Path to directories */
	public static final String atlasesDir = "atlases/";
	public static final String backgroundsDir = "backgrounds/";
	public static final String charactersDir = "characters/";
	public static final String fontDir = "fonts/";
	public static final String mapsDir = "maps/";

	/** Paths to the game font */
	public static final String defaultFont = fontDir + "font_01.fnt";

	/** Path to Splash screen background */
	public static final String splashBackground = backgroundsDir + "splash.png";

	/** Path to Stage background */
	public static final String stageBackground = backgroundsDir + "stage_cloud.png";

	/** Path to UI pack assets */
	public static final String uiPack = atlasesDir + "ui.pack";

	/** Path to Loading pack */
	public static final String loadingPack = atlasesDir + "loading.pack";

	/** Path to characters pack */
	public static final String charactersPack = atlasesDir + "characters.pack";

	/** Path to the map tiles atlas */
	public static final String tilesPack = atlasesDir + "tiles.pack";

	/** Path to MapChunk's .tmx files */
	public static final String[] mapChunksTmx = {
			mapsDir + "chunk_01.tmx",
			mapsDir + "chunk_02.tmx",
			mapsDir + "chunk_03.tmx",
			mapsDir + "chunk_04.tmx"
	};

	/** Path to Characters atlases */
	public static final String[] charactersAtlases = {
			charactersDir + "naruto.txt",
			charactersDir + "misaka.txt",
			charactersDir + "shana.txt"
	};

	/** Characters list */
	public static final String[] characters = {
			"Naruto",
			"Misaka",
			"Shana"
	};

	/** Map's chunks */
	public static MapChunk[] chunks = null;

	/** Instanciate chunks if needed and return it */
	public static MapChunk[] getChunks() {
		if (chunks == null) {
			// First call : instanciate
			// Get all chunks' filepath
			String[] chunksPath = mapChunksTmx;

			// Instanciate chunks
			chunks = new MapChunk[chunksPath.length];
			for (int i = 0; i < chunks.length; i++)
				chunks[i] = new MapChunk(chunksPath[i]);
		}

		return chunks;
	}
}
