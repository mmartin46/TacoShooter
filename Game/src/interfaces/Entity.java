package interfaces;

import javafx.scene.Group;

/**
 * Represents an object
 * that can be viewed on the
 * screen.
 */
public interface Entity {
	// Represents the default sprite width.
	final double DEFAULT_WIDTH = 20.0;
	// Represents the default sprite height.
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
	
	// Returns the health of the sprite.
	double getHealth();
	// Sets the health of the sprite.
	void setHealth(double health);
	
	// Draws the sprite.
	void draw(Group group);
}
