package probus.jam.entity;

import java.awt.Graphics;
import java.util.ArrayList;

import probus.jam.graphics.GameArt;
import probus.jam.levels.GameLevel;
import probus.jam.network.TurnSynchroniser;
import probus.jam.physics.BoundingBox;

public class Mob extends MovingEntity {

	enum State
	{
		WAITING,
		WALK
	}
	
	private int walkTicksDelay = 20;
	private int waitingTicksDelay = 100;
	
	@SuppressWarnings("unused")
	private int timeInDirection = 0;
	
	private State state = State.WAITING;
	
	private int health = 10;
	
	public Mob(GameLevel level, int xx, int yy)
	{
		super(level, xx, yy);
		speed = 1;		
	}
	
	public void init()
	{
		bBox = new BoundingBox(this, x, y, 48, 48);
	}
	
	public void tick()
	{
		int beginX = x;
		int beginY = y;
		
		wander();
		
		bBox.UpdatePosition(beginX, y);
		
		if(gameLevel.getCollisions(bBox).size() > 0)
		{
			y = beginY;
		}
		
		bBox.UpdatePosition(x,beginY);
		if(gameLevel.getCollisions(bBox).size() > 0)
		{
			x = beginX;
		}
		
		bBox.UpdatePosition(x, y);
	}
	
	public void render(Graphics g)
	{
		if(state == State.WALK)
		{
			spriteX = (walkTime / 8 % 4);
		}
		
		if(direction == Direction.DOWN) GameArt.mommy.Draw(g, spriteX, 1, x, y);
		else if(direction == Direction.UP) GameArt.mommy.Draw(g, spriteX, 0, x, y);
		else if(direction == Direction.LEFT) GameArt.mommy.Draw(g, spriteX, 2, x, y);
		else if(direction == Direction.RIGHT) GameArt.mommy.Draw(g, spriteX, 3, x, y);
	}
	
	public void hit()
	{
		health-=2;
		
		if(health <= 0)
			kill();
	}
	
	public void kill()
	{
		gameLevel.removeEntity(this);
	}
	
	private void wander()
	{
		walkTime++;
		timeInDirection++;
		
		if(state == State.WAITING)
		{
			if(walkTime >= waitingTicksDelay)
			{
				state = State.WALK;
				walkTime = 0;
				
				int rand = TurnSynchroniser.synchedRandom.nextInt(2);
				
				if(rand == 1)
				{
					int dir = TurnSynchroniser.synchedRandom.nextInt(4);
					
					if(dir == 0) direction = Direction.DOWN;
					else if(dir == 1) direction = Direction.UP;
					else if(dir == 2) direction = Direction.LEFT;
					else if(dir == 3) direction = Direction.RIGHT;
				}
			}
		}
		
		else if(state == State.WALK)
		{
			if(walkTime < walkTicksDelay)
			{
				if(direction == Direction.DOWN) y += speed;
				else if(direction == Direction.UP) y -= speed;
				else if(direction == Direction.LEFT) x -= speed;
				else if(direction == Direction.RIGHT) x += speed;
			}
			
			else
			{
				state = State.WAITING;
				walkTime = 0;
			}
		}
	}
}
