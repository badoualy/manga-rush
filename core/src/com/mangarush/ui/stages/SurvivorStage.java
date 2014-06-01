package com.mangarush.ui.stages;

import static com.mangarush.ui.Game.V_HEIGHT;
import static com.mangarush.ui.Game.V_WIDTH;
import static com.mangarush.utils.B2DVars.PPM;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mangarush.ui.Game;
import com.mangarush.ui.actors.HUD;
import com.mangarush.ui.actors.Player;
import com.mangarush.ui.actors.RandomMapActor;
import com.mangarush.ui.handlers.MRContactListener;
import com.mangarush.utils.B2DVars;
import com.mangarush.utils.GDXVars;

/** Survivor stage : continue until player lose */
public class SurvivorStage extends Stage {
	// Map
	private RandomMapActor map;
	private final int tileSize = 32;

	// B2D
	private World world;

	// Cameras
	private OrthographicCamera b2dCam;
	private OrthographicCamera cam; // Useful to have a direct reference

	// Actors
	private Player player;
	private HUD hud;

	public SurvivorStage() {
		super();
		initViewport(); // Worldsize, cameras
		initActors(); // Stage content (actors)
		initB2DWorld(); // B2D world
		initMap(); // Initialise map
	}

	/** Load cameras and set up viewport */
	private void initViewport() {
		getViewport().setWorldSize(V_WIDTH, V_HEIGHT);

		// Default cam : don't need BoundedCamera
		cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		getViewport().setCamera(cam); // Set stage default camera

		// Use first position to set HUD's batch projection : entire screen
		GDXVars.hudBatch.setProjectionMatrix(cam.combined);

		// B2D camera
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);
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

		// Bodies and fixtures
		Body body;
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape ps;

		// Player body (first position : top-left corner)
		float pWidth = player.getWidth();
		float pHeight = player.getHeight();
		bdef.position.set(pWidth / 2f / PPM, (V_HEIGHT - pHeight / 2) / PPM);
		bdef.type = BodyType.DynamicBody; // To dynamic
		bdef.fixedRotation = true;
		body = world.createBody(bdef);
		body.setUserData(player); // Set userData as player to retrieve in contact listener
		player.setBody(body); // Set player's actor's body

		// Player fixture
		fdef.shape = ps = new PolygonShape();
		fdef.friction = 0;
		fdef.filter.categoryBits = B2DVars.PLAYER_MASK;
		fdef.filter.maskBits = B2DVars.GROUND_MASK;
		ps.setAsBox(pWidth / 2f / PPM, pHeight / 2f / PPM);
		body.createFixture(fdef);
		ps.dispose();

		// Player foot sensor
		fdef.isSensor = true;
		fdef.shape = ps = new PolygonShape();
		ps.setAsBox(pWidth / 6f / PPM, 5f / PPM, new Vector2(0, -pHeight / 2f / PPM), 0f);
		body.createFixture(fdef).setUserData(B2DVars.USERD_FOOT_SENSOR);
		ps.dispose();

		// Player wall sensor
		fdef.isSensor = true;
		fdef.shape = ps = new PolygonShape();
		ps.setAsBox(pWidth / 6f / PPM, (player.getHeight() - 5) / 2f / PPM, new Vector2(pWidth / 2f / PPM, 0), 0f);
		body.createFixture(fdef).setUserData(B2DVars.USERD_WALL_SENSOR);
		ps.dispose();
	}

	private void initMap() {
		// Map
		map = new RandomMapActor(world, V_WIDTH / tileSize, V_HEIGHT / tileSize, tileSize);

		addActor(map);
		map.toBack();
	}

	/** Gamestep */
	private void update(float delta) {
		// Check if bodies to remove
		if (!B2DVars.fixToRemove.isEmpty()) {
			synchronized (B2DVars.floorBody) {
				// Remove from world
				for (int i = 0; i < B2DVars.fixToRemove.size();)
					B2DVars.floorBody.destroyFixture(B2DVars.fixToRemove.get(i));
			}
		}

		// Update B2D world
		world.step(Game.STEP, 6, 2);
	}

	@Override
	public void draw() {
		// Update default cam position (+ fix position)
		cam.position.x = Math.max(player.getCenterX(), cam.viewportWidth / 2);
		cam.update();
		map.setPosition(cam.position.x - cam.viewportWidth / 2);
		getBatch().setProjectionMatrix(cam.combined);

		// Update Box2D camera position (+ fix position)
		b2dCam.position.x = Math.max(player.getCenterX() / PPM, b2dCam.viewportWidth / 2);
		b2dCam.update();

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

	@Override
	public void dispose() {
		super.dispose();
		world.dispose();
	}

	public Player getPlayer() {
		return player;
	}
}
