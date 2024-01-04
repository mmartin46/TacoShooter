package interfaces;


import javafx.scene.Group;
import javafx.scene.image.ImageView;

public interface GameTool {
	double getX();
	double getY();
	void setX(double x);
	void setY(double y);
	ImageView getImageView();
	
	void draw(Group group);
	void update();
}
