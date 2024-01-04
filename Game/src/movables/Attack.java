package movables;

import java.io.InputStream;

import interfaces.Direction;
import interfaces.Entity;
import interfaces.GameTool;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.Pair;

public class Attack implements GameTool, Entity {
	
	private final int OFFSET_X = 7;
	private final int OFFSET_Y = 8;
	
	private Pair<Integer, Integer> offset;
	
	private ImageView currentSprite;
	private Player matchedEntity;
	private Direction lastDirection;
	private boolean allowShot;
	
	public Attack(Player matchedEntity) {
		lastDirection = Direction.RIGHT;
		setAllowShot(false);
		this.matchedEntity = matchedEntity;
		
		initializeSprites();
	}
	
	private void initializeSprites() {
		offset = new Pair<Integer, Integer>();
		currentSprite = new ImageView();
		
		InputStream inputStream = getClass().getResourceAsStream("bulletsprite/bullet.png");
		currentSprite.setImage(new Image(inputStream));
	}
	
	private void initializeCoordinates() {
		offset.setFirst(OFFSET_X);
		offset.setSecond(OFFSET_Y);
		
		setX(matchedEntity.getX() + offset.getFirst());
		setY(matchedEntity.getY() + offset.getSecond());
	}
	
	private void updateCoordinates() {
		if (!getAllowShot()) {
			initializeCoordinates();
			lastDirection = matchedEntity.getCurrentDirection();
		} else {
			switch (lastDirection) {
				case LEFT:
					setX(getX() - 1);
					break;
				case RIGHT:
					setX(getX() + 1);
					break;
				case DOWN:
					setY(getY() + 1);
					break;
				case UP:
					setY(getY() - 1);
					break;
				default:
			}	
		}
	}
	
	public boolean getAllowShot() {
		return allowShot;
	}
	
	public void setAllowShot(boolean allowShot) {
		this.allowShot = allowShot;
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
	}

	@Override
	public double getDX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDX(double dx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDY(double dy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getHealth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setHealth(double health) {
		// TODO Auto-generated method stub
		
	}

}
