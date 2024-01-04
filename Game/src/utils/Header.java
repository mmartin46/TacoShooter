package utils;

import java.io.InputStream;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javafx.scene.text.Text;
import movables.Player;

public class Header {
	
	private Text textGraphic;
	private Font textFont;
	private Player player;
	
	private String headerSentence;
	private final int DEFAULT_FONT_SIZE = 20;
	
	private final int DEFAULT_HEADER_X = 10;
	private final int DEFAULT_HEADER_Y = 20;
	private final String DEFAULT_FONT = "Arial";
	
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
	
	private Font loadFont(String fontPath, int fontSize) {
		try (InputStream iStream = getClass().getClassLoader().getResourceAsStream(fontPath)) {
			return Font.loadFont(iStream, fontSize);
		} catch (Exception e) {
			// Couldn't load the font.
			System.err.println("loadFont(): Couldn't find font.");
			return Font.font(DEFAULT_FONT, fontSize);
		}
	}
	
	
	private void setupFontGraphic(int x, int y) {
		textGraphic.setLayoutX(x);
		textGraphic.setLayoutY(y);
		
		updateFontText();
	}
	
	
	private void updateFontText() {
		headerSentence = "Player x" + (int) player.getHealth();
		headerSentence += "\tCoins x" + (int) player.getCoinsCollected();
		this.textGraphic.setText(headerSentence);
	}
	
	public void update() {
		updateFontText();
	}
	
	public void draw(Group group) {
		if (!group.getChildren().contains(textGraphic)) {
			group.getChildren().add(textGraphic);
		}
	}
	
	
}
