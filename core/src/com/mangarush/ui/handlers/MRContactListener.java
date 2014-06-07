package com.mangarush.ui.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mangarush.ui.actors.Player;
import com.mangarush.ui.utils.B2DVars;

/** B2D collisions listener */
public class MRContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Fixture fA = contact.getFixtureA();
		Fixture fB = contact.getFixtureB();
		Fixture fTmp;

		// Rotation : want Player in fA
		if (fB != null && fB.getBody().getUserData() instanceof Player) {
			fTmp = fA;
			fA = fB;
			fB = fTmp;
		}

		if (fA.getBody().getUserData() != null && fA.getBody().getUserData() instanceof Player) {
			// fA is a fixture from player
			Player player = ((Player) fA.getBody().getUserData());
			if (fA.getUserData() != null) {
				// Chck which sensor
				if (fA.getUserData().equals(B2DVars.USERD_FOOT_SENSOR)) {
					// fA is foot sensor
					player.touchedGround(fB);
				}
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fA = contact.getFixtureA();
		Fixture fB = contact.getFixtureB();
		Fixture fTmp;

		// Rotation : want Player in fA
		if (fB != null && fB.getBody().getUserData() instanceof Player) {
			fTmp = fA;
			fA = fB;
			fB = fTmp;
		}

		if (fA.getBody().getUserData() != null && fA.getBody().getUserData() instanceof Player) {
			// fA is a fixture from player
			Player player = ((Player) fA.getBody().getUserData());
			if (fA.getUserData() != null && fA.getUserData().equals(B2DVars.USERD_FOOT_SENSOR)) {
				// fA is foot sensor
				player.leftGround();
			}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}
}
