package movables;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import input.InputManager;
import interfaces.Direction;
import interfaces.Entity;
import interfaces.Movable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import sounds.SoundManager;
import utils.GameConfigurations;


/**
 * Represents a player with sprites, coordinates,
 * and collides with tiles.
 */
public class Player extends Movable implements Entity {
	
	// Constants
	private final int INITIAL_SPRITE = 0;

	// Player Sprite
	private int numSprites;
	

	
	// Player Utilities
	private double health;
	private double coinsCollected = 0;
	private boolean playerDead;

	private ArrayList<Attack> bullets;
	private int currentBulletIndex = 0;
	private final int NUM_BULLETS = 50;

	// Timer
	private long lastSpacePressed = 0;
	private final long bulletDelayMillis = 100;

	// Sprites
	private ImageView currentSprite;
	HashMap<Direction, List<Image>> playerImages;
	
	// Player Direction
	private Direction currentDirection;
	private Direction lastDirection;
	
	private final double PLAYER_SPEED = 2.0;
	
	public ArrayList<Attack> getBullets() {
		return bullets;
	}
	
	public Player() {
		
	}
	
	public Player(double x, double y, int numSprites) {
		setDidPlayerDie(true);
		this.numSprites = numSprites;
		playerImages = new HashMap<>();
		setCurrentDirection(Direction.RIGHT);
		initializePlayer(x, y);
		
		// Attacks
		initializeBullets();
		
		// Sounds
		soundManager = new SoundManager();
	}
	
	
	
	/* Initializes the array of bullets
	 that the player can use to shoot.
	 */
	private void initializeBullets() {
		bullets = new ArrayList<Attack>();
		
		for (int i = 0; i < NUM_BULLETS; ++i) {
			bullets.add(new Attack(this));
		}
	}
	
	/*
	 * Resets the coordinates for the player
	 * to specified x and y coordinates.
	 */
	public void resetAll(double x, double y) {
		setX(x);
		setY(y);
		setHealth(GameConfigurations.DEFAULT_PLAYER_HEALTH);
		setCoinsCollected(0);
	}

	/*
	 * Returns the current sprite.
	 */
	public ImageView getSprite() {
		return currentSprite;
	}
	
	private void initializePlayer(double x, double y) {
		setHealth(DEFAULT_HEALTH);
		initalizeAllSprites();
		
		currentSprite = new ImageView(playerImages.get(currentDirection).get(INITIAL_SPRITE));
		initalizePlayerCoordinates(x, y);
	}
	
	
	private void initalizePlayerCoordinates(double x, double y) {
		setX(x);
		setY(y);
	}
	
	private void handleShotMovement() {
		long currentTime = System.currentTimeMillis();
		if (isKeyPressed(KeyCode.SPACE) &&
			enoughTimePassedForBullet(currentTime)) {
			
			soundManager.playSound(SoundFilePaths.shotFilePath,
								   SoundFilePaths.DEFAULT_VOLUME);

			allowShotToMove();
			lastSpacePressed = currentTime;
		}
	}
	
	// Time passed until another bullet can be shot.
	private boolean enoughTimePassedForBullet(long currentTime) {
		return (currentTime - lastSpacePressed) > bulletDelayMillis;
	}
	
	/*
	 * Check if there are enough bullets
	 * to shoot before allowing the
	 * player to shoot.
	 */
	private void allowShotToMove() {
		if (currentBulletIndex >= NUM_BULLETS) {
			currentBulletIndex = 0;
			resetBulletCoordinates();
		} else {
			bullets.get(currentBulletIndex++).setAllowShot(true);
		}
	}
	
