package interfaces;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.Group;
import movables.Player;
import utils.GameConfigurations;

public abstract class BuildingMap {
	public final int GRASS_TILE = 17;
	public final int NUM_ENEMY_SPRITES = 16;

	public abstract void initializeMap(int columns, int rows);
	public abstract void loadMapFromCSVFile();
	public abstract void setupMap();
	

	public Image getSpriteSheetImage(String tileSheetPath) {
		InputStream spriteSheetInputStream = null;
		// Try to load the spriteSheet
		try {
			
			spriteSheetInputStream = getClass().getResourceAsStream(tileSheetPath);
			
			if (spriteSheetInputStream == null) {
				throw new IllegalArgumentException("spriteSheetInputStream(): Invalid path");
			}
		} catch (IllegalArgumentException e) {
			System.err.println("spriteSheetInputStream(): Error loading tile sheet.");
		}
		
		Image spriteSheetImage = new Image(spriteSheetInputStream);
		return spriteSheetImage;
	}
	
	public void draw(Group group, Player player) {

		// Renders some tiles at a time.
		int cameraX = (int) (player.getY() - (GameConfigurations.doubledScreenWidth()) / 4);
		int cameraY = (int) (player.getX() - (GameConfigurations.doubledScreenHeight()) / 4) - 100;
	
		
		
		lazyDraw(group, (int) cameraX, cameraY, GameConfigurations.doubledScreenWidth(), 
												GameConfigurations.doubledScreenHeight());
	}
	
	public abstract void lazyDraw(Group group, int cameraX, int cameraY, int screenWidth, int screenHeight);
}
