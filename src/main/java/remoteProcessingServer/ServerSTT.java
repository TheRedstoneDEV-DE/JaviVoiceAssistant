package remoteProcessingServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import org.vosk.Model;
import org.vosk.Recognizer;

public class ServerSTT {
	public void start() {
		SSLServerSocket server = null;
		try {
			SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
			server = (SSLServerSocket)sslServerSocketFactory.createServerSocket(8078);
			server.setEnabledCipherSuites(server.getSupportedCipherSuites());
			server.setReuseAddress(true);
			while (true) {
				Socket client = server.accept();
				ClientHandler clientSock = new ClientHandler(client);
				clientSock.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {

				}
			}
		}
	}

	private static class ClientHandler {
		private final Socket clientSocket;
		private Recognizer recognizer;
		private Model model;
		private Boolean initialized = false;

		public ClientHandler(Socket socket) {
			this.clientSocket = socket;
		}

		PrintWriter out = null;

		public void start() {
			System.out.println("Client connected to STT Server!");
			try {
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				OutputStream outStream = clientSocket.getOutputStream();
				InputStream in = clientSocket.getInputStream();
				byte[] request = new byte[250];
				try {
					int numBytes;
					String InitText;
					int len = 250;
					while ((numBytes = in.read(request, 0, len)) != -1) {
						if ((InitText = new String(request)).startsWith("INIT_PACKET:dic=") && !initialized) {
							InitText = InitText.replace("INIT_PACKET:dic=", "");
							System.out.println(InitText);
							System.out.println("Client transfered INIT, initializing STT-Recodnizer...");
							System.out.println("done");
							init(InitText);
							break;
						}
					}
					int numBytesRead = 0;
					int CHUNK_SIZE = 512;
					byte[] targetArray = new byte[512];
					while (true) {
							numBytesRead = in.read(targetArray, 0, CHUNK_SIZE);
							if(numBytesRead == -1){
								System.out.println("Connection lost, closing STT socket...");
								break;
							}
							if (recognizer.acceptWaveForm(targetArray, numBytesRead)) {
								String res = recognizer.getResult();
								if (res != null && res != "{\n"
										+ "  \"text\" : \"\"\n"
										+ "}" && res != "{\n"
												+ "  \"text\" : \"the\"\n"
												+ "}") {
									out.println(res.replaceAll("\n", ""));
								}
							}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				outStream.close();
				clientSocket.close();
			} catch (

			IOException e) {
				e.printStackTrace();
			}
		}
		private void init(String dict){
			try {
				model = new Model("model");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			recognizer = new Recognizer(model, 120000, "[\"" + dict + "\", \"\"]");
			//at the end
			initialized = true;
		}
	}
	
}
