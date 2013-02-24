package probus.jam.entity;

import probus.jam.entity.tiles.Tile;
import probus.jam.gameengine.Keys;
import probus.jam.gameengine.ProbusJam;
import probus.jam.gameengine.Sound;
import probus.jam.graphics.*;
import probus.jam.gui.Font;
import probus.jam.levels.GameLevel;
import probus.jam.physics.BoundingBox;

import java.awt.Graphics;
import java.util.ArrayList;

public class Player extends MovingEntity {

	private int respawnX;
	private int respawnY;
	private int respawnDelay = 300;
	private int respawnTimer;
	private boolean death = false;
	
	private int shootingTicksDelay = 12;
	private int shootingTick = 0;
	private int walkingTicksDelay = 10;
	private int walkingTick = 0;
	private boolean shooted = false;
	
	private Direction shootingDirection = Direction.UP;
	private Keys keys;
	
	private Entity objectHeld = null;
	
	private int maxHealth = 50;
	private int health = 50;

	private boolean invincible = false;
	private boolean ignoreCollisions = false;
	private boolean canTakeDamage = true;
	private int invincibilityDelay = 4;
	private int invincibilityTime = 0;
	
	private boolean cameraFollow;
	public SpriteSheet sheet = GameArt.player1;

	public Player(GameLevel level, Keys k, int xx, int yy, boolean thisPlayer)
	{
		super(level, xx, yy);
		
		respawnX = xx;
		respawnY = yy;
		
		cameraFollow = thisPlayer;
		
		gameLevel = level;
		
		walkTime = 0;
		keys = k;
		
		speed = 4;
		//speed = 6;
	}
	
	public void init()
	{
		bBox = new BoundingBox(this, x, y, 32, 48);
	}
	
	public void render(Graphics g)
	{
		if(death && cameraFollow )
		{
			int w = Font.getStringWidth("Waiting to respawn");
			int dx = x - (w * 2);
			int dy = y - 8;
			
			Font.render(g, "Waiting to respawn", dx, dy);
			return;
		}
		
		if(!canTakeDamage)
		{
			if(invincibilityTime % 4 == 1)
			{
				return;
			}
		}
		
		int spriteDirection = 0;
		spriteX = (walkTime / 8 % 4);
		
		switch(direction)
		{
		case LEFT:
			spriteDirection = 2;
			break;
			
		case RIGHT:
			spriteDirection = 3;
			break;
			
		case UP:
			spriteDirection = 0;
			break;
			
		case DOWN:
			spriteDirection = 1;
			break;
		}
		
		sheet.Draw(g, spriteX, spriteDirection, x, y);
		
		if (cameraFollow && objectHeld != null) {
			Tile front = getTileInFront();
			g.drawRect(front.x, front.y, front.width, front.height);
		}
	}
	
	public void tick()
	{
		/*==
		if(keys.invincible.wasJustPressed())
			invincible = !invincible;
		
		if(keys.playerCollision.wasJustPressed())
			ignoreCollisions = !ignoreCollisions;*/
		
		if(death)
		{
			respawnTimer++;
			
			if(respawnTimer >= respawnDelay)
				respawn();
		}
		
		else
		{
			move();
			walkTime++;
			
			action();
			
			if(objectHeld == null)
				fire();
			
			if(!canTakeDamage)
			{
				invincibilityTime++;
				
				if(invincibilityTime >= invincibilityDelay)
				{
					invincibilityTime = 0;
					canTakeDamage = true;
				}
			}
		}
	}
	
	private void respawn()
	{
		death = false;
		health = maxHealth;
		respawnTimer = 0;
	}
	
