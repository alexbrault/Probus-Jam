package probus.jam.entity.buildings;

import java.awt.Graphics;

import probus.jam.entity.Beam;
import probus.jam.entity.Entity;
import probus.jam.graphics.GameArt;
import probus.jam.levels.GameLevel;
import probus.jam.physics.BoundingBox;

public class BeamGenerator extends Entity implements BeamAware {

	public BeamGenerator(GameLevel level, int xx, int yy) {
		super(level, xx, yy);
	}
	
	public void init()
	{
		gameLevel.addEntity(new Beam(gameLevel, x, y, Direction.DOWN, this, 1));
		gameLevel.addEntity(new Beam(gameLevel, x, y, Direction.UP, this, 1));
		bBox = new BoundingBox(this, x, y, 32, 32);
		gameLevel.getTileAtPosition(x, y).entityOnTile = this;
	}

	@Override
	public boolean beamCanPass() {
		return false;
	}

	@Override
	public void manageBeamCollision(Beam beam) {
	}

	@Override
	public void beamHasStopped(Beam beam) {
	}

	@Override
	public void tick() {
	}

	@Override
	public void render(Graphics g) {
		GameArt.generator.Draw(g, x, y);
	}

}
