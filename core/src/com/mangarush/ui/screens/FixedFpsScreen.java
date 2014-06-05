package com.mangarush.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mangarush.ui.Game;

/** Generic screen with a stage that fixes FPS */
public class FixedFpsScreen extends ScreenAdapter {
	protected final Stage stage;

	private float accumulator;

	public FixedFpsScreen(Stage stage) {
		this.stage = stage;
		accumulator = Game.FIXED_FPS;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Max frame time
		if (delta > 0.25f)
			delta = 0.25f;

		accumulator += delta;

		// We update simulation as much as needed
		while (accumulator >= Game.FIXED_FPS) {
			stage.act(); // Update

			accumulator -= Game.FIXED_FPS;
		}

		// We still draw once
		stage.draw(); // Draw
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
