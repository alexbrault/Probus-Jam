package probus.jam.entity;

import java.awt.Graphics;
import java.util.ArrayList;

import probus.jam.graphics.GameArt;
import probus.jam.levels.GameLevel;
import probus.jam.network.TurnSynchroniser;
import probus.jam.physics.BoundingBox;

public class Bullet extends MovingEntity {

	private int velX;
	private int velY;
	
	private long ticksLived = 0;
	private long ticksToKill = 60;
	
	public Bullet(GameLevel level, int xx, int yy, int vx, int vy)
	{
		super(level, xx, yy);
	
		velX = vx;
		velY = vy;
	}
	
	public void init()
	{
		speed = 4;
		
		walkTime = TurnSynchroniser.synchedRandom.nextInt(4);
		
		bBox = new BoundingBox(this, x, y, 16, 16);
		gameLevel.addEntity(this);
	}
	
	public void kill()
	{
		gameLevel.removeEntity(this);
	}
	
	public void tick()
	{
		move();
		
		ticksLived++;
		walkTime++;
		
		if(ticksLived >= ticksToKill)
			kill();
	}
	
	public void render(Graphics g)
	{
		spriteX = (walkTime / 4) % 4;
		GameArt.bullet.Draw(g, spriteX, 0, x, y);
	}
	
	private void move()
	{
		x += velX * speed;
		y += velY * speed;
		
		bBox.UpdatePosition(x, y);
		
		ArrayList<BoundingBox> collisions = new ArrayList<BoundingBox>(gameLevel.getCollisions(bBox));
		
		for(int i = 0; i < collisions.size(); i++)
		{
			if(collisions.get(i).getEntity().getClass().getName() == "probus.jam.entity.Mob")
			{
				collisions.get(i).getEntity().hit();
				kill();
			}
		}
	}
}
