package com.mangarush.ui.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mangarush.ui.utils.B2DVars;

/** Character instance for an enemy */
public class Enemy extends Character {
	// Animations
	private final Animation stanceAnimation;
	private final Animation bumpAnimation;

	public Enemy(int characterId, World world, final Vector2 position) {
		super(characterId, world, position);

		// Load animations
		stanceAnimation = new Animation(0.15f, atlas.findRegions("stance"), PlayMode.LOOP);
		bumpAnimation = new Animation(0.08f, atlas.findRegions("bump"));
	}

	@Override
	protected void initBody(World world, Vector2 position, float width, float height) {
		super.initBody(world, position, width, height);

		// By default in character class, category = player and mask = ground
		Filter filter = body.getFixtureList().get(0).getFilterData();
		filter.categoryBits = B2DVars.ENEMY_MASK;
		filter.maskBits = B2DVars.GROUND_MASK | B2DVars.PROJECTILE_MASK | B2DVars.PLAYER_MASK;
		body.getFixtureList().get(0).setFilterData(filter);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		TextureRegion currFrame = stanceAnimation.getKeyFrame(stateTime);
		if (state == State.BUMP)
			currFrame = bumpAnimation.getKeyFrame(stateTime);

		if (!currFrame.isFlipX())
			currFrame.flip(true, false);

		// Draw
		Color color = batch.getColor();
		batch.setColor(getColor());
		super.draw(batch, parentAlpha, currFrame);
		batch.setColor(color);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (state == State.BUMP && bumpAnimation.isAnimationFinished(stateTime))
			addAction(Actions.sequence(Actions.fadeOut(0.1f), new Action() {
				@Override
				public boolean act(float delta) {
					remove();
					return true;
				}
			}));
	}

	/** Kill the enemy */
	public void kill() {
		state = State.BUMP;
		stateTime = 0f;

		// Now don't collide with player or projectile anymore
		// He is dead, but we can't remove it now until animation is done
		Filter filter = body.getFixtureList().get(0).getFilterData();
		filter.categoryBits = B2DVars.ENEMY_MASK;
		filter.maskBits = B2DVars.GROUND_MASK;
		body.getFixtureList().get(0).setFilterData(filter);
	}
}
