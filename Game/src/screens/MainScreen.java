package screens;

import java.io.InputStream;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import utils.Game;
import utils.GameConfigurations;
import javafx.scene.image.Image;

public class MainScreen {
	
	private final int MAIN_SCREEN_X = 0;
	private final int MAIN_SCREEN_Y = 0;
	
	private final int PLAY_BUTTON_X = (int) (GameConfigurations.SCREEN_WIDTH / 3);
	private final int PLAY_BUTTON_Y = (int) (GameConfigurations.SCREEN_HEIGHT / 2);

	private ImageView screenImage;
	private InputStream inputStream;
	
	private Button playButton;
	private ImageView playButtonImage;
	
	private Game game;
	
	public MainScreen(Game game) {
		this.game = game;
		this.game.setAllowGameRun(false);
		
		screenImage = new ImageView();
		setScreenImage("main_screen.png", MAIN_SCREEN_X, MAIN_SCREEN_Y);
		
		playButton = new Button();
		setButtonImage("play_button.png", PLAY_BUTTON_X, PLAY_BUTTON_Y);
	}
	
	private void setScreenImage(String filePath, int x, int y) {
		getResourceAsStream(filePath);
		screenImage = new ImageView(new Image(inputStream));
		screenImage.setTranslateX(x);
		screenImage.setTranslateY(y);
	}
	
	private void getResourceAsStream(String filePath) {
		try {
			inputStream = getClass().getResourceAsStream(filePath);
		} catch (Exception e) {
			System.err.println("MainScreen(): Invalid file path");
		}
	}
	
	private void setButtonImage(String filePath, int x, int y) {
		playButton.setTranslateX(x);
		playButton.setTranslateY(y);
		playButton.setOnAction(e -> buttonHandler());
		
		getResourceAsStream(filePath);
		
		playButtonImage = new ImageView(new Image(inputStream));
		playButton.setGraphic(playButtonImage);
	}
	
	
	public void buttonHandler() {
		game.setAllowGameRun(true);
		screenImage.setVisible(false);
		playButton.setVisible(false);
	}

	
	public void draw(Group group) {
		if (!group.getChildren().contains(screenImage) && screenImage.isVisible()) {
			group.getChildren().add(screenImage);
		}
		
		if (!group.getChildren().contains(playButton) &&
			!group.getChildren().contains(playButtonImage) &&
			playButton.isVisible() &&
			playButtonImage.isVisible()) {
			group.getChildren().add(playButton);
			group.getChildren().add(playButtonImage);
		}
		
		if (game.getAllowGameRun() && !group.getChildren().isEmpty()) {
			group.getChildren().clear();
		}
	}
}
