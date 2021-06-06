package Server.DataCommunication;

import ConnectFour.Disc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConnectFourkDataServer implements DataResponseCallback {
	private int port;
	private List<CFDataServerTask> clients;
	private ServerSocket serverSocket;

	private boolean running;
	private boolean clientSearchRunning;

	public ConnectFourkDataServer(int port) {
		this.port = port;
		this.clients = new ArrayList<>();
	}

	public void startServer() {
		try {
			this.serverSocket = new ServerSocket(this.port);
			this.running = true;
			this.clientSearchRunning = true;

			Scanner reader = new Scanner(System.in);
			new Thread(() -> {
				while (this.running) {
					System.out.println("Type \"close\" to close the server: ");
					String response = reader.nextLine();
					if (response.toLowerCase().equals("close")) {
						close();
					}

				}
			}).start();

			while (this.clientSearchRunning) {
				System.out.println("Waiting on clients...");
				Socket client = serverSocket.accept();

				System.out.println("New client found!");
				CFDataServerTask dataServerTask = new CFDataServerTask(client, this);
				Thread thread = new Thread(dataServerTask);
				thread.start();

				this.clients.add(dataServerTask);

				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (this.clients.size() == 2) {
					this.clients.get(0).sendDataToClient("DISC RED");
					this.clients.get(1).sendDataToClient("DISC YELLOW");
					this.clientSearchRunning = false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not connect to the server: " + e.getMessage());
		}
	}

	public void close() {
		this.running = false;
		this.clientSearchRunning = false;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (CFDataServerTask client : this.clients) {
			client.close();
		}
	}


	@Override
	public void stringMessageReceived(String string) {
		for (CFDataServerTask client : this.clients) {
			client.sendDataToClient(string);
		}
	}
}
