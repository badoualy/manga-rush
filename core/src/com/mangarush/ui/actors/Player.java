package com.mangarush.ui.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mangarush.ui.Game;
import com.mangarush.ui.actions.HighScoreAction;
import com.mangarush.utils.B2DVars;
import com.mangarush.utils.MRVars;

/** Player actor to use in stage */
public class Player extends Actor {
	private enum State {
		RUN, JUMP, FALL, LANDED, DEAD
	}

	// B2D-body
	private Body body;

	// Player state
	private State state;
	private boolean alive;
	private boolean highScored; // New high score ?

	// Sprites and animations
	private TextureAtlas atlas;
	private final int characterId;
	private Animation runAnimation;
	private Animation jumpAnimation;
	private Animation fallAnimation;
	private float stateTime;

	// Utils
	private Fixture floorFix; // Current floor fixture
	private int groundContacts;
	private float lastJump; // Time elapsed since last jump(in seconds)
	private boolean doubleJumped;

	// Actions
	private Action highScoreAction;

	public Player(int characterId) {
		body = null;

		// Initial state
		state = State.FALL;
		alive = true;
		highScored = false;

		// Create animations and textures
		atlas = Game.GDXVars().getTextureAtlas(MRVars.charactersAtlases[characterId]);
		this.characterId = characterId;
		runAnimation = new Animation(0.10f, atlas.findRegions("run"), PlayMode.LOOP);
		jumpAnimation = new Animation(0.15f, atlas.findRegions("jump"), PlayMode.LOOP);
		fallAnimation = new Animation(0.15f, atlas.findRegions("fall"), PlayMode.LOOP);
		stateTime = 0f;

		// In the air and can't jump
		groundContacts = 0;
		lastJump = B2DVars.JUMP_TIMEOUT;
		doubleJumped = true;

		// Default bounds
		setBounds(0, 0, 45, 50);

		// Will check until highscore then stop
		highScoreAction = Actions.forever(new HighScoreAction(this));
		addAction(highScoreAction);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// Get frame to draw
		TextureRegion currFrame;
		switch (state) {
			case JUMP:
				currFrame = jumpAnimation.getKeyFrame(stateTime);
				break;
			case DEAD: // Not in sight
			case FALL:
				currFrame = fallAnimation.getKeyFrame(stateTime);
				break;
			case LANDED:
			case RUN:
			default:
				currFrame = runAnimation.getKeyFrame(stateTime);
				break;
		}

		// Draw
		batch.draw(currFrame, getCenterX() - currFrame.getRegionWidth() / 2f, getY());
	}

	@Override
	public void act(float delta) {
		// Update animation stateTime
		stateTime += delta;

		// Update state
		updateState();

		// Give run impulse to b2d-body if we are running
		if (state == State.RUN)
			body.setLinearVelocity(B2DVars.PLAYER_MAX_SPEED, 0);

		// Update jump permission
		if (lastJump < B2DVars.JUMP_TIMEOUT)
			lastJump += delta;

		// Call added actions
		super.act(delta);
	}

	/** Update player state */
	private void updateState() {
		// Update player state
		if (state == State.LANDED) {
			// Landed : showed LAND frame, now we run
			state = State.RUN;
			stateTime = 0f;
		} else if (state == State.JUMP && body.getLinearVelocity().y < 0) {
			// Jumping and vy < 0 means falling
			state = State.FALL;
			stateTime = 0f;
		} else if (state == State.FALL && isOnGround()) {
			// Falling and on ground means landed
			state = State.LANDED;
			lastJump = 0f;
			doubleJumped = false;
			stateTime = 0f;
		} else if (state == State.RUN && !isOnGround()) {
			// Running and not on ground means falling
			state = State.FALL;
			stateTime = 0f;
		} else if (alive && getY() < -getHeight() * 1.5f) {
			// Player is under the map : lost : *1.5 for drawing safety
			body.setLinearVelocity(0, 0);
			state = State.DEAD;
			alive = false;
		}
	}

	/** Start a jump */
	public void jump() {
		body.setLinearVelocity(0, 0);
		Vector2 impulse = new Vector2(B2DVars.PLAYER_MAX_SPEED, B2DVars.JUMP_FORCE);
		body.applyLinearImpulse(impulse, body.getWorldCenter(), true);

		// If we are on ground : first jump, else double jump
		if (isOnGround())
			lastJump = 0f;
		else {
			doubleJumped = true;
		}

		state = State.JUMP;
		stateTime = 0f;
	}

	@Override
	public float getX() {
		// Use B2D object's coordinates : auto-updated
		if (body != null)
			return body.getPosition().x * B2DVars.PPM - getWidth() / 2;
		// Shouldn't happen !
		return super.getX();
	}

	@Override
	public float getY() {
		// Use B2D object's coordinates : auto-updated
		if (body != null)
			return body.getPosition().y * B2DVars.PPM - getHeight() / 2;
		// Shouldn't happen !
		return super.getY();
	}

	public float getCenterX() {
		// Use B2D object's coordinates : auto-updated
		if (body != null)
			return body.getPosition().x * B2DVars.PPM;
		// Shouldn't happen !
		return super.getX() + getWidth() / 2;
	}

	public boolean isOnGround() {
		return groundContacts > 0;
	}

	public boolean canJump() {
		return (isOnGround() || !doubleJumped) && lastJump >= B2DVars.JUMP_TIMEOUT;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public void touchedGround(Fixture floorFix) {
		groundContacts++;
		this.floorFix = floorFix;
	}

	public void leftGround() {
		groundContacts--;
		this.floorFix = null;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public Fixture getFloorFix() {
		return floorFix;
	}

	/** Return current score : position in B2DWorld */
	public int getScore() {
		return (int) body.getPosition().x;
	}

	/** Return true if player beat his highscore this game */
	public boolean hasHighScored() {
		return highScored;
	}

	/** Call this to notify when player has a new highscore */
	public void highScored() {
		highScored = true;
		removeAction(highScoreAction);
	}

	public Vector2 getLinearVelocity() {
		return body.getLinearVelocity();
	}

	public int getCharacterId() {
		return characterId;
	}
}
