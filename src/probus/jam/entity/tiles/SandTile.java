package probus.jam.entity.tiles;

import probus.jam.graphics.*;
import probus.jam.levels.GameLevel;

import java.awt.Graphics;

public class SandTile extends Tile {

	public SandTile(GameLevel level, int xx, int yy)
	{
		super(level, xx, yy);
	}
	
	public void render(Graphics g)
	{
		GameArt.sandTile.Draw(g, x, y);
	}
}
