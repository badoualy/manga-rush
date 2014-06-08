package com.mangarush.ui.stages;

import static com.mangarush.ui.Game.V_HEIGHT;
import static com.mangarush.ui.Game.V_WIDTH;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mangarush.constants.Paths;
import com.mangarush.ui.Game;
import com.mangarush.ui.actions.HighScoreAction;
import com.mangarush.ui.actors.HUD;
import com.mangarush.ui.actors.Player;
import com.mangarush.ui.actors.RandomMapRenderer;
import com.mangarush.ui.graphics.Background;
import com.mangarush.ui.handlers.MRContactListener;
import com.mangarush.ui.utils.B2DVars;
import com.mangarush.utils.Message;
import com.mangarush.utils.SaveData;

/** Survivor stage : continue until player lose */
public class SurvivorStage extends Stage {
	// Constants
	private final int tileSize = 32;
	private final float backgroundSpeed = 1f / 2f; // Background speed (relative to platform speed)
	private final float timeBeforeStart = 0.5f; // Time in seconds before player starts falling

	// Map
	private RandomMapRenderer map;

	// B2D
	private World world;

	// References to super attributes
	private OrthographicCamera cam;
	private Batch batch;

	// Actors and graphics
	private Player player;
	private HUD hud;
	private Background background;

	// Actions
	Action highScoreAction; // Check highscore action

	// Some useful values
	private boolean over; // Game is over ?
	private boolean highScored; // New high score ?
	private float elapsedTime; // Time elapsed since start
	private float camCenterX; // Center of screen in cam coordinates

	public SurvivorStage(int character) {
		super();

		initViewport(); // Worldsize, cameras
		initB2DWorld(); // B2D world
		initActors(character); // Stage content (actors)
		initMap(); // Initialise map

		over = false;
		highScored = false;
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
	}

	/**
	 * Setup Box2D world : after some issues, only thing working was to use
	 * boxes
	 */
	private void initB2DWorld() {
		// World : Graivity gx = 0, gy = -9.81 topward
		world = new World(B2DVars.GRAVITY, true);
		world.setContactListener(new MRContactListener());

		// Floor body
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.StaticBody;
		bdef.fixedRotation = true;
		bdef.position.set(0, 0);
		B2DVars.floorBody = world.createBody(bdef);
	}

	/** Init stage's actors (HUD, players, ...) */
	private void initActors(int character) {
		// Player
		player = new Player(character, world);

		// HUD (bounds = screen)
		hud = new HUD(player);
		hud.setBounds(0, 0, V_WIDTH, V_HEIGHT);

		// Backgorund
		background = new Background(Game.GDXVars().getTexture(Paths.stageBackground));

		// Add actors in right order
		addActor(hud);
		addActor(player);

		// Highscore checking action : we had it to the player for more logic
		highScoreAction = new HighScoreAction(this);
		player.addAction(highScoreAction);
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
		if (player.isOnGround() && !B2DVars.floorFixtures.isEmpty()) {
			// First fixture should be the one player is on right now
			while (B2DVars.floorFixtures.get(0) != player.getFloorFix()) {
				B2DVars.floorBody.destroyFixture(B2DVars.floorFixtures.get(0));
				B2DVars.floorFixtures.remove(0);
			}
		}

		// Update B2D world
		world.step(Game.FIXED_FPS, 6, 3);
	}

	@Override
	public void draw() {
		// Update cam position (+ fix position) (test to see if we can start moving camera)
		if (player.getCenterX() > cam.viewportWidth / 2f) {
			// We can start moving camera : cast to int to be smoother
			cam.position.x = player.getCenterX();
			cam.update();
			// Set current position in map (to avoid useless draws
			map.setPosition(cam.position.x - camCenterX);
			hud.setPosition(cam.position.x - cam.viewportWidth / 2f, 0);
			background.setPosition(cam.position.x - cam.viewportWidth / 2f, 0);
		}
		// Update batch projection matrix
		batch.setProjectionMatrix(cam.combined);

		/* Instead of calling super.draw() which would set a second time
		 * camera's position and bach projection matrix, copy the useful
		 * content of super.draw() */
		batch.begin();
		// Draw background : fill screen : disable blending : better perf
		batch.disableBlending();
		background.draw(batch, cam.position.x * backgroundSpeed, 0, V_WIDTH, V_HEIGHT);
		batch.enableBlending();
		// Draw actors
		getRoot().draw(batch, 1);
		batch.end();
	}

	@Override
	public void act(float delta) {
		elapsedTime += delta;
		if (elapsedTime < timeBeforeStart)
			return;

		// No need to update b2d world after it's over
		if (!over)
			update(delta); // Update game -> new step : execute before others

		// If player lost, we continue acting because HUD may not be done
		super.act(delta);

		// We wait for the player to fall under map or on the ground to end
		if (over == player.isAlive())
			endGame();
	}

	@Override
	public void dispose() {
		// Leaving before game over
		if (!over)
			endGame();

		// batch is disposed in super.dipose()
		super.dispose();
		world.dispose();

		// So it's recreated in MapChunk if player replays
		B2DVars.floorBody = null;
		B2DVars.floorFixtures.clear();
	}

	/** Player made a new highscore */
	public void highScoreBeaten() {
		highScored = true;
		// Print message on hud
		hud.showMessage(Message.highScoreMessage());
	}

	/** End the current game, do some ending stuff */
	public void endGame() {
		over = true;

		// Print gameover message
		hud.showMessage(Message.gameOverMessage());
		hud.growUp();

		// Update save file
		SaveData save = Game.Save();
		save.timeSpend += elapsedTime; // Update timespend playing
		if (highScored) { // New high score
			save.highScore = player.getScore();
		}
		// Save now (flush)	in case the game crashes
		save.save();
	}

	public Player getPlayer() {
		return player;
	}
}
