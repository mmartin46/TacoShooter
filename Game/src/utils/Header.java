package utils;


import interfaces.OnScreenSetting;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import javafx.scene.text.Text;
import movables.Player;

public class Header extends OnScreenSetting {
	
	private Player player;
	
	private String headerSentence;
	private final int DEFAULT_FONT_SIZE = 14; 
	
	private final int DEFAULT_HEADER_X = 10;
	private final int DEFAULT_HEADER_Y = 20;
	
	public Header(Player player) {
		this.player = player;
		this.textGraphic = new Text();
		this.textFont = loadFont("fonts/alagard.ttf", DEFAULT_FONT_SIZE);
		
		textGraphic.setFont(this.textFont);
		textGraphic.setFill(Color.LIGHTBLUE);
		
		setupFontGraphic(DEFAULT_HEADER_X,
				DEFAULT_HEADER_Y);
	}
	
	public Header(Player player, int fontSize) {
		this.player = player;
		this.textGraphic = new Text();
		this.textFont = loadFont("fonts/alagard.ttf", fontSize);
		
		textGraphic.setFont(this.textFont);
		textGraphic.setFill(Color.LIGHTBLUE);
		
		setupFontGraphic(DEFAULT_HEADER_X,
						DEFAULT_HEADER_Y);
	}
	

	
	@Override	
	public void updateFontText() {
		headerSentence = "Player x" + (int) player.getHealth();
		headerSentence += "      Coins x" + (int) player.getCoinsCollected();
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
