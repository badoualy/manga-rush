package com.mangarush.ui.actors;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mangarush.ui.Game;
import com.mangarush.utils.MRVars;

/**
 * Create a group with a content and two arrows to select one among all atlas
 * regions
 */
public class CharactersSelector extends Group {
	private Image[] contents;
	private int selectionIndex;
	private TextButton leftButton, rightButton;

	public CharactersSelector(TextureAtlas atlas) {
		// Load carrousel elements
		contents = new Image[atlas.getRegions().size];
		for (int i = 0; i < contents.length; i++) {
			contents[i] = new Image(atlas.getRegions().get(i));
			addActor(contents[i]);
			if (i != 0)
				contents[i].setColor(contents[i].getColor().sub(0f, 0f, 0f, 1f));
		}

		// Initial selection
		selectionIndex = 0;

		// Buttons
		leftButton = new TextButton(" < ", new TextButtonStyle(null, null, null, Game.GDXVars().getFont(
				MRVars.defaultFont)));
		rightButton = new TextButton(" > ", new TextButtonStyle(null, null, null, Game.GDXVars().getFont(
				MRVars.defaultFont)));

		// Add buttons
		addActor(leftButton);
		addActor(rightButton);

		// Click listeners
		leftButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Move out current image : from center to right
				ParallelAction moveOutAction = new ParallelAction();
				moveOutAction.addAction(Actions.moveTo(getWidth(), 0, 0.25f));
				moveOutAction.addAction(Actions.fadeOut(0.25f));
				contents[selectionIndex].addAction(moveOutAction);

				selectionIndex--;
				if (selectionIndex < 0)
					selectionIndex = contents.length - 1;

				// Move in new selection : from left to center
				ParallelAction moveInAction = new ParallelAction();
				moveInAction.addAction(Actions.moveTo(0, 0)); // Move to left side before starting
				moveInAction.addAction(Actions.moveTo(getWidth() / 2f - contents[selectionIndex].getWidth() / 2f, 0,
						0.25f));
				moveInAction.addAction(Actions.fadeIn(0.25f));
				contents[selectionIndex].addAction(moveInAction);
			}
		});

		rightButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Move out current image : from center to left
				ParallelAction moveOutAction = new ParallelAction();
				moveOutAction.addAction(Actions.moveTo(0, 0, 0.25f));
				moveOutAction.addAction(Actions.fadeOut(0.25f));
				contents[selectionIndex].addAction(moveOutAction);

				// Update current selection index
				selectionIndex = (selectionIndex + 1) % contents.length;

				// Move in new selection : from right to center
				ParallelAction moveInAction = new ParallelAction();
				moveInAction.addAction(Actions.moveTo(getWidth(), 0)); // Move to right side before starting
				moveInAction.addAction(Actions.moveTo(getWidth() / 2f - contents[selectionIndex].getWidth() / 2f, 0,
						0.25f));
				moveInAction.addAction(Actions.fadeIn(0.25f));
				contents[selectionIndex].addAction(moveInAction);
			}
		});

		setSize(200, 20);
	}

	public int getSelectionIndex() {
		return selectionIndex;
	}

	@Override
	protected void sizeChanged() {
		// Set images' position
		for (Image image : contents)
			image.setPosition(getX() + getWidth() / 2f - image.getWidth() / 2f, getY());

		// Change buttons' position
		leftButton.setPosition(0, 0);
		rightButton.setPosition(getWidth() - leftButton.getWidth(), 0);
	}
}
