package Server;

public class DataServerMain
{
	public static void main(String[] args) {
		ConnectFourkDataServer server = new ConnectFourkDataServer(27272);
		server.start();
	}
}
