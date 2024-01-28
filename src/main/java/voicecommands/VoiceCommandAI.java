package voicecommands;

import general.CmdMask;
import general.Main;
import tts.TextToSpeech;

public class VoiceCommandAI extends CmdMask {
    @Override
    public void execute(String command, TextToSpeech tts, Main main) {
        if(command.startsWith("start artificial intelligence")&&main.man.get("llm-enabled")!=null){
            if (main.man.get("llm-enabled").contains("true")) {
                tts.speak("switched to AI mode, to continue say any word");
                main.va.aiMode = true;
                main.va.isRunning = false;
            }else{
                tts.speak("AI mode is disabled, consult the manual");
            }
        }else{
            tts.speak("AI mode is disabled, consult the manual");
        }    
    }
}
