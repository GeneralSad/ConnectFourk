package Client.ObjectCommunication;

import ConnectFour.ConnectFourApplication;
import ConnectFour.Disc;
import Server.ObjectCommunication.ObjectResponseCallback;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ConnectFourkObjectClient {
	private String host;
	private int port;

	private Socket socket;
	private ObjectInputStream clientObjectInput;
	private ObjectOutputStream clientObjectOutput;

	private ObjectResponseCallback callback;

	private boolean running;

	public ConnectFourkObjectClient(String host, int port, ObjectResponseCallback callback) {
		this.host = host;
		this.port = port;
		this.callback = callback;
	}

	public void startClient() {

		try {
			this.socket = new Socket(this.host, this.port);

			this.clientObjectOutput = new ObjectOutputStream(this.socket.getOutputStream());
			this.clientObjectOutput.flush();
			this.clientObjectInput = new ObjectInputStream(this.socket.getInputStream());


			this.running = true;
			new Thread(() -> {
				try {
					Object response = this.clientObjectInput.readObject();
					System.out.println(response);
					if (response instanceof Disc) {
					this.callback.objectMessageReceived(response);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				while (this.running) {
					try {
						Object response = this.clientObjectInput.readObject();
						System.out.println(response);

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
