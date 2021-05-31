package Server.ObjectCommunication;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class CFObjectServerTask implements Runnable{
	private Socket client;
	private ObjectInputStream clientObjectInput;
	private ObjectOutputStream clientObjectOutput;
	private boolean running;
	private ObjectResponseCallback callback;

	public CFObjectServerTask(Socket client, ObjectResponseCallback callback) {
		this.client = client;
		this.callback = callback;
	}


    @Override
    public void run() {
        try {
            this.clientObjectOutput = new ObjectOutputStream(this.client.getOutputStream());
            this.clientObjectOutput.flush();
            this.clientObjectInput = new ObjectInputStream(this.client.getInputStream());

        } catch (IOException e) {
            System.out.println("Could not create input or output stream: " + e.getMessage());
        }

        this.running = true;
        InetAddress inetAddress = this.client.getInetAddress();
        System.out.println("Client connected: " + inetAddress.getHostName() + ", " + inetAddress.getHostAddress() + ".");

		try {
			while (this.running) {
				Object responseObject = this.clientObjectInput.readObject();
				System.out.println(responseObject);
				//TODO Do something with this.
				//this.callback.objectMessageReceived();
			}
		} catch (IOException e) {
			System.out.println("Could not send or receive messages.");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			this.clientObjectInput.close();
			this.clientObjectOutput.close();
			this.client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendObjectToClient(Object message) {
		try {
			this.clientObjectOutput.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
