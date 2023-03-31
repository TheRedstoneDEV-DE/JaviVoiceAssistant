package remoteProcessingServer;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.server.MaryServer.ClientHandler;

public class TextToSpeech {
    private MaryInterface marytts;
    public TextToSpeech(){
        try {
            marytts = new LocalMaryInterface();
        } catch (MaryConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void speak(String text, ServerTTS.ClientHandler instance){
        try (AudioInputStream audio = marytts.generateAudio(text)) {
            int nRead = 0;
            byte[] abData = new byte[4096];
            while ((nRead!= -1)) {
                try {
                    nRead = audio.read(abData, 0, abData.length);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (nRead >= 0) {
                    instance.send(abData);
                }
                Thread.sleep(105);
            }
        }catch(Exception ex){
            
        }
    }
}
