package objects;

import interfaces.Entity;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import states.TileType;


/**
 * The Tile can be used for colliding
 * or non-colliding.
 */
public class Tile implements Entity {
	
	private final int INVISIBLE_INDEX = -1;
	
	double x, y, dx, dy;
	int index;
	TileType tileType;
	ImageView imageView;
	boolean allowVisibility;
	
	public Tile(int index, double x, double y, TileType tileType, ImageView imageView) {
		setIndex(index);
		setX(x);
		setY(y);
		setImageView(imageView);
		setVisibility(true);
	}
	
	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}
	
	public ImageView getImageView() {
		return imageView;
	}
	
	public void setTileType(TileType tileType) {
		this.tileType = tileType;
	}
	
	public TileType getTileType() {
		return tileType;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}
	
	@Override
	public double getWidth() {
		return DEFAULT_WIDTH;
	}
	
	@Override
	public double getHeight() {
		return DEFAULT_HEIGHT;
	}

	public void update() {
		
	}
	
	// Set if the tile should be drawn
	// to the screen or not.
	public void setVisibility(boolean visibility) {
		this.allowVisibility = visibility;
	}
	
	// Check the visibility of the tile.
	public boolean getVisibility() {
		return allowVisibility;
	}

	@Override
	public void draw(Group group) {
		if (getVisibility()) {
			imageView.setLayoutX(getX());
			imageView.setLayoutY(getY());
			group.getChildren().add(imageView);
		}
	}
	
	public void makeTileInvisible() {
		setVisibility(false);
		setIndex(INVISIBLE_INDEX);
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
