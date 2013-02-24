package probus.jam.entity.buildings;

import java.awt.Graphics;

import probus.jam.entity.Beam;
import probus.jam.entity.Entity;
import probus.jam.graphics.GameArt;
import probus.jam.levels.GameLevel;

public class Lens extends Entity implements Carriable, Actionnable, BeamAware {

	Beam horizontalInBeam;
	Beam verticalInBeam;
	
	Beam horizontalBeam;
	Beam verticalBeam;
	
	private int color = Beam.GREEN;
	
	public Lens(GameLevel level, int xx, int yy) {
		super(level, xx, yy);
		
		horizontalInBeam = null;
		verticalInBeam = null;
		
		horizontalBeam = null;
		verticalBeam = null;
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean beamCanPass() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void Activate() {
		
		if(color == Beam.GREEN) color = Beam.YELLOW;
		else if(color == Beam.YELLOW) color = Beam.RED;
		else if(color == Beam.RED) color = Beam.BLUE;
		else if(color == Beam.BLUE) color = Beam.GREEN;
		
		if(horizontalBeam != null) horizontalBeam.setColor(color);
		if(verticalBeam != null) verticalBeam.setColor(color);
	}

	@Override
	public void AltActivate() {
		if(color == Beam.GREEN) color = Beam.BLUE;
		else if(color == Beam.YELLOW) color = Beam.GREEN;
		else if(color == Beam.RED) color = Beam.YELLOW;
		else if(color == Beam.BLUE) color = Beam.RED;
		
		if(horizontalBeam != null) horizontalBeam.setColor(color);
		if(verticalBeam != null) verticalBeam.setColor(color);
	}
	
	@Override
	public void manageBeamCollision(Beam beam) {
		
		if(horizontalInBeam == null)
		{
			if(beam.getDirection() == Direction.LEFT)
			{
				Beam temp = new Beam(gameLevel, x, y, Direction.LEFT, this, 1);
				temp.init();
				
				horizontalBeam = temp;
				horizontalBeam.setColor(color);
				gameLevel.addEntity(temp);				
				horizontalInBeam = beam;
			}
			if(beam.getDirection() == Direction.RIGHT)
			{
				Beam temp = new Beam(gameLevel, x, y, Direction.RIGHT, this, 1);
				temp.init();
				
				horizontalBeam = temp;
				horizontalBeam.setColor(color);
				gameLevel.addEntity(temp);				
				horizontalInBeam = beam;
			}
		}
		
		if(horizontalBeam != null)
		{
			horizontalBeam.setColor(color);
		}
		
		if(verticalInBeam == null)
		{
			if(beam.getDirection() == Direction.DOWN)
			{
				Beam temp = new Beam(gameLevel, x, y, Direction.DOWN, this, 1);
				temp.init();
				
				verticalBeam = temp;
				verticalBeam.setColor(color);
				gameLevel.addEntity(temp);
				verticalInBeam = beam;
			}
			if(beam.getDirection() == Direction.UP)
			{
				Beam temp = new Beam(gameLevel, x, y, Direction.UP, this, 1);
				temp.init();
				
				verticalBeam = temp;
				verticalBeam.setColor(color);
				gameLevel.addEntity(temp);
				verticalInBeam = beam;
			}
		}
		
		if(verticalBeam != null)
		{
			verticalBeam.setColor(color);
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
	
	public void carried(Entity carrier)
	{
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
		/*
		if(color == Beam.GREEN) color = Beam.BLUE;
		else if(color == Beam.YELLOW) color = Beam.GREEN;
		else if(color == Beam.RED) color = Beam.RED;
		else if(color == Beam.BLUE) color = Beam.BLUE;*/
		
		if(horizontalBeam != null) horizontalBeam.setColor(color);
		if(verticalBeam != null) verticalBeam.setColor(color);
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
	}

	@Override
	public void render(Graphics g) {
		if(color == Beam.GREEN) GameArt.lens.Draw(g, 1, 0, x, y);
		else if(color == Beam.YELLOW) GameArt.lens.Draw(g, 0, 0, x, y);
		else if(color == Beam.RED) GameArt.lens.Draw(g, 3, 0, x, y);
		else if(color == Beam.BLUE) GameArt.lens.Draw(g, 2, 0, x, y);
	}

}
