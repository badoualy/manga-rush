package com.mangarush.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mangarush.ui.Game;

public class DesktopLauncher {
	public static void main(String[] arg) {
		ApplicationListener game = new Game();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Manga rush";
		config.width = Game.V_WIDTH;
		config.height = Game.V_HEIGHT;
		config.resizable = false;

		new LwjglApplication(game, config);
	}
}
