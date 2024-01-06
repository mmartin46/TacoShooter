package movables;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import collisions.Collisions;
import interfaces.Direction;
import interfaces.Entity;
import interfaces.Movable;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import objects.Tile;
import sounds.SoundManager;

/**
 * Represents an enemy that is used against
 * the Player class.
 */
public class Enemy extends Movable implements Entity {
	// Constants
	private final int INITIAL_SPRITE = 0;
	

	// Player Sprite
	private int numSprites;
	
	private double health;
	private ImageView currentSprite;
	HashMap<Direction, List<Image>> enemyImages;
	private HealthBar healthBar;
	
	// Player Direction
	private Direction currentDirection;
	
	private final double ENEMY_SPEED = 0.4;


	public Enemy(int x, int y, int numSprites) {
		this.numSprites = numSprites;
		enemyImages = new HashMap<>();
		initializeEnemy(x, y);
		
		// Sound initialization
		soundManager = new SoundManager();
	}
	
	private void initalizeEnemyCoordinates(double x, double y) {
		setX(x);
		setY(y);
	}
	
	private void initializeEnemy(double x, double y) {
		initializeDirections();
		initializeAllSprites();

		currentDirection = Direction.RIGHT;
		currentSprite = new ImageView(enemyImages.get(currentDirection).get(INITIAL_SPRITE));
		initalizeEnemyCoordinates(x, y);
		initializeHealthBar();
	}	

	private void initializeHealthBar() {
		setHealth(DEFAULT_HEALTH);
		healthBar = new HealthBar(this);
	}
	
	// Initializes all the sprites.
	private void initializeAllSprites() {
		int index = 1;
		InputStream inputStream;

		
		
		for (index = 0; index < numSprites; ++index) {
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
	
	/**
	 * Updates the current sprite
	 * based on the time.
	 */
	private void updateCurrentSprite() {
		List<Image> sprites = enemyImages.get(currentDirection);
		if (sprites != null && !sprites.isEmpty()) {
			int currentSpriteIndex = (int) ((System.currentTimeMillis() / 100) % sprites.size());
			currentSprite.setImage(sprites.get(currentSpriteIndex));
		}
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
	


	/**
	 * Updates the position of the enemy based
	 * on the tileMap and the given entity.
	 * @param entity
	 * @param tileMap
	 */
	public void update(Entity entity, Tile[][] tileMap) {
		setDirection(entity);
		
				
		collisionWithTile(entity, tileMap);
		// Updates the velocity based on the input.
		currentSprite.setTranslateX(getX() + dx);
		currentSprite.setTranslateY(getY() + dy);
		
		updateCurrentSprite();
		healthBar.update();
	}
	
	// Handles collision with a tile.
	private void collisionWithTile(Entity entity, Tile[][] tileMap) {
		int x, y;
		for (x = 0; x < tileMap.length; ++x) {
			for (y = 0; y < tileMap[0].length; ++y) {
				if (Collisions.isMovethruTile(tileMap, x, y)) {
					Collisions.playerBlockCollision(this, tileMap[x][y], 
												Collisions.COLLIDE_WITH_ENTITY);
					
					// Checks for collision with the player.
					if (Collisions.playerBlockCollision(this, entity, 
										Collisions.PASS_THORUGH_ENTITY)) {
						if (entity instanceof Player) {
							entity.setHealth(entity.getHealth() - 0.00004);
						}
					}
				}
			}
		}
	}
	
	// Handles collision with a bullet.
	public void collisionWithBullet(ArrayList<Attack> bullets) {
		for (Attack bullet : bullets) {
			if (Collisions.playerBlockCollision(this, bullet, Collisions.PASS_THORUGH_ENTITY) &&
				bullet.getAllowShot()) {
				
				soundManager.playSound(SoundFilePaths.enemyHitFilePath,
										SoundFilePaths.LOW_VOLUME);
				
				setHealth(getHealth() - 1);
			}
		}
	}
	
	

	@Override
	public void draw(Group group) {
		group.getChildren().add(currentSprite);
		healthBar.draw(group);
	}

	@Override
	public void setCurrentDirection(Direction currentDirection) {
		this.currentDirection = currentDirection;
	}

	@Override
	public Direction getCurrentDirection() {
		return currentDirection;
	}
	

	public void setDirection(Entity entity) {
		
		Double movedRight = Collisions.calculateDistance(this, entity.getX() - 30, entity.getY());
		Double movedLeft = Collisions.calculateDistance(this, entity.getX() + 30, entity.getY());
		Double movedUp = Collisions.calculateDistance(this, entity.getX(), entity.getY() + 30);
		Double movedDown = Collisions.calculateDistance(this, entity.getX(), entity.getY() - 30);
		
		Double[] allDistances = { movedRight, movedLeft, movedUp, movedDown };
		Double minDistance = getMin(allDistances);
		
		if (minDistance == movedRight) {
			moveRight(ENEMY_SPEED);
		} else if (minDistance == movedLeft) {
			moveLeft(ENEMY_SPEED);
		} else if (minDistance == movedUp) {
			moveUp(ENEMY_SPEED);
		} else if (minDistance == movedDown) {
			moveDown(ENEMY_SPEED);
		} else {
			// Do nothing
			dx = 0.0;
			dy = 0.0;
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

	
	// Getters and Setters
	
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

	@Override
	public double getHealth() {
		return health;
	}

	@Override
	public void setHealth(double health) {
		this.health = health;
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

	@Override
	public double getWidth() {
		return DEFAULT_WIDTH;
	}
	@Override
	public double getHeight() {
		return DEFAULT_HEIGHT;
	}
	
	// For Debugging
	
	// Used for debugging distance from entity.
	private void debugMinDistance(Double[] allDistances) {
		String[] directions = { "RIGHT", "LEFT", "UP", "DOWN" };
		for (int i = 0; i < allDistances.length; ++i) {
			System.out.print(directions[i] + "=" + allDistances[i] + ",");
		}
		System.out.println();
	}
	
	private void debugXYCoordinates() {
		System.out.printf("Enemy() : x = %.1f, y = %.1f\n", this.getX(), this.getY());
	}
	
	
}
