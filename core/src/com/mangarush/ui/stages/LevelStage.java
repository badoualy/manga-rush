package com.mangarush.ui.stages;

import static com.mangarush.ui.Game.V_HEIGHT;
import static com.mangarush.ui.Game.V_WIDTH;
import static com.mangarush.utils.B2DVars.PPM;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mangarush.ui.Game;
import com.mangarush.ui.actors.HUD;
import com.mangarush.ui.actors.Player;
import com.mangarush.ui.graphics.BoundedCamera;
import com.mangarush.ui.handlers.MRContactListener;
import com.mangarush.utils.B2DVars;

public class LevelStage extends Stage {
	private TiledMap tiledMap;
	private int tileSize;
	private int tileMapWidth, tileMapHeight;
	private OrthogonalTiledMapRenderer tmr;

	private World world;
	private Box2DDebugRenderer b2dr;

	private BoundedCamera b2dCam;
	private BoundedCamera cam; // Useful to have a direct reference

	private Player player;
	private HUD hud;

	public LevelStage(String levelPath) {
		super();
		initTiledMap(levelPath); // Tiledmap (HAS to be before cameras init)
		initViewport(); // Worldsize, cameras
		initActors(); // Stage content (actors)
		initB2DWorld(); // B2D world
	}

	/** Load level's map and some variables */
	private void initTiledMap(String levelPath) {
		tiledMap = new TmxMapLoader().load(levelPath);
		tileMapWidth = tiledMap.getProperties().get("width", Integer.class);
		tileMapHeight = tiledMap.getProperties().get("height", Integer.class);
		tileSize = tiledMap.getProperties().get("tilewidth", Integer.class);
		tmr = new OrthogonalTiledMapRenderer(tiledMap);
	}

	/** Load cameras and set up viewport */
	private void initViewport() {
		getViewport().setWorldSize(V_WIDTH, V_HEIGHT);

		// Default cam
		cam = new BoundedCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		cam.setBounds(0, tileMapWidth * tileSize, 0, tileMapHeight * tileSize);
		getViewport().setCamera(cam); // Set stage default camera

		// Use first position to set HUD's batch projection : entire screen
		Game.GDXVars().hudBatch.setProjectionMatrix(cam.combined);

		// B2D camera
		b2dCam = new BoundedCamera();
		b2dCam.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);
		b2dCam.setBounds(0, (tileMapWidth * tileSize) / PPM, 0, (tileMapHeight * tileSize) / PPM);
	}

	/** Init stage's actors (HUD, players, ...) */
	private void initActors() {
		// Player
		player = new Player();

		// HUD (bounds = screen)
		hud = new HUD(player);
		hud.setBounds(0, 0, V_WIDTH, V_HEIGHT);

		// Add actors in right order
		addActor(hud);
		addActor(player);
	}

	/**
	 * Setup Box2D world : after some issues, only thing working was to use
	 * boxes
	 */
	private void initB2DWorld() {
		// World : Graivity gx = 0, gy = -9.81 topward
		world = new World(B2DVars.GRAVITY, true);
		world.setContactListener(new MRContactListener());
		b2dr = new Box2DDebugRenderer();

		// Bodies and fixtures
		Body body;
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape ps;

		// Floor fixtures : 1 fixture for every block
		MapLayer layer = tiledMap.getLayers().get("floor_obj");
		bdef.type = BodyType.StaticBody;
		bdef.fixedRotation = true;
		fdef.filter.categoryBits = B2DVars.GROUND_MASK;
		fdef.filter.maskBits = B2DVars.PLAYER_MASK;
		Rectangle rect;
		for (MapObject obj : layer.getObjects()) { // Every block
			if (obj instanceof RectangleMapObject) { // To RectangleMapObject
				rect = ((RectangleMapObject) obj).getRectangle();

				// Body : position : x + halfWidth (placing center)
				bdef.position.set((rect.x + rect.width / 2) / PPM, (rect.y + rect.height / 2) / PPM);
				body = world.createBody(bdef);

				// Fixture
				fdef.shape = ps = new PolygonShape();
				ps.setAsBox(rect.width / 2 / PPM, rect.height / 2 / PPM);
				body.createFixture(fdef);
			}
		}

		// Player body (first position : top-left corner)
		float pWidth = player.getWidth();
		float pHeight = player.getHeight();
		bdef.position.set(pWidth / 2f / PPM, (V_HEIGHT - pHeight / 2) / PPM);
		bdef.type = BodyType.DynamicBody; // To dynamic
		body = world.createBody(bdef);
		body.setUserData(player); // Set userData as player to retrieve in contact listener
		player.setBody(body); // Set player's actor's body

		// Player fixture
		fdef.shape = ps = new PolygonShape();
		fdef.filter.categoryBits = B2DVars.PLAYER_MASK;
		fdef.filter.maskBits = B2DVars.GROUND_MASK;
		ps.setAsBox(pWidth / 2f / PPM, pHeight / 2f / PPM);
		body.createFixture(fdef);

		// Player foot sensor
		fdef.isSensor = true;
		fdef.shape = ps = new PolygonShape();
		ps.setAsBox(pWidth / 6f / PPM, 5f / PPM, new Vector2(0, -pHeight / 2f / PPM), 0f);
		body.createFixture(fdef).setUserData(B2DVars.USERD_FOOT_SENSOR);

		// Player wall sensor
		fdef.isSensor = true;
		fdef.shape = ps = new PolygonShape();
		ps.setAsBox(pWidth / 6f / PPM, 5f / PPM, new Vector2(pWidth / 2f / PPM, 0), 0f);
		body.createFixture(fdef).setUserData(B2DVars.USERD_WALL_SENSOR);
	}

	/** Gamestep */
	private void update(float delta) {
		// Update B2D world
		world.step(Game.STEP, 6, 2);

		// Exit when done or fall : TODO
		if (player.getX() / PPM > b2dCam.getXmax() || player.getY() / PPM < 0)
			player.setAlive(false);
	}

	@Override
	public void draw() {
		// Update default cam position
		cam.setPosition(player.getCenterX(), 0);
		cam.update();
		getBatch().setProjectionMatrix(cam.combined);

		// Update Box2D camera position
		b2dCam.setPosition(player.getCenterX() / PPM, 0);
		b2dCam.update();

		// Draw tiled map : no need to use actor
		tmr.setView(cam);
		tmr.render();

		// Draw B2D world : no need to use actor
		b2dr.render(world, b2dCam.combined);

		// Draw actors
		super.draw();
	}

	@Override
	public void act(float delta) {
		update(delta); // Update game -> new step : execute before others (better than addAction)

		// If player lost, we continue printing, but not acting
		// Means we update screen for position only
		if (player.isAlive())
			super.act(delta);
	}

	public Player getPlayer() {
		return player;
	}
}
