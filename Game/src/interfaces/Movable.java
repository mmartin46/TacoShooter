package interfaces;

import sounds.SoundManager;

/**
 * Represents an object that
 * is allowed to move around.
 */
public abstract class Movable {
	
	// Sounds
	public SoundManager soundManager;
	
	public double dx, dy;
	public final double DEFAULT_HEALTH = 100.0;
	public abstract void setCurrentDirection(Direction currentDirection);
	public abstract Direction getCurrentDirection();
	
	public void moveLeft(double speed) {
		dx = -speed;
		setCurrentDirection(Direction.LEFT);
	}
	public void moveRight(double speed) {
		dx = speed;
		setCurrentDirection(Direction.RIGHT);
	}
	public void moveUp(double speed) {
		dy = -speed;
		setCurrentDirection(Direction.UP);
	}
	public void moveDown(double speed) {
		dy = speed;
		setCurrentDirection(Direction.DOWN);
	}
	
	public static class SoundFilePaths
	{
		public final static String shotFilePath = "soundfiles/shot_sound.wav";
		public final static String enemyHitFilePath = "soundfiles/enemy_hit.wav";
		public final static String coinCollectFilePath = "soundfiles/coin_collect.wav";
		
		public final static double DEFAULT_VOLUME = 4.0;
		public final static double LOW_VOLUME = 1.0;
	}
}
