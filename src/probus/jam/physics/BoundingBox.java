package probus.jam.physics;

import java.awt.Graphics;

import probus.jam.entity.Entity;

public class BoundingBox {

	private int x;
	private int y;
	
	private int width;
	private int height;
	
	private Entity entity;
	
	public BoundingBox(Entity ent, int xx, int yy, int w, int h)
	{
		x = xx;
		y = yy;
		width = w;
		height = h;
		
		entity = ent;
	}
	
	public Entity getEntity()
	{
		return entity;
	}
	
	public void UpdatePosition(int xx, int yy)
	{
		x = xx;
		y = yy;
	}
	
	public void DebugRender(Graphics g)
	{
		g.drawRect(x, y, width, height);
	}
	
	public boolean Collided(BoundingBox bb)
	{
		if(bb == null)
			return false;
		
		if(y + height < bb.y) return false;
		if(y > bb.y + bb.height) return false;
		if(x + width < bb.x) return false;
		if(x > bb.x + bb.width) return false;
		
		return true;
	}
	
	public boolean pointInBox(int xx, int yy)
	{
		if(xx > x && xx < x + width)
		{
			if(yy > y && yy < y + height)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void resize(int w, int h)
	{
		width = w;
		height = h;
	}
	
	public int getCenterX()
	{
		return x + width / 2;
	}
	
	public int getCenterY()
	{
		return y + height / 2;
	}
}
