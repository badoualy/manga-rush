package com.mangarush.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.mangarush.ui.handlers.MRGestureListener;
import com.mangarush.ui.stages.LevelStage;

/** Screen for a LevelStage */
public class LevelScreen extends ScreenAdapter {
	private LevelStage stage;

	public LevelScreen() {
		stage = new LevelStage("res/maps/level_01.tmx");
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.81f, 0.96f, 0.97f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(); // Update
		stage.draw(); // Dessine
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(new GestureDetector(new MRGestureListener(stage.getPlayer())));
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}
}
