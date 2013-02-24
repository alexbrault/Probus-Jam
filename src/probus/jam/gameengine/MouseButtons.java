package probus.jam.gameengine;

import java.awt.Point;

public class MouseButtons {
	private ButtonState[] currentState = new ButtonState[4];
	private ButtonState[] nextState = new ButtonState[4];
	
	private int x, y;
	
	public void press(int button) {
		nextState[button] = ButtonState.DOWN;
	}

	public void release(int button) {
		nextState[button] = ButtonState.UP;
	}
	
	public boolean wasJustPressed(int button) {
		return currentState[button] != nextState[button] && nextState[button] == ButtonState.DOWN;
	}
	
	public boolean wasJustReleased(int button) {
		return currentState[button] != nextState[button] && nextState[button] == ButtonState.UP;			
	}
	
	public boolean isDown(int button) {
		return currentState[button] == ButtonState.DOWN;
	}
	
	public void releaseAll() {
		for (int i = 0; i < nextState.length; ++i) {
			release(i);
		}
	}
	
	public void tick() {
		for (int i = 0; i < nextState.length; ++i) {
			currentState[i] = nextState[i];
		}
	}
	
	public void setPosition(Point position) {
		if (position != null) {
			x = position.x;
			y = position.y;
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}

enum ButtonState {
	DOWN,
	UP,
}
