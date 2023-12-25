package interfaces;

import javafx.scene.Group;

public interface Entity {
	double getX();
	double getY();
	void setX(double x);
	void setY(double y);
	
	void draw(Group group);
}
