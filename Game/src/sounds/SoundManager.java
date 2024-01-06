package sounds;


import javafx.scene.media.Media;

import javafx.scene.media.MediaPlayer;

public class SoundManager {
	
	private String soundFilePath;
	private Media soundMedia;
	private MediaPlayer soundMediaPlayer;
	
	public SoundManager() {

	}
	
	public void setSoundFilePath(String soundFilePath) {
		this.soundFilePath = soundFilePath;
		soundMedia = new Media(getClass().getResource(soundFilePath).toExternalForm());
		soundMediaPlayer = new MediaPlayer(soundMedia);
	}
	
	public void playSound(String soundFilePath, double volume) {
		setSoundFilePath(soundFilePath);
		soundMediaPlayer.setVolume(volume);
		soundMediaPlayer.play();
	}
	
	public void stopSound() {
		soundMediaPlayer.stop();
	}

	public String getSoundFilePath() {
		return soundFilePath;
	}

}
