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
	
	private Enemy enemy;

	
	public Game(Scene scene) {
		this.scene = scene;
		
		layers = new ArrayList<>();
		
		initialize();
	}
	
	
	private void initialize() {	
				
		initializeLayers();
		initializePlayer();
		initializeInputManager();
	}
	
	private void initializeLayers() {
		
		String csvFilePath = "mapfiles/world_1_1_walkable.csv";
		String csvFilePath2 = "mapfiles/world_1_1_nonwalkable.csv";
		String csvFilePath3 = "mapfiles/world_1_1_enemies.csv";
		// TODO: Create a COIN MAP.
		String csvFilePath4 = "mapfiles/world_1_1_tacos.csv";

		String tileSheetPath = "mapfiles/maptilesheet.png";

		layers.add(new TileMap(csvFilePath, tileSheetPath));
		layers.add(new TileMap(csvFilePath2, tileSheetPath));
		layers.add(new TileMap(csvFilePath4, tileSheetPath));
		enemyLayer = new EnemyMap(csvFilePath3, tileSheetPath);
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
	
	
	
	public void update() {
		enemyLayer.update(player, layers.get(NONMOVABLE_LAYER).getTileMap());
		player.update();
		
		checkForTileCollisions();
	}
	

	
	public Player getPlayer() {
		return player;
	}
	
	public Scene getScene() {
		return scene;
	}
	
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
		for (x = 0; x < tileMap.length; ++x) {
			for (y = 0; y < tileMap[0].length; ++y) {
				Tile currentTile = tileMap[x][y];
				Tile currentCoin = coinMap[x][y];
				if (Collisions.isMovethruTile(tileMap, x, y)) {
					Collisions.playerBlockCollision(player, currentTile, Collisions.COLLIDE_WITH_ENTITY);
				}
				
				if (Collisions.playerBlockCollision(player, currentCoin, Collisions.PASS_THORUGH_ENTITY)) {
					System.out.println("WOAH");
				}
			}
		}
	}
	
	

	
	private Pair<Double, Double> getPlayerCoordinates() {
		Double centeredX = ((scene.getWidth() / 2) - player.getX());
		Double centeredY = ((scene.getHeight() / 2) - player.getY());
		
		Pair<Double, Double> coordinates;
		coordinates = new Pair<Double, Double>(centeredX, centeredY);
		return coordinates;
	}
	
	private void centerCoordinatesWithPlayer(Group group) {
		Double offsetX = getPlayerCoordinates().getFirst();
		Double offsetY = getPlayerCoordinates().getSecond();
		
		group.setTranslateX(offsetX);
		group.setTranslateY(offsetY);
	}

	
	
}
