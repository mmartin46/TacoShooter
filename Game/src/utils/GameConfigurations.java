package utils;

// Holds configurations for the game.
public class GameConfigurations {
	public static int SCREEN_WIDTH = 420;
	public static int SCREEN_HEIGHT = 240;
	public static int NUM_MILLIS_DELAY = 16;
	
	public static int DEFAULT_COIN_ADDITION = 1;
	
	public static int ADJUSTED_WIDTH = SCREEN_WIDTH;
	public static int ADJUSTED_HEIGHT = SCREEN_HEIGHT;
	
	public static int doubledScreenWidth() {
		return GameConfigurations.SCREEN_WIDTH * 2;
	}
	
	public static int doubledScreenHeight() {
		return GameConfigurations.SCREEN_HEIGHT * 2;
	}
}
