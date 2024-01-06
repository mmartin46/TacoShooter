package interfaces;

import java.io.InputStream;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

public abstract class OnScreenSetting {
	public Text textGraphic;
	public Font textFont;
	
	public Font loadFont(String fontPath, int fontSize) {
		try (InputStream iStream = getClass().getResourceAsStream(fontPath)) {
			return Font.loadFont(iStream, fontSize);
		} catch (Exception e) {
			// Couldn't load the font.
			System.err.println("loadFont(): Couldn't find font.");
			return Font.font("Arial", fontSize);
		}
	}

	public void setupFontGraphic(int x, int y) {
		textGraphic.setLayoutX(x);
		textGraphic.setLayoutY(y);
	}
	
	public abstract void updateFontText();
	public abstract void update();
}
