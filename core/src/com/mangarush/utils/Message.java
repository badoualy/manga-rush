package com.mangarush.utils;

import com.mangarush.constants.Strings;

/**
 * Store a message (string), how long should the message be printed and the
 * current timestate
 */
public class Message {
	/** Some constants values */
	public static final float HIGHSCORE_DURATION = 2f;
	public static final float INFINITE_DURATION = -1f;

	public final String message;
	public final float duration;
	public float state;

	public Message(final String message, final float duration) {
		this.message = message;
		this.duration = duration;
		state = 0f;
	}

	/** Shortcut new Message(Strings.HIGHSCORE_MESSAGE, HIGHSCORE_DURATION) */
	public static Message highScoreMessage() {
		return new Message(Strings.HIGHSCORE_MESSAGE, HIGHSCORE_DURATION);
	}

	/** Shortcut new Message(Strings.GAMEOVER_MESSAGE, INFINITE_DURATION) */
	public static Message gameOverMessage() {
		return new Message(Strings.GAMEOVER_MESSAGE, INFINITE_DURATION);
	}
}
