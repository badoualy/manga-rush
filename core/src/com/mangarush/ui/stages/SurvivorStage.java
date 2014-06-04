package com.mangarush.ui.stages;

import static com.mangarush.ui.Game.V_HEIGHT;
import static com.mangarush.ui.Game.V_WIDTH;
import static com.mangarush.utils.B2DVars.PPM;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.mangarush.ui.actors.RandomMapRenderer;
import com.mangarush.ui.graphics.Background;
import com.mangarush.ui.handlers.MRContactListener;
import com.mangarush.utils.B2DVars;
import com.mangarush.utils.MRVars;
import com.mangarush.utils.SaveData;

/** Survivor stage : continue until player lose */
public class SurvivorStage extends Stage {
	// Constants
	private final int tileSize = 32;
	private final float backgroundSpeed = 1f / 2f; // Background speed (relative to platform speed)
	private final float timeBeforeStart = 0.75f; // Time in seconds before player starts falling

	// Map
	private RandomMapRenderer map;

	// B2D
	private World world;

	// Cameras
	private OrthographicCamera cam; // Useful to have a direct reference
	//private OrthographicCamera b2dCam; // B2D World coordinates camera

	// Sprite Batchs
	private Batch batch; // Useful to have a direct reference
	private Batch hudBatch; // HUD batch (static position on screen)

	// Actors and graphics
	private Player player;
	private HUD hud;
	private Background background;

	// Some useful values
	private boolean over; // Game is over
	private float elapsedTime; // Time elapsed since start
	private float camCenterX; // Center of screen in cam coordinates

	public SurvivorStage() {
		super();

		initViewport(); // Worldsize, cameras
		initActors(); // Stage content (actors)
		initB2DWorld(); // B2D world
		initMap(); // Initialise map

		over = false;
		elapsedTime = 0f;
	}

	/** Load cameras and set up viewport */
	private void initViewport() {
		getViewport().setWorldSize(V_WIDTH, V_HEIGHT);

		// Default cam : don't need BoundedCamera
		cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		getViewport().setCamera(cam); // Set stage default camera
		camCenterX = cam.viewportWidth / 2f;

		// Get default batch
		batch = getBatch();

		// Use first position to set HUD's batch projection : entire screen
		hudBatch = new SpriteBatch();
		hudBatch.setProjectionMatrix(cam.combined);

		// B2D camera
		//b2dCam = new OrthographicCamera();
		//b2dCam.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);
	}

	/** Init stage's actors (HUD, players, ...) */
	private void initActors() {
		// Player
		player = new Player();

		// HUD (bounds = screen)
		hud = new HUD(player, hudBatch);
		hud.setBounds(0, 0, V_WIDTH, V_HEIGHT);

		// Backgorund
		background = new Background(Game.GDXVars().getTexture(MRVars.stageBackground), hudBatch);

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
		bdef.position.set(pWidth / 2f / PPM, (V_HEIGHT - pHeight) / PPM);
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
		map = new RandomMapRenderer(world, V_WIDTH / tileSize, V_HEIGHT / tileSize, tileSize);

		// Add to the back (behind all other actors)
		addActor(map);
		map.toBack();
	}

	/** Gamestep */
	private void update(float delta) {
		// Check to remove old fixtures
		if (player.isOnGround()) {
			// First fixture should be the one player is on right now
			while (B2DVars.fixtures.get(0) != player.getFloorFix()) {
				B2DVars.floorBody.destroyFixture(B2DVars.fixtures.get(0));
				B2DVars.fixtures.remove(0);
			}
		}

		// Update B2D world
		world.step(Game.FIXED_FPS, 6, 2);
	}

	@Override
	public void draw() {
		// Update cam position (+ fix position)
		if (player.getCenterX() > cam.viewportWidth / 2f) {
			// We can start moving camera : to int to smooth
			cam.position.x = (int) player.getCenterX();
			cam.update();
		}
		// Set current position in map (to avoid useless draws
		map.setPosition(cam.position.x - camCenterX);
		// Update batch projection matrix
		batch.setProjectionMatrix(cam.combined);

		// Update Box2D camera position (+ fix position)
		//b2dCam.position.x = Math.max(player.getCenterX() / PPM, b2dCam.viewportWidth / 2);
		//b2dCam.update();

		// Draw background : fill screen
		background.draw(hudBatch, cam.position.x * backgroundSpeed, 0, V_WIDTH, V_HEIGHT);

		// Draw actors
		/* Instead of calling super.draw() which would set a second time
		 * camera's position and bach projection matrix, copy the useful
		 * content of super.draw() */
		batch.begin();
		getRoot().draw(batch, 1);
		batch.end();
	}

	@Override
	public void act(float delta) {
		elapsedTime += delta;
		if (elapsedTime < timeBeforeStart)
			return;

		// No need to update b2d world after it's over
		if (!over) {
			update(delta); // Update game -> new step : execute before others (better than addAction)
		}

		// If player lost, we continue acting because HUD may not be done
		super.act(delta);

		// We wait for the player to fall under map or on the ground to end
		if (over == player.isAlive() && player.getLinearVelocity().y == 0)
			endGame();
	}

	@Override
	public void dispose() {
		// batch is disposed in super.dipose()
		super.dispose();
		world.dispose();
		hudBatch.dispose();

		// So it's recreated
		B2DVars.floorBody = null;
	}

	public void endGame() {
		over = true;
		// Update save file
		SaveData save = Game.Save();
		save.timeSpend += elapsedTime; // Update timespend playing
		if (player.hasHighScored()) { // New high score
			save.highScore = player.getScore();
		}
	}

	public Player getPlayer() {
		return player;
	}
}
