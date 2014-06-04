package com.mangarush.ui.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.mangarush.ui.Game;
import com.mangarush.ui.actors.Player;
import com.mangarush.utils.SaveData;

/**
 * Action that check if player has beaten his highscore, and notify the player
 * when he did
 */
public class HighScoreAction extends Action {
	private Player player;
	private SaveData save;

	public HighScoreAction(Player player) {
		this.player = player;
		save = Game.Save();
	}

	@Override
	public boolean act(float delta) {
		if (player.getScore() > save.highScore) {
			player.highScored();
			return true;
		}
		return false;
	}
}
