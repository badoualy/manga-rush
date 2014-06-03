package com.mangarush.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Bunch of constants LibGDX-related : textures MUST be non-static : or will end
 * up in a bug when exiting and relaunching the game on android
 */
public class GDXVars {
	/** Game's asset manager */
	public final AssetManager assetManager = new AssetManager();

	/**
	 * HUD-batch
	 * Don't move : projectionMatrix can be set once
	 * Need to be set (default coor sys = pixel : if scaled : bug)
	 */
	public final Batch hudBatch = new SpriteBatch();

	/** Path to directories */
	public final String atlasesDir = "atlases/";
	public final String backgroundsDir = "backgrounds/";
	public final String charactersDir = "characters/";
	public final String fontDir = "fonts/";
	public final String mapsDir = "maps/";
	public final String tilesDir = "tiles/";

	/** Paths to the game font */
	public final String defaultFont = fontDir + "font_01.fnt";

	/** Splash screen background */
	public final String splashBackground = backgroundsDir + "splash.png";

	/** Loading pack screen assets */
	public final String loadingPack = atlasesDir + "loading.pack";

	/** Contains path to all map tiles textures */
	private final String[] mapTilesTextures = {
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
	public final String[] mapChunksTmx = {
			mapsDir + "chunk_01.tmx",
			mapsDir + "chunk_02.tmx",
			mapsDir + "chunk_03.tmx",
			mapsDir + "chunk_04.tmx"
	};

	/** Characters atlases */
	public final String[] charactersAtlases = {
		charactersDir + "naruto.txt",
		charactersDir + "mikasa.txt",
		charactersDir + "shana.txt"
	};

	/** Load all needed assets in assetManager */
	public void loadSurvivorStageAssets() {
		// Map tiles textures
		for (String tile : mapTilesTextures)
			assetManager.load(tile, Texture.class);

		// Characters
		for (String character : charactersAtlases)
			assetManager.load(character, TextureAtlas.class);
	}

	/** Load all needed assets for main menu */
	public void loadSplashAssets() {
		// Tell the manager to load assets for the loading screen
		assetManager.load("atlases/loading.pack", TextureAtlas.class);
		assetManager.load("backgrounds/splash.png", Texture.class);
		assetManager.load(defaultFont, BitmapFont.class);
	}

	/** Return the texture associated with the value */
	public Texture getMapTileTexture(short value) {
		if (value < 0 || value >= mapTilesTextures.length)
			return null;

		return assetManager.get(mapTilesTextures[value], Texture.class);
	}

	/** Return the texture loaded by the asset manager */
	public Texture getTexture(String name) {
		return assetManager.get(name, Texture.class);
	}

	/** Return the texture atlas loaded by the asset manager */
	public TextureAtlas getTextureAtlas(String name) {
		return assetManager.get(name, TextureAtlas.class);
	}

	/** Return the bitmap font loaded by the asset manager */
	public BitmapFont getFont(String name) {
		return assetManager.get(name, BitmapFont.class);
	}
}