	private void move()
	{
		int beginX = x;
		int beginY = y;
		
		int moveX = 0;
		int moveY = 0;
		
		if(keys.left.isDown()) moveX--;
		if(keys.right.isDown()) moveX++;
		if(keys.up.isDown()) moveY--;
		if(keys.down.isDown()) moveY++;
		
		if(moveX < 0){ direction = Direction.LEFT; x -= speed; }
		if(moveX > 0){ direction = Direction.RIGHT; x += speed; }
		if(moveY < 0){ direction = Direction.UP; y -= speed; }
		if(moveY > 0){ direction = Direction.DOWN; y += speed; }
		
		bBox.UpdatePosition(x, y);
		
		if(!ignoreCollisions)
		{
			ArrayList<BoundingBox> collsisions = gameLevel.getCollisions(bBox);
			
			for(int i = 0; i < collsisions.size(); i++)
			{
				if(collsisions.get(i).getEntity() instanceof probus.jam.entity.Bullet)
				{
					gameLevel.removeEntity( collsisions.get(i).getEntity() );
					takeDamage(2);
				}
				
				else
				{
					if(collsisions.get(i).getEntity() instanceof probus.jam.entity.Mob)
						takeDamage(2);
					
					int initialX = x;
	
					x = beginX;
					bBox.UpdatePosition(x, y);
					if(gameLevel.hasCollided(bBox))
					{
						y = beginY;
					}
					
					x=initialX;
					bBox.UpdatePosition(x,y);
					if(gameLevel.hasCollided(bBox))
					{
						x=beginX;
					}
				}
			}
		}
		
		bBox.UpdatePosition(x, y);
		
		if(cameraFollow)
		{
			ProbusJam.camera.x = ProbusJam.VIEWPORT_WIDTH / 2 - x - 16;
			ProbusJam.camera.y = ProbusJam.VIEWPORT_HEIGHT / 2 - y - 16;
		}
		
		if(shooted == true)
			direction = shootingDirection;
		
		if(moveX == 0 && moveY == 0)
			walkTime = 0;
	
			
		
		if(walkingTick >= walkingTicksDelay){
			//TO DO: verify
			//Sound.walk.Play();
			walkingTick = 0;
		}
		
		if(objectHeld != null)
		{
			objectHeld.setPosition(x, y);
		}
	}
	
	public void kill() {
		
		if(objectHeld != null)
			tryDropObject();
		
		death = true;
		x = respawnX;
		y = respawnY;
		
		bBox.UpdatePosition(x, y);
		
		if(cameraFollow)
		{
			ProbusJam.camera.x = ProbusJam.VIEWPORT_WIDTH / 2 - x - 16;
			ProbusJam.camera.y = ProbusJam.VIEWPORT_HEIGHT / 2 - y - 16;
		}
	}
	
	public void heal()
	{
		health = maxHealth;
	}
	
	public void takeDamage(int damage)
	{
		if(canTakeDamage)
		{
			if(!invincible)
				health -= damage;
			
			if(health <= 0)
				kill();
			
			canTakeDamage = false;
		}
	}

	private void fire()
	{
		if(shooted == true)
		{
			shootingTick++;
			
			if(shootingTick >= shootingTicksDelay)
			{
				shooted = false;
			}
		}
		
		if(shooted == false)
		{
			Bullet temp = null;
			if(keys.fireUp.isDown()){temp = new Bullet(gameLevel, x + 8, y - 24, 0, -2); shootingDirection = Direction.UP; shooted = true;}
			else if(keys.fireDown.isDown()){temp =  new Bullet(gameLevel, x + 8, y + 56, 0, 2); shootingDirection = Direction.DOWN; shooted = true;}
			else if(keys.fireLeft.isDown()){temp =  new Bullet(gameLevel, x - 24, y + 16, -2, 0); shootingDirection = Direction.LEFT; shooted = true;}
			else if(keys.fireRight.isDown()){temp =  new Bullet(gameLevel, x + 40, y + 16, 2, 0); shootingDirection = Direction.RIGHT; shooted = true;}
			
				
			if(shooted == true)
				temp.init();
				shootingTick = 0;
		}
	}
	
	private void action()
	{
		if(keys.take.wasJustPressed())
		{
			if(objectHeld == null)
			{
				tryTakeObject();
			}
			
			else
			{
				tryDropObject();
			}
		}
		
		if(keys.action.wasJustPressed())
		{
			tryActivateObject();
		}
		
		if(keys.altAction.wasJustPressed())
		{
			tryAltActivateObject();
		}
	}
	
