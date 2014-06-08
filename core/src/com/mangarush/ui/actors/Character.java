package com.mangarush.ui.actors;

import static com.mangarush.ui.Game.V_HEIGHT;
import static com.mangarush.ui.utils.B2DVars.PPM;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mangarush.constants.Paths;
import com.mangarush.ui.Game;
import com.mangarush.ui.utils.B2DVars;

/**
 * Abstract actor for a character on screen (player or enemy) with an associated
 * B2D-body
 */
public abstract class Character extends Actor {
	protected enum State {
		RUN, JUMP, FALL, LANDED, DEAD
	}

	// B2D-body
	protected Body body;

	// Sprites and animations
	private final int characterId;
	protected final TextureAtlas atlas;
	protected float stateTime;

	/** Takes the ID and the world to add body in */
	public Character(final int characterId, final World world) {
		// Textures
		this.characterId = characterId;
		atlas = Game.GDXVars().getTextureAtlas(Paths.charactersAtlases[characterId]);
		stateTime = 0f;

		// Default bounds
		setBounds(0, 0, 45, 50);

		if (world != null)
			initBody(world);
		else
			body = null;
	}

	/** Init B2D body */
	private void initBody(final World world) {
		// Object instanciate
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape ps;

		// Player body (first position : top-left corner)
		float pWidth = getWidth();
		float pHeight = getHeight();
		bdef.position.set(pWidth / 2f / PPM, (V_HEIGHT - pHeight) / PPM);
		bdef.type = BodyType.DynamicBody; // To dynamic
		bdef.fixedRotation = true;
		body = world.createBody(bdef);
		body.setUserData(this); // Set userData as player to retrieve in contact listener

		// Player fixture
		fdef.shape = ps = new PolygonShape();
		fdef.friction = 0;
		fdef.density = 1;
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
	}

	// Force override
	@Override
	public abstract void draw(Batch batch, float parentAlpha);

	/**
	 * Draw frame at (getCenterX() - frame.getRegionWidth() / 2f, getY()) with
	 * the given batch
	 */
	public void draw(Batch batch, float parentAlpha, TextureRegion frame) {
		// Draw
		batch.draw(frame, getCenterX() - frame.getRegionWidth() / 2f, getY());
	}

	@Override
	public void act(float delta) {
		// Update animation stateTime
		stateTime += delta;

		// Call added actions
		super.act(delta);
	}

	@Override
	public float getX() {
		// Use B2D object's coordinates : auto-updated
		if (body != null)
			return body.getPosition().x * B2DVars.PPM - getWidth() / 2f;
		// Shouldn't happen !
		return super.getX();
	}

	@Override
	public float getY() {
		// Use B2D object's coordinates : auto-updated
		if (body != null)
			return body.getPosition().y * B2DVars.PPM - getHeight() / 2f;
		// Shouldn't happen !
		return super.getY();
	}

	public float getCenterX() {
		// Use B2D object's coordinates : auto-updated
		if (body != null)
			return body.getPosition().x * B2DVars.PPM;
		// Shouldn't happen !
		return super.getX() + getWidth() / 2f;
	}

	public float getCenterY() {
		// Use B2D object's coordinates : auto-updated
		if (body != null)
			return body.getPosition().y * B2DVars.PPM;
		// Shouldn't happen !
		return super.getY() + getHeight() / 2f;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public int getCharacterId() {
		return characterId;
	}
}
