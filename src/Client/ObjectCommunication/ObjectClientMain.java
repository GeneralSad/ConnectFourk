package Client.ObjectCommunication;

public class ObjectClientMain {

	public static void main(String[] args) {
		ConnectFourkObjectClient client = new ConnectFourkObjectClient("localhost", 27272);
		client.startClient();
	}
}
