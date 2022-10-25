import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.*;
import static java.nio.charset.StandardCharsets.UTF_8;


public class ChatServer {

	private List<PrintWriter> clientWriters = new ArrayList<>();
	
	public void run(int port) {
		ExecutorService threadPool = Executors.newCachedThreadPool();
		try {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.bind(new InetSocketAddress(port));
			while (serverSocketChannel.isOpen()) {
				SocketChannel clientSocket = serverSocketChannel.accept();
				PrintWriter writer = new PrintWriter(Channels.newWriter(clientSocket, UTF_8));
				clientWriters.add(writer);
				threadPool.submit(new ClientHandler(clientSocket));
				System.out.println("got a connection");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void messageClient(String message) {
		for (PrintWriter writer : clientWriters) {
			writer.println(message);
			writer.flush();
		}
	}
	
	public class ClientHandler implements Runnable {
		BufferedReader reader;
		SocketChannel socket;
		
		public ClientHandler(SocketChannel clientSocket) {
			socket = clientSocket;
			reader = new BufferedReader(Channels.newReader(socket, UTF_8));
		}
		
		public void run() { 
			String message;
			try {
				while  ((message = reader.readLine()) != null) {
					System.out.println("read " + message);
					messageClient(message);
			}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress("google.com", 80));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("Your ip is: " + socket.getLocalAddress());
		
		try {
			socket.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Scanner reader = new Scanner(System.in);
		System.out.println("Enter the port number for your server (5000 for testing purposes): ");
		int port = reader.nextInt();
		reader.close();
		new ChatServer().run(port);
	}
}
