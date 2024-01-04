package collisions;

import interfaces.Entity;
import objects.Tile;

public class Collisions {
	
	public final static boolean COLLIDE_WITH_ENTITY = true;
	public final static boolean PASS_THORUGH_ENTITY = false;
	
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
	
	public static boolean isMovethruTile(Tile[][] tileMatrix, int x, int y) {
		return (tileMatrix[x][y].getIndex() != -1);
	}
	
	// Uses the Manhattan distance to calculate
	// where the player needs to move.
	public static double calculateDistance(Entity currentEntity, double x, double y) {
		double dx = x - currentEntity.getX();
		double dy = y - currentEntity.getY();
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	public static boolean playerBlockCollision(Entity entity, Entity block, boolean doCollide) {
		double px = entity.getX(), py = entity.getY();
		double pw = entity.getWidth(), ph = entity.getHeight();

		double bx = block.getX(), by = block.getY();
		double bw = block.getWidth(), bh = block.getHeight();
		
		boolean collided = false;
		
		// Bottom collision
		if (px + pw / 2 > bx && px + pw / 2 < bx + bw && py < by + bh && py > by && entity.getDY() < 0) {
			
			if (doCollide == COLLIDE_WITH_ENTITY) {
				entity.setY(by + bh);
				entity.setDY(0);
			}
			collided = true;
		}
		// Top Collision
		if (px + pw > bx && px < bx + bw && py + ph > by && py < by && entity.getDY() > 0) {
			if (doCollide == COLLIDE_WITH_ENTITY) {
				entity.setY(by - ph);
				entity.setDY(0);
			}
			collided = true;
		}
		// Right Edge
		if (py + ph > by && py < by + bh && px < bx + bw && px + pw > bx + bw && entity.getDX() < 0) {
			if (doCollide == COLLIDE_WITH_ENTITY) {
				entity.setX(bx + bw);
				entity.setDX(0);
			}
			collided = true;
		} 
		// Left Edge
		if (py + ph > by && py < by + bh && px + pw > bx && px < bx && entity.getDX() > 0) {
			if (doCollide == COLLIDE_WITH_ENTITY) {
				entity.setX(bx - pw);
				entity.setDX(0);
			}
			collided = true;
		} 
		return collided;
	}

}
