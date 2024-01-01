package interfaces;

import javafx.scene.Group;

public interface Entity {
	final double DEFAULT_WIDTH = 20.0;
	final double DEFAULT_HEIGHT = 20.0;
	
	double getX();
	double getY();
	void setX(double x);
	void setY(double y);
	
	double getDX();
	double getDY();
	void setDX(double dx);
	void setDY(double dy);
	
	double getWidth();
	double getHeight();
	
	double getHealth();
	void setHealth(double health);
	
	void draw(Group group);
}
