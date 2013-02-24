package probus.jam.entity;

import java.awt.Graphics;

import probus.jam.levels.GameLevel;

public abstract class MovingEntity extends Entity {

	protected int spriteX;
	
	protected int walkTime;
	protected int speed;
	
	public MovingEntity(GameLevel level, int xx, int yy)
	{
		super(level, xx, yy);
		
		spriteX = 0;
		direction = Direction.UP;
		speed = 1;
		
		gameLevel = level;
	}
	
	public abstract void render(Graphics g);
}
