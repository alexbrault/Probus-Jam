package probus.jam.entity;

import java.awt.Graphics;
import java.util.ArrayList;

import probus.jam.entity.buildings.BeamAware;
import probus.jam.entity.tiles.Tile;
import probus.jam.graphics.GameArt;
import probus.jam.levels.GameLevel;

public class Beam extends MovingEntity {
	
	public static final int WHITE = 0;
	public static final int RED = 1;
	public static final int BLUE = 2;
	public static final int YELLOW = 3;
	public static final int GREEN = 4;
	
	private boolean isDestroyed = false;
	private Tile startingTile;
	private ArrayList<Tile> occupiedTiles;
	private Entity owner;
	
	private int beamPower;
	private int beamColor;
	
	private int[] colors;
	
	public Beam(GameLevel level, int xx, int yy, Direction dir, Entity owner, int power)
	{
		super(level, xx, yy);
		this.owner = owner;
		
		direction = dir;
		startingTile = level.getTileAtPosition(xx, yy);
		beamPower = power;
		colors = new int[5];
		beamColor = YELLOW;
		
		fillColors();
	}
	
	private void fillColors() {
		colors[WHITE] = 0x00ffffff;
		colors[RED] = 0x00ff0000;
		colors[YELLOW] = 0x00ffff00;
		colors[GREEN] = 0x0000ff00;
		colors[BLUE] = 0x000000ff;
	}

	public void init()
	{
		occupiedTiles = new ArrayList<Tile>();
		occupiedTiles.add(startingTile);
		
		computeBeam();
	}
	
	public void setColor(int c)
	{
		beamColor = c;
	}
	
	public int getColor()
	{
		return beamColor;
	}
	
	public int getPower()
	{
		return beamPower;
	}
	
	public void setPower(int power)
	{
		beamPower = power;
		
		if(beamPower < 0)
			beamPower = 0;
		
		if(beamPower > 4)
			beamPower = 4;
	}
	
	public Direction getDirection()
	{
		return direction;
	}
	
	public void increasePower(int p)
	{
		beamPower += p;
		
		if(beamPower < 0)
			beamPower = 0;
		
		if(beamPower > 4)
			beamPower = 4;
	}
	
	private int fromIndexRemove;
	@Override
	public void tick() {
		if (isDestroyed) return;
		boolean mustKillPart = false;
		fromIndexRemove = occupiedTiles.size();
		
		Tile tile = null;
		
		if(occupiedTiles.size() == 0)
			return;
		
		for(int i = 0; i < occupiedTiles.size(); i++)
		{
			tile = occupiedTiles.get(i);
			
			ArrayList<Entity> collisions = gameLevel.collisionsOnTile(tile.x, tile.y, 12, 12, 8, 8 );
			
			for(int a = 0; a < collisions.size(); a++)
			{
				if(collisions.get(a) instanceof Player) {
					//Sound.bzzt.Play();
					collisions.get(a).takeDamage(getPower());
				}
					
			}
			
			if(tile.entityOnTile != null)
			{
				if(tile.entityOnTile instanceof BeamAware && tile.entityOnTile != owner)
				{
					((BeamAware) tile.entityOnTile).manageBeamCollision(this);
					
					if(i < occupiedTiles.size() - 1)
					{
						if(!((BeamAware) tile.entityOnTile).beamCanPass())
						{
							mustKillPart = true;
							fromIndexRemove = i + 1;
							break;
						}
					}
				}
			}
		}
		
		tile = occupiedTiles.get(occupiedTiles.size() - 1);
		
		if(mustKillPart)
		{
			while(occupiedTiles.size() > fromIndexRemove) {
				Tile t = occupiedTiles.get(fromIndexRemove);
				if (t.entityOnTile != null && t.entityOnTile instanceof BeamAware) {
					((BeamAware)t.entityOnTile).beamHasStopped(this);
				}
				occupiedTiles.remove(fromIndexRemove);
			}
			
			computeBeam();
		}
		
		else if(tile.entityOnTile == null || !(tile.entityOnTile instanceof probus.jam.entity.buildings.BeamAware))
		{
			computeBeam();
		}
	}
	
	public void computeBeam()
	{
		boolean mustStop = false;
		int tx = startingTile.x + startingTile.width / 2;
		int ty = startingTile.y + startingTile.height / 2;
		
		int index = 0;
		
		occupiedTiles.clear();
		occupiedTiles.add(startingTile);
		
		while(!mustStop)
		{
			if(direction == Direction.LEFT){ tx -= occupiedTiles.get(index).width; }
			else if(direction == Direction.RIGHT){ tx += occupiedTiles.get(index).width; }
			else if(direction == Direction.UP){ ty -= occupiedTiles.get(index).width; }
			else if(direction == Direction.DOWN){ ty += occupiedTiles.get(index).width; }
			
			Tile nextTile = gameLevel.getTileAtPosition(tx, ty);
			
			if(nextTile == null)
				mustStop = true;
			
			else
			{
				if(nextTile.entityOnTile == null)
					occupiedTiles.add(nextTile);
					
				else if(nextTile.entityOnTile instanceof probus.jam.entity.buildings.BeamAware)
				{
					if(((BeamAware) nextTile.entityOnTile).beamCanPass() && nextTile.entityOnTile != owner)
						occupiedTiles.add(nextTile);
					
					else
					{
						occupiedTiles.add(nextTile);
						mustStop = true;
					}
					
					((BeamAware) nextTile.entityOnTile).manageBeamCollision(this);
				}
				
				else
					mustStop = true;
			}
			
			index++;
			/*
			if(index >= 10)
				mustStop = true;*/
		}
	}
	
	public void destroy() {
		for (Tile t : occupiedTiles) {
			if (t.entityOnTile != null && t.entityOnTile instanceof BeamAware) {
				((BeamAware)t.entityOnTile).beamHasStopped(this);
			}
		}
		
		gameLevel.removeEntity(this);
		isDestroyed = true;
	}
	
	@Override
	public void render(Graphics g) {		
		for(int i = 1; i < occupiedTiles.size() - 1; i++)
		{
			switch (this.direction) {
			case UP:
				spriteX = 4;
				break;
			case DOWN:
				spriteX = 4;
				break;
			case LEFT:
				spriteX = 0;
				break;
			case RIGHT:
			default:
				spriteX = 0;
				break;
			}
			
			spriteX += (beamPower - 1);
			if(spriteX > 7) spriteX = 7;
	
			
			if(beamColor == YELLOW) GameArt.beam.Draw(g, spriteX, beamColor - 1, occupiedTiles.get(i).x, occupiedTiles.get(i).y);
			else if(beamColor == BLUE) GameArt.beam.Draw(g, spriteX, beamColor - 1, occupiedTiles.get(i).x, occupiedTiles.get(i).y);
			else if(beamColor == RED) GameArt.beam.Draw(g, spriteX, beamColor - 1, occupiedTiles.get(i).x, occupiedTiles.get(i).y);
			else if(beamColor == GREEN) GameArt.beam.Draw(g, spriteX, beamColor - 1, occupiedTiles.get(i).x, occupiedTiles.get(i).y);
			
			//GameArt.beam.Draw(g, (int)frameCount / 8 % 4, direction, occupiedTiles.get(i).x, occupiedTiles.get(i).y,colors[beamColor] + (beamPower * 0x20000000));
			//colors[beamColor][beamPower-1]
		}
	}
}
