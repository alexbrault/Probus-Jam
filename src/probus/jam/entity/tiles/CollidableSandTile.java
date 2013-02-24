package probus.jam.entity.tiles;
	
import probus.jam.entity.Beam;
import probus.jam.entity.buildings.BeamAware;
import probus.jam.graphics.*;
import probus.jam.levels.GameLevel;
import probus.jam.physics.BoundingBox;

import java.awt.Graphics;

public class CollidableSandTile extends Tile implements BeamAware{
	
	public CollidableSandTile(GameLevel level, int xx, int yy)
	{
		super(level, xx,yy);
		bBox = new BoundingBox(this, xx,yy,32,32);
		entityOnTile = this;
	}
	
	public void render(Graphics g)
	{
		GameArt.sandTile.Draw(g, x, y);
	}

	@Override
	public void beamHasStopped(Beam beam) {
	}

	@Override
	public boolean beamCanPass() {
		return false;
	}

	@Override
	public void manageBeamCollision(Beam beam) {
	}
}
