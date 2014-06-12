package com.mangarush.ui.actors;

import static com.mangarush.ui.utils.B2DVars.PPM;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mangarush.ui.utils.B2DVars;

/** Projectile actor that fly straight until hitting something */
public class Projectile extends BodyActor {
	private final TextureRegion texture;

	private boolean hit; // Has hit ?

	public Projectile(TextureRegion texture, World world, Vector2 position) {
		super(world, position, texture.getRegionWidth(), texture.getRegionHeight());

		this.texture = texture;
		setBounds(position.x, position.y, texture.getRegionWidth(), texture.getRegionHeight());

		hit = false;
	}

	@Override
	protected void initBody(World world, Vector2 position, float width, float height) {
		// Object instanciate
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape ps;

		// Body
		bdef.position.set(position.x / PPM, position.y / PPM);
		bdef.type = BodyType.DynamicBody; // To dynamic
		bdef.fixedRotation = true; // Animation handle the rotations
		body = world.createBody(bdef);
		body.setUserData(this);

		// Fixture
		fdef.shape = ps = new PolygonShape();
		fdef.friction = 0;
		fdef.isSensor = true; // Avoid moving target
		fdef.density = 1;
		fdef.filter.categoryBits = B2DVars.PROJECTILE_MASK;
		fdef.filter.maskBits = B2DVars.ENEMY_MASK;
		ps.setAsBox(width / 2f / PPM, height / 2f / PPM);
		body.createFixture(fdef);
		ps.dispose();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = batch.getColor();
		batch.setColor(getColor());
		batch.draw(texture, getX(), getY());
		batch.setColor(color);
	}

	@Override
	public void act(float delta) {
		// Check if have to remove from stage
		if (hit && getActions().size == 0) {
			addAction(Actions.sequence(Actions.fadeOut(0.05f), new Action() {

				@Override
				public boolean act(float delta) {
					remove();
					return true;
				}
			}));
		}

		// Setting speed
		body.setLinearVelocity(15f, 0f);
		super.act(delta);
	}

	/** Notify to destroy the projectile */
	public void hit() {
		hit = true;
	}
}
