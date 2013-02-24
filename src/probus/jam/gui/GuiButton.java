package probus.jam.gui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import probus.jam.gameengine.MouseButtons;
import probus.jam.graphics.GameArt;

public class GuiButton extends GuiComponent {

	private final int y;
	private final int x;
	private final int id;
	
	public static final int WIDTH = 256;
	public static final int HEIGHT = 32;
	
	private boolean isPressed;
	private boolean performClick = false;
	private String msg;
	private List<ButtonListener> listeners;
	

	public GuiButton(int id, String msg, int x, int y) {
		this.id = id;
		this.msg = msg;
		this.y = y;
		this.x = x;
	}

	@Override
	public void render(Graphics g) {
		if (!isPressed) {
			GameArt.button.Draw(g, 0, 0, x, y);
		}
		else {
			GameArt.button.Draw(g, 0, 1, x, y);			
		}
		
		int xx = (WIDTH - Font.getStringWidth(msg)) / 2;
		int yy = (HEIGHT - 16) / 2;
		Font.render(g, msg , xx + x, yy + y);
	}

	@Override
	public void tick(MouseButtons mouse) {
		int mx = mouse.getX();
		int my = mouse.getY();
		isPressed = false;
		if (mx >= x && my >= y && mx < (x + WIDTH) && my < (y + HEIGHT)) {
			if (mouse.wasJustReleased(1)) {
				postClick();
			}
			else if (mouse.isDown(1)) {
				isPressed = true;
			}
		}
		
		if (performClick) {
			if (listeners != null) {
				for (ButtonListener listener : listeners) {
					listener.buttonPressed(this);
				}
			}
			performClick = false;
		}
	}

	public void postClick() {
		performClick = true;
	}

	public boolean isPressed() {
		return isPressed;
	}
	
	public void addListener(ButtonListener listener) {
		if (listeners == null) {
			listeners = new ArrayList<ButtonListener>();
		}
		
		listeners.add(listener);
	}
	
	public int getId() {
		return id;
	}
}
