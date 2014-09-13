package com.mangarush.characters.unlockers;

/** Factory to get instance of the proper IUnlocker */
public abstract class UnlockerFactory {
	/** @return Return the Unlocker class associated to the character id */
	public static Unlocker getInstance(int id) {
		switch (id) {
			case 3:
				return new LuffyUnlocker();
			case 4:
				return new YohUnlocker();
			case 5:
				return new UekiUnlocker();
			default:
				break;
		}

		return null;
	}

	/**
	 * @param id
	 * @return
	 *         The unlocking conditions description
	 */
	public static String getDescription(int id) {
		switch (id) {
			case 3:
				return LuffyUnlocker.getDescription();
			case 4:
				return YohUnlocker.getDescription();
			case 5:
				return UekiUnlocker.getDescription();
			default:
				break;
		}

		return null;
	}
}