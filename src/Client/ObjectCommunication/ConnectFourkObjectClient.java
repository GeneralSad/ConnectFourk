package Client.ObjectCommunication;

import Server.ObjectCommunication.ObjectResponseCallback;
import java.io.*;
import java.net.Socket;

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
				while (this.running) {
					try {
						Object response = this.clientObjectInput.readObject();
						this.callback.objectMessageReceived(response);


					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
						try {
							Thread.sleep(500);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
				close();
			}).start();
		} catch (IOException e) {
			System.out.println("Could not connect to the server: " + e.getMessage());
		}
	}

	public void sendObjectMessage(Object message) {
		try {
			if (this.clientObjectOutput != null) {
				this.clientObjectOutput.writeObject(message);
				System.out.println("message: " + message);
			}
			Thread.sleep(200);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}



	public void close() {
		try {
			this.running = false;
			this.clientObjectInput.close();
			this.socket.close();
			this.clientObjectOutput.close();

		} catch (IOException e) {
			System.out.println("Could not close something: " + e.getMessage());
		}
	}
}
