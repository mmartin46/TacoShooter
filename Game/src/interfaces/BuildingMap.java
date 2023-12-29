package interfaces;

import javafx.scene.image.Image;

public interface BuildingMap {
	public final int GRASS_TILE = 17;
	public final int NUM_ENEMY_SPRITES = 16;

	public void initializeMap(int columns, int rows);
	public void loadMapFromCSVFile();
	public void setupMap();
	public Image getSpriteSheetImage(String tileSheetPath);
}
