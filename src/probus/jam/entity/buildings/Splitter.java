package probus.jam.entity.buildings;

import java.awt.Graphics;
import java.util.ArrayList;

import probus.jam.entity.Beam;
import probus.jam.entity.Entity;
import probus.jam.graphics.GameArt;
import probus.jam.levels.GameLevel;

public class Splitter extends Entity implements Carriable, Actionnable, BeamAware {

	private Beam incommingBeam = null;
	private ArrayList<Beam> reflectedBeam;
	
	public Splitter(GameLevel level, int xx, int yy) {
		super(level, xx, yy);
		direction = Direction.UP;
		reflectedBeam = new ArrayList<Beam>();
	}
	
	public void init()
	{
		
	}
	
	@Override
	public boolean beamCanPass() {
		return false;
	}

	@Override
	public void manageBeamCollision(Beam beam) {
		
		if(beam != null)
		{
			for(int i = 0; i < reflectedBeam.size(); i++)
			{
				if(reflectedBeam.get(i) != null)
				{
					reflectedBeam.get(i).setColor(beam.getColor());
					reflectedBeam.get(i).setPower(beam.getPower());
				}
			}
		}
		
		boolean managed = false;
		if(beam == incommingBeam) return;
		
		if(direction == Direction.UP)
		{
			if(beam.getDirection() == Direction.DOWN)
			{
				managed = true;
				Beam temp = new Beam(gameLevel, x, y, Direction.LEFT, this,beam.getPower());
				temp.init();
				reflectedBeam.add(temp);
				gameLevel.addEntity(temp);
				
				
				temp = new Beam(gameLevel, x, y, Direction.RIGHT, this,beam.getPower());
				temp.init();
				reflectedBeam.add(temp);
				gameLevel.addEntity(temp);
				
				incommingBeam = beam;
			}
		}
		else if(direction == Direction.RIGHT)
		{
			if(beam.getDirection() == Direction.LEFT)
			{
				managed = true;
				Beam temp = new Beam(gameLevel, x, y, Direction.UP, this,beam.getPower());
				temp.init();
				reflectedBeam.add(temp);
				gameLevel.addEntity(temp);
				
				temp = new Beam(gameLevel, x, y, Direction.DOWN, this,beam.getPower());
				temp.init();
				reflectedBeam.add(temp);
				gameLevel.addEntity(temp);				
				
				incommingBeam = beam;
			}
		}
		else if(direction == Direction.DOWN)
		{
			if(beam.getDirection() == Direction.UP)
			{
				managed = true;
				Beam temp = new Beam(gameLevel, x, y, Direction.LEFT, this,beam.getPower());
				temp.init();
				reflectedBeam.add(temp);
				gameLevel.addEntity(temp);
				
				temp = new Beam(gameLevel, x, y, Direction.RIGHT, this,beam.getPower());
				temp.init();
				reflectedBeam.add(temp);
				gameLevel.addEntity(temp);
				
				incommingBeam = beam;
			}
		}
		else if(direction == Direction.LEFT)
		{
			if(beam.getDirection() == Direction.RIGHT)
			{
				managed = true;
				Beam temp = new Beam(gameLevel, x, y, Direction.UP, this,beam.getPower());
				temp.init();
				reflectedBeam.add(temp);
				gameLevel.addEntity(temp);
				
				temp = new Beam(gameLevel, x, y, Direction.DOWN, this,beam.getPower());
				temp.init();
				reflectedBeam.add(temp);
				gameLevel.addEntity(temp);				
				
				incommingBeam = beam;
			}
		}
		if(!managed)
			for(int i = 0; i < reflectedBeam.size(); i++)
			{
				System.out.println("Woot");
				reflectedBeam.get(i).destroy();
			}
	}

	@Override
	public void beamHasStopped(Beam beam) {
		
		if(beam == incommingBeam)
		{
			cleanup();
		}
	}

	@Override
	public void Activate() {
		
		cleanup();
		
		if(direction == Direction.LEFT) direction = Direction.UP;
		else if(direction == Direction.RIGHT) direction = Direction.DOWN;
		else if(direction == Direction.UP) direction = Direction.RIGHT;
		else if(direction == Direction.DOWN) direction = Direction.LEFT;
	}

	@Override
	public void AltActivate() {
		cleanup();
		
		if(direction == Direction.LEFT) direction = Direction.DOWN;
		else if(direction == Direction.RIGHT) direction = Direction.UP;
		else if(direction == Direction.UP) direction = Direction.LEFT;
		else if(direction == Direction.DOWN) direction = Direction.RIGHT;
	}

	@Override
	public void carried(Entity carrier) {
		cleanup();
	}
	
	public void dropped()
	{
		
	}

	private void cleanup() {
		if(reflectedBeam.size()>0)
		{
			for(int i = 0; i< reflectedBeam.size(); i++)
			{
				reflectedBeam.get(i).destroy();
			}
			
			reflectedBeam.clear();
			
			incommingBeam = null;
		
		}
		System.out.println("CleanedUp!");
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}
	
	

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		if(direction == Direction.LEFT) GameArt.splitter.Draw(g, 0, 2, x, y);
		else if(direction == Direction.RIGHT) GameArt.splitter.Draw(g, 0, 3, x, y);
		else if(direction == Direction.UP) GameArt.splitter.Draw(g, 0, 0, x, y);
		else if(direction == Direction.DOWN) GameArt.splitter.Draw(g, 0, 1, x, y);
		
	}
	
	
}
