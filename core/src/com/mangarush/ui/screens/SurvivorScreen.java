package com.mangarush.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.mangarush.ui.handlers.MRGestureListener;
import com.mangarush.ui.stages.SurvivorStage;

/** Screen for a SurvivorStage mode */
public class SurvivorScreen extends FixedFpsScreen {
	private final SurvivorStage stage;

	public SurvivorScreen(final int character) {
		super(new SurvivorStage(character));
		stage = (SurvivorStage) super.stage;
	}

	@Override
	public void show() {
		InputMultiplexer im = new InputMultiplexer();
		im.addProcessor(stage);
		im.addProcessor(new GestureDetector(new MRGestureListener(stage.getPlayer())));
		Gdx.input.setInputProcessor(im);
	}

	@Override
	public void hide() {
		super.hide();
		dispose();
	}
}
