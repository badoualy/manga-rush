package com.mangarush.map;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mangarush.map.RandomMap.MapConstants;
import com.mangarush.ui.utils.B2DVars;

/** A chunk of map used for map generation, fixed size */
public class MapChunk {
	/** Chunks size in tiles */
	public static final int WIDTH = 30;
	public static final int HEIGHT = 15;

	private short[][] map; // Tiles values
	private List<Rectangle> floorRects; // Floor objects rectangles

	public MapChunk(String file) {
		map = new short[WIDTH][HEIGHT];
		floorRects = new ArrayList<Rectangle>(10);

		// Load chunk
		parseMap(file);
	}

	/** Load a *.tmx file using LibGDX's api and retrieve cells datas */
	private void parseMap(String file) {
		// Load tmx file
		TiledMap tiledMap = new TmxMapLoader().load(file);

		// Load floor layer and loop through all floor cells
		TiledMapTileLayer floorLayer = (TiledMapTileLayer) tiledMap.getLayers().get("floor");
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				Cell cell = floorLayer.getCell(i, j);
				if (cell != null) {
					// -1 because id in tmx files starts with 1
					map[i][j] = (short) (cell.getTile().getId() - 1);
				} else
					map[i][j] = MapConstants.EMPTY;
			}
		}

		// Load floor objects layer
		MapLayer objLayer = tiledMap.getLayers().get("floor_obj");
		if (objLayer != null && objLayer.getObjects() != null) {
			for (MapObject obj : objLayer.getObjects()) {
				if (obj instanceof RectangleMapObject)
					floorRects.add(((RectangleMapObject) obj).getRectangle());
			}
		}

		// Free, don't need it anymore
		tiledMap.dispose();
	}

	/**
	 * Add chunk's objects in world at the given origin (no need to instanciate
	 * same chunk multiple times). Fixtures are added to B2DVars.floorBody and
	 * B2DVars.floorFixtures
	 * 
	 * @param world
	 *            world to update
	 * @param originX
	 *            the chunk's x-axis origin in stage coordinates (!B2D)
	 * @param originY
	 *            the chunk's y-axis origin in stage coordinates (!B2D)
	 */
	public void setupToWorld(World world, float originX, float originY) {
		// Safety
		if (world == null)
			return;

		// Temp vars
		FixtureDef fdef = new FixtureDef();
		PolygonShape ps;
		Vector2 center = new Vector2();

		// Common fixture defs 
		fdef.filter.categoryBits = B2DVars.GROUND_MASK;
		fdef.filter.maskBits = B2DVars.PLAYER_MASK | B2DVars.ENEMY_MASK;

		// Loop through all floor rectangles
		// Don't need to be synchronized if called in ui's thread
		for (Rectangle rect : floorRects) {
			// Fixture
			center.x = (originX + rect.x + rect.width / 2f) / B2DVars.PPM;
			center.y = (originY + rect.y + rect.height / 2f) / B2DVars.PPM;
			fdef.shape = ps = new PolygonShape();
			ps.setAsBox(rect.width / 2f / B2DVars.PPM, rect.height / 2f / B2DVars.PPM, center, 0);
			B2DVars.groundFixtures.add(B2DVars.groundBody.createFixture(fdef));
			ps.dispose();
		}
	}

	public short getMap(int x, int y) {
		return map[x][y];
	}
}
