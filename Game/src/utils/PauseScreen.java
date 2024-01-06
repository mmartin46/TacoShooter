package utils;


import interfaces.OnScreenSetting;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class PauseScreen extends OnScreenSetting {

	private boolean pauseButtonClicked;	
	
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
	

	
	@Override
	public void updateFontText() {
		this.textGraphic.setText("PAUSED");
	}
	
	@Override
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
