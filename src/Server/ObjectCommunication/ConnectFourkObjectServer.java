package Server.ObjectCommunication;

import Server.DataCommunication.CFDataServerTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectFourkObjectServer implements ClientObjectResponseCallback {
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
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not connect to the server: " + e.getMessage());
		}
	}

	@Override
	public void objectMessageReceived(Object object) {

	}
}