	/*
	 * Resets the bullets positions to allow
	 * for more bullets to be shot.
	 */
	private void resetBulletCoordinates() {
		for (int i = 0; i < NUM_BULLETS; ++i) {
			Attack currentBullet = bullets.get(i);
			currentBullet.setAllowShot(false);
		}
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
	
	

	
	// Handles horizontal and vertical input.
	private void handleInput() {
		handleHorizontalInput();
		handleVerticalInput();
	}
	
	/*
	 * Changes the dx based on where
	 * the player moves.
	 */
	private void handleHorizontalInput() {
		if (isKeyPressed(KeyCode.LEFT)) {
			moveLeft(PLAYER_SPEED);
		} else if (isKeyPressed(KeyCode.RIGHT)) {
			moveRight(PLAYER_SPEED);
		} else {
			dx = 0.0;
		}
	}

	/*
	 * Changes the dy based on where
	 * the player moves.
	 */
	private void handleVerticalInput() {
		if (isKeyPressed(KeyCode.UP)) {
			moveUp(PLAYER_SPEED);
		} else if (isKeyPressed(KeyCode.DOWN)) {
			moveDown(PLAYER_SPEED);
		} else {
			dy = 0.0;
		}
	}
	
	/*
	 * Updates the player's position 
	 * and the player's bullets.
	 */
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
		updateBullets();
		handleShotMovement();
	}
	
	// Draws the list of bullets.
	private void drawBullets(Group group) {
		for (int i = 0; i < NUM_BULLETS; ++i) {
			bullets.get(i).draw(group);
		}
	}
	// Updates the list of bullets.
	private void updateBullets() {
		for (int i = 0; i < NUM_BULLETS; ++i) {
			bullets.get(i).update();
		}
	}
	
	// Returns true if the player isn't moving.
	private boolean isNotMoving() {
		return getDX() == 0.0 && getDY() == 0.0;
	}

	// Draws the player and the bullets.
	@Override
	public void draw(Group group) {
		drawBullets(group);
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
	
	@Override
	public double getWidth() {
		return DEFAULT_WIDTH;
	}
	@Override
	public double getHeight() {
		return DEFAULT_HEIGHT;
	}
	
	
	@Override
	public double getHealth() {
		return health;
	}
	
	@Override
	public void setHealth(double health) {
		this.health = health;
	}
	
	public void setDidPlayerDie(boolean didDie) {
		playerDead = didDie;
	}
	
	public boolean getDidPlayerDie() {
		return playerDead;
	}
	
	@Override
	public double getDX() {
		return dx;
	}
	
	@Override
	public double getDY() {
		return dy;
	}
	
	@Override
	public void setDX(double dx) {
		this.dx = dx;
	}
	
	@Override
	public void setDY(double dy) {
		this.dy = dy;
	}

	public double getCoinsCollected() {
		return coinsCollected;
	}

	public void setCoinsCollected(double coinsCollected) {
		this.coinsCollected = coinsCollected;
	}

	public int getCurrentBulletIndex() {
		return currentBulletIndex;
	}

	public void setCurrentBulletIndex(int currentBulletIndex) {
		this.currentBulletIndex = currentBulletIndex;
	}
	
	
	
	// Sprite Handling

	// Update the current direction
	public void setCurrentDirection(Direction currentDirection) {
		this.currentDirection = currentDirection;
	}
	
	public Direction getCurrentDirection() {
		return currentDirection;
	}
	
	/**
	 *  Make the current sprite
	 *	the initial sprite of the list of sprites.
	 */
	private void updateToFirstIndex() {
		List<Image> sprites = playerImages.get(currentDirection);
		
		if (sprites != null && !sprites.isEmpty()) {
			currentSprite.setImage(sprites.get(INITIAL_SPRITE));
		}
	}
	
	/**
	 * Updates the current sprite
	 * based on the time.
	 */
	private void updateCurrentSprite() {
		List<Image> sprites = playerImages.get(currentDirection);
		if (sprites != null && !sprites.isEmpty()) {
			int currentSpriteIndex = (int) ((System.currentTimeMillis() / 100) % sprites.size());
			currentSprite.setImage(sprites.get(currentSpriteIndex));
		}
	}
	
	public void increaseCoinsCollected(int numCoins) {
		coinsCollected += numCoins;
	}
	
	


	

}
