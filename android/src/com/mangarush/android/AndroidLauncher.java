package com.mangarush.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mangarush.ui.Game;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.hideStatusBar = true;
		config.useImmersiveMode = true;
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useWakelock = true;
		config.depth = 8;
		config.stencil = 8;
		initialize(new Game(), config);
	}
}
