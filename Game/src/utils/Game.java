package utils;


import java.util.ArrayList;

import input.InputManager;
import javafx.scene.Group;
import javafx.scene.Scene;
import movables.Player;

public class Game {
	
	private final int DEFAULT_PLAYER_X = 500;
	private final int DEFAULT_PLAYER_Y = 500;
	private final int NUM_PLAYER_SPRITES = 31;
	private final int GAME_SCALE = 2;
	
	
	private Scene scene;
	private InputManager inputManager;
	private Player player;
	private ArrayList<TileMap> layers;
	
	

	
	public Game(Scene scene) {
		this.scene = scene;
		
		layers = new ArrayList<>();
		
		initialize();
	}
	
	private void initialize() {		
		initializeTileMap();
		initializePlayer();
		initializeInputManager();
	}
	
	private void initializeTileMap() {
		
		String csvFilePath = "mapfiles/mapfile.csv";
		String csvFilePath2 = "mapfiles/mapfile_l2.csv";
		String tileSheetPath = "mapfiles/maptilesheet.png";

		layers.add(new TileMap(csvFilePath, tileSheetPath));
		layers.add(new TileMap(csvFilePath2, tileSheetPath));
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
		player.update();
	}
	
	public void draw(Group group) {
		clearGameGroup(group);
		centerCoordinatesWithPlayer(group);
		
		for (TileMap layer : layers) {
			layer.draw(group);
		}
		player.draw(group);
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
