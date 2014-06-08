package com.mangarush.ui.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.mangarush.ui.utils.B2DVars;

/** Player actor to use in stage */
public class Player extends Character {
	// Player state
	private State state;
	private boolean alive;

	// Animations
	private final Animation runAnimation;
	private final Animation jumpAnimation;
	private final Animation fallAnimation;

	// Utils
	private Fixture floorFix; // Current floor fixture
	private int groundContacts;
	private float lastJump; // Time elapsed since last jump(in seconds)
	private boolean doubleJumped;

	public Player(final int characterId, World world, final Vector2 position) {
		super(characterId, world, position);

		// Initial state
		state = State.FALL;
		alive = true;

		// Create animations and textures
		runAnimation = new Animation(0.10f, atlas.findRegions("run"), PlayMode.LOOP);
		jumpAnimation = new Animation(0.15f, atlas.findRegions("jump"), PlayMode.LOOP);
		fallAnimation = new Animation(0.15f, atlas.findRegions("fall"), PlayMode.LOOP);
		stateTime = 0f;

		// In the air and can't jump
		groundContacts = 0;
		lastJump = 0;
		doubleJumped = true;

		// Default bounds
		setBounds(0, 0, 45, 50);
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
		super.draw(batch, parentAlpha, currFrame);
	}

	@Override
	public void act(float delta) {
		// Update state
		updateState();

		// Give run impulse to b2d-body if we are running
		if (state == State.RUN)
			body.setLinearVelocity(B2DVars.PLAYER_MAX_SPEED, 0);

		// Update jump permission
		lastJump += delta;

		// Update stateTime and call actions
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
		else
			doubleJumped = true;

		state = State.JUMP;
		stateTime = 0f;
	}

	/** Return true if player is currently on the ground */
	public boolean isOnGround() {
		return groundContacts > 0;
	}

	/** Return true if player can jump now */
	public boolean canJump() {
		return (isOnGround() || !doubleJumped) && lastJump >= B2DVars.JUMP_TIMEOUT;
	}

	/** Notify the player he touched the ground */
	public void touchedGround(Fixture floorFix) {
		groundContacts++;
		this.floorFix = floorFix;
	}

	/** Notify the player he left the ground */
	public void leftGround() {
		groundContacts--;
		this.floorFix = null;
	}

	/** Return true if player is alive */
	public boolean isAlive() {
		return alive;
	}

	/** Return the fixture the player is on or null is !isOnGroun() */
	public Fixture getFloorFix() {
		return floorFix;
	}

	/** Return current score : position in B2DWorld */
	public int getScore() {
		return (int) body.getPosition().x;
	}
}
