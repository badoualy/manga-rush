package com.mangarush.ui.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mangarush.constants.Paths;
import com.mangarush.map.MapConstants;

/**
 * Bunch of constants and globals LibGDX-related : textures MUST be non-static :
 * or will end
 * up in a bug when exiting and relaunching the game on android
 * Some stuff are non-static (assetManager) because it causes problem on android
 * on resuming. Strings and stuff don't need to be non-static.
 */
public class GDXVars {
	/** Game's asset manager */
	public final AssetManager assetManager = new AssetManager();

	/**
	 * Map tiles regions : using Regions instead of Texture makes the draw
	 * faster, because the atlas is send once to the GPU
	 */
	public TextureRegion[] tilesRegions = null;

	/** Load all needed assets for LoadingStage in assetManager */
	public void loadSplashAssets() {
		// Tell the manager to load assets for the loading screen
		assetManager.load("atlases/ui.pack", TextureAtlas.class);
		assetManager.load("atlases/loading.pack", TextureAtlas.class);
		assetManager.load("atlases/characters.pack", TextureAtlas.class);
		assetManager.load("backgrounds/splash.png", Texture.class);
		assetManager.load(Paths.defaultFont, BitmapFont.class);
	}

	/** Load all needed assets for SurvivorStage in assetManager */
	public void loadSurvivorStageAssets() {
		// Background
		assetManager.load(Paths.stageBackground, Texture.class);

		// Atlas
		assetManager.load(Paths.tilesPack, TextureAtlas.class);

		// Characters
		for (String character : Paths.charactersAtlases)
			assetManager.load(character, TextureAtlas.class);

		// Sounds
		assetManager.load(Paths.jumpSound, Sound.class);
	}

	/** Return the texture associated with the value */
	public TextureRegion getTileTexture(short type) {
		// Non existing type
		if (type < 0 || type > MapConstants.TYPE_COUNT)
			return null;
		// First call
		if (tilesRegions == null) {
			// First call allocate an array with all texture : findRegion can be slow, cache the result
			TextureAtlas tilesAtlas = getTextureAtlas(Paths.tilesPack);
			tilesRegions = new TextureRegion[MapConstants.TYPE_COUNT];
			for (int i = 0; i < tilesRegions.length; i++)
				tilesRegions[i] = tilesAtlas.findRegion("" + i);
		}

		return tilesRegions[type];
	}

	/** Shortcut assetManager.get(name, Texture.class) */
	public Texture getTexture(String name) {
		return assetManager.get(name, Texture.class);
	}

	/** Shortcut assetManager.get(name, TextureAtlas.class) */
	public TextureAtlas getTextureAtlas(String name) {
		return assetManager.get(name, TextureAtlas.class);
	}

	/** Shortcut assetManager.get(name, BitmapFont.class) */
	public BitmapFont getFont(String name) {
		return assetManager.get(name, BitmapFont.class);
	}
}
