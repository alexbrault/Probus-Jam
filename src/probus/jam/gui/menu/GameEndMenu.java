package probus.jam.gui.menu;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import probus.jam.gameengine.ProbusJam;
import probus.jam.graphics.GameArt;
import probus.jam.gui.Font;
import probus.jam.gui.GuiButton;
import probus.jam.gui.GuiMenu;

public class GameEndMenu extends GuiMenu {
	private int victoriousPlayer;
	private int xx;

	public GameEndMenu(int victoriousPlayer) {
		super();
		this.victoriousPlayer = victoriousPlayer;
		
		xx = (ProbusJam.VIEWPORT_WIDTH - GuiButton.WIDTH) / 2;
		addButton(new GuiButton(TitleMenu.RESTART_ID, "Home", xx, 500));
	}

	@Override
	public void render(Graphics g) {
		GameArt.endScreen.Draw(g, 0, 0);
		String msg = "";
		if (victoriousPlayer == 1) {
			msg = "La schtroumpfette est victorieuse!";
		}
		if (victoriousPlayer == 2) {
			msg = "      Miss Scarlet a conquis!";
		}
		Font.render(g, msg, 180, 460);
		super.render(g);
		
		if (victoriousPlayer == 1) {
			GameArt.player1.Draw(g, 0, 1, 140, 445);
		}
		if (victoriousPlayer == 2) {
			GameArt.player2.Draw(g, 0, 1, 750, 445);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER){
			buttons.get(0).postClick();
		}
			
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
