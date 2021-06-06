package Server.ObjectCommunication;

import ConnectFour.ConnectFourDataObject;
import ConnectFour.Disc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConnectFourkObjectServer implements ObjectResponseCallback {
	private int port;
	List<CFObjectServerTask> clients;
	private ServerSocket serverSocket;
	private boolean running;
	private boolean clientSearchRunning;

	private Disc turn = Disc.RED;
	private ConnectFourDataObject dataObject;

	public ConnectFourkObjectServer(int port) {
		this.port = port;
		this.clients = new ArrayList<>();
		this.dataObject = new ConnectFourDataObject();
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
				CFObjectServerTask objectServerTask = new CFObjectServerTask(client, this);
				Thread thread = new Thread(objectServerTask);
				thread.start();

				this.clients.add(objectServerTask);

				if (this.clients.size() == 2) {
					this.clients.get(0).sendObjectToClient(Disc.RED);
					this.clients.get(1).sendObjectToClient(Disc.YELLOW);
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
		for (CFObjectServerTask client : this.clients) {
			client.close();
		}
	}

	@Override
	public void objectMessageReceived(Object object) {
		System.out.println("Received from client: " + object);
		if (object instanceof ConnectFourDataObject) {

			if (((ConnectFourDataObject) object).getTurn().equals(this.turn) &&
					!Arrays.deepEquals(this.dataObject.getDiscLocations(), ((ConnectFourDataObject) object).getDiscLocations())) {

				changeTurns();
				((ConnectFourDataObject) object).setTurn(this.turn);
				sendObjectToClients(object);
				this.dataObject = ((ConnectFourDataObject) object);
			} else {
				sendObjectToClients(object);
				this.dataObject = ((ConnectFourDataObject) object);
				this.turn = dataObject.getTurn();
			}
		}

		if (object instanceof String) {
			sendObjectToClients(object);
		}
	}

	private void sendObjectToClients(Object o) {
		for (CFObjectServerTask client : clients) {
			client.sendObjectToClient(o);
		}
	}

	private void changeTurns() {
		if (this.turn.equals(Disc.RED)) {
			this.turn = Disc.YELLOW;
		} else {
			this.turn = Disc.RED;
		}
	}
}