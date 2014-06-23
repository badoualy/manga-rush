package com.mangarush.ui.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.mangarush.constants.Strings;
import com.mangarush.ui.Game;
import com.mangarush.ui.stages.SurvivorStage;
import com.mangarush.utils.Message;
import com.mangarush.utils.SaveData;

public class CharacterUnlockAction extends Action {
	private final SurvivorStage stage;
	private final String character;
	private final int score;

	private final SaveData save;

	public CharacterUnlockAction(final SurvivorStage stage, final String character, final int score) {
		this.stage = stage;
		this.character = character;
		this.score = score;

		save = Game.Save();
	}

	@Override
	public boolean act(float delta) {
		if (stage.getPlayer().getScore() >= score) {
			save.characters.put(character, true);
			stage.getHud().showMessage(new Message(Strings.UNLOCKED_MESSAGE + character, 1.5f));
			return true;
		}
		return false;
	}
}
