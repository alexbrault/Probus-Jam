package probus.jam.levels;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import probus.jam.entity.Entity;
import probus.jam.entity.YOrderComparator;
import probus.jam.entity.buildings.Base;
import probus.jam.entity.buildings.BeamGenerator;
import probus.jam.entity.buildings.Decoration;
import probus.jam.entity.tiles.CollidableSandTile;
import probus.jam.entity.tiles.SandTile;
import probus.jam.entity.tiles.Tile;
import probus.jam.gameengine.GraphicConfiguration;
import probus.jam.gameengine.ProbusJam;
import probus.jam.physics.BoundingBox;

public class GameLevel {

	private static final int TILE_HEIGHT = 32;
	private static final int TILE_WIDTH = 32;
	int width;
	int height;
	
	Tile[][] tiles;
	List<Entity> entities;
	
	public GameLevel(String path)
	{
		entities = new ArrayList<Entity>();
		//entities.add(new Mob(this, 500, 500));
		
		width = 81;
		height = 69;
		
		tiles = new Tile[width][height];
		
		LoadMap(path);
	}
	
	public void LoadMap(String path)
	{
		try
		{
			BufferedImage image = ImageIO.read(ProbusJam.class.getResource(path));
			
			if(image.getColorModel().equals(GraphicConfiguration.config.getColorModel()))
			{
				image.setAccelerationPriority(1.0f);
			}
			else
			{
				BufferedImage new_image = GraphicConfiguration.config.createCompatibleImage(image.getWidth(),image.getHeight(),image.getTransparency());
				
				Graphics2D g2d = (Graphics2D) new_image.getGraphics();
				
				g2d.drawImage(image,0,0,null);
				g2d.dispose();
				
				new_image.setAccelerationPriority(1.0f);
				
				image = new_image;
			}
			
			for(int y = 0; y < image.getHeight(); y++)
			{
				for(int x = 0; x < image.getWidth(); x++)
				{
					tiles[x][y] = LoadTile(x, y, image.getRGB(x, y));
				}
			}
			
			for(int i = 0; i < entities.size(); i++)
			{
				entities.get(i).init();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public int getNumberOfEntities()
	{
		return entities.size();
	}
	
	private Tile LoadTile(int i, int j, int rgb)
	{
		rgb = (rgb & 0xffffff);
		
		if(rgb == 0xffffff) return new SandTile(this, i * TILE_WIDTH, j * TILE_HEIGHT);
		if(rgb == 0x000000) 
			return new CollidableSandTile(this, i * TILE_WIDTH, j * TILE_HEIGHT);
		
		if(rgb == 0xff0000)
		{
			Tile out = new SandTile(this, i * TILE_WIDTH, j * TILE_HEIGHT);
			
			BeamGenerator gen = new BeamGenerator(this, i * TILE_WIDTH, j * TILE_HEIGHT);
			
			entities.add(gen);
			out.entityOnTile = gen;
			return out;
		}
		
		if(rgb == 0x0800FF){
			Tile out = new SandTile(this, i * TILE_WIDTH, j * TILE_HEIGHT);
			
			Decoration decoration = new Decoration(this, i * TILE_WIDTH, j * TILE_HEIGHT, 0);
			
			entities.add(decoration);
			out.entityOnTile = decoration;
			return out;
			
		}
		
		if(rgb == 0x54FF00){
			Tile out = new SandTile(this, i * TILE_WIDTH, j * TILE_HEIGHT);
			
			Decoration decoration = new Decoration(this, i * TILE_WIDTH, j * TILE_HEIGHT, 1);
			
			entities.add(decoration);
			out.entityOnTile = decoration;
			return out;
			
		}
		
		if(rgb == 0x9B7FFF){
			Tile out = new SandTile(this, i * TILE_WIDTH, j * TILE_HEIGHT);
			
			Decoration decoration = new Decoration(this, i * TILE_WIDTH, j * TILE_HEIGHT, 2);
			
			entities.add(decoration);
			out.entityOnTile = decoration;
			return out;
			
		}
		
		if(rgb == 0x51FF99){
			Tile out = new SandTile(this, i * TILE_WIDTH, j * TILE_HEIGHT);
			
			Decoration decoration = new Decoration(this, i * TILE_WIDTH, j * TILE_HEIGHT, 3);
			
			entities.add(decoration);
			out.entityOnTile = decoration;
			return out;
			
		}
		
		if(rgb == 0xFF8800){
			Tile out = new SandTile(this, i * TILE_WIDTH, j * TILE_HEIGHT);
			
			Decoration decoration = new Decoration(this, i * TILE_WIDTH, j * TILE_HEIGHT, 4);
			
			entities.add(decoration);
			out.entityOnTile = decoration;
			return out;
			
		}
		
		if(rgb == 0xFFC49B){
			Tile out = new SandTile(this, i * TILE_WIDTH, j * TILE_HEIGHT);
			
			Decoration decoration = new Decoration(this, i * TILE_WIDTH, j * TILE_HEIGHT, 5);
			
			entities.add(decoration);
			out.entityOnTile = decoration;
			return out;
			
		}
		if(rgb == 0xff00ff || rgb == 0xffff00)
		{
			Tile out = new SandTile(this, i * TILE_WIDTH, j * TILE_HEIGHT);
			
			Base base = null;
			
			if(rgb == 0xff00ff) base = new Base(this, i * TILE_WIDTH, j * TILE_HEIGHT, true);
			if(rgb == 0xffff00) base = new Base(this, i * TILE_WIDTH, j * TILE_HEIGHT, false);
			
			entities.add(base);
			out.entityOnTile = base;
			return out;
		}
		
		return new SandTile(this, i * TILE_WIDTH, j * TILE_HEIGHT);
		
	}
	
	public void addEntity(Entity e)
	{
		if(e != null)
			entities.add(e);
		
		System.out.println(entities.size());
	}
	
	public void removeEntity(Entity e)
	{
		entities.remove(e);
		System.out.println(entities.size());
	}
	
	public void tick()
	{
		ArrayList<Entity> entitiesToTick = new ArrayList<Entity>(entities);
		ArrayList<Entity> players = new ArrayList<Entity>();
		
		for(int i = 0; i < entitiesToTick.size(); i++)
		{
			if(entitiesToTick.get(i) instanceof probus.jam.entity.Player)
			{
				players.add(entitiesToTick.get(i));
			}
			else
			{
				entitiesToTick.get(i).tick();
			}
		}
		
		for(int i = 0; i < players.size();i++)
		{
			players.get(i).tick();
		}
		

	}
	
	public ArrayList<BoundingBox> getCollisions(BoundingBox box)
	{
		ArrayList<BoundingBox> collided = new ArrayList<BoundingBox>();

		// Tiles
		for(int i = 0; i < tiles.length; i++)
		{
			for(int j = 0; j < tiles[i].length; j++)
			{
				BoundingBox bb = tiles[i][j].getBox();
				
				if(bb != null && box.Collided(bb) && bb != box)
				{
					collided.add(bb);
				}
			}
		}
		
		// Entities
		for(int i = 0; i < entities.size(); i++)
		{
			BoundingBox bb = entities.get(i).getBox();
			
			if(bb != null && box.Collided(bb) && bb != box)
			{
				collided.add(bb);
			}
		}
		
		return collided;
	}
	
	public boolean hasCollided(BoundingBox box)
	{
		boolean collision = false;
		
		for(int i = 0; i<tiles.length;i++)
		{
			for(int j = 0; j < tiles[i].length;j++)
			{
				BoundingBox bb = tiles[i][j].getBox();
				
				if(bb!=null && bb.Collided(box)&& bb != box )
				{
					collision = true;
				}
			}
		}
		
		for(int i = 0; i<entities.size();i++)
		{
			BoundingBox bb = entities.get(i).getBox();
			
			if(bb!=null && bb.Collided(box)&& bb != box)
			{
				collision = true;
			}
		}
		
		return collision;
	}
	
	public Tile getCurrentTile(BoundingBox box)
	{
		int centerX = box.getCenterX();
		int centerY = box.getCenterY();
		
		return getTileAtPosition(centerX, centerY);
	}
	
	public ArrayList<Entity> collisionsOnTile(int x, int y)
	{
		ArrayList<Entity> collisions = new ArrayList<Entity>();
		
		BoundingBox bb = new BoundingBox(null, x, y, 32, 32);
		
		int i = x / 32;
		int j = y / 32;
		
		for(int a = 0; a < entities.size(); a++)
		{
			if(bb.Collided(entities.get(a).getBox()))
			{
				collisions.add(entities.get(a));
			}
		}
		
		return collisions;
	}
	
	public ArrayList<Entity> collisionsOnTile(int x, int y, int offsetX, int offsetY, int width, int height)
	{
		ArrayList<Entity> collisions = new ArrayList<Entity>();
		
		BoundingBox bb = new BoundingBox(null, x + offsetX, y + offsetY, width, height);
		
		int i = x / 32;
		int j = y / 32;
		
		for(int a = 0; a < entities.size(); a++)
		{
			if(bb.Collided(entities.get(a).getBox()))
			{
				collisions.add(entities.get(a));
			}
		}
		
		return collisions;
	}
	
	public Tile getTileAtPosition(int x, int y)
	{		
		int i = x / 32;
		int j = y / 32;
		
		if(i >= 0 && i < tiles.length)
		{
			if(j >= 0 && j < tiles[i].length)
			{
				return tiles[i][j];
			}
		}
		
		return null;
	}
	
	public void render(Graphics g)
	{
		for(int i = 0; i < tiles.length; i++)
		{
			for(int j = 0; j < tiles[i].length; j++)
			{
				tiles[i][j].render(g);
			}
		}
		
		Collections.sort(entities, new YOrderComparator());
		
		for(int i = 0; i < entities.size(); i++)
		{
			entities.get(i).render(g);
		}
		
		// Debug
		/*
		for(int i = 0; i < tiles.length; i++)
		{
			for(int j = 0; j < tiles[i].length; j++)
			{
				tiles[i][j].debugRender(g);
			}
		}
		
		for(int i = 0; i < entities.size(); i++)
		{
			entities.get(i).debugRender(g);
		}*/
	}
}
