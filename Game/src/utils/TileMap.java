package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import interfaces.BuildingMap;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import movables.Enemy;
import movables.Player;
import objects.Tile;
import states.TileType;



public class TileMap implements BuildingMap {
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
		
		initializeMap(0, 0);
		
		loadMapFromCSVFile();
		setupMap();
	}
	
	@Override
	public final void initializeMap(int columns, int rows) {
		tileMap = new Tile[rows][columns];
		indexTileMap = new int[rows][columns];
	}
	
	// Checks if the spritesheet is null and returns it if not.
	@Override
	public final Image getSpriteSheetImage(String tileSheetPath) {
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
	@Override
	public final void loadMapFromCSVFile() {
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
				indexTileMap[i][j] = indexValues.get(i * numColumns + j);
			}
		}
		
		
		// Set and load the tiles
		setupMap();
	}
	
	
	// Loads all the tiles
	@Override
	public final void setupMap() {
		int x, y;
		
		try {
			for (x = 0; x < indexTileMap.length; ++x) {
				for (y = 0; y < indexTileMap[x].length; ++y) {
					int tileIndex = filterTileIndex(indexTileMap[x][y]);
					
										
					// Locate the sprite within the map.
					int columnWithinSheet = tileIndex % (int)(tileSheet.getWidth() / TILE_SIZE);
					int rowWithinSheet = tileIndex / (int)(tileSheet.getWidth() / TILE_SIZE);
					
					// Turn it into a sprite of its own.
					ImageView tileImageView = getTileImageView(rowWithinSheet, columnWithinSheet);
										
					// Create the Tile instance.
					TileType tileType = getTileType(tileIndex);
					Tile tile = new Tile(tileIndex, y * TILE_SIZE, x * TILE_SIZE, tileType, tileImageView);
					tileMap[x][y] = tile;
				}
			}
		} catch (Exception e) {
			
			System.out.println("Rows: " + indexTileMap.length);
			System.out.println("Columns: " + indexTileMap[0].length);
		}
	}
	
	// If a tile hasn't been loaded properly (due to the Tile editor) load a grass tile.
	private int filterTileIndex(int tileIndex) {
		if (Math.abs(tileIndex) > 500) {
			return GRASS_TILE;
		}
		return tileIndex;
	}
	
	public void draw(Group group, Player player) {

		// Renders some tiles at a time.
		int cameraX = (int) (player.getY() - (doubledScreenWidth()) / 4);
		int cameraY = (int) (player.getX() - (doubledScreenHeight()) / 4) - 100;
	
		
		
		lazyDraw(group, (int) cameraX, cameraY, doubledScreenWidth(), 
												doubledScreenHeight());
	}
	
	private int doubledScreenWidth() {
		return GameConfigurations.SCREEN_WIDTH * 2;
	}
	
	private int doubledScreenHeight() {
		return GameConfigurations.SCREEN_HEIGHT * 2;
	}

	    
	public void lazyDraw(Group group, int cameraX, int cameraY, int screenWidth, int screenHeight) {
		int camStartX = Math.max(0,  cameraX / TILE_SIZE);
		int camStartY = Math.max(0, cameraY / TILE_SIZE);
		
		int camEndX = Math.min(tileMapDimensions.getColumns(), (cameraX + screenWidth) / TILE_SIZE + 1);
		int camEndY = Math.min(tileMapDimensions.getRows(), (cameraY + screenHeight) / TILE_SIZE + 1);
		
		
		for (int x = camStartX; x < camEndX; ++x) {
			for (int y = camStartY; y < camEndY; ++y) {
				Tile currentTile = tileMap[x][y];
				currentTile.draw(group);
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
	
	public Tile[][] getTileMap() {
		return tileMap;
	}
	
	
}