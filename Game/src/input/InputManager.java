package input;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class InputManager {
	private static InputManager instance;
	private boolean[] keys;
	private final int NUM_KEYS = 256;
	
	private InputManager() {
		// Initializes all the keys
		keys = new boolean[NUM_KEYS];
	}
	
	// Returns an instance of the input manager.
	public static InputManager getSingleton() {
		if (instance == null) {
			instance = new InputManager();
		}
		return instance;
	}
	
	// Initialize the InputManager with the scene.
	public void initialize(Scene scene) {
		scene.setOnKeyPressed(e -> keys[e.getCode().ordinal()] = true);
		scene.setOnKeyReleased(e -> keys[e.getCode().ordinal()] = false);
	}
	
	// Checks if a key was pressed
	public boolean isKeyPressed(KeyCode key) {
		return keys[key.ordinal()];
	}
}
