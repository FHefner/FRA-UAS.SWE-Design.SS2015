import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Bell {
	private String soundfile;
	private static boolean loop;
	private static final int BUFFER_SIZE = 4096;

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

	public Bell(String sound) {
		this.setSoundfile(sound);
		Bell.setLoop(false);
	}

	public void ring() {
		File audioFile = new File(this.soundfile);
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(info);
            audioLine.open(format);
            audioLine.start();
            byte[] bytesBuffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
 
            while ((bytesRead = audioStream.read(bytesBuffer)) != -1) {
                audioLine.write(bytesBuffer, 0, bytesRead);
            }
             
            audioLine.drain();
            audioLine.close();
            audioStream.close();
            System.out.println("Bell rang!");
             
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
	}
}
