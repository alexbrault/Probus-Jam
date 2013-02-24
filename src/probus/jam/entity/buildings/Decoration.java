package probus.jam.entity.buildings;

import java.awt.Graphics;

import probus.jam.entity.Entity;
import probus.jam.graphics.GameArt;
import probus.jam.levels.GameLevel;
import probus.jam.physics.BoundingBox;

public class Decoration extends Entity{

	private int deco;
	
	public Decoration(GameLevel level, int xx, int yy, int decoChoice){
		super(level, xx, yy);
		deco = decoChoice;

	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g) {
		switch(deco)
		{
		case(0):
				GameArt.arbre.Draw(g,  x, y);
				if(bBox == null){
					bBox = new BoundingBox(this, x+32, y+20, 8, 8);
				}
			break;
		case(1):
				GameArt.arbre2.Draw(g, x, y);
				if(bBox == null){
					bBox = new BoundingBox(this, x+12, y+35, 106, 46);
				}
			 break;
		case(2):
				GameArt.arbre3.Draw(g, x, y);
				if(bBox == null){
					bBox = new BoundingBox(this, x+16, y+12, 1, 1);
				}
			break;
		case(3):
				GameArt.rock1.Draw(g,  x, y);
				if(bBox == null){
					bBox = new BoundingBox(this, x+12, y+8, 8 , 1);
				}
			break;
		case(4):
				GameArt.rock2.Draw(g,  x, y);	
					if(bBox == null){
						bBox = new BoundingBox(this, x+8, y+18, 12 , 1);
					}
			break;
			
		case(5):
			GameArt.wall.Draw(g, x, y);	
				if(bBox == null){
					bBox = new BoundingBox(this, x, y, 32 , 32);
				}
		break;
		default:
				GameArt.rock4.Draw(g,  x, y);
			break;
		}
		
	}

	
	
}
