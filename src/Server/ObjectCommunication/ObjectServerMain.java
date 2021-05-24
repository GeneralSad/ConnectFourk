package Server.ObjectCommunication;

public class ObjectServerMain {
	public static void main(String[] args) {
		ConnectFourkObjectServer connectFourkObjectServer = new ConnectFourkObjectServer(27272);
		connectFourkObjectServer.startServer();
	}
}
