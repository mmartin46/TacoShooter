package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import collisions.Collisions;
import interfaces.BuildingMap;
import javafx.scene.Group;
import javafx.scene.image.Image;
import movables.Enemy;
import movables.Player;
import objects.Tile;

public class EnemyMap extends BuildingMap {

	private static final double TILE_SIZE = 20;
	private final double NO_ENEMY_HEALTH = 0;
	public Enemy[][] enemyMap;
	private int[][] indexEnemyMap;
	
	private MapDimensions tileMapDimensions;
	private String csvFilePath;
	
	@Override
	public void initializeMap(int columns, int rows) {
		enemyMap = new Enemy[rows][columns];
		indexEnemyMap = new int[rows][columns];
	}
	
	/**
	 * 
	 * @param csvFilePath - represents the CSV File used to locate enemy positions.
	 * @param tileSheetPath - represents a PNG File for the tile sheet.
	 */
	public EnemyMap(String csvFilePath, String tileSheetPath) {
		tileMapDimensions = new MapDimensions();
		this.csvFilePath = csvFilePath;
		
		initializeMap(0, 0);
		
		loadMapFromCSVFile();
		setupMap();
	}

	/**
	 * Parses the CSV file into two
	 * arrays.
	 * Arrays:
	 * 2D Array of Enemies
	 * 2D Array of integer positions.
	 */
	@Override
	public void loadMapFromCSVFile() {
		int numRows = 0, numColumns = 0;
		ArrayList<Integer> indexValues = new ArrayList<Integer>();
		try (InputStream inputStream = getClass().getResourceAsStream(csvFilePath)) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			numRows = 0;
			numColumns = 0;
			
			while ((line = bufferedReader.readLine()) != null) {
				// Split the CSV line
				String[] values = line.split(",");
				if (numColumns == 0) {
					numColumns = values.length;
				}
				
				for (String value : values) {
					indexValues.add(Integer.parseInt(value));
				}
				
				++numRows;
			}
		} catch (IOException e) {
			System.err.println("loadCSVFile(): Invalid file path");
		}
		
		
		// Initialize the rows and columns
		tileMapDimensions.setRows(numRows);
		tileMapDimensions.setColumns(numColumns);
		initializeMap(numRows, numColumns);
		
		// System.out.println("indexTileMap dimensions: " + indexTileMap.length + "x" + indexTileMap[0].length);
		for (int i = 0; i < numRows; ++i) {
			for (int j = 0; j < numColumns; ++j) {
				indexEnemyMap[i][j] = indexValues.get(i * numColumns + j);
			}
		}
		
		
		// Set and load the tiles
		setupMap();
	}
	
	/**
	 Sets up the enemies
	 positions based on the locations
	 of the CSV File.
	 */
	@Override
	public void setupMap() {
		int x, y;
		
		try {
			for (x = 0; x < indexEnemyMap.length; ++x) {
				for (y = 0; y < indexEnemyMap[x].length; ++y) {
					int tileIndex = indexEnemyMap[x][y];
					
					// Create the Enemy instance.
					if (tileIndex != -1) {
						Enemy enemy = new Enemy((int) (x * TILE_SIZE), 
												(int) (y * TILE_SIZE), 
												NUM_ENEMY_SPRITES);
						enemyMap[x][y] = enemy;
					}
				}
			}
		} catch (Exception e) {
			
			System.out.println("Rows: " + indexEnemyMap.length);
			System.out.println("Columns: " + indexEnemyMap[0].length);
		}
	}
	
	
	/**
	 * Updates the positions of all the enemies
	 * based on the player's position and the given tiles.
	 * @param player
	 * @param tileMap
	 */
	public void update(Player player, Tile[][] tileMap) {
		int x, y;
		for (x = 0; x < enemyMap.length; ++x) {
			for (y = 0; y < enemyMap[0].length; ++y) {
				if (enemyMap[x][y] != null) {
					// Update every enemy in the map.
					enemyMap[x][y].update(player, tileMap);
					// Check for collisions with a bullet.
					enemyMap[x][y].collisionWithBullet(player.getBullets());
					
					// If the enemy needs to be removed
					// based on specific boolean checks. Remove the enemy.
					if (shouldRemoveEnemy(enemyMap[x][y])) {
						enemyMap[x][y] = null;
					}
				}
			}
		}
	}
	
	// Remove the enemy if the health is empty.
	private boolean shouldRemoveEnemy(Enemy enemy) {
		return enemy.getHealth() <= NO_ENEMY_HEALTH;
	}
	
	
	
	
	// Draws all the enemies within the map.
	@Override
	public void draw(Group group, Player player) {
		int x, y;
		for (x = 0; x < enemyMap.length; ++x) {
			for (y = 0; y < enemyMap[0].length; ++y) {
				if (enemyMap[x][y] != null) {
					enemyMap[x][y].draw(group);
				}
			}
		}
	}
	
	/*
	 * Draws the enemies based on the position
	 * of the camera coordinates given.
	 */
	@Override
	public void lazyDraw(Group group, int cameraX, int cameraY, int screenWidth, int screenHeight) {
		int camStartX = (int) Math.max(0,  cameraX / TILE_SIZE);
		int camStartY = (int) Math.max(0, cameraY / TILE_SIZE);
		
		int camEndX = (int) Math.min(tileMapDimensions.getColumns(), (cameraX + screenWidth) / TILE_SIZE + 1);
		int camEndY = (int) Math.min(tileMapDimensions.getRows(), (cameraY + screenHeight) / TILE_SIZE + 1);
		
		
		for (int x = camStartX; x < camEndX; ++x) {
			for (int y = camStartY; y < camEndY; ++y) {
				Enemy currentEnemy = enemyMap[x][y];
				// If the enemy is within the given
				// coordinates allow the enemy to be drawn.
				if (currentEnemy != null) {
					currentEnemy.draw(group);
				}
			}
		}
	}

}
