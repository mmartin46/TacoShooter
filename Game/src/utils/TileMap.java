package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import objects.Tile;
import states.TileType;



public class TileMap {
	private final int TILE_SIZE = 20;
	
	// Represents all the tiles in the map
	public Tile[][] tileMap;
	// Represents the integer values of the tiles of
	// the map.
	public int[][] indexTileMap;
	
	
	String csvFilePath;
	MapDimensions tileMapDimensions;
	Image tileSheet;
	InputStream inputStream;
	
	public TileMap(String csvFilePath, String tileSheetPath) {
		tileMapDimensions = new MapDimensions();
		this.csvFilePath = csvFilePath;
		this.tileSheet = getSpriteSheetImage(tileSheetPath);
		
		initializeTileMap(0, 0);
		
		loadTileMapFromCSVFile();
		setupTileMap();
	}
	
	private void initializeTileMap(int columns, int rows) {
		tileMap = new Tile[rows][columns];
		indexTileMap = new int[rows][columns];
	}
	
	// Checks if the spritesheet is null and returns it if not.
	private Image getSpriteSheetImage(String tileSheetPath) {
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
	
	
	// Loads the CSV document
	private void loadTileMapFromCSVFile() {
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
		initializeTileMap(numRows, numColumns);
		
		for (int i = 0; i < numRows; ++i) {
			for (int j = 0; j < numColumns; ++j) {
				indexTileMap[i][j] = indexValues.get(i * numColumns + j);
			}
		}
		
		
		// Set and load the tiles
		setupTileMap();
	}
	
	
	// Loads all the tiles
	private void setupTileMap() {
		int x, y;
		
		try {
			for (x = 0; x < indexTileMap.length; ++x) {
				for (y = 0; y < indexTileMap[x].length; ++y) {
					int tileIndex = indexTileMap[x][y];
										
					// Locate the sprite within the map.
					int columnWithinSheet = tileIndex % (int)(tileSheet.getWidth() / TILE_SIZE);
					int rowWithinSheet = tileIndex / (int)(tileSheet.getWidth() / TILE_SIZE);
					
					// Turn it into a sprite of its own.
					ImageView tileImageView = getTileImageView(rowWithinSheet, columnWithinSheet);
					
					// Create the Tile instance.
					TileType tileType = getTileType(tileIndex);
					Tile tile = new Tile(tileIndex, x * TILE_SIZE, y * TILE_SIZE, tileType, tileImageView);
					tileMap[x][y] = tile;
				}
			}
		} catch (Exception e) {
			
			System.out.println("Rows: " + indexTileMap.length);
			System.out.println("Columns: " + indexTileMap[0].length);
		}
	}
	
	public void draw(Group group) {
		int x, y;
		for (x = 0; x < indexTileMap.length; ++x) {
			for (y = 0; y < indexTileMap[x].length; ++y) {
				// Find the tile within the tile map.
				Tile currentTile = tileMap[x][y];
				

				
				
				// Insert the image within the group of the window.
				ImageView currentTileSprite = currentTile.getImageView();
				currentTileSprite.setLayoutX(currentTile.getX());
				currentTileSprite.setLayoutY(currentTile.getY());
				group.getChildren().add(currentTileSprite);
			}
		}
	}
	
	private TileType getTileType(int tileIndex) {
		if (tileIndex >= 0 && tileIndex <= 8) {
			return TileType.WALK_THROUGH;
		}
		return TileType.NON_WALKTHROUGH;
	}
	
	private ImageView getTileImageView(int row, int column) {
		ImageView imageView = new ImageView(tileSheet);
		imageView.setViewport(new Rectangle2D(column * TILE_SIZE, row * TILE_SIZE, 
								TILE_SIZE, TILE_SIZE));
		return imageView;
	}
	
}