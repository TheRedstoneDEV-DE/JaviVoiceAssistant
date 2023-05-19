

<p align="center">
 <img width="100px" src="https://res.cloudinary.com/anuraghazra/image/upload/v1594908242/logo_ccswme.svg" align="center" alt="GitHub Readme Stats" />
 <h2 align="center">OpenSource VoiceAssitant Javi</h2>
 <p align="center">A multifunction and fully offline usable open-source voice assistant written in Java!</p>
</p>
  <p align="center">
    <a href="https://github.com/theredstonedev-de/voiceassistant/actions">
      <img alt="Tests Passing" src="https://github.com/anuraghazra/github-readme-stats/workflows/Test/badge.svg" />
    </a>
    <a href="https://github.com/theredstonedev-de/releases">
      <img alt="GitHub Release" src="https://img.shields.io/github/releases/theredstonedev-de/voiceassistant" />
    </a>
    <a href="https://codecov.io/gh/anuraghazra/github-readme-stats">
      <img src="https://codecov.io/gh/anuraghazra/github-readme-stats/branch/master/graph/badge.svg" />
    </a>
    <a href="https://github.com/TheRedstoneDEV-DE/VoiceAssistant/issues">
      <img alt="Issues" src="https://img.shields.io/github/issues/theredstonedev-de/voiceassistant?color=0088ff" />
    </a>
    <a href="https://github.com/TheRedstoneDEV-DE/VoiceAssistant/pulls">
      <img alt="GitHub pull requests" src="https://img.shields.io/github/issues-pr/theredstonedev-de/voiceassistant?color=0088ff" />
    </a>
    <br />
    <br />
  </p>


A multifunction and fully offline usable open-source voice assistant written in Java

Liecence Infomation of used libraries can be found here: https://theredstonedev-de.github.io/VoiceAssistant/target/site/

**Please Note:**

- This is still Work-In-Progress Software

**Please inform me about bugs you have found via the issues!**

#### Used libraries in this Project:

- MaryTTS (Text-To-Speech)

- Vosk (speech recognizer)

- QT / QT Jambi (just the simple Text-Overlay)

- java-discord-rpc (Discord RPC)

- dbus java for communicating with MPRIS

### Building and Running

---

Requirements to run fully:

- a PC based on x86_64 (amd64) with a Linux installation

- Java (version 18 recommended)

- QT 6

- Maven (mvn on command line)

- Git (optional, only for downloading)

Requirements to run (only basic funtionality):

- any x86_64 PC OS doesn't matter

- Java

- Git (optional, only for downloading)
  
  *meida control, volume control and some other features WILL NOT WORK on Windows!*

#### Running standalone:

 ---

1. Clone the repository:

   `git clone https://github.com/TheRedstoneDEV-DE/VoiceAssistant` 

   and get the voice model for vosk from (the small one is recommended)
   [VOSK Models](https://alphacephei.com/vosk/models)
   unzip it, rename the folder inside it to 'model' and put it into the project root

2. Build

   `mvn install`

3. Run the Jar file, which was generated in the 'target' folder after copieng it into the project root

   `java -jar [JARFILE]`

4. Cofigure via setup window

The fields in the window should be selfexplaining.

- 'Overlay position' definines the coordinates for the overlay (should only be necessary, if you have multiple screens)
- 'CPU temp file' defines the file, in which the cpu temperature is logged by the kernel (if you don't have the overlay enabled, this is not used at all)
- any MPRIS settings are just defining, which player you are targeting in the dbus interface (you can start, pause and skip titles with voicecommands)#

#### Running with recognition server:

---

1. Clone the repository:

     `git clone https://github.com/TheRedstoneDEV-DE/VoiceAssistant`

      and get the voice model for vosk from (the small one is recommended)

      [VOSK Models](https://alphacephei.com/vosk/models)
      unzip it, rename the folder inside it to 'model' and put it into the project root on          the server.

2. Build

      `mvn install`

3. Copy the Jar file from the 'target' folder in the project root and run the client standalone for setup.

       `java -jar [JARFILE]`

4. Copy the built jar from the target folder, the 'model' folder and the 'keys' folder to the server device (here it really could be Windows and should'n affect anything)

5. Generate the Keystore and Truststore for Client and Server on the Server.
   To do that, go into the 'keys' folder and execute the bash file. Next you will be asked some questions, because of the certificate and a password for the key (you should remember it, because you need it when running)
   
   `bash genkeys.sh`

6. Move the 'client-trust.jks' from the 'keys' folder of the server to the 'keys' folder of the client

7. Run!
   First run the server.
   
   `KEY_PASSPHRASE=[Password of the Keystore] java -jar [JARFILE] --server`
   
   Then the client (you could even use multiple with one server).
   
   `KEY_PASSPHRASE=[password of the Truststore] java -jar [JARFILE] --client [server IP address]` 

### List of commands

---

| command                                      | short explanation                                                             |
| -------------------------------------------- | ----------------------------------------------------------------------------- |
| hey computer                                 | just the base command, has to be said, before any other voice command         |
| set volume to [number between 10-90] percent | sets the volume of the main audio output via amixer                           |
| open [application name]                      | opens the named application in another "screen"                               |
| reconfigure                                  | opens the setup ui again (in case you missconfigured something)               |
| reconfigure programs                         | opens the setup ui for adding programs to the open [application name] command |
| [hide/show] overlay                          | hides or shows the overlay                                                    |
| pause/resume/next/previous                   | plays/pauses a mediaplayer, skippes one title forward/backward                |

### List of Environment variables

---

| Env variable  | explanation                                                                                                                             |
|:------------- | --------------------------------------------------------------------------------------------------------------------------------------- |
| `OUTPUT_GAIN` | adjusts the output volume of the tts system (in case you are using it with some audio server like alsa)                                 |
| `AUDIODEV`    | sets the audio device for input and ouput, but only if it is supported, if it is not supported the voice assistant won't have any audio |
