package probus.jam.gui.menu;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import probus.jam.gui.Font;
import probus.jam.gui.GuiButton;
import probus.jam.gui.GuiMenu;

public class JoinMenu extends GuiMenu {

	private GuiButton joinButton;
	
	public JoinMenu() {
		super();
		
		joinButton = addButton(new GuiButton(TitleMenu.PERFORM_JOIN_ID, "Join", 100, 180));
		addButton(new GuiButton(TitleMenu.CANCEL_JOIN_ID, "Cancel", 400, 180));
	}
	
	

	@Override
	public void render(Graphics g) {
		Font.render(g, "Enter host:", 100, 100);
		Font.render(g, TitleMenu.serverIp+"_", 100, 120);
		super.render(g);
	}



	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER && !TitleMenu.serverIp.isEmpty()) {
			joinButton.postClick();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE && !TitleMenu.serverIp.isEmpty()) {
			TitleMenu.serverIp = TitleMenu.serverIp.substring(0, TitleMenu.serverIp.length() - 1);
		} else if (Font.canRender(e.getKeyChar())) {
			TitleMenu.serverIp += e.getKeyChar();
		}
	}

}
