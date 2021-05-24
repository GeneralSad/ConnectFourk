package Server.DataCommunication;

import Server.DataCommunication.CFDataServerTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectFourkDataServer implements ClientDataResponseCallback {
	private int port;
	List<CFDataServerTask> clients;

	private boolean running;

	public ConnectFourkDataServer(int port) {
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
				CFDataServerTask dataServerTask = new CFDataServerTask(client, this);
				Thread thread = new Thread(dataServerTask);
				thread.start();

				this.clients.add(dataServerTask);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not connect to the server: " + e.getMessage());
		}
	}


	@Override
	public void stringMessageReceived(String string) {

	}
}
