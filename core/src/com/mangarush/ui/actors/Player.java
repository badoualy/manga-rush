package com.mangarush.ui.actors;

import static com.mangarush.ui.utils.B2DVars.PPM;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mangarush.constants.Paths;
import com.mangarush.ui.Game;
import com.mangarush.ui.utils.B2DVars;

/** Player actor to use in stage */
public class Player extends Character {
	// Animations
	private final Animation runAnimation;
	private final Animation jumpAnimation;
	private final Animation fallAnimation;
	private final Animation throwAnimation;
	private final TextureRegion projectile;

	// Utils
	private Fixture floorFix; // Current floor fixture
	private int groundContacts;
	private float lastJump; // Time elapsed since last jump(in seconds)
	private boolean doubleJumped;
	private boolean threw; // Did throw the projectile ?

	public Player(final int characterId, final World world, final Vector2 position) {
		super(characterId, world, new Rectangle(position.x, position.y, 45, 50));

		// Initial state
		state = State.FALL;

		// Create animations and textures
		runAnimation = new Animation(0.10f, atlas.findRegions("run"), PlayMode.LOOP);
		jumpAnimation = new Animation(0.15f, atlas.findRegions("jump"), PlayMode.LOOP);
		fallAnimation = new Animation(0.15f, atlas.findRegions("fall"), PlayMode.LOOP);
		throwAnimation = new Animation(0.06f, atlas.findRegions("throw"));
		projectile = atlas.findRegion("projectile");
		stateTime = 0f;

		// In the air and can't jump
		groundContacts = 0;
		lastJump = 0;
		doubleJumped = true;
		threw = false;
	}

	@Override
	protected void initBody(final World world, final Rectangle bounds) {
		super.initBody(world, bounds);

		/* Init the player foot sensor, basic characters don't need it since
		 * they don't jump */
		FixtureDef fdef = new FixtureDef();
		PolygonShape ps;

		fdef.isSensor = true;
		fdef.friction = 0;
		fdef.density = 1;
		fdef.filter.categoryBits = B2DVars.PLAYER_MASK;
		fdef.filter.maskBits = B2DVars.GROUND_MASK;
		fdef.shape = ps = new PolygonShape();
		ps.setAsBox(bounds.width / 6f / PPM, 5f / PPM, new Vector2(0, -bounds.height / 2f / PPM), 0f);
		body.createFixture(fdef).setUserData(B2DVars.USERD_FOOT_SENSOR);
		ps.dispose();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// Get frame to draw
		TextureRegion currFrame;
		switch (state) {
			case JUMP:
				currFrame = jumpAnimation.getKeyFrame(stateTime);
				break;
			case THROW:
				currFrame = throwAnimation.getKeyFrame(stateTime);
				break;
			case BUMP: // Shouldn't happen
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

		// Check if need to create projectile
		if (state == State.THROW && !threw && throwAnimation.getKeyFrameIndex(stateTime) == 1) {
			// Get center-right position
			Vector2 pos = getPosition().add(getWidth(), getHeight() / 2f);
			getStage().addActor(new Projectile(projectile, body.getWorld(), pos));
			threw = true;
		}

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
		} else if (state == State.THROW && throwAnimation.isAnimationFinished(stateTime)) {
			// By putting this case before JUMP, don't have to test y velocity
			if (isOnGround())
				state = State.RUN;
			else
				state = State.JUMP;
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
		} else if (isAlive() && getY() < -getHeight() * 1.5f) {
			// Player is under the map : lost : *1.5 for drawing safety
			body.setLinearVelocity(0, 0);
			setAlive(false);
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

		Game.GDXVars().assetManager.get(Paths.jumpSound, Sound.class).play();
		state = State.JUMP;
		stateTime = 0f;
	}

	/** Throw a projectile */
	public void throwProjectile() {
		if (state == State.THROW)
			return;
		state = State.THROW;
		threw = false;
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

	/** Return the fixture the player is on or null is !isOnGroun() */
	public Fixture getFloorFix() {
		return floorFix;
	}

	/** Return current score : position in B2DWorld */
	public int getScore() {
		return (int) body.getPosition().x;
	}
}
