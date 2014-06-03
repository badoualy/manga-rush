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
		config.disableAudio = true;
		config.hideStatusBar = true;
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useWakelock = true;
		initialize(new Game(), config);
	}
}
