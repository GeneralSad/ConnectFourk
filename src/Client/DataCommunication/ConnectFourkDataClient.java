package Client.DataCommunication;

import Server.DataCommunication.DataResponseCallback;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ConnectFourkDataClient {
	private String host;
	private int port;

	private Socket socket;
	private DataInputStream clientDataInput;
	private DataOutputStream clientDataOutput;

	private DataResponseCallback callback;

	private boolean running;

	public ConnectFourkDataClient(String host, int port, DataResponseCallback callback) {
		this.host = host;
		this.port = port;
		this.callback = callback;
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
						System.out.println("Response: " + response);
						this.callback.stringMessageReceived(response);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}).start();

		} catch (IOException e) {
			System.out.println("Could not connect to the server: " + e.getMessage());
		}
	}

	public void sendMessage(String message) {
		try {
			this.clientDataOutput.writeUTF(message);
			Thread.sleep(200);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			this.running = false;
			this.clientDataInput.close();
			this.socket.close();
			this.clientDataOutput.close();

		} catch (IOException e) {
			System.out.println("Could not close something: " + e.getMessage());
		}
	}
}
