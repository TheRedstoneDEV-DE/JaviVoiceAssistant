package general;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class WebServer {
	public static void start(){
		ServerSocket server = null;
		try {
			server = new ServerSocket(8050);
			server.setReuseAddress(true);
			while (true) {
				Socket client = server.accept();
				ClientHandler clientSock = new ClientHandler(client);
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

	private static class ClientHandler implements Runnable {
		private final Socket clientSocket;
		
		public ClientHandler(Socket socket) {
			this.clientSocket = socket;
		}

		@Override
		public void run() {

			byte[] bytesToClient = "NullPointerException!".getBytes();
			String status = "404 NotFound";
			String OutToClient = "NullPointerException!";
			PrintWriter out = null;
			BufferedReader in = null;
			try {
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				OutputStream outStream = clientSocket.getOutputStream();
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String[] requestLine = in.readLine().split(" ");
				if (!requestLine[1].endsWith("?type=resource")) {
					String rLine = requestLine[1];
					try {
						if (requestLine[1].endsWith("?command")) {
							String line;
							int postDataI = 0;
							while ((line = in.readLine()) != null && line.length() != 0) {
								if (line.indexOf("Content-Length:") > -1)
									postDataI = (new Integer(
											line.substring(line.indexOf("Content-Length:") + 16, line.length())))
													.intValue();
							}
							String postData = "";
							if (postDataI > 0) {
								char[] charArray = new char[postDataI];
								in.read(charArray, 0, postDataI);
								postData = new String(charArray);
								System.out.println(postData);
								String[] split = postData.split("&");
								String command = split[0].replaceAll("command=", "");
								Main.getMain().va.processCommand(command);
							}
							status = "200 OK";
						}

					} catch (Exception e) {
						e.printStackTrace();
						bytesToClient = "404 File Not Found".getBytes();
						status = "404 Not Found";
					}
				}
				// Thread.sleep(10);
				String information = "HTTP/1.0 " + status + "\r\n";
				outStream.write(information.getBytes());
				outStream.write("Access-Control-Allow-Origin: *\r\n".getBytes());
				outStream.write("\r\n".getBytes());
				outStream.write(bytesToClient);
				outStream.write("\r\n\r\n".getBytes());
				outStream.flush();
				clientSocket.close();
				// while ((line = in.readLine()) != null) {
				// System.out.printf(" Sent from the client %s\n", line);
				// }

			} catch (IOException e) {

			}
		}

	}
}