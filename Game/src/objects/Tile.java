package objects;

import interfaces.Entity;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import states.TileType;

public class Tile implements Entity {
	double x, y, dx, dy;
	int index;
	TileType tileType;
	ImageView imageView;
	
	public Tile(int index, double x, double y, TileType tileType, ImageView imageView) {
		setIndex(index);
		setX(x);
		setY(y);
		setImageView(imageView);
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

	@Override
	public void draw(Group group) {
		imageView.setLayoutX(getX());
		imageView.setLayoutY(getY());
		group.getChildren().add(imageView);
	}

	@Override
	public double getDX() {
		return 0;
	}

	@Override
	public double getDY() {
		return 0;
	}
	
	@Override
	public void setDX(double dx) {
		this.dx = dx;
	}
	
	@Override
	public void setDY(double dy) {
		this.dy = dy;
	}

}
