package probus.jam.gameengine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import probus.jam.gameengine.Keys.Key;

public class InputHandler implements KeyListener {
	private Map<Integer, Key> mappings = new HashMap<Integer, Key>();
	
	public InputHandler(Keys keys) {
		mappings.put(KeyEvent.VK_1, keys.invincible);
		mappings.put(KeyEvent.VK_2, keys.playerCollision);
		
		mappings.put(KeyEvent.VK_SPACE, keys.take);
		mappings.put(KeyEvent.VK_E, keys.action);
		mappings.put(KeyEvent.VK_Q, keys.altAction);
		
		mappings.put(KeyEvent.VK_NUMPAD0, keys.take);
		mappings.put(KeyEvent.VK_NUMPAD1, keys.action);
		mappings.put(KeyEvent.VK_NUMPAD3, keys.altAction);
				
		mappings.put(KeyEvent.VK_UP, keys.fireUp);
		mappings.put(KeyEvent.VK_DOWN, keys.fireDown);
		mappings.put(KeyEvent.VK_LEFT, keys.fireLeft);
		mappings.put(KeyEvent.VK_RIGHT, keys.fireRight);

		mappings.put(KeyEvent.VK_W, keys.up);
		mappings.put(KeyEvent.VK_A, keys.left);
		mappings.put(KeyEvent.VK_S, keys.down);
		mappings.put(KeyEvent.VK_D, keys.right);

		mappings.put(KeyEvent.VK_NUMPAD8, keys.up);
		mappings.put(KeyEvent.VK_NUMPAD2, keys.down);
		mappings.put(KeyEvent.VK_NUMPAD4, keys.left);
		mappings.put(KeyEvent.VK_NUMPAD6, keys.right);
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Key k = mappings.get(e.getKeyCode());
		if (k != null) {
			k.press();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Key k = mappings.get(e.getKeyCode());
		if (k != null) {
			k.release();
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
