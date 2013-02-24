package probus.jam.entity.buildings;

import java.awt.Graphics;

import probus.jam.entity.Beam;
import probus.jam.entity.Entity;
import probus.jam.entity.tiles.Tile;
import probus.jam.graphics.GameArt;
import probus.jam.levels.GameLevel;
import probus.jam.physics.BoundingBox;

public class Base extends Entity implements BeamAware {
	
	enum DispensedItem
	{
		MIRROR,
		FOCUSSOR,
		SPLITTER,
		LENS,
		WEAPON,
		NONE
	}
	
	
	public Dispenser[] dispensers;
	public static Base[] bases = new Base[2];
	public boolean p1;
	
	private Tile spawnTile;
	
	public Base(GameLevel level, int xx, int yy, boolean thisPlayer)
	{
		super(level, xx,yy);
		
		p1 = thisPlayer;
		bases[p1?0:1] = this;
	}
	
	public void init()
	{
		dispensers = new Dispenser[5];
		
		if(p1) bBox = new BoundingBox(this, x + 128, y, 96, 160);
		else bBox = new BoundingBox(this, x - 32, y, 96, 160);
		
		if(p1)
		{
			dispensers[0] = new Dispenser(gameLevel, x + 96, y, p1, Beam.RED, DispensedItem.WEAPON, GameArt.indicatorP1Red); dispensers[0].init();
			dispensers[1] = new Dispenser(gameLevel, x + 96, y + 32, p1, Beam.BLUE, DispensedItem.FOCUSSOR, GameArt.indicatorP1Blue); dispensers[1].init();
			dispensers[2] = new Dispenser(gameLevel, x + 96, y + 64, p1, Beam.WHITE, DispensedItem.NONE, GameArt.indicatorP1Canon); dispensers[2].init();
			dispensers[3] = new Dispenser(gameLevel, x + 96, y + 96, p1, Beam.GREEN, DispensedItem.SPLITTER, GameArt.indicatorP1Green); dispensers[3].init();
			dispensers[4] = new Dispenser(gameLevel, x + 96, y + 128, p1, Beam.YELLOW, DispensedItem.LENS, GameArt.indicatorP1Yellow); dispensers[4].init();
		}
		
		else
		{
			dispensers[0] = new Dispenser(gameLevel, x + 64, y, p1, Beam.YELLOW, DispensedItem.LENS, GameArt.indicatorP2Yellow); dispensers[0].init();
			dispensers[1] = new Dispenser(gameLevel, x + 64, y + 32, p1, Beam.GREEN, DispensedItem.SPLITTER, GameArt.indicatorP2Green); dispensers[1].init();
			dispensers[2] = new Dispenser(gameLevel, x + 64, y + 64, p1, Beam.WHITE, DispensedItem.NONE, GameArt.indicatorP2Canon); dispensers[2].init();
			dispensers[3] = new Dispenser(gameLevel, x + 64, y + 96, p1, Beam.BLUE, DispensedItem.FOCUSSOR, GameArt.indicatorP2Blue); dispensers[3].init();
			dispensers[4] = new Dispenser(gameLevel, x + 64, y + 128, p1, Beam.RED, DispensedItem.WEAPON, GameArt.indicatorP2Red); dispensers[4].init();
		}
		
		for(int i = 0; i< 5 ; i++)
		{
			if(p1) gameLevel.getTileAtPosition(x + 192, y + i * 32).entityOnTile = this;
			else gameLevel.getTileAtPosition(x, y + i * 32).entityOnTile = this;
			
			gameLevel.addEntity(dispensers[i]);
		}
		
		if(p1) spawnTile = gameLevel.getTileAtPosition(x, y + 64);
		else spawnTile = gameLevel.getTileAtPosition(x + 160, y + 64);
		
		spawnTile.entityOnTile = null;
	}
	
	public void tick()
	{		
		if(spawnTile.entityOnTile == null)
		{
			Mirror mirror = new Mirror(gameLevel, spawnTile.x, spawnTile.y);
			
			gameLevel.addEntity(mirror);
			spawnTile.entityOnTile = mirror;
		}
	}
	
	public void render(Graphics g)
	{
		if(p1)
			GameArt.baseP1.Draw(g, 0, 0, x, y);
		
		else
			GameArt.baseP2.Draw(g, 0, 0, x, y);
	}

	public boolean beamCanPass()
	{
		return false;
	}
	
	public void manageBeamCollision(Beam beam)
	{
		int i = (beam.y - y) / 32;
		
		if(i >= 0 && i < 5)
		{
			if(dispensers[i].color == beam.getColor())
				dispensers[i].powerFactor = beam.getPower();
			
			else if(dispensers[i].color == Beam.WHITE)
				dispensers[i].powerFactor = beam.getPower();
			
			else
				dispensers[i].powerFactor = 0;
		}
	}
	
	public void beamHasStopped(Beam beam) {
		int i = (beam.y - y) / 32;
		
		if(i >= 0 && i < 5)
			dispensers[i].powerFactor = 0;
	}
}
