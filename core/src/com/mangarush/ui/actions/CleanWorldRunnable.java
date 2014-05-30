package com.mangarush.ui.actions;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.Body;
import com.mangarush.utils.B2DVars;

/** Runnable that clean world from objects that are not needed anymore */
public class CleanWorldRunnable implements Runnable {
	private float position, lastPosition;

	public CleanWorldRunnable() {
		position = lastPosition = 0;
	}

	@Override
	public void run() {
		// Calculate current position in B2D coordinates
		float b2dPosition = position / B2DVars.PPM;

		// Construct a list of bodies to remove
		List<Body> toRemove = new ArrayList<Body>(10);
		for (Body body : new ArrayList<Body>(B2DVars.bodies)) {
			if (body.getPosition().x < b2dPosition) {
				toRemove.add(body);
			} else {
				// bodies are in x-position in ascending order
				break;
			}
		}

		// Update global bodiesToRemove list
		if (toRemove.size() != 0) {
			synchronized (B2DVars.bodies) {
				B2DVars.bodiesToRemove.addAll(toRemove);
			}
		}

		// Sets last checked position
		lastPosition = position;
	}

	/** Set player current position */
	public void setPosition(float position) {
		this.position = position;
	}

	public float getLastPosition() {
		return lastPosition;
	}
}
