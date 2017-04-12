package battleship.sounds;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import battleship.model.Board;
import battleship.model.Place;

public aspect SoundAspect {
	private static final String SOUND_DIR = "sounds/";
	before(Place x): call(void Board.hit(Place)) && args(x){
		//stuff here
		if (!x.isEmpty() && x.ship().isSunk()){
			//playAudio("sunk.wav");
		}
		else{
			//playAudio("hit.wav");
		}
	}
	public void playAudio(String filename) {
		File yourFile = new File(SOUND_DIR+filename);
	    AudioInputStream stream = null;
	    AudioFormat format;
	    DataLine.Info info;
	    Clip clip = null;

	    try {
			stream = AudioSystem.getAudioInputStream(yourFile);
		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    format = stream.getFormat();
	    info = new DataLine.Info(Clip.class, format);
	    try {
			clip = (Clip) AudioSystem.getLine(info);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			clip.open(stream);
		} catch (LineUnavailableException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    clip.start();
    }
	
}