package com.mangarush.core;

import static com.mangarush.utils.B2DVars.floorBody;

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
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mangarush.core.RandomMap.MapConstants;
import com.mangarush.utils.B2DVars;

/** A chunk of map used for map generation, fixed size */
public class MapChunk {
	// Size in tiles
	public static final int WIDTH = 30;
	public static final int HEIGHT = 15;

	private short[][] map;
	private List<Rectangle> floorRects; // floor objects rectangles

	public MapChunk(String file) {
		map = new short[WIDTH][HEIGHT];
		floorRects = new ArrayList<Rectangle>(10);

		// Load chunk
		parseMap(file);
	}

	private void parseMap(String file) {
		// Load tmx file
		TiledMap tiledMap = new TmxMapLoader().load(file);

		// Load map and loop through all floor cells
		TiledMapTileLayer floorLayer = (TiledMapTileLayer) tiledMap.getLayers().get("floor");
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				Cell cell = floorLayer.getCell(i, j);
				if (cell != null) {
					// -1 because starts with 1
					map[i][j] = (short) (cell.getTile().getId() - 1);
				} else
					map[i][j] = MapConstants.EMPTY;
			}
		}

		// Load floor objects
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
	 * same chunk multiple times)
	 * 
	 * @param world
	 *            world to update
	 * @param originX
	 *            the chunk's x-axis origin
	 * @param originY
	 *            the chunk's y-axis origin
	 */
	public void setupToWorld(World world, float originX, float originY) {
		// Safety
		if (world == null)
			return;

		// First body allocation
		if (floorBody == null) {
			BodyDef bdef = new BodyDef();
			bdef.type = BodyType.StaticBody;
			bdef.fixedRotation = true;
			bdef.position.set(0, 0);

			floorBody = world.createBody(bdef);
		}

		// Temp vars
		FixtureDef fdef = new FixtureDef();
		PolygonShape ps;
		Vector2 center = new Vector2();

		// Common fixture defs 
		fdef.filter.categoryBits = B2DVars.GROUND_MASK;
		fdef.filter.maskBits = B2DVars.PLAYER_MASK;

		// Loop through all floor rectangles
		// Don't need to be synchronized if called in ui's thread
		for (Rectangle rect : floorRects) {
			// Fixture
			center.x = (originX + rect.x + rect.width / 2f) / B2DVars.PPM;
			center.y = (originY + rect.y + rect.height / 2f) / B2DVars.PPM;
			fdef.shape = ps = new PolygonShape();
			ps.setAsBox(rect.width / 2f / B2DVars.PPM, rect.height / 2f / B2DVars.PPM, center, 0);
			floorBody.createFixture(fdef);
			ps.dispose();
		}
	}

	public short getMap(int x, int y) {
		return map[x][y];
	}
}
