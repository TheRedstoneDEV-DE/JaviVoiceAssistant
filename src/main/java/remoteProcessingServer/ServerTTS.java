package remoteProcessingServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class ServerTTS {
	public void start() {
		SSLServerSocket server = null;
		try {
			SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
			server = (SSLServerSocket) sslServerSocketFactory.createServerSocket(8079);
			server.setEnabledCipherSuites(server.getSupportedCipherSuites());
			server.setReuseAddress(true);
			while (true) {
				Socket client = server.accept();
				System.out.println("New client connected " + client.getInetAddress().getHostAddress());
				TextToSpeech tts = new TextToSpeech();
				ClientHandler clientSock = new ClientHandler(client, tts);
				new Thread(clientSock).start();
			}
		} catch (IOException e) {

		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {

				}
			}
		}
	}

	public static class ClientHandler implements Runnable {
		private final Socket clientSocket;
		private final TextToSpeech tts;

		public ClientHandler(Socket socket, TextToSpeech tts) {
			this.clientSocket = socket;
			this.tts = tts;
		}

		String request;
		OutputStream out = null;
		@Override
		public void run() {
			System.out.println("Client connected to TTS Server!");
			try {
				out = clientSocket.getOutputStream();
				OutputStream outStream = clientSocket.getOutputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				int bytesRead;
				while ((request = in.readLine()) != null) {
					if(request.startsWith("TTS_PACKET")){
						System.out.println("Got TTS-Request from client: \""+request+"\"");
						String text = request.replace("TTS_PACKET:", "");
						tts.speak(text, this);
					}
				}
				outStream.close();
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void send(byte[] audio){
			try {
				out.write(audio);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
