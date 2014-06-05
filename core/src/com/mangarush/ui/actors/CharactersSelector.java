package com.mangarush.ui.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
	private TextureRegion[] contents;
	private TextureRegion selection;
	private int selectionIndex;
	private TextButton leftButton, rightButton;

	public CharactersSelector(TextureAtlas atlas) {
		setSize(200, 20);

		// Load carrousel elements
		contents = new TextureRegion[atlas.getRegions().size];
		for (int i = 0; i < contents.length; i++)
			contents[i] = atlas.getRegions().get(i);

		// Initial selection
		selectionIndex = 0;
		selection = contents[0];

		// Buttons
		leftButton = new TextButton(" < ", new TextButtonStyle(null, null, null, Game.GDXVars().getFont(
				MRVars.defaultFont)));
		rightButton = new TextButton(" > ", new TextButtonStyle(null, null, null, Game.GDXVars().getFont(
				MRVars.defaultFont)));

		// Add buttons
		addActor(leftButton);
		addActor(rightButton);

		// Change button's position
		leftButton.setPosition(0, 0);
		rightButton.setPosition(getWidth() - leftButton.getWidth(), 0);

		// Click listeners
		leftButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				selectionIndex--;
				if (selectionIndex < 0)
					selectionIndex = contents.length - 1;
				selection = contents[selectionIndex];
			}
		});

		rightButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				selectionIndex = (selectionIndex + 1) % contents.length;
				selection = contents[selectionIndex];
			}
		});
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(selection, getX() + getWidth() / 2f - selection.getRegionWidth() / 2f, getY());
	}

	public int getSelectionIndex() {
		return selectionIndex;
	}
}
