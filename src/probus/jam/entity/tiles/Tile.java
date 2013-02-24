package probus.jam.entity.tiles;

import java.awt.Graphics;

import probus.jam.entity.Entity;
import probus.jam.levels.GameLevel;

public class Tile extends Entity {

	public Entity entityOnTile = null;
	
	public int width = 32;
	public int height = 32;
	
	public Tile(GameLevel level, int xx, int yy)
	{
		super(level, xx, yy);
	}
	
	public void init()
	{
		
	}
	
	public void tick()
	{
	}
	
	public void render(Graphics g)
	{
	}
	
	public boolean pointInTile(int xx, int yy)
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
}
