package probus.jam.gui.menu;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import probus.jam.gameengine.ProbusJam;
import probus.jam.graphics.GameArt;
import probus.jam.gui.GuiButton;
import probus.jam.gui.GuiMenu;

public class TitleMenu extends GuiMenu {
    public static final int START_GAME_ID = 1000;
    public static final int HOST_GAME_ID = 1002;
    public static final int JOIN_GAME_ID = 1003;
    public static final int EXIT_GAME_ID = 1001;
	public static final int CANCEL_JOIN_ID = 1004;
	public static final int PERFORM_JOIN_ID = 1005;
	public static final int RESTART_ID = 1006;
    
    public static String serverIp = "";
    private int selectedItem = 0;
    private int xx;
    
    public TitleMenu() {
    	super();
    	
    	xx = 500;
    	addButton(new GuiButton(START_GAME_ID, "Start", xx, 100));
    	addButton(new GuiButton(HOST_GAME_ID, "Host", xx, 140));
    	addButton(new GuiButton(JOIN_GAME_ID, "Join", xx, 180));
    	addButton(new GuiButton(EXIT_GAME_ID, "Exit", xx, 220));
    }
    
    public void render(Graphics g) {
    	
    	GameArt.titleScreen.Draw(g, 0, 0);
    	super.render(g);
    	GameArt.generator.Draw(g, xx - 40, 100 + selectedItem * 40);
    }
    
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			selectedItem--;
			if (selectedItem < 0) {
				selectedItem = 3;
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			selectedItem++;
			if (selectedItem > 3) {
				selectedItem = 0;
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			e.consume();
			buttons.get(selectedItem).postClick();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
