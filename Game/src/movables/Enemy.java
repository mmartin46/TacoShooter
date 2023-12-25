package movables;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import interfaces.Direction;
import interfaces.Entity;
import interfaces.Movable;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Enemy extends Movable implements Entity {
	// Constants
	private final int INITIAL_SPRITE = 0;
	
	// Player Coordinates
	private double dx = 0.0;
	private double dy = 0.0;
	
	// Player Sprite
	private int numSprites;
	private ImageView currentSprite;
	HashMap<Direction, List<Image>> enemyImages;
	
	// Player Direction
	private Direction currentDirection;
	private Direction lastDirection;
	
	private final double ENEMY_SPEED = 2.0;
	
	public Enemy(int x, int y, int numSprites) {
		this.numSprites = numSprites;
		enemyImages = new HashMap<>();
	}
	
	private void initalizePlayerCoordinates(double x, double y) {
		setX(x);
		setY(y);
	}
	
	private void initializeEnemy(double x, double y) {
		initalizeAllSprites();
		
		currentSprite = new ImageView(enemyImages.get(currentDirection).get(INITIAL_SPRITE));
		initalizePlayerCoordinates(x, y);
	}	

	// Initializes all the sprites.
	private void initalizeAllSprites() {
		int index = 1;
		InputStream inputStream;

		initializeDirections();
		
		
		for (index = 0; index <= numSprites; ++index) {
			inputStream = getClass().getResourceAsStream("enemysprites/enemy" + 
					Integer.toString(index) + 
					".png");
			
			Direction currentDirection = getIndexDirection(index);
			if (currentDirection != null) {
				enemyImages.get(currentDirection).add(new Image(inputStream));
			}
		}
	}

	// Initializes directions for the map.
	private void initializeDirections() {
		for (Direction direction : Direction.values()) {
			enemyImages.put(direction, new ArrayList<>());
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

	

	public void update(Entity entity) {
		setDirection(entity);
	}

	@Override
	public void draw(Group group) {
		
	}

	@Override
	public void setCurrentDirection(Direction currentDirection) {
		this.currentDirection = currentDirection;
	}

	@Override
	public Direction getCurrentDirection() {
		return currentDirection;
	}
	

	public double calculateDistance(double x, double y) {
		return Math.abs(x + this.getX()) + 
			   Math.abs(y + this.getY());
	}
	
	public void setDirection(Entity entity) {
		Double movedRight = this.getX() + 10;
		Double movedLeft = this.getX() - 10;
		Double movedUp = this.getY() - 10;
		Double movedDown = this.getY() + 10;
		
		Double[] allDistances = { movedRight, movedLeft, movedUp, movedDown };
		Double minDistance = getMin(allDistances);
		
		if (minDistance == movedRight) {
			moveRight(ENEMY_SPEED);
		} else if (minDistance == movedLeft) {
			moveLeft(ENEMY_SPEED);
		} else if (minDistance == movedUp) {
			moveUp(ENEMY_SPEED);
		} else if (minDistance == movedDown) {
			moveRight(ENEMY_SPEED);
		} else {
			// Do nothing
		}
	}
	
	// Returns a minimum of an array.
	private Double getMin(Double[] array) {
		Double min = array[0];
		for (int i = 0; i < array.length; ++i) {
			if (array[i] < min) {
				min = array[i];
			}
		}
		return min;
	}
	
}
