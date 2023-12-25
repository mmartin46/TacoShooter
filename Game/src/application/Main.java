package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.Game;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.animation.Timeline;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;


public class Main extends Application {
	
	
	private Pane rootPane;
	private Game game;
	private Group gameGroup;
	
	final int SCREEN_WIDTH = 1280;
	final int SCREEN_HEIGHT = 720;
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("TacoRPG");
		
		rootPane = new Pane();
		Scene scene = new Scene(rootPane, SCREEN_WIDTH, SCREEN_HEIGHT);
		primaryStage.setScene(scene);
		
		initializeGame(scene);
		primaryStage.show();
		
		
		Timeline gameLoop = new Timeline(new KeyFrame(Duration.millis(16), e -> update()));
		gameLoop.setCycleCount(Animation.INDEFINITE);
		gameLoop.play();
	}
	
	private void initializeGame(Scene scene) {
		initializeGameGroup();
		game = new Game(scene);
	}
	
	private void initializeGameGroup() {
		gameGroup = new Group();
		rootPane.getChildren().add(gameGroup);
	}
	


	private void update() {
		game.update();
		draw();
	}
	
	private void draw() {
		game.draw(gameGroup);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
