package com.mangarush.ui.actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mangarush.ui.utils.B2DVars;

/**
 * Abstract type defining an actor following a B2D body position. Actor size can
 * be different from body's size. body.getUserData() should be a reference to
 * the related BodyActor instance
 */
public abstract class BodyActor extends Actor {
	// B2D-body
	protected Body body;

	public BodyActor(final World world, final Rectangle bounds) {
		initBody(world, bounds);
	}

	/**
	 * Instanciate and set a B2D body in the given world with the current
	 * initial position as center
	 */
	protected abstract void initBody(final World world, final Rectangle bounds);

	@Override
	public boolean remove() {
		// Return true if it had parent
		if (super.remove()) {
			// Remove body
			body.getWorld().destroyBody(body);
			return true;
		}

		return false;
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

	public Vector2 getPosition() {
		return new Vector2(getX(), getY());
	}
}
