package com.mangarush.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.mangarush.ui.handlers.MRGestureListener;
import com.mangarush.ui.stages.SurvivorStage;

/** Screen for a SurvivorStage mode */
public class SurvivorScreen extends FixedFpsScreen {
	private SurvivorStage stage;

	public SurvivorScreen() {
		super(new SurvivorStage());
		stage = (SurvivorStage) super.stage;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(new GestureDetector(new MRGestureListener(stage.getPlayer())));
	}

	@Override
	public void hide() {
		dispose();
	}
}
