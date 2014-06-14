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
import com.mangarush.ui.actors.Enemy;
import com.mangarush.ui.stages.SurvivorStage;
import com.mangarush.ui.utils.B2DVars;

/** A chunk of map used for map generation, fixed size */
public class MapChunk {
	/** Chunks size in tiles */
	public static final int WIDTH = 30;
	public static final int HEIGHT = 15;

	private short[][] map; // Tiles values
	private List<Rectangle> groundRects; // ground objects rectangles
	private List<Vector2> enemiesPos; // Positions of all enemy on chunk

	public MapChunk(String file) {
		map = new short[WIDTH][HEIGHT];
		groundRects = new ArrayList<Rectangle>(10);
		enemiesPos = new ArrayList<Vector2>(10);

		// Load chunk
		parseMap(file);
	}

	/** Load a *.tmx file using LibGDX's api and retrieve cells datas */
	private void parseMap(String file) {
		// Load tmx file
		TiledMap tiledMap = new TmxMapLoader().load(file);

		// Load ground layer and loop through all ground cells
		TiledMapTileLayer groundLayer = (TiledMapTileLayer) tiledMap.getLayers().get("ground");
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				Cell cell = groundLayer.getCell(i, j);
				if (cell != null) {
					// -1 because id in tmx files starts with 1
					map[i][j] = (short) (cell.getTile().getId() - 1);
				} else
					map[i][j] = MapConstants.EMPTY;
			}
		}

		// Load ground objects layer
		MapLayer objLayer = tiledMap.getLayers().get("ground_obj");
		if (objLayer != null && objLayer.getObjects() != null) {
			for (MapObject obj : objLayer.getObjects()) {
				if (obj instanceof RectangleMapObject)
					groundRects.add(((RectangleMapObject) obj).getRectangle());
			}
		}

		// Load enemies position
		MapLayer enemiesLayer = tiledMap.getLayers().get("enemy_obj");
		Vector2 position;
		if (enemiesLayer != null && enemiesLayer.getObjects() != null) {
			for (MapObject obj : enemiesLayer.getObjects()) {
				if (obj instanceof RectangleMapObject) {
					position = new Vector2();
					enemiesPos.add(((RectangleMapObject) obj).getRectangle().getPosition(position));
				}
			}
		}

		// Free, don't need it anymore
		tiledMap.dispose();
	}

	/**
	 * Setup this chunk in the stage at the given position. Add needed actors
	 * and bodies (ground + enemies). (no need to instanciate same chunk
	 * multiple times).
	 * 
	 * @param stage
	 *            stage to add actors in
	 * @param originX
	 *            the chunk's x-axis origin in stage coordinates (!B2D)
	 * @param originY
	 *            the chunk's y-axis origin in stage coordinates (!B2D)
	 */
	public void setupToStage(SurvivorStage stage, float originX, float originY) {
		setupGrounToWorld(originX, originY); // Setup ground
		//setupEnemies(stage, originX, originY);
	}

	/**
	 * Called by setupToStage()
	 * Add chunk's ground objects in world at the given origin.
	 * Fixtures are added to B2DVars.groundBody and B2DVars.groundFixtures
	 * 
	 * @param originX
	 *            the chunk's x-axis origin in stage coordinates (!B2D)
	 * @param originY
	 *            the chunk's y-axis origin in stage coordinates (!B2D)
	 */
	private void setupGrounToWorld(float originX, float originY) {
		// Temp vars
		FixtureDef fdef = new FixtureDef();
		PolygonShape ps;
		Vector2 center = new Vector2();

		// Common fixture defs 
		fdef.filter.categoryBits = B2DVars.GROUND_MASK;
		fdef.filter.maskBits = B2DVars.PLAYER_MASK | B2DVars.ENEMY_MASK;

		// Loop through all ground rectangles
		// Don't need to be synchronized if called in ui's thread
		for (Rectangle rect : groundRects) {
			// Fixture
			center.x = (originX + rect.x + rect.width / 2f) / B2DVars.PPM;
			center.y = (originY + rect.y + rect.height / 2f) / B2DVars.PPM;
			fdef.shape = ps = new PolygonShape();
			ps.setAsBox(rect.width / 2f / B2DVars.PPM, rect.height / 2f / B2DVars.PPM, center, 0);
			B2DVars.groundFixtures.add(B2DVars.groundBody.createFixture(fdef));
			ps.dispose();
		}
	}

	/**
	 * Called by setupToStage()
	 * Add chunk's enemies in stage (and their bodies). Bodies are added to
	 * B2DVars.groundBody.getWorld()
	 * 
	 * @param originX
	 *            the chunk's x-axis origin in stage coordinates (!B2D)
	 * @param originY
	 *            the chunk's y-axis origin in stage coordinates (!B2D)
	 */
	private void setupEnemies(SurvivorStage stage, float originX, float originY) {
		for (Vector2 position : enemiesPos) {
			// Have to create a new instance, Vector2.add would change it
			Vector2 pos = new Vector2((position.x + originX) / B2DVars.PPM, (position.y) / B2DVars.PPM);
			stage.addActor(new Enemy(3, B2DVars.groundBody.getWorld(), pos));
		}
	}

	public short getMap(int x, int y) {
		return map[x][y];
	}
}
