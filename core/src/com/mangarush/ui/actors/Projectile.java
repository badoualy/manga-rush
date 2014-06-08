package com.mangarush.ui.actors;

import static com.mangarush.ui.utils.B2DVars.PPM;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mangarush.ui.utils.B2DVars;

/** Projectile actor that fly straight until hitting something */
public class Projectile extends BodyActor {
	private final TextureRegion texture;

	public Projectile(TextureRegion texture, World world, Vector2 position) {
		super(world, position, texture.getRegionWidth(), texture.getRegionHeight());

		this.texture = texture;
		setBounds(position.x, position.y, texture.getRegionWidth(), texture.getRegionHeight());
	}

	@Override
	protected void initBody(World world, Vector2 position, float width, float height) {
		// Object instanciate
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape ps;

		// Body
		bdef.position.set((position.x + width / 2f) / B2DVars.PPM, (position.y + height / 2f) / B2DVars.PPM);
		bdef.type = BodyType.DynamicBody; // To dynamic
		bdef.fixedRotation = true; // Animation handle the rotations
		body = world.createBody(bdef);

		// Fixture
		fdef.shape = ps = new PolygonShape();
		fdef.friction = 0;
		fdef.density = 1;
		fdef.filter.categoryBits = B2DVars.PROJECTILE_MASK;
		fdef.filter.maskBits = B2DVars.ENEMY_MASK;
		ps.setAsBox(width / 2f / PPM, height / 2f / PPM);
		body.createFixture(fdef);
		ps.dispose();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, getX(), getY());
	}

	@Override
	public void act(float delta) {
		// Setting speed
		body.setLinearVelocity(15f, 0f);
	}
}
