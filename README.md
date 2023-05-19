

<p align="center">
 <img width="140px" src="https://github.com/TheRedstoneDEV-DE/VoiceAssistant/blob/main/javi-low-resolution-logo-color-on-transparent-background.png" align="center" alt="GitHub Readme Stats" />
 <h2 align="center">OpenSource VoiceAssitant Javi</h2>
 <p align="center">A multifunction and fully offline usable open-source voice assistant written in Java!</p>
</p>
  <p align="center">
    <a href="https://github.com/theredstonedev-de/voiceassistant/actions">
      <img alt="Tests Passing" src="https://github.com/anuraghazra/github-readme-stats/workflows/Test/badge.svg" />
    </a>
    <a href="https://github.com/TheRedstoneDEV-DE/VoiceAssistant/releases">
      <img alt="GitHub Release" src="https://img.shields.io/github/release/theredstonedev-de/voiceassistant" />
    </a>
    <a href="https://github.com/TheRedstoneDEV-DE/VoiceAssistant/issues">
      <img alt="Issues" src="https://img.shields.io/github/issues/theredstonedev-de/voiceassistant?color=0088ff" />
    </a>
    <a href="https://github.com/TheRedstoneDEV-DE/VoiceAssistant/pulls">
      <img alt="GitHub pull requests" src="https://img.shields.io/github/issues-pr/theredstonedev-de/voiceassistant?color=0088ff" />
    </a>
    <br />
  </p>
  

<p align="center">
 Liecence Infomation of used libraries can be found here: https://theredstonedev-de.github.io/VoiceAssistant/target/site/
 <br>
 Please inform me about bugs you have found via the issues!
⚠️ This is still Work-In-Progress Software!
</p>
  <br />
<hr>

  <br />

<div align="center">
<h4 id="used-libraries-in-this-project-">Used libraries in this Project:</h4>
<ul>
<li><p>MaryTTS (Text-To-Speech)</p>
</li>
<li><p>Vosk (speech recognizer)</p>
</li>
<li><p>QT / QT Jambi (just the simple Text-Overlay)</p>
</li>
<li><p>java-discord-rpc (Discord RPC)</p>
</li>
<li><p>dbus java for communicating with MPRIS</p>
</li>
</ul>
<hr>
<h3 id="building-and-running">Building and Running</h3>

<p>Requirements to run fully:</p>
<ul>
<li><p>a PC based on x86_64 (amd64) with a Linux installation</p>
</li>
<li><p>Java (version 18 recommended)</p>
</li>
<li><p>QT 6</p>
</li>
<li><p>Maven (mvn on command line)</p>
</li>
<li><p>Git (optional, only for downloading)</p>
</li>
</ul>
<p>Requirements to run (only basic funtionality):</p>
<ul>
<li><p>any x86_64 PC OS doesn&#39;t matter</p>
</li>
<li><p>Java</p>
</li>
<li><p>Git (optional, only for downloading)</p>
<p><em>meida control, volume control and some other features WILL NOT WORK on Windows!</em></p>
</li>
</ul>
 </div>
<h4 id="running-standalone-">Running standalone:</h4>
<hr>
<ol>
<li><p>Clone the repository:</p>
<p><code>git clone https://github.com/TheRedstoneDEV-DE/VoiceAssistant</code> </p>
<p>and get the voice model for vosk from (the small one is recommended)
<a href="https://alphacephei.com/vosk/models">VOSK Models</a>
unzip it, rename the folder inside it to &#39;model&#39; and put it into the project root</p>
</li>
<li><p>Build</p>
<p><code>mvn install</code></p>
</li>
<li><p>Run the Jar file, which was generated in the &#39;target&#39; folder after copieng it into the project root</p>
<p><code>java -jar [JARFILE]</code></p>
</li>
<li><p>Cofigure via setup window</p>
</li>
</ol>
<p>The fields in the window should be selfexplaining.</p>
<ul>
<li>&#39;Overlay position&#39; definines the coordinates for the overlay (should only be necessary, if you have multiple screens)</li>
<li>&#39;CPU temp file&#39; defines the file, in which the cpu temperature is logged by the kernel (if you don&#39;t have the overlay enabled, this is not used at all)</li>
<li>any MPRIS settings are just defining, which player you are targeting in the dbus interface (you can start, pause and skip titles with voicecommands)#</li>
</ul>
 <div>
