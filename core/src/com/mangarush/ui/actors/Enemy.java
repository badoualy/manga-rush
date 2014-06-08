package com.mangarush.ui.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.mangarush.ui.utils.B2DVars;

/** Character instance for an enemy */
public class Enemy extends Character {
	// Animations
	private final Animation stanceAnimation;

	public Enemy(int characterId, World world, final Vector2 position) {
		super(characterId, world, position);

		// Load animations
		stanceAnimation = new Animation(0.15f, atlas.findRegions("stance"), PlayMode.LOOP);
	}

	@Override
	protected void initBody(World world, Vector2 position, float width, float height) {
		super.initBody(world, position, width, height);

		// By default in character class, category = player and mask = ground
		Filter filter = body.getFixtureList().get(0).getFilterData();
		filter.categoryBits = B2DVars.ENEMY_MASK;
		filter.maskBits = B2DVars.GROUND_MASK | B2DVars.PROJECTILE_MASK;
		body.getFixtureList().get(0).setFilterData(filter);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		TextureRegion currFrame = stanceAnimation.getKeyFrame(stateTime);
		if (!currFrame.isFlipX())
			currFrame.flip(true, false);

		// Draw
		super.draw(batch, parentAlpha, currFrame);
	}
}
