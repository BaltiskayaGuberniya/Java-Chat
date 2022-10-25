package application;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.charset.StandardCharsets.UTF_8;


//ADD USER NAME
//ADD FILE SEND FUNCTION

public final class ChatClient {
	
	//private JTextField outgoing;
	private static PrintWriter writer;
	private static BufferedReader reader;
	public static SocketChannel serverChannel;
	
	public static String readMessage() {
		String message;
		try {
			while  ((message = reader.readLine()) != null) {
				System.out.println("Client read: " + message);
				return message;
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean connected = false;
	
	private ChatClient() {
		
	}
	public static void run(String ip, int port) {
		setUpNetworking(ip, port);
	}
	public static void run() {
		String ip = "127.0.0.1";
		int port = 5000;
		setUpNetworking(ip, port);
	}
	
	private static void setUpNetworking(String ip, int port) {
		if (connected == true) {
			return;
		}
		System.out.println(ip);
		System.out.println(port);
		try {
			InetSocketAddress serverAddress = new InetSocketAddress(ip, port);
			serverChannel = SocketChannel.open(serverAddress);
			writer = new PrintWriter(Channels.newWriter(serverChannel, UTF_8));
			reader = new BufferedReader(Channels.newReader(serverChannel, UTF_8));
			System.out.println("Sucessfully connected to server!");
			connected = true;
		} catch (IOException e) {
			System.out.println("Failed to connect to server!");
			e.printStackTrace();
			connected = false;
		}
		
	}
	
	public static void sendMessage(String message) {
		writer.println(message);
		writer.flush();
	}
}
