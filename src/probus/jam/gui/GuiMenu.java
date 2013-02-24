package probus.jam.gui;

import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import probus.jam.gameengine.MouseButtons;

public abstract class GuiMenu extends GuiComponent implements ButtonListener,
		KeyListener {
	protected List<GuiButton> buttons = new ArrayList<GuiButton>();

	protected GuiButton addButton(GuiButton button) {
		buttons .add(button);
		button.addListener(this);
		return button;
	}

	@Override
	public void buttonPressed(GuiButton button) {
		
	}

	@Override
	public void render(Graphics g) {
		for (GuiButton b : buttons) {
			b.render(g);
		}
	}

	@Override
	public void tick(MouseButtons mouse) {
		for (GuiButton b : buttons) {
			b.tick(mouse);
		}
	}
	
	public void addButtonListener(ButtonListener listener) {
		for (GuiButton b : buttons) {
			b.addListener(listener);
		}
	}
}
