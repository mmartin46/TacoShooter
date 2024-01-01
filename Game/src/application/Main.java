package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.Game;
import utils.GameConfigurations;
import utils.Header;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.animation.Timeline;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;


public class Main extends Application {
	
	
	private Pane rootPane;
	private Game game;
	private Group headerGroup;
	private Group gameGroup;
	private Header header;
	private Scene scene;
	
	final int SCREEN_WIDTH = GameConfigurations.SCREEN_WIDTH;
	final int SCREEN_HEIGHT = GameConfigurations.SCREEN_HEIGHT;
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("TacoRPG");
		
		rootPane = new Pane();
		scene = new Scene(rootPane, SCREEN_WIDTH, SCREEN_HEIGHT);
		setPixelatedScaling(scene);
		primaryStage.setScene(scene);
		
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
		header = new Header(game.getPlayer());
		if (game == null) {
			throw new IllegalArgumentException("initializeHeader(): Game wasn't inititalized");
		}
	}

	

	private void update() {
		header.update();
		game.update();
		draw();
	}
	
	private void draw() {
		header.draw(headerGroup);
		game.draw(gameGroup);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
