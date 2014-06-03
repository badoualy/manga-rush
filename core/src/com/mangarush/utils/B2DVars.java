package com.mangarush.utils;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

/** Bunch of constants B2D-related */
public abstract class B2DVars {
	/** PPM px = 1 meter **/
	public static final float PPM = 50f;

	/** Gravity */
	public static final Vector2 GRAVITY = new Vector2(0, -9.81f);

	/** Masks for collision handling (bitmask) */
	public static final int GROUND_MASK = 2;
	public static final int PLAYER_MASK = 4;

	/** User data to identify fixtures */
	public static final String USERD_FOOT_SENSOR = "foot_sensor";
	public static final String USERD_WALL_SENSOR = "wall_sensor";

	/** Player max speed : impulse to apply */
	public static final float PLAYER_MAX_SPEED = 225f / PPM;
	/** Player jump force : impulse to apply */
	public static final float JUMP_FORCE = 200f / PPM;
	/** Player jump timeout in seconds */
	public static final float JUMP_TIMEOUT = 0f;

	/** Floor body */
	public static Body floorBody = null;
	/** List containing fixtures to remove from world : TAKE A LOCK ON floorBody */
	public static final List<Fixture> fixToRemove = new ArrayList<Fixture>(15);
}
