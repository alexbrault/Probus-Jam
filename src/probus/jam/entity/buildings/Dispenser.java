package probus.jam.entity.buildings;

import java.awt.Graphics;

import probus.jam.entity.Beam;
import probus.jam.entity.Entity;
import probus.jam.entity.buildings.Base.DispensedItem;
import probus.jam.entity.tiles.Tile;
import probus.jam.graphics.SpriteSheet;
import probus.jam.levels.GameLevel;

public class Dispenser extends Entity {
	
	protected int dispenserLevel;
	protected int powerLevel;
	
	protected int powerFactor;
	protected int powerNeeded;
	
	private GameLevel gameLevel;
	private int x;
	private int y;
	
	private Tile spawnTile = null;
	private DispensedItem dispensedItem;
	
	private boolean shouldRenderIndicator;
	private SpriteSheet indicator;
	
	private boolean isP1;
	
	public int color = 0;

	public Dispenser(GameLevel level, int xx, int yy, boolean p1, int c, DispensedItem item, SpriteSheet ind)
	{
		super(level, xx, yy);
		
		dispenserLevel = 1;
		powerLevel = 0;
		powerFactor = 0;
		powerNeeded = 100;
		
		gameLevel = level;
		x = xx;
		y = yy;
		
		dispensedItem = item;
		indicator = ind;
		
		shouldRenderIndicator = false;
		isP1 = p1;
		color = c;
	}
	
	@Override
	public void init() {

		if(!(color == Beam.WHITE)) {
			spawnTile = gameLevel.getTileAtPosition(x, y);
		}
		else
			powerNeeded = 20000;
		
	}
	
	private void dispense()
	{
		Entity dispensed = null;
		
		if(dispensedItem == DispensedItem.SPLITTER) dispensed = new Splitter(gameLevel, x, y);
		else if(dispensedItem == DispensedItem.FOCUSSOR) dispensed = new Focusseur(gameLevel, x, y);
		else if(dispensedItem == DispensedItem.LENS) dispensed = new Lens(gameLevel, x, y);
		else if(dispensedItem == DispensedItem.WEAPON) dispensed = new Healing(gameLevel, x, y);
		
		spawnTile.entityOnTile = dispensed;
		gameLevel.addEntity(dispensed);
	}


	@Override
	public void tick() {
		
		shouldRenderIndicator = false;
		
		if(powerFactor > 0)
		{
			shouldRenderIndicator = true;
		}
		
		if(spawnTile != null && spawnTile.entityOnTile != null)
		{
			powerLevel = 0;
		}
		
		else
		{
			if(powerFactor > 0)
			{				
				powerLevel += powerFactor;
				
				if(powerLevel >= powerNeeded)
				{
					if(color == Beam.WHITE)
						System.out.println("Win");
					
					else
					{
						dispense();
						powerLevel = 0;
					}
				}
			}
		}
	}


	public int getPowerLevel() {
		if (spawnTile!= null && spawnTile.entityOnTile != null)
			return powerNeeded;
		return powerLevel;
	}

	public int getPowerNeeded() {
		return powerNeeded;
	}
	
	public boolean isComplete() {
		return getPowerLevel() >= getPowerNeeded();
	}

	@Override
	public void render(Graphics g) {
		if(shouldRenderIndicator)
		{
			if(isP1) indicator.Draw(g, 0, 0, x + 96, y);
			else indicator.Draw(g, 0, 0, x - 96, y);
		}
	}
}
