package probus.jam.gui;

import java.awt.Graphics;

import probus.jam.graphics.*;


public class Font {
	
	private static String fonts = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"+
								  "0123456789!?().,:-_'\"     ";
	
	private Font()
	{
		
	}
	
	public static boolean canRender(String msg)	{
		msg = msg.toUpperCase();
		for(int i = 0; i < msg.length(); i++)
		{
			int c = fonts.indexOf(msg.charAt(i));
			if(c < 0) return false;
	
		}
			
		return true;
	}
	
	public static int getStringWidth(String msg) {
		int w = 0;
		msg = msg.toUpperCase();
		for(int i = 0; i<msg.length(); i++)
		{
			int c = fonts.indexOf(msg.charAt(i));
			if(c < 0) continue;
			w += 19;

		}
		
		return w;
	}
	
	public static void render(Graphics g,String msg, int xx, int yy)
	{
		int x = xx;
		int y = yy;
		int lenght = 0;
		
		msg = msg.toUpperCase();
		lenght = msg.length();
		
		for(int i = 0; i<lenght; i++)
		{
			int c = fonts.indexOf(msg.charAt(i));
			if(c < 0) continue;
			
			GameArt.font.Draw(g,c%26 ,c/26 , x, y);
			x += 19;

		}
		
		
	}

	public static boolean canRender(char c) {
		return fonts.indexOf(Character.toUpperCase(c)) >= 0;
	}

}
