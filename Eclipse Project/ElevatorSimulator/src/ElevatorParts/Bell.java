package ElevatorParts;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Bell {
	private String soundfile;
	private Clip soundClip;
	private FloatControl volume;
	private static boolean loop;

	public static boolean isLoop() {
		return loop;
	}

	public static void setLoop(boolean loop) {
		Bell.loop = loop;
	}

	public String getSoundfile() {
		return this.soundfile;
	}

	public void setSoundfile(String soundfile) {
		this.soundfile = soundfile;
	}

	public Bell(String path) {
		this.soundfile = path;
		Bell.loop = false;
		AudioInputStream audioStream = null;
		try {
			audioStream = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(this.soundfile));
			soundClip = AudioSystem.getClip();
			soundClip.open(audioStream);
			setVolume((FloatControl) soundClip.getControl(FloatControl.Type.MASTER_GAIN));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void ring() {
		soundClip.setMicrosecondPosition(0);
		soundClip.start();
	}

	public FloatControl getVolume() {
		return volume;
	}

	public void setVolume(FloatControl volume) {
		this.volume = volume;
	}
}
