package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ConnectFourkDataClient {
	private String host;
	private int port;

	private Socket socket;
	private DataInputStream clientDataInput;
	private DataOutputStream clientDataOutput;

	private boolean running;

	public ConnectFourkDataClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public static void main(String[] args) {
//		System.out.println("What is your name?");
//		Scanner reader = new Scanner(System.in);
//		String name = reader.nextLine();

		ConnectFourkDataClient connectFourkDataClient = new ConnectFourkDataClient("localhost", 27272);

		connectFourkDataClient.startClient();
	}


	public void startClient() {

		try {
			this.socket = new Socket(this.host, this.port);

			this.clientDataInput = new DataInputStream(this.socket.getInputStream());
			this.clientDataOutput = new DataOutputStream(this.socket.getOutputStream());
		//	this.clientDataOutput.writeUTF(this.name);

			this.running = true;
			new Thread(() -> {
				while (this.running) {
					try {
						String response = clientDataInput.readUTF();
						//TODO Do something with the response;

					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}).start();

			new Thread(() -> {
				while (this.running) {
					Scanner reader = new Scanner(System.in);
					try {
						System.out.print("Message: ");
						String message = reader.nextLine();
						this.clientDataOutput.writeUTF(message);
						Thread.sleep(1000);

					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		} catch (IOException e) {
			System.out.println("Could not connect to the server: " + e.getMessage());
		}
	}

	public void sendMessage(String message) {

	}

	public void close() {
		try {
			this.clientDataInput.close();
			this.socket.close();
			this.clientDataOutput.close();

		} catch (IOException e) {
			System.out.println("Could not close something: " + e.getMessage());
		}
	}
}
