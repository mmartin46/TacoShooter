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
import sounds.SoundManager;

public class Game {
	
	static class Layers {
		protected static final int MOVABLE_LAYER = 0;
		protected static final int NONMOVABLE_LAYER = 1;
		protected static final int COIN_LAYER = 2;
		protected static final int ENEMY_LAYER = 3;
	}
	
	
	private final int DEFAULT_PLAYER_X = 800;
	private final int DEFAULT_PLAYER_Y = 500;
	private final int NUM_PLAYER_SPRITES = 31;
	
	private Scene scene;
	private InputManager inputManager;
	private Player player;
	private ArrayList<TileMap> layers;
	private EnemyMap enemyLayer;
	
	private SoundManager soundManager;
	
	private ArrayList<String> filePaths;
		
	private boolean allowGameRun;
	
	// Temporary
	private int numOfCoinsNeeded = 200;
	private int numEnemiesToBeat = 10;
	
	private int currentLevel = 1;
	private int currentWorld = 1;
	private final int NUM_LEVELS = 4;

	
	private int gameTimer = 0;
		
	public int getGameTimer() {
		return gameTimer;
	}
	
	public int getNumOfCoinsNeeded() {
		return numOfCoinsNeeded;
	}

	public void setNumOfCoinsNeeded(int numOfCoinsNeeded) {
		this.numOfCoinsNeeded = numOfCoinsNeeded;
	}

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
		intializeSoundManagers();
		playMusic();
	}
	
	private void intializeSoundManagers() {
		soundManager = new SoundManager();
	}
	
	/*
	 *  Creates maps based on coordinates of the
	 *  map based on CSV files.
	 */
	private void initializeLayers() {
		
		layers.clear();
		initializeFilePaths();

		String tileSheetPath = "mapfiles/maptilesheet" + currentLevel + ".png";

		layers.add(new TileMap(filePaths.get(Layers.MOVABLE_LAYER), tileSheetPath));
		layers.add(new TileMap(filePaths.get(Layers.NONMOVABLE_LAYER), tileSheetPath));
		layers.add(new TileMap(filePaths.get(Layers.COIN_LAYER), tileSheetPath));
		enemyLayer = new EnemyMap(filePaths.get(Layers.ENEMY_LAYER), tileSheetPath);
	}
	
	private void initializeFilePaths() {
		filePaths = new ArrayList<String>();
		addToFilePaths("mapfiles/world_" + currentWorld + "_" + currentLevel + "_walkable.csv");
		addToFilePaths("mapfiles/world_" + currentWorld + "_" + currentLevel + "_nonwalkable.csv");
		addToFilePaths("mapfiles/world_" + currentWorld + "_" + currentLevel + "_tacos.csv");
		addToFilePaths("mapfiles/world_" + currentWorld + "_" + currentLevel + "_enemies.csv");
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
		if (currentLevel > NUM_LEVELS) {
			System.exit(0);
		}		

		if (getAllowGameRun()) {
			enemyLayer.update(player, layers.get(Layers.NONMOVABLE_LAYER).getTileMap());
			player.update();
			
			checkForTileCollisions();
			checkForPlayerDeath();
			checkToChangeLevel();
			++gameTimer;
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
		Tile[][] tileMap = layers.get(Layers.NONMOVABLE_LAYER).getTileMap();
		Tile[][] coinMap = layers.get(Layers.COIN_LAYER).getTileMap();
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
						playCoinSound();
						currentCoin.makeTileInvisible();
						player.increaseCoinsCollected(GameConfigurations.DEFAULT_COIN_ADDITION);
					}
				}
			}
		}
	}
	
	private void playCoinSound() {
		player.soundManager.playSound("soundfiles/coin_collect.wav", 20.0);
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
		if (player.getHealth() <= 1.0) {
			player.setDidPlayerDie(true);
			handlePlayerDeath();
		}
	}
	
	private boolean enoughCoinsCollected() {
		if (player.getCoinsCollected() >= numOfCoinsNeeded) {
			return true;
		}
		return false;
	}
	
	private boolean enoughEnemiesBeat() {
		if (player.getNumOfEnemiesBeat() >= numEnemiesToBeat) {
			return true;
		}
		return false;
	}
	
	private void checkToChangeLevel() {

		
		if (enoughCoinsCollected() && enoughEnemiesBeat()) {
			currentLevel++;
			initializeLayers();
			playMusic();
			player.resetAll(DEFAULT_PLAYER_X, DEFAULT_PLAYER_X);
		}
	}
	
	private void playMusic() {
		if (soundManager.isSoundAllocated()) {
			soundManager.stopSound();
		}
		soundManager.playSound("soundfiles/track_1.wav", 20.0);
	}
	
	
	private void handlePlayerDeath() {
		initializeLayers();
		player.resetAll(DEFAULT_PLAYER_X, DEFAULT_PLAYER_Y);
	}

	public int getNumEnemiesToBeat() {
		return numEnemiesToBeat;
	}

	public void setNumEnemiesToBeat(int numEnemiesToBeat) {
		this.numEnemiesToBeat = numEnemiesToBeat;
	}

	
	
}
