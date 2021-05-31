package Server.ObjectCommunication;

import ConnectFour.ConnectFourDataObject;
import ConnectFour.Disc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectFourkObjectServer implements ObjectResponseCallback {
	private int port;
	List<CFObjectServerTask> clients;
	private Disc turn = Disc.RED;

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
		System.out.println("Received: " + object);
		if (object instanceof ConnectFourDataObject) {
			if (((ConnectFourDataObject) object).getTurn().equals(this.turn)) {
				changeTurns();
				((ConnectFourDataObject) object).setTurn(this.turn);
				for (CFObjectServerTask client : clients)
				{
					client.sendObjectToClient(object);
				}
			}
		}

		if (object instanceof String) {
			for (CFObjectServerTask client : clients) {
				client.sendObjectToClient(object);
			}
		}
//		for (CFObjectServerTask client : clients)
//		{
//			client.sendObjectToClient(object);
//		}
	}

	private void changeTurns() {
		if (this.turn.equals(Disc.RED)) {
			this.turn = Disc.YELLOW;
		} else {
			this.turn = Disc.RED;
		}
	}
}