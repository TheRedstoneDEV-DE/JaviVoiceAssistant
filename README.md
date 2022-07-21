# VoiceAssistant

A multifunction and fully offline usable voice assistant written in Java



**Please Note:**

- This is still Work-In-Progress Software

- **The traffic beween client and local server is not encrypted!** If you use only the built-in recognizer (without setting up a local server) there should everything be nothing insecure

- The Program was just developed for my PC and might not work everywhere

**Please inform me about bugs you have found via the issues!**

#### Used libraries in this Project:

- MaryTTS (Text-To-Speech)

- Vosk (speech recognizer)

- RXTX (communication with Arduino / ATMEGA 328P for RGB backlight)

- QT / QT Jambi (just the simple Text-Overlay)

- java-discord-rpc (Discord RPC)

- 2 Natives:
  
  - small C++-Program for getting avarage screen color (just for Backlight)
  
  - MPRIS-Interface for controlling nearly any media on Desktop (standart setting only works for the KDE Plasma Browser Extension)



### Building and Running

---

Requirements to run fully:

- a PC based on x86_64 (amd64) with a Linux installation

- Java (version 16 recommended)

- QT 6

- Maven (mvn on command line)

- OpenCV (only for backlight)

- Git

Requirements to run (only basic funtionality):

- any x86_64 PC OS doesn't matter

- Java

- Git
  
  *meida control, volume control, automatic backlight and some other things WILL NOT WORK!*



1. Clone the repository:

`git clone https://github.com/TheRedstoneDEV-DE/VoiceAssistant` 

2. Build

`mvn install`

3. Open the target JAR in an archive program and edit configuration/VoiceAssistant.properties for your needs.

**Explanation:**

| parameter                                 | explanation                                                                                                                                              |
| ----------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------- |
| mpris-module-activated                    | enables / disables the MPRIS module [yes\|no]                                                                                                            |
| mpris-module-lib-path                     | full path to the MPRIS native (libMPRIS.so)                                                                                                              |
| rgb-control-module-activated              | enables / disables the rgb module [yes\|no]                                                                                                              |
| rgb-control-module-lib-path               | full path to the avgScreenColor native (libavgColorOfScreenLib.so)                                                                                       |
| rgb-control-module-serialport             | serial port for communication with Arduino / ATMEGA 328P                                                                                                 |
| system-status-module-activated            | sets if the system profiling unit should be active  [yes\|no]                                                                                            |
| system-status-module-cpu-temperature-file | path to the file, which is used to read cpu temperature (on Linux of course)                                                                             |
| overlay-module-activated                  | enables / disables the overlay module [yes\|no]                                                                                                          |
| discord-rpc-module-activated              | enables / disables the discord rpc [yes\|no]                                                                                                             |
| use-local-recognition                     | enables / disables the remote client (if disabled voice processing is done locally)                                                                      |
| rms-threshold                             | sets the basic noise threshold (so the processing is only running, if somthing is said **recommended to use with a denoiser like Cadmus or NoiseTorch**) |
4. Run the Jar like you would do it!

*NOTE:* Before you run this voice assistant, please download a voice recognition model for Vosk! (the small model is recommended, because of latency)
Vosk model download:
[Model Download](https://alphacephei.com/vosk/models)

Then unpack the ZIP file and rename the contained folder to "model" (without "" of course) (The folder has to be in the same directory as the JAR file)

After this just run the program and the recognition should work perfectly.

`java -jar [JARFILE]`

