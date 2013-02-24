package probus.jam.gameengine;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {

	public static final Sound turnMiror = new Sound("/turnMiror.wav");
	public static final Sound turnSplitter = new Sound("/turnSplitter.wav");
	public static final Sound liftItem = new Sound("/liftItem.wav");
	public static final Sound throwItem = new Sound("/throwItem.wav");
	public static final Sound walk = new Sound("/move.wav");
	public static final Sound bzzt = new Sound("/ditzzz.wav");
	
	
	private AudioClip clip;
	
	private Sound(String name){
		try{			
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		} catch (Throwable e) {
				e.printStackTrace();
			}
	}
	
	public void Play(){
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
			}.start();
		} catch(Throwable e) {
				e.printStackTrace();
			}		
	}
}
