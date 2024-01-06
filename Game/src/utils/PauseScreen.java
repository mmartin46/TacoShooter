package utils;

import java.io.InputStream;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PauseScreen {

	private boolean pauseButtonClicked;
	private Text textGraphic;
	private Font textFont;	
	
	private final int PAUSE_X = (int) (GameConfigurations.SCREEN_WIDTH / 3.1);
	private final int PAUSE_Y = (int) (GameConfigurations.SCREEN_HEIGHT / 1.8);

	public PauseScreen(boolean pauseButtonClicked) {
		this.pauseButtonClicked = pauseButtonClicked;
		
		textGraphic = new Text();
		this.textFont = loadFont("fonts/alagard.ttf", 40);
		
		textGraphic.setFont(this.textFont);
		textGraphic.setFill(Color.LIGHTBLUE);
		setupFontGraphic(PAUSE_X, PAUSE_Y);
	}
	
	private Font loadFont(String fontPath, int fontSize) {
		try (InputStream iStream = getClass().getResourceAsStream(fontPath)) {
			return Font.loadFont(iStream, fontSize);
		} catch (Exception e) {
			// Couldn't load the font.
			System.err.println("loadFont(): Couldn't find font.");
			return Font.font("Arial", fontSize);
		}
	}
	
	
	private void setupFontGraphic(int x, int y) {
		textGraphic.setLayoutX(x);
		textGraphic.setLayoutY(y);
	}
	
	private void updateFontText() {
		this.textGraphic.setText("PAUSED");
	}
	
	public void update() {
		if (pauseButtonClicked) {
			updateFontText();
		}
	}
	
	public void draw(Group group, boolean pauseButtonClicked) {
		if (!pauseButtonClicked) {
			textGraphic.setVisible(true);
		} else {
			textGraphic.setVisible(false);
		}
		if (!group.getChildren().contains(textGraphic)) {
			group.getChildren().add(textGraphic);
		}
	}
}
