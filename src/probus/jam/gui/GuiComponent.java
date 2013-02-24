package probus.jam.gui;

import java.awt.Graphics;

import probus.jam.gameengine.MouseButtons;

public abstract class GuiComponent {
	public abstract void render(Graphics g);
	public abstract void tick(MouseButtons mouse);
}
