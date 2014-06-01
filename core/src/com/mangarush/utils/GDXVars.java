package com.mangarush.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/** Bunch of constants LibGDX-related */
public class GDXVars {
	/** Game's asset manager */
	public static final AssetManager assetManager = new AssetManager();

	// HUD-batch
	// Don't move : projectionMatrix can be set once
	// Need to be set (default coor sys = pixel : if scaled : bug)
	public static final Batch hudBatch = new SpriteBatch();

	/** Path to directories */
	public static final String atlasesDir = "atlases/";
	public static final String backgroundDir = "backgrounds/";
	public static final String charactersDir = "characters/";
	public static final String fontDir = "fonts/";
	public static final String mapsDir = "maps/";
	public static final String tilesDir = "tiles/";

	/** Paths to the game font */
	public static final String defaultFont = fontDir + "font_01.fnt";

	/** Contains path to all map tiles textures */
	private static final String[] mapTilesTextures = {
			tilesDir + "grassLeft.png",
			tilesDir + "grassMid.png",
			tilesDir + "grassRight.png",
			tilesDir + "grassFill.png",
			tilesDir + "grassHalfLeft.png",
			tilesDir + "grassHalfMid.png",
			tilesDir + "grassHalfRight.png",
			tilesDir + "grassHalfWaterLeft.png",
			tilesDir + "grassHalfWaterMid.png",
			tilesDir + "grassHalfWaterRight.png",
			tilesDir + "waterFill.png",
			tilesDir + "waterTop.png",
			tilesDir + "lavaFill.png",
			tilesDir + "lavaTop.png",
			tilesDir + "signRight.png"
	};

	/** MapChunk's .tmx files */
	public static final String[] mapChunksTmx = {
			mapsDir + "chunk_01.tmx",
			mapsDir + "chunk_02.tmx",
			mapsDir + "chunk_03.tmx",
			mapsDir + "chunk_04.tmx"
	};

	/** Load all needed assets in assetManager */
	public static void loadSurvivorStageAssets() {
		// Map tiles textures
		for (String tile : mapTilesTextures)
			assetManager.load(tile, Texture.class);
	}

	/** Load all needed assets for main menu */
	public static void loadSplashAssets() {
		// Tell the manager to load assets for the loading screen
		assetManager.load("atlases/loading.pack", TextureAtlas.class);
		assetManager.load("backgrounds/splash.png", Texture.class);
		assetManager.load(defaultFont, BitmapFont.class);
	}

	/** Return the texture associated with the value */
	public static Texture getMapTileTexture(short value) {
		if (value < 0 || value >= mapTilesTextures.length)
			return null;

		return assetManager.get(mapTilesTextures[value], Texture.class);
	}
}
