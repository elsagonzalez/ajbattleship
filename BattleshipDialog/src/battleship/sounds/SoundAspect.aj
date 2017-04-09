package battleship.sounds;

import java.io.FileInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import battleship.model.Board;
import battleship.model.Place;

public aspect SoundAspect {
	private static final String SOUND_DIR = "sounds/";
	before(Place x): call(void Board.hit(Place)) && args(x){
		//stuff here
		if (!x.isEmpty()){
		    if (x.ship().isSunk()){
		    	playAudio("sunk.m4r");
		    }
		}
		else{
			playAudio("hit.m4r");
		}
	}
	public static void playAudio(String filename) {
      try {
          AudioInputStream audioIn = AudioSystem.getAudioInputStream(
        		  new FileInputStream(SOUND_DIR + filename));
          Clip clip = AudioSystem.getClip();
          clip.open(audioIn);
          clip.start();
      } catch (UnsupportedAudioFileException 
            | IOException | LineUnavailableException e) {
          e.printStackTrace();
      }
    }
	
}