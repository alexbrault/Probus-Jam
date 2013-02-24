package probus.jam.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import probus.jam.gameengine.GraphicConfiguration;
import probus.jam.gameengine.ProbusJam;

public class SpriteSheet {
	
	private class Sprite
	{
		public int X;
		public int Y;
		
		public Sprite(int x, int y)
		{
			X=x;
			Y=y;
		}
	}
		
		private BufferedImage image = null;
		
		private int width;
		private int height;
		private Sprite[][] sprites;
		
		public SpriteSheet(String path, int w, int h)
		{
			LoadImage(path, w, h);
		}
		
		public void LoadImage(String path,int w, int h)
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
				
				sprites = new Sprite[image.getWidth()/w][image.getHeight()/h];
				
				for(int i=0;i<image.getWidth()/w;i++)
				{
					for(int j=0;j<image.getHeight()/h;j++)
					{
						sprites[i][j] = new Sprite(i*w,j*h);
					}
				}
				
				width = w;
				height = h;
			}
			catch(IOException e)
			{}
		}

		public void Draw(Graphics g, int i, int j, int x, int y)
		{
			Sprite sprite = sprites[i][j];
			
			BufferedImage subImage = image.getSubimage(sprite.X,sprite.Y, width,height);
			
			g.drawImage(subImage, x, y, null);
		}
		
		public void Draw(Graphics gr, int i, int j, int x, int y, int colour) {
			Sprite sprite = sprites[i][j];
			BufferedImage subImage = image.getSubimage(sprite.X, sprite.Y, width, height);
			BufferedImage output = new BufferedImage(subImage.getWidth(), subImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
			
			int a2 = (colour >> 24) & 0xff;
			int a1 = 256 - a2;
			
			int rr = colour & 0xff0000;
			int gg = colour & 0xff00;
			int bb = colour & 0xff;
			for (int yy = 0; yy < subImage.getHeight(); ++yy) {
				for (int xx = 0; xx < subImage.getWidth(); ++xx) {
					int col = subImage.getRGB(xx, yy);
					int a = col & 0xff000000;
					int r = col & 0xff0000;
					int g = col & 0xff00;
					int b = col & 0xff;
					
					r = ((r * a1 + rr * a2) >> 8) & 0xff0000;
					g = ((g * a1 + gg * a2) >> 8) & 0xff00;
					b = ((b * a1 + bb * a2) >> 8) & 0xff;
					
					col = a | r | g | b;
					
					output.setRGB(xx, yy, col);
				}
			}

			gr.drawImage(output, x, y, null);			
		}
	}
