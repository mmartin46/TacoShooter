package movables;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import input.InputManager;
import interfaces.Direction;
import interfaces.Entity;
import interfaces.Movable;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;


public class Player extends Movable implements Entity {
	
	// Constants
	private final int INITIAL_SPRITE = 0;

	// Player Sprite
	private int numSprites;
	private int health = 100;
	private ImageView currentSprite;
	HashMap<Direction, List<Image>> playerImages;
	
	// Player Direction
	private Direction currentDirection;
	private Direction lastDirection;
	
	private final double PLAYER_SPEED = 2.0;
	
	public Player() {
		
	}
	
	public Player(double x, double y, int numSprites) {
		this.numSprites = numSprites;
		playerImages = new HashMap<>();
		setCurrentDirection(Direction.RIGHT);
		initializePlayer(x, y);
	}

	public ImageView getSprite() {
		return currentSprite;
	}
	
	private void initializePlayer(double x, double y) {
		initalizeAllSprites();
		
		currentSprite = new ImageView(playerImages.get(currentDirection).get(INITIAL_SPRITE));
		initalizePlayerCoordinates(x, y);
	}
	
	
	private void initalizePlayerCoordinates(double x, double y) {
		setX(x);
		setY(y);
	}
	
	// Initializes all the sprites.
	private void initalizeAllSprites() {
		int index = 1;
		InputStream inputStream;

		initializeDirections();
		
		
		for (index = 1; index <= numSprites; ++index) {
			inputStream = getClass().getResourceAsStream("playersprites/player/walking" + 
					Integer.toString(index) + 
					".png");
			
			Direction currentDirection = getIndexDirection(index);
			if (currentDirection != null) {
				playerImages.get(currentDirection).add(new Image(inputStream));
			}
		}
	}
	
	// Initializes directions for the map.
	private void initializeDirections() {
		for (Direction direction : Direction.values()) {
			playerImages.put(direction, new ArrayList<>());
		}
	}
	
	// Get the current direction based on the index.
	private Direction getIndexDirection(int index) {
		if (matchRightSprites(index)) {
			return Direction.RIGHT;
		} else if (matchDownSprites(index)) {
			return Direction.DOWN;
		} else if (matchUpSprites(index)) {
			return Direction.UP;
		} else if (matchLeftSprites(index)) {
			return Direction.LEFT;
		}
		return null;
	}
	
	
	private boolean matchRightSprites(int index) {
		return index >= 0 && index <= 3;
	}
	
	private boolean matchDownSprites(int index) {
		return index >= 4 && index <= 7;
	}
	
	private boolean matchUpSprites(int index) {
		return index >= 8 && index <= 11;
	}
	
	private boolean matchLeftSprites(int index) {
		return index >= 12 && index <= 15;
	}
	
	@Override
	public double getWidth() {
		return DEFAULT_WIDTH;
	}
	@Override
	public double getHeight() {
		return DEFAULT_HEIGHT;
	}
	
	// Handles horizontal and vertical input.
	private void handleInput() {
		handleHorizontalInput();
		handleVerticalInput();
	}
	
	private void handleHorizontalInput() {
		if (isKeyPressed(KeyCode.LEFT)) {
			moveLeft(PLAYER_SPEED);
		} else if (isKeyPressed(KeyCode.RIGHT)) {
			moveRight(PLAYER_SPEED);
		} else {
			dx = 0.0;
		}
	}
	
	private void handleVerticalInput() {
		if (isKeyPressed(KeyCode.UP)) {
			moveUp(PLAYER_SPEED);
		} else if (isKeyPressed(KeyCode.DOWN)) {
			moveDown(PLAYER_SPEED);
		} else {
			dy = 0.0;
		}
	}
	
	public void update() {
		// Checks for keys for player movement.
		handleInput();
		
		// Updates the velocity based on the input.
		currentSprite.setTranslateX(currentSprite.getTranslateX() + dx);
		currentSprite.setTranslateY(currentSprite.getTranslateY() + dy);
		
		
		// Changes the current sprite considering
		// the direction.		
		if (isNotMoving()) {
			// If the sprite isn't moving, keep
			// at the first index.
			setCurrentDirection(lastDirection);
			updateToFirstIndex();
		} else {
			lastDirection = currentDirection;
			updateCurrentSprite();
		}
	}
	
	// Returns true if the player isn't moving.
	private boolean isNotMoving() {
		return getDX() == 0.0 && getDY() == 0.0;
	}

	@Override
	public void draw(Group group) {
		group.getChildren().add(currentSprite);
	}
	
	
	// Checks if a key is being pressed.
	private boolean isKeyPressed(KeyCode key) {
		return InputManager.getSingleton().isKeyPressed(key);
	}
	
	// Getters and Setters
	
	@Override
	public double getX() {
		return currentSprite.getTranslateX();
	}
	
	@Override
	public double getY() {
		return currentSprite.getTranslateY();
	}
	
	@Override
	public void setX(double x) {
		currentSprite.setTranslateX(x);
	}

	@Override
	public void setY(double y) {
		currentSprite.setTranslateY(y);
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	
	// Update the current direction
	public void setCurrentDirection(Direction currentDirection) {
		this.currentDirection = currentDirection;
	}
	
	public Direction getCurrentDirection() {
		return currentDirection;
	}
	
	private void updateToFirstIndex() {
		List<Image> sprites = playerImages.get(currentDirection);
		
		if (sprites != null && !sprites.isEmpty()) {
			currentSprite.setImage(sprites.get(INITIAL_SPRITE));
		}
	}
	
	private void updateCurrentSprite() {
		List<Image> sprites = playerImages.get(currentDirection);
		if (sprites != null && !sprites.isEmpty()) {
			int currentSpriteIndex = (int) ((System.currentTimeMillis() / 100) % sprites.size());
			currentSprite.setImage(sprites.get(currentSpriteIndex));
		}
	}
	
	
	
	public double getDX() {
		return dx;
	}
	
	public double getDY() {
		return dy;
	}
	

}
