package probus.jam.entity.buildings;

import java.awt.Graphics;

import probus.jam.entity.Entity;
import probus.jam.graphics.GameArt;
import probus.jam.levels.GameLevel;

public class Healing extends Entity implements Carriable {

	public Healing(GameLevel level, int xx, int yy) {
		super(level, xx, yy);
		// TODO Auto-generated constructor stub
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

		GameArt.heart.Draw(g, x, y);
	}

	public void carried(Entity carrier) {
		carrier.heal();
		carrier.emptyHands();
		gameLevel.removeEntity(this);
	}
}
