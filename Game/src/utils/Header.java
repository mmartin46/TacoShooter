package utils;


import interfaces.OnScreenSetting;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import javafx.scene.text.Text;
import movables.Player;

public class Header extends OnScreenSetting {
	
	private Player player;
	
	private String headerSentence;
	
	private int coinsNeeded;
	private int enemiesNeeded;
	
	private final int DEFAULT_FONT_SIZE = 14; 
	
	private final int DEFAULT_HEADER_X = 10;
	private final int DEFAULT_HEADER_Y = 20;
	
	public Header(Player player, int coinsNeeded, int enemiesNeeded) {
		this.player = player;
		this.textGraphic = new Text();
		this.textFont = loadFont("fonts/alagard.ttf", DEFAULT_FONT_SIZE);
		
		this.coinsNeeded = coinsNeeded;
		this.enemiesNeeded = enemiesNeeded;
		
		textGraphic.setFont(this.textFont);
		textGraphic.setFill(Color.LIGHTBLUE);
		
		setupFontGraphic(DEFAULT_HEADER_X,
				DEFAULT_HEADER_Y);
	}
	
	public Header(Player player, int fontSize, int coinsNeeded, int enemiesNeeded) {
		this.player = player;
		this.textGraphic = new Text();
		this.textFont = loadFont("fonts/alagard.ttf", fontSize);
		
		this.coinsNeeded = coinsNeeded;
		this.enemiesNeeded = enemiesNeeded;
		
		textGraphic.setFont(this.textFont);
		textGraphic.setFill(Color.LIGHTBLUE);
		
		setupFontGraphic(DEFAULT_HEADER_X,
						DEFAULT_HEADER_Y);
	}
	

	
	@Override	
	public void updateFontText() {
		headerSentence = "Player x" + (int) player.getHealth();
		headerSentence += "      Coins x" + (int) player.getCoinsCollected() + "/" +
							coinsNeeded;
		
		headerSentence += "	  Enemies x" + player.getNumOfEnemiesBeat() + "/" + enemiesNeeded;
		this.textGraphic.setText(headerSentence);
	}
	
	@Override
	public void update() {
		updateFontText();
	}
	
	public void draw(Group group) {
		if (!group.getChildren().contains(textGraphic)) {
			group.getChildren().add(textGraphic);
		}
	}
	
	
}
