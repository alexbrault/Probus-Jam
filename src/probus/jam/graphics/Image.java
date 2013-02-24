package probus.jam.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import probus.jam.gameengine.GraphicConfiguration;
import probus.jam.gameengine.ProbusJam;

public class Image{
	
	private BufferedImage image = null;
	
	public Image(String path)
	{
		LoadImage(path);
	}
	
	public void LoadImage(String path)
	{
		try
		{
			image = ImageIO.read(ProbusJam.class.getResource(path));
			
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
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void Draw(Graphics g,int x, int y)
	{
		g.drawImage(image,x,y,null);
	}
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}

}