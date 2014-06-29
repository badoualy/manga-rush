package com.mangarush.ui.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mangarush.characters.unlockers.UnlockerFactory;
import com.mangarush.constants.Paths;
import com.mangarush.ui.Game;

/**
 * Create a group with a content and two arrows to select one textureregion
 * among all atlas regions
 */
public class AtlasSelector extends Group {
	// Selections
	private final Image[] contents;
	private boolean[] selectable;
	private final Color[] colors; // Real colors
	private int selectedIndex;

	// Font
	private final BitmapFont font;

	// Buttons
	private final TextButton leftButton, rightButton;

	public AtlasSelector(final TextureAtlas atlas, final boolean[] selectable) {
		// Load carrousel elements
		contents = new Image[atlas.getRegions().size];
		colors = new Color[contents.length];
		this.selectable = selectable; // contents[i] can be selected if selectable[i] == true
		for (int i = 0; i < contents.length; i++) {
			contents[i] = new Image(atlas.getRegions().get(i));
			colors[i] = contents[i].getColor(); // Save real colors
			addActor(contents[i]);

			// Make them transparent except first one
			if (i != 0)
				contents[i].setColor(colors[i].sub(0f, 0f, 0f, 1f));
		}

		// Initial selection
		selectedIndex = 0;

		// Buttons
		font = Game.GDXVars().getFont(Paths.defaultFont);
		font.setScale(1f);
		leftButton = new TextButton(" < ", new TextButtonStyle(null, null, null, font));
		rightButton = new TextButton(" > ", new TextButtonStyle(null, null, null, font));

		// Add buttons
		addActor(leftButton);
		addActor(rightButton);

		initListeners();

		setSize(200, 20);
	}

	private void initListeners() {
		// Click listeners
		leftButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Move out current image : from center to right
				ParallelAction moveOutAction = new ParallelAction();
				moveOutAction.addAction(Actions.moveTo(getWidth(), 0, 0.25f));
				moveOutAction.addAction(Actions.fadeOut(0.25f));
				contents[selectedIndex].addAction(moveOutAction);

				selectedIndex--;
				if (selectedIndex < 0)
					selectedIndex = contents.length - 1;
				updateContentsColor();

				// Move in new selection : from left to center
				ParallelAction moveInAction = new ParallelAction();
				moveInAction.addAction(Actions.moveTo(0, 0)); // Move to left side before starting
				moveInAction.addAction(Actions.moveTo(getWidth() / 2f - contents[selectedIndex].getWidth() / 2f, 0,
						0.25f));
				moveInAction.addAction(Actions.fadeIn(0.25f));
				contents[selectedIndex].addAction(moveInAction);
			}
		});

		rightButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Move out current image : from center to left
				ParallelAction moveOutAction = new ParallelAction();
				moveOutAction.addAction(Actions.moveTo(0, 0, 0.25f));
				moveOutAction.addAction(Actions.fadeOut(0.25f));
				contents[selectedIndex].addAction(moveOutAction);

				// Update current selection index
				selectedIndex = (selectedIndex + 1) % contents.length;
				updateContentsColor();

				// Move in new selection : from right to center
				ParallelAction moveInAction = new ParallelAction();
				moveInAction.addAction(Actions.moveTo(getWidth(), 0)); // Move to right side before starting
				moveInAction.addAction(Actions.moveTo(getWidth() / 2f - contents[selectedIndex].getWidth() / 2f, 0,
						0.25f));
				moveInAction.addAction(Actions.fadeIn(0.25f));
				contents[selectedIndex].addAction(moveInAction);
			}
		});

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		leftButton.getLabel().setFontScale(1f);
		rightButton.getLabel().setFontScale(1f);
		super.draw(batch, parentAlpha);

		if (!selectable[selectedIndex]) {
			font.setScale(0.5f);
			String str = UnlockerFactory.getDescription(selectedIndex);
			TextBounds bounds = font.getBounds(str);
			font.draw(batch, str, getCenterX() - bounds.width / 2f, getY());
		}
	}

	@Override
	protected void sizeChanged() {
		// Set images' position
		for (Image image : contents)
			image.setPosition(getCenterX() - image.getWidth() / 2f, getY());

		// Change buttons' position
		leftButton.setPosition(0, 0);
		rightButton.setPosition(getWidth() - leftButton.getWidth(), 0);
	}

	/**
	 * Check if Image should be painted in black or in it's real color and
	 * update
	 */
	private void updateContentsColor() {
		if (!selectable[selectedIndex])
			contents[selectedIndex].setColor(Color.BLACK);
		else
			contents[selectedIndex].setColor(colors[selectedIndex]);
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectable(boolean[] selectable) {
		this.selectable = selectable;
	}
}
