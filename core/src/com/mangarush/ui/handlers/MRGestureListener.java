package com.mangarush.ui.handlers;

import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.mangarush.ui.actors.Player;

/** Stage gestture listener */
public class MRGestureListener extends GestureAdapter {
	private final Player player;

	public MRGestureListener(final Player player) {
		this.player = player;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		if (player.canJump() && player.isAlive()) {
			player.jump();
			return true;
		}
		return false;
	}
}
