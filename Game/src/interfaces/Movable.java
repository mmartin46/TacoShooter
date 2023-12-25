package interfaces;



public abstract class Movable {
	public double dx, dy;
	public abstract void setCurrentDirection(Direction currentDirection);
	public abstract Direction getCurrentDirection();
	
	public void moveLeft(double speed) {
		dx = -speed;
		setCurrentDirection(Direction.LEFT);
	}
	public void moveRight(double speed) {
		dx = speed;
		setCurrentDirection(Direction.RIGHT);
	}
	public void moveUp(double speed) {
		dy = -speed;
		setCurrentDirection(Direction.UP);
	}
	public void moveDown(double speed) {
		dy = speed;
		setCurrentDirection(Direction.DOWN);
	}
}