<h4 id="running-with-recognition-server-">Running with recognition server:</h4>
<hr>
<ol>
<li><p>Clone the repository:</p>
<p>  <code>git clone https://github.com/TheRedstoneDEV-DE/VoiceAssistant</code></p>
<p>   and get the voice model for vosk from (the small one is recommended)</p>
<p>   <a href="https://alphacephei.com/vosk/models">VOSK Models</a>
   unzip it, rename the folder inside it to &#39;model&#39; and put it into the project root on          the server.</p>
</li>
<li><p>Build</p>
<p>   <code>mvn install</code></p>
</li>
<li><p>Copy the Jar file from the &#39;target&#39; folder in the project root and run the client standalone for setup.</p>
<pre><code>`java -jar [JARFILE]`
</code></pre></li>
<li><p>Copy the built jar from the target folder, the &#39;model&#39; folder and the &#39;keys&#39; folder to the server device (here it really could be Windows and should&#39;n affect anything)</p>
</li>
<li><p>Generate the Keystore and Truststore for Client and Server on the Server.
To do that, go into the &#39;keys&#39; folder and execute the bash file. Next you will be asked some questions, because of the certificate and a password for the key (you should remember it, because you need it when running)</p>
<p><code>bash genkeys.sh</code></p>
</li>
<li><p>Move the &#39;client-trust.jks&#39; from the &#39;keys&#39; folder of the server to the &#39;keys&#39; folder of the client</p>
</li>
<li><p>Run!
First run the server.</p>
<p><code>KEY_PASSPHRASE=[Password of the Keystore] java -jar [JARFILE] --server</code></p>
<p>Then the client (you could even use multiple with one server).</p>
<p><code>KEY_PASSPHRASE=[password of the Truststore] java -jar [JARFILE] --client [server IP address]</code> </p>
</li>
</ol>
<h3 id="list-of-commands">List of commands</h3>
<hr>
<table>
<thead>
<tr>
<th>command</th>
<th>short explanation</th>
</tr>
</thead>
<tbody>
<tr>
<td>hey computer</td>
<td>just the base command, has to be said, before any other voice command</td>
</tr>
<tr>
<td>set volume to [number between 10-90] percent</td>
<td>sets the volume of the main audio output via amixer</td>
</tr>
<tr>
<td>open [application name]</td>
<td>opens the named application in another &quot;screen&quot;</td>
</tr>
<tr>
<td>reconfigure</td>
<td>opens the setup ui again (in case you missconfigured something)</td>
</tr>
<tr>
<td>reconfigure programs</td>
<td>opens the setup ui for adding programs to the open [application name] command</td>
</tr>
<tr>
<td>[hide/show] overlay</td>
<td>hides or shows the overlay</td>
</tr>
<tr>
<td>pause/resume/next/previous</td>
<td>plays/pauses a mediaplayer, skippes one title forward/backward</td>
</tr>
</tbody>
</table>
<h3 id="list-of-environment-variables">List of Environment variables</h3>
<hr>
<table>
<thead>
<tr>
<th style="text-align:left">Env variable</th>
<th>explanation</th>
</tr>
</thead>
<tbody>
<tr>
<td style="text-align:left"><code>OUTPUT_GAIN</code></td>
<td>adjusts the output volume of the tts system (in case you are using it with some audio server like alsa)</td>
</tr>
<tr>
<td style="text-align:left"><code>AUDIODEV</code></td>
<td>sets the audio device for input and ouput, but only if it is supported, if it is not supported the voice assistant won&#39;t have any audio</td>
</tr>
</tbody>
</table>

