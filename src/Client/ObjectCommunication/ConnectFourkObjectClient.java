package Client.ObjectCommunication;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ConnectFourkObjectClient {
	private String host;
	private int port;

	private Socket socket;
	private ObjectInputStream clientObjectInput;
	private ObjectOutputStream clientObjectOutput;

	private boolean running;

	public ConnectFourkObjectClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void startClient() {

		try {
			this.socket = new Socket(this.host, this.port);

			this.clientObjectOutput = new ObjectOutputStream(this.socket.getOutputStream());
			this.clientObjectOutput.flush();
			this.clientObjectInput = new ObjectInputStream(this.socket.getInputStream());


			this.running = true;
			new Thread(() -> {
				while (this.running) {
					try {
						Object response = this.clientObjectInput.readObject();
						//TODO Do something with the response;

					} catch (IOException | ClassNotFoundException e) {
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
						this.clientObjectOutput.writeObject(message);
						//TODO Send something useful;
						Thread.sleep(1000);
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				}
				close();
			}).start();
		} catch (IOException e) {
			System.out.println("Could not connect to the server: " + e.getMessage());
		}
	}

	public void sendMessage(String message) {

	}

	public void close() {
		try {
			this.clientObjectInput.close();
			this.socket.close();
			this.clientObjectOutput.close();

		} catch (IOException e) {
			System.out.println("Could not close something: " + e.getMessage());
		}
	}
}
