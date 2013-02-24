package probus.jam.gameengine;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class GraphicConfiguration{

	private static GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	private static GraphicsDevice device = env.getDefaultScreenDevice();
	public static GraphicsConfiguration config = device.getDefaultConfiguration();
}