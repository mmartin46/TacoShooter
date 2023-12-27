package collisions;

import interfaces.Entity;

public class Collisions {
	
	public static boolean entityCollision(Entity entity, Entity enemy) {
		boolean horizontalCollision = leftEdgeTouched(entity, enemy) 
								   && rightEdgeTouched(entity, enemy);
	
		boolean verticalCollision = upEdgeTouched(entity, enemy) 
								   && bottomEdgeTouched(entity, enemy);

		return horizontalCollision && verticalCollision; 
	}
	
	
	public static boolean rightEdgeTouched(Entity entity, Entity enemy) {
		return entity.getX() < enemy.getX() + enemy.getWidth();
	}
	
	public static boolean leftEdgeTouched(Entity entity, Entity enemy) {
		return entity.getX() + entity.getWidth() > enemy.getX();
	}
	
	public static boolean bottomEdgeTouched(Entity entity, Entity enemy) {
		return entity.getY() < enemy.getY() + enemy.getHeight();
	}
	
	public static boolean upEdgeTouched(Entity entity, Entity enemy) {
		return entity.getY() + entity.getHeight() > enemy.getY();
	}
	
}
