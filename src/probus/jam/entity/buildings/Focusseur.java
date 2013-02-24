package probus.jam.entity.buildings;

import java.awt.Graphics;

import probus.jam.entity.Beam;
import probus.jam.entity.Entity;
import probus.jam.entity.Mob;
import probus.jam.graphics.GameArt;
import probus.jam.levels.GameLevel;
import probus.jam.network.TurnSynchroniser;

public class Focusseur extends Entity implements MobSpawner, Carriable, Actionnable, BeamAware {

	Beam horizontalInBeam;
	Beam verticalInBeam;
	
	Beam horizontalBeam;
	Beam verticalBeam;
	
	private boolean canSpawn = false;
	private int spawnDelay = 200;
	private int spawnTimer = 0;
	
	public Focusseur(GameLevel level, int xx, int yy) {
		super(level, xx, yy);
		
		horizontalInBeam = null;
		verticalInBeam = null;
		
		horizontalBeam = null;
		verticalBeam = null;
	}

	@Override
	public boolean beamCanPass() {
		// TODO Autod-generated method stub
		return false;
	}

	@Override
	public void manageBeamCollision(Beam beam) {
		if(beam != null)
		{
			if(horizontalBeam != null)
			{
				horizontalBeam.setColor(beam.getColor());
			}
			if(verticalBeam != null)
			{
				verticalBeam.setColor(beam.getColor());
			}
		}
		
		if(horizontalInBeam ==null)
		{
			if(beam.getDirection() == Direction.LEFT)
			{
				Beam temp = new Beam(gameLevel, x, y, Direction.LEFT, this, beam.getPower());
				horizontalBeam = temp;
				gameLevel.addEntity(temp);
				
				
				temp.increasePower(1);
				temp.setColor(beam.getColor());
				temp.init();
				
				horizontalInBeam = beam;
			}
			if(beam.getDirection() == Direction.RIGHT)
			{
				Beam temp = new Beam(gameLevel, x, y, Direction.RIGHT, this, beam.getPower());
				horizontalBeam = temp;
				gameLevel.addEntity(temp);
				
				
				temp.increasePower(1);
				temp.setColor(beam.getColor());
				temp.init();
				
				horizontalInBeam = beam;
			}
		}
		if(verticalInBeam == null)
		{
			if(beam.getDirection() == Direction.DOWN)
			{
				Beam temp = new Beam(gameLevel, x, y, Direction.DOWN, this, beam.getPower());
				verticalBeam = temp;
				gameLevel.addEntity(temp);
				
				temp.increasePower(1);
				temp.setColor(beam.getColor());
				temp.init();
				
				verticalInBeam = beam;
			}
			if(beam.getDirection() == Direction.UP)
			{
				Beam temp = new Beam(gameLevel, x, y, Direction.UP, this, beam.getPower());
				verticalBeam = temp;
				gameLevel.addEntity(temp);
				
				temp.increasePower(1);
				temp.setColor(beam.getColor());
				temp.init();
				
				verticalInBeam = beam;
			}
		}
		
	}

	@Override
	public void beamHasStopped(Beam beam) {
		if(horizontalInBeam == beam)
		{
			horizontalBeam.destroy();
			horizontalBeam = null;
			
			horizontalInBeam = null;
		}
		
		if(verticalInBeam == beam)
		{
			verticalBeam.destroy();
			verticalBeam = null;
			
			verticalInBeam = null;
		}
		
	}

	@Override
	public void Activate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AltActivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void carried(Entity carrier) {
		canSpawn = false;
		
		if(horizontalInBeam != null)
		{
			horizontalBeam.destroy();
			horizontalBeam = null;
			
			horizontalInBeam = null;
		}
		
		if(verticalInBeam != null)
		{
			verticalBeam.destroy();
			verticalBeam = null;
			
			verticalInBeam = null;
		}
		
	}
	
	public void dropped()
	{
		canSpawn = true;
	}

	@Override
	public void tick() {

		if(canSpawn)
		{
			if(horizontalBeam != null || verticalBeam != null)
			{
				spawnTimer++;
				
				if(spawnTimer >= spawnDelay)
				{
					spawnTimer = 0;
					spawn();
				}
			}
		}
		
		else
			spawnTimer = 0;
	}

	@Override
	public void render(Graphics g) {
		GameArt.focusseur.Draw(g, x, y);
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	public void spawn()
	{
		if(gameLevel.getNumberOfEntities() > 360)
			return;
		
		int offsetX = TurnSynchroniser.synchedRandom.nextInt(96) - 48;
		int offsetY = TurnSynchroniser.synchedRandom.nextInt(96) - 48;
		
		if(gameLevel.collisionsOnTile(x + offsetX, y + offsetY).size() == 0)
		{
			Mob mob = new Mob(gameLevel, x + offsetX, y + offsetY);
			mob.init();
			
			gameLevel.addEntity(mob);
		}
	}
}
