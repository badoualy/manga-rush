package com.mangarush.ui.utils;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

/** Bunch of constants and globals B2D-related */
public abstract class B2DVars {
	/** PPM px = 1 meter **/
	public static final float PPM = 50f;

	/** Gravity vector */
	public static final Vector2 GRAVITY = new Vector2(0, -9.81f);

	/** Masks for collision handling (bitmask) */
	public static final int GROUND_MASK = 2;
	public static final int PLAYER_MASK = 4;

	/** User data to identify fixtures */
	public static final String USERD_FOOT_SENSOR = "foot_sensor";

	/** Player max velocity (x-axis) : run force */
	public static final float PLAYER_MAX_SPEED = 225f / PPM;
	/** Player jump force (y-axis velocity) */
	public static final float JUMP_FORCE = 200f / PPM;
	/** Player jump timeout in seconds (time before a new jump) */
	public static final float JUMP_TIMEOUT = 0f;

	/** Floor body */
	public static Body floorBody = null;
	/** List containing fixtures */
	public static List<Fixture> floorFixtures = new ArrayList<Fixture>(60);
}