	private void tryAltActivateObject()
	{
		Tile tile = gameLevel.getCurrentTile(bBox);
		
		if(tile == null)
			return;
		
		int tx = tile.x + tile.width / 2;
		int ty = tile.y + tile.height / 2;
		
		// Current Tile
		if(tile.entityOnTile != null && tile.entityOnTile instanceof probus.jam.entity.buildings.Actionnable)
		{
			tile.entityOnTile.AltActivate();
			return;
		}
		
		// Next Tile
		if(direction == Direction.LEFT) tx -= tile.width;
		else if(direction == Direction.RIGHT) tx += tile.width;
		else if(direction == Direction.UP) ty -= tile.height;
		else if(direction == Direction.DOWN) ty += tile.height;
		
		tile = gameLevel.getTileAtPosition(tx, ty);
		
		if(tile.entityOnTile != null && tile.entityOnTile instanceof probus.jam.entity.buildings.Actionnable)
		{
			tile.entityOnTile.AltActivate();
		}
	}
	
	private void tryActivateObject()
	{
		Tile tile = gameLevel.getCurrentTile(bBox);
		
		if(tile == null)
			return;
		
		int tx = tile.x + tile.width / 2;
		int ty = tile.y + tile.height / 2;
		
		// Current Tile
		if(tile.entityOnTile != null && tile.entityOnTile instanceof probus.jam.entity.buildings.Actionnable)
		{
			tile.entityOnTile.Activate();
			return;
		}
		
		// Next Tile
		if(direction == Direction.LEFT) tx -= tile.width;
		else if(direction == Direction.RIGHT) tx += tile.width;
		else if(direction == Direction.UP) ty -= tile.height;
		else if(direction == Direction.DOWN) ty += tile.height;
		
		tile = gameLevel.getTileAtPosition(tx, ty);
		
		if(tile.entityOnTile != null && tile.entityOnTile instanceof probus.jam.entity.buildings.Actionnable)
		{
			tile.entityOnTile.Activate();
		}
	}
	
	private void tryTakeObject()
	{
		Tile tile = gameLevel.getCurrentTile(bBox);
		
		if(tile == null)
			return;
		
		int tx = tile.x + tile.width / 2;
		int ty = tile.y + tile.height / 2;
		
		// Current Tile
		if(tile.entityOnTile != null && tile.entityOnTile instanceof probus.jam.entity.buildings.Carriable)
		{
			objectHeld = tile.entityOnTile;
			tile.entityOnTile = null;
			
			objectHeld.carried(this);
			
			Sound.liftItem.Play();

			return;
		}
		
		// Next Tile
		if(direction == Direction.LEFT) tx -= tile.width;
		else if(direction == Direction.RIGHT) tx += tile.width;
		else if(direction == Direction.UP) ty -= tile.height;
		else if(direction == Direction.DOWN) ty += tile.height;
		
		tile = gameLevel.getTileAtPosition(tx, ty);
		

		
		if(tile.entityOnTile != null && tile.entityOnTile instanceof probus.jam.entity.buildings.Carriable)
		{
			objectHeld = tile.entityOnTile;
			tile.entityOnTile = null;

			objectHeld.carried(this);
			Sound.liftItem.Play();
		}
	}
	
	private void tryDropObject()
	{
		Tile tile = getTileInFront();
		
		if(tile != null && tile.entityOnTile == null)
		{
			tile.entityOnTile = objectHeld;
			objectHeld = null;
			
			tile.entityOnTile.x = tile.x;
			tile.entityOnTile.y = tile.y;
			
			tile.entityOnTile.dropped();
			
			Sound.throwItem.Play();
		}
	}
	
	public void emptyHands()
	{
		objectHeld = null;
	}

	private Tile getTileInFront() {
		Tile tile = gameLevel.getCurrentTile(bBox);
		
		if(tile == null)
			return null;
		
		int tx = tile.x + tile.width / 2;
		int ty = tile.y + tile.height / 2;
		
		if(direction == Direction.LEFT) tx -= tile.width;
		else if(direction == Direction.RIGHT) tx += tile.width;
		else if(direction == Direction.UP) ty -= tile.height;
		else if(direction == Direction.DOWN) ty += tile.height;
		
		tile = gameLevel.getTileAtPosition(tx, ty);
		return tile;
	}

	public int getHealth() {
		return health;
	}
	
	public int getMaxHealth() {
		return 50;
	}
}
