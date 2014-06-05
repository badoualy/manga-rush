package com.mangarush.ui.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.mangarush.ui.Game;
import com.mangarush.ui.stages.SurvivorStage;
import com.mangarush.utils.SaveData;

/**
 * Action that check if player has beaten his highscore, and notify the stage
 * when he does
 */
public class HighScoreAction extends Action {
	private final SurvivorStage stage;
	private final SaveData save;

	public HighScoreAction(SurvivorStage stage) {
		this.stage = stage;
		save = Game.Save();
	}

	@Override
	public boolean act(float delta) {
		if (stage.getPlayer().getScore() > save.highScore) {
			stage.highScoreBeaten();
			return true;
		}
		return false;
	}
}
