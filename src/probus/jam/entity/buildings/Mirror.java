package probus.jam.entity.buildings;

import java.awt.Graphics;

import probus.jam.entity.Beam;
import probus.jam.entity.Entity;
import probus.jam.gameengine.Sound;
import probus.jam.graphics.GameArt;
import probus.jam.levels.GameLevel;
import probus.jam.physics.BoundingBox;

public class Mirror extends Entity implements Carriable, Actionnable, BeamAware {

	private Beam reflectedBeam = null;
	private Beam incomingBeam = null;
	
	public Mirror(GameLevel level, int xx, int yy)
	{
		super(level, xx, yy);
		direction = Direction.DOWN;
	}
	
	public void init()
	{
		
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
	}

	@Override
	public void render(Graphics g) {
		
		if(direction == Direction.LEFT) GameArt.mirror.Draw(g, 1, 0, x, y);
		else if(direction == Direction.RIGHT) GameArt.mirror.Draw(g, 3, 0, x, y);
		else if(direction == Direction.UP) GameArt.mirror.Draw(g, 2, 0, x, y);
		else if(direction == Direction.DOWN) GameArt.mirror.Draw(g, 0, 0, x, y);
	}

	@Override
	public void Activate() {
		
		if(reflectedBeam != null) {
			reflectedBeam.destroy();
			reflectedBeam = null;
		}
		incomingBeam = null;
		
		if(direction == Direction.LEFT) direction = Direction.UP;
		else if(direction == Direction.RIGHT) direction = Direction.DOWN;
		else if(direction == Direction.UP) direction = Direction.RIGHT;
		else if(direction == Direction.DOWN) direction = Direction.LEFT;
		
		Sound.turnMiror.Play();
	}
	
	public void AltActivate() {
		
		if(reflectedBeam != null) {
			reflectedBeam.destroy();
			reflectedBeam = null;
		}
		incomingBeam = null;
		
		if(direction == Direction.LEFT) direction = Direction.DOWN;
		else if(direction == Direction.RIGHT) direction = Direction.UP;
		else if(direction == Direction.UP) direction = Direction.LEFT;
		else if(direction == Direction.DOWN) direction = Direction.RIGHT;
	}
	
	public void carried(Entity carrier)
	{
		if(reflectedBeam != null) {
			reflectedBeam.destroy();
			reflectedBeam = null;
		}		
		incomingBeam = null;
	}

	@Override
	public void manageBeamCollision(Beam beam) {
		if(reflectedBeam != null && beam!= null)
		{
			reflectedBeam.setColor(beam.getColor());
			reflectedBeam.setPower(beam.getPower());
		}
		if (beam == incomingBeam) return;
		
		boolean shouldReflect = false;
		Direction beamDirection = Direction.LEFT;
		
		if(direction == Direction.LEFT)
		{
			if(beam.getDirection() == Direction.RIGHT)
			{
				shouldReflect = true;
				beamDirection = Direction.DOWN;
			}
			
			else if(beam.getDirection() == Direction.UP)
			{
				shouldReflect = true;
				beamDirection = Direction.LEFT;
			}
		}
		else if(direction == Direction.RIGHT)
		{
			if(beam.getDirection() == Direction.LEFT)
			{
				shouldReflect = true;
				beamDirection = Direction.UP;
			}
			
			else if(beam.getDirection() == Direction.DOWN)
			{
				shouldReflect = true;
				beamDirection = Direction.RIGHT;
			}
		}
		else if(direction == Direction.UP)
		{
			if(beam.getDirection() == Direction.RIGHT)
			{
				shouldReflect = true;
				beamDirection = Direction.UP;
			}
			
			else if(beam.getDirection() == Direction.DOWN)
			{
				shouldReflect = true;
				beamDirection = Direction.LEFT;
			}
		}
		else if(direction == Direction.DOWN)
		{
			if(beam.getDirection() == Direction.LEFT)
			{
				shouldReflect = true;
				beamDirection = Direction.DOWN;
			}
			
			else if(beam.getDirection() == Direction.UP)
			{
				shouldReflect = true;
				beamDirection = Direction.RIGHT;
			}
		}
		
		if(shouldReflect)
		{
			if(reflectedBeam != null)
				reflectedBeam.destroy();
			
			reflectedBeam = new Beam(gameLevel, x, y, beamDirection, this, beam.getPower());
			incomingBeam = beam;
			gameLevel.addEntity(reflectedBeam);
			
			reflectedBeam.init();
		}
	}

	@Override
	public boolean beamCanPass() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void beamHasStopped(Beam beam) {
		if (beam == incomingBeam) {
			if (reflectedBeam != null) {
				reflectedBeam.destroy();
			}
		}
	}
}
