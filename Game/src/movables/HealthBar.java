package movables;

import java.io.InputStream;
import java.util.ArrayList;

import interfaces.Entity;
import interfaces.GameTool;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Used for the player to see how
 * much health the enemy has.
 * The class is used in composition with the
 * enemy.
 */
public class HealthBar implements GameTool {
	
	private final int DISTANCE_FROM_ENEMY = 15;
	private final int NUM_HEALTH_BAR_SPRITES = 5;
	private final int INITIAL_SPRITE = 0;
	
	private ImageView currentSprite;
	private ArrayList<Image> allSprites;
	private Enemy enemy;
	
	public HealthBar(Enemy enemy) {
		this.enemy = enemy;
		initializeSprites();
		initializeCoordinates();
	}
	
	private void initializeSprites() {
		int index = 0;
		InputStream inputStream;
		
		// Initialize data structures
		currentSprite = new ImageView();
		allSprites = new ArrayList<Image>();
		
		// Load sprites
		for (index = 0; index < NUM_HEALTH_BAR_SPRITES; ++index) {
			inputStream = getClass().getResourceAsStream("healthbarsprites/enemybar" + 
					Integer.toString(index) + ".png");
			
			// Add the image to new list of sprites.
			allSprites.add(new Image(inputStream));
		}
		
		// Sets the sprite to the first sprite.
		currentSprite.setImage(allSprites.get(INITIAL_SPRITE));
	}
	
	private void initializeCoordinates() {
		setX(enemy.getX());
		setY(enemy.getY() - DISTANCE_FROM_ENEMY);
	}
	
	private void updateCoordinates() {
		initializeCoordinates();
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
	public ImageView getImageView() {
		return currentSprite;
	}

	@Override
	public void draw(Group group) {

		group.getChildren().add(currentSprite);
	}

	@Override
	public void update() {
		updateCoordinates();
		updateHealthBarSprite();
		// debugHealthBarCoordinates();
	}
	
	private void debugHealthBarCoordinates() {
		System.out.printf("HealthBar(): x=%.1f y=%.1f\n", getX(), getY());
		System.out.printf("Enemy(): x=%.1f y=%.1f\n", enemy.getX(), enemy.getY());

	}
	
	private void updateHealthBarSprite() {
		double enemyHealth = enemy.getHealth();
		int index = 0;
		if (enemyHealth < 85 && enemyHealth >= 75) {
			index = 1;
		} else if (enemyHealth < 75 && enemyHealth >= 55) {
			index = 2;
		} else if (enemyHealth < 55 && enemyHealth >= 35) {
			index = 3;
		} else if (enemyHealth < 35){
			index = 4;
		}
		currentSprite.setImage(allSprites.get(index));
	}

	
}
