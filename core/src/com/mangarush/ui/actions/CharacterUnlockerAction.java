package com.mangarush.ui.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.mangarush.characters.unlockers.Unlocker;
import com.mangarush.constants.Strings;
import com.mangarush.ui.Game;
import com.mangarush.ui.stages.SurvivorStage;
import com.mangarush.utils.Message;
import com.mangarush.utils.SaveData;

/**
 * Action that check if player has reach score needed to unlock a character and
 * show a message when he does
 */
public class CharacterUnlockerAction extends Action {
	private final SurvivorStage stage;
	private final Unlocker unlocker;

	private final SaveData save;

	/**
	 * @param stage
	 *            The stage
	 * @param unlocker
	 *            Use the IUnlocker.hasUnlock() to check if character has been
	 *            unlocked
	 */
	public CharacterUnlockerAction(final SurvivorStage stage, final Unlocker unlocker) {
		this.stage = stage;
		this.unlocker = unlocker;

		save = Game.Save();
	}

	@Override
	public boolean act(float delta) {
		if (unlocker.hasUnlock(stage.getPlayer())) {
			save.characters[unlocker.getId()] = true;
			stage.getHud().showMessage(new Message(Strings.UNLOCKED_MESSAGE + unlocker.getName(), 1.5f));
			return true;
		}

		return false;
	}
}
