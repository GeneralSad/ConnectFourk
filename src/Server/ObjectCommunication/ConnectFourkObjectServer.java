package Server.ObjectCommunication;

import ConnectFour.Disc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectFourkObjectServer implements ObjectResponseCallback {
	private int port;
	List<CFObjectServerTask> clients;

	private boolean running;

	public ConnectFourkObjectServer(int port) {
		this.port = port;
		this.clients = new ArrayList<>();
	}

	public void startServer() {
		try {
			ServerSocket serverSocket = new ServerSocket(this.port);
			this.running = true;

			while (this.running) {
				System.out.println("Waiting on clients...");
				Socket client = serverSocket.accept();

				System.out.println("New client found!");
				CFObjectServerTask objectServerTask = new CFObjectServerTask(client, this);
				Thread thread = new Thread(objectServerTask);
				thread.start();

				this.clients.add(objectServerTask);

				if (this.clients.size() == 2) {
					this.clients.get(0).sendObjectToClient(Disc.RED);
					this.clients.get(1).sendObjectToClient(Disc.YELLOW);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not connect to the server: " + e.getMessage());
		}
	}

	@Override
	public void objectMessageReceived(Object object) {
		for (CFObjectServerTask client : clients)
		{
			client.sendObjectToClient(object);
		}
	}
}