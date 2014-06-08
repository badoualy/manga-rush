package com.mangarush.ui.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

/** Character instance for an enemy */
public class Enemy extends Character {
	// Animations
	private final Animation stanceAnimation;

	public Enemy(int characterId, World world) {
		super(characterId, world);

		// Load animations
		stanceAnimation = new Animation(0.11f, atlas.findRegions("stance"), PlayMode.LOOP);
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
