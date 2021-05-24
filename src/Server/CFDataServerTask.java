package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class CFDataServerTask implements Runnable{
	private Socket client;
	private DataInputStream clientDataInput;
	private DataOutputStream clientDataOutput;
	private boolean running;
	private ClientResponseCallback callback;

	public CFDataServerTask(Socket client, ClientResponseCallback callback) {
		this.client = client;
		this.callback = callback;
	}


	@Override
	public void run() {
		try {
			this.clientDataInput = new DataInputStream(this.client.getInputStream());
			this.clientDataOutput = new DataOutputStream(this.client.getOutputStream());
		} catch (IOException e) {
			System.out.println("Could not create input or output stream: " + e.getMessage());
		}

		this.running = true;
		InetAddress inetAddress = this.client.getInetAddress();
		System.out.println("Client connected: " + inetAddress.getHostName() + ", " + inetAddress.getHostAddress() + ".");

		try {
			while (this.running) {
				String message = this.clientDataInput.readUTF();
				System.out.println(message);
				//TODO Do something with this.
			}
		} catch (IOException e) {
			System.out.println("Could not send or receive messages.");
			e.printStackTrace();
		}

		try {
			this.clientDataInput.close();
			this.clientDataOutput.close();
			this.client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
