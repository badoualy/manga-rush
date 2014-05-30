package com.mangarush.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mangarush.core.RandomMap.MapConstants;

/** Bunch of constants LibGDX-related */
public class GDXVars {
	// Path to directories
	public static final String fontPath = "fonts/";
	public static final String mapsPath = "maps/";
	public static final String charactersPath = "characters/";
	public static final String tilesPath = "tiles/";

	// Game font
	public static final BitmapFont font = new BitmapFont(Gdx.files.internal(fontPath + "font_01.fnt"));

	// Custom batch
	// Don't move : projectionMatrix can be set once
	// Need to be set (default coor sys = pixel : if scaled : bug)
	public static final Batch hudBatch = new SpriteBatch();

	// Texture for SurvivorMap
	public static final Texture GRASS_LEFT_TEX = new Texture(tilesPath + "grassLeft.png");
	public static final Texture GRASS_MID_TEX = new Texture(tilesPath + "grassMid.png");
	public static final Texture GRASS_RIGHT_TEX = new Texture(tilesPath + "grassRight.png");
	public static final Texture GRASS_FILL_TEX = new Texture(tilesPath + "grassFill.png");

	public static final Texture GRASS_HALF_LEFT_TEX = new Texture(tilesPath + "grassHalfLeft.png");
	public static final Texture GRASS_HALF_MID_TEX = new Texture(tilesPath + "grassHalfMid.png");
	public static final Texture GRASS_HALF_RIGHT_TEX = new Texture(tilesPath + "grassHalfRight.png");

	public static final Texture GRASS_HALF_WATER_LEFT_TEX = new Texture(tilesPath + "grassHalfWaterLeft.png");
	public static final Texture GRASS_HALF_WATER_MID_TEX = new Texture(tilesPath + "grassHalfWaterMid.png");
	public static final Texture GRASS_HALF_WATER_RIGHT_TEX = new Texture(tilesPath + "grassHalfWaterRight.png");

	public static final Texture WATER_FILL_TEX = new Texture(tilesPath + "waterFill.png");
	public static final Texture WATER_TOP_TEX = new Texture(tilesPath + "waterTop.png");
	public static final Texture LAVA_FILL_TEX = new Texture(tilesPath + "lavaFill.png");
	public static final Texture LAVA_TOP_TEX = new Texture(tilesPath + "lavaTop.png");
	public static final Texture SIGN_RIGHT_TEX = new Texture(tilesPath + "signRight.png");

	// Return the texture associated with the value (grass_left : grass_left_tex)
	public static Texture getTexture(short value) {
		switch (value) {
			case MapConstants.GRASS_LEFT:
				return GRASS_LEFT_TEX;
			case MapConstants.GRASS_MID:
				return GRASS_MID_TEX;
			case MapConstants.GRASS_RIGHT:
				return GRASS_RIGHT_TEX;
			case MapConstants.GRASS_FILL:
				return GRASS_FILL_TEX;

			case MapConstants.GRASS_HALF_LEFT:
				return GRASS_HALF_LEFT_TEX;
			case MapConstants.GRASS_HALF_MID:
				return GRASS_HALF_MID_TEX;
			case MapConstants.GRASS_HALF_RIGHT:
				return GRASS_HALF_RIGHT_TEX;

			case MapConstants.GRASS_HALF_WATER_LEFT:
				return GRASS_HALF_WATER_LEFT_TEX;
			case MapConstants.GRASS_HALF_WATER_MID:
				return GRASS_HALF_WATER_MID_TEX;
			case MapConstants.GRASS_HALF_WATER_RIGHT:
				return GRASS_HALF_WATER_RIGHT_TEX;

			case MapConstants.WATER_FILL:
				return WATER_FILL_TEX;
			case MapConstants.WATER_TOP:
				return WATER_TOP_TEX;
			case MapConstants.LAVA_FILL:
				return LAVA_FILL_TEX;
			case MapConstants.LAVA_TOP:
				return LAVA_TOP_TEX;

			case MapConstants.SIGN_RIGHT:
				return SIGN_RIGHT_TEX;

			case MapConstants.EMPTY:
			default:
				break;
		}
		return null;
	}

	// MapChunk's .tmx files
	public static final String[] chunksPath = new String[] {
			mapsPath + "chunk_01.tmx", mapsPath + "chunk_02.tmx", mapsPath + "chunk_03.tmx", mapsPath + "chunk_04.tmx"
	};
}
