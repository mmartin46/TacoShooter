package objects;

import interfaces.Entity;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import states.TileType;

public class Tile implements Entity {
	double x, y;
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
	public void update() {
		
	}

	@Override
	public void draw(Group group) {
		
	}

}
