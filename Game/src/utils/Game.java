package utils;


import java.util.ArrayList;

import collisions.Collisions;
import input.InputManager;
import interfaces.Entity;
import javafx.scene.Group;
import javafx.scene.Scene;
import movables.Enemy;
import movables.Player;
import objects.Tile;

public class Game {
	
	private final int MOVABLE_LAYER = 0;
	private final int NONMOVABLE_LAYER = 1;
	private final int COIN_LAYER = 2;
	private final int ENEMY_LAYER = 3;
	
	private final int DEFAULT_PLAYER_X = 800;
	private final int DEFAULT_PLAYER_Y = 500;
	private final int NUM_PLAYER_SPRITES = 31;
	private final int NUM_ENEMY_SPRITES = 16;

	private final int GAME_SCALE = 2;
	
	
	private Scene scene;
	private InputManager inputManager;
	private Player player;
	private ArrayList<TileMap> layers;
	private EnemyMap enemyLayer;
	
	private ArrayList<String> filePaths;
	
	private Enemy enemy;
	
	private boolean allowGameRun;

	
	public Game(Scene scene) {
		this.scene = scene;
		
		layers = new ArrayList<>();
		
		initialize();
	}
	
	/**
	 * Allows the game to be ran.
	 * @param allowGameRun
	 */
	public void setAllowGameRun(boolean allowGameRun) {
		this.allowGameRun = allowGameRun;
	}
	
	public boolean getAllowGameRun() {
		return allowGameRun;
	}
	
	
	// Initializes everything within the game.
	private void initialize() {	
		setAllowGameRun(true);
		initializeLayers();
		initializePlayer();
		initializeInputManager();
	}
	
	/*
	 *  Creates maps based on coordinates of the
	 *  map based on CSV files.
	 */
	private void initializeLayers() {
		layers.clear();
		initializeFilePaths();

		String tileSheetPath = "mapfiles/maptilesheet.png";

		layers.add(new TileMap(filePaths.get(MOVABLE_LAYER), tileSheetPath));
		layers.add(new TileMap(filePaths.get(NONMOVABLE_LAYER), tileSheetPath));
		layers.add(new TileMap(filePaths.get(COIN_LAYER), tileSheetPath));
		enemyLayer = new EnemyMap(filePaths.get(ENEMY_LAYER), tileSheetPath);
	}
	
	private void initializeFilePaths() {
		filePaths = new ArrayList<String>();
		addToFilePaths("mapfiles/world_1_1_walkable.csv");
		addToFilePaths("mapfiles/world_1_1_nonwalkable.csv");
		addToFilePaths("mapfiles/world_1_1_tacos.csv");
		addToFilePaths("mapfiles/world_1_1_enemies.csv");
	}
	
	// Add to the file paths for the maps
	// if there are not too many maps.
	private void addToFilePaths(String filePath) {
		if (filePaths.size() <= 4) {
			filePaths.add(filePath);
		} else {
			System.err.println("addToFilePaths(): filePath list is full");
		}
	}
	

	private void initializePlayer() {
		player = new Player(DEFAULT_PLAYER_X,
							DEFAULT_PLAYER_Y, NUM_PLAYER_SPRITES);
	}
	
	// Initializes the input.
	private void initializeInputManager() {
		inputManager = InputManager.getSingleton();
		inputManager.initialize(scene);
	}

	// Clear to avoid stacking sprites.
	private void clearGameGroup(Group gameGroup) {
		gameGroup.getChildren().clear();
	}
	
	
	// Updates the game
	public void update() {
		if (getAllowGameRun()) {
			enemyLayer.update(player, layers.get(NONMOVABLE_LAYER).getTileMap());
			player.update();
			
			checkForTileCollisions();
			checkForPlayerDeath();
		}
	}
	

	
	public Player getPlayer() {
		return player;
	}
	
	public Scene getScene() {
		return scene;
	}
	
	// Draws the game to a specific group.
	public void draw(Group group) {
		clearGameGroup(group);
		centerCoordinatesWithPlayer(group);
		
		for (TileMap layer : layers) {
			layer.draw(group, player);
		}
		enemyLayer.draw(group, player);
		player.draw(group);
	}
	
	private void checkForTileCollisions() {
		int x, y;
		Tile[][] tileMap = layers.get(NONMOVABLE_LAYER).getTileMap();
		Tile[][] coinMap = layers.get(COIN_LAYER).getTileMap();
		Tile currentTile, currentCoin;
		
		for (x = 0; x < tileMap.length; ++x) {
			for (y = 0; y < tileMap[0].length; ++y) {
				currentTile = tileMap[x][y];
				currentCoin = coinMap[x][y];
				
				/*
				 * If the player can move through
				 * the tile, don't block the player's
				 * movement.
				 */
				if (Collisions.isMovethruTile(tileMap, x, y)) {
					Collisions.playerBlockCollision(player, currentTile, Collisions.COLLIDE_WITH_ENTITY);
				}
				
				// If the player collided with a coin.
				if (currentCoin != null) {
					if (currentCoin.getIndex() == TileMap.COIN_VALUE && Collisions.playerBlockCollision(player, currentCoin, Collisions.PASS_THORUGH_ENTITY)) {
						currentCoin.makeTileInvisible();
						player.increaseCoinsCollected(GameConfigurations.DEFAULT_COIN_ADDITION);
					}
				}
			}
		}
	}
	
	
	// Returns the centered coordinates.
	private Pair<Double, Double> getPlayerCoordinates() {
		Double centeredX = ((scene.getWidth() / 2) - player.getX());
		Double centeredY = ((scene.getHeight() / 2) - player.getY());
		
		Pair<Double, Double> coordinates;
		coordinates = new Pair<Double, Double>(centeredX, centeredY);
		return coordinates;
	}
	
	// Centers the position of the scene based on the player.
	private void centerCoordinatesWithPlayer(Group group) {
		Double offsetX = getPlayerCoordinates().getFirst();
		Double offsetY = getPlayerCoordinates().getSecond();
		
		group.setTranslateX(offsetX);
		group.setTranslateY(offsetY);
	}
	
	private void checkForPlayerDeath() {
		if (player.getHealth() <= 0.0) {
			player.setDidPlayerDie(true);
			handlePlayerDeath();
		}
	}
	
	private void handlePlayerDeath() {
		initializeLayers();
		player.resetAll(DEFAULT_PLAYER_X, DEFAULT_PLAYER_Y);
	}

	
	
}
