package probus.jam.gameengine;

import java.util.ArrayList;
import java.util.List;

public class Keys {
	public final class Key {
		private KeyState nextState = KeyState.UP;
		private KeyState currentState = KeyState.UP;
		private KeyState lastState = KeyState.UP;
		
		public Key() {
			all.add(this);
		}
		
		public void tick() {
			lastState = currentState;
			currentState = nextState;
		}
		
		public boolean wasJustPressed() {
			return lastState != currentState && currentState == KeyState.DOWN;
		}
		
		public boolean wasJustReleased() {
			return lastState != currentState && currentState == KeyState.UP;			
		}
		
		public boolean isDown() {
			return currentState == KeyState.DOWN;
		}
		
		public boolean willToggleOnNextTick() {
			return currentState != nextState;
		}
		
		public void press() {
			nextState = KeyState.DOWN;
		}
		
		public void release() {
			nextState = KeyState.UP;
		}
		
		public void toggle() {
			nextState = nextState == KeyState.DOWN ? KeyState.UP : KeyState.DOWN;
		}
	}
	
	private List<Key> all = new ArrayList<Key>();
	
	public Key invincible = new Key();
	public Key playerCollision = new Key();
	
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	
	public Key fireUp = new Key();
	public Key fireDown = new Key();
	public Key fireLeft = new Key();
	public Key fireRight = new Key();
	
	public Key take = new Key();
	public Key action = new Key();
	public Key altAction = new Key();
	
	public Key close = new Key();
	
	public void tick() {
		for (Key k : all) {
			k.tick();
		}
	}
	
	public void releaseAll() {
		for (Key k : all) {
			k.release();
		}
	}
	
	public final List<Key> getAll() {
		return all;
	}
}

enum KeyState {
	DOWN,
	UP,
}