package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.Game;
import utils.GameConfigurations;
import utils.Header;
import utils.PauseScreen;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.animation.Timeline;
import input.InputManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;


public class Main extends Application {
	
	
	private Pane rootPane;
	private Game game;
	private Group headerGroup;
	private Group gameGroup;
	private Header header;
	private Scene scene;
	private PauseScreen pauseScreen;
	
	// Check for pause
	private long lastTimePauseWasPressed;
	private static final long DELAY_PAUSE_TIME = 800;
	
	final int SCREEN_WIDTH = GameConfigurations.SCREEN_WIDTH;
	final int SCREEN_HEIGHT = GameConfigurations.SCREEN_HEIGHT;
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("TacoRPG");
		
		rootPane = new Pane();
		scene = new Scene(rootPane, SCREEN_WIDTH, SCREEN_HEIGHT);
		setPixelatedScaling(scene);
		primaryStage.setScene(scene);
		initializeInputManager();

		
		initializeGame(scene);
		
		//primaryStage.setResizable(false);
		primaryStage.show();
		
		intitalizeGameLoop();
	}
	
	private void intitalizeGameLoop() {
		Timeline gameLoop = new Timeline(new KeyFrame(Duration.millis(
				GameConfigurations.NUM_MILLIS_DELAY
			), e -> update()));
		gameLoop.setCycleCount(Animation.INDEFINITE);
		gameLoop.play();
	}
	
	private void initializeGame(Scene scene) {
		initializeGroups();
		game = new Game(scene);
		initializeHeader();
		initializePauseScreen();
	}
	
	private void initializePauseScreen() {
		pauseScreen = new PauseScreen(game.getAllowGameRun());
	}
	
	private void initializeInputManager() {
		InputManager.getSingleton().initialize(scene);
	}
	
	public void checkForKeyPressed() {
		long currentTime = System.currentTimeMillis();
		
		if (InputManager.getSingleton().isKeyPressed(KeyCode.P) &&
			(currentTime - lastTimePauseWasPressed >= DELAY_PAUSE_TIME)) {
			
			lastTimePauseWasPressed = currentTime;
			
			boolean currentState = game.getAllowGameRun();
			game.setAllowGameRun(currentState == true ? false : true);
		}
	}
	
	
	private void setPixelatedScaling(Scene scene) {
		scene.getRoot().setStyle("-fx-image-rendering: optimize-speed; -fx-aliasing: pixelated;");
	}
	
	private void initializeGroups() {
		headerGroup = new Group();
		gameGroup = new Group();
		rootPane.getChildren().add(gameGroup);
		rootPane.getChildren().add(headerGroup);
	}
	
	private void initializeHeader() {
		header = new Header(game.getPlayer(), game.getNumOfCoinsNeeded(),
							game.getNumEnemiesToBeat(), game.getGameTimer());
		if (game == null) {
			throw new IllegalArgumentException("initializeHeader(): Game wasn't inititalized");
		}
	}

	

	private void update() {
		checkForKeyPressed();
		
		header.update();
		game.update();
		pauseScreen.update();
		
		draw();
	}
	
	private void draw() {
		header.draw(headerGroup);
		game.draw(gameGroup);
		pauseScreen.draw(headerGroup, game.getAllowGameRun());
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
