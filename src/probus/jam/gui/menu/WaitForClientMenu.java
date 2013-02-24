package probus.jam.gui.menu;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import probus.jam.gui.Font;
import probus.jam.gui.GuiButton;
import probus.jam.gui.GuiMenu;

public class WaitForClientMenu extends GuiMenu {
	private String ip;
	
	public WaitForClientMenu() {
		super();
		
		try {
			ip = InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException e) {
			ip = "Unknown host";
		}
		
		addButton(new GuiButton(TitleMenu.CANCEL_JOIN_ID, "Cancel", 250, 180));
	}
	
	public void render(Graphics g) {
		Font.render(g, "Waiting for a client...", 100, 100);
		Font.render(g, "Host: " + ip, 100, 120);
		super.render(g);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
