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
	
	public static boolean entityIsMovingHorizontally(Entity entity) {
		return entity.getDX() > 0;
	}

	public static boolean entityIsMovingVertically(Entity entity) {
		return entity.getDY() > 0;
	}
	
	public static void playerBlockCollision(Entity entity, Entity block) {
		double px = entity.getX(), py = entity.getY();
		double pw = entity.getWidth(), ph = entity.getHeight();

		double bx = block.getX(), by = block.getY();
		double bw = block.getWidth(), bh = block.getHeight();
		
		// Bottom collision
		if (px + pw / 2 > bx && px + pw / 2 < bx + bw && py < by + bh && py > by && entity.getDY() < 0) {
			entity.setY(by + bh);
			entity.setDY(0);
		}
		// Top Collision
		if (px + pw > bx && px < bx + bw && py + ph > by && py < by && entity.getDY() > 0) {
			entity.setY(by - ph);
			entity.setDY(0);
		}
		// Right Edge
		if (py + ph > by && py < by + bh && px < bx + bw && px + pw > bx + bw && entity.getDX() < 0) {
			entity.setX(bx + bw);
			entity.setDX(0);
		} 
		// Left Edge
		if (py + ph > by && py < by + bh && px + pw > bx && px < bx && entity.getDX() > 0) {
			entity.setX(bx - pw);
			entity.setDX(0);
		} 
	}

}
