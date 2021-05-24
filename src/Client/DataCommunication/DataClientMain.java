package Client.DataCommunication;
public class DataClientMain
{

	public static void main(String[] args) {
		ConnectFourkDataClient connectFourkDataClient = new ConnectFourkDataClient("localhost", 27272);
		connectFourkDataClient.startClient();
	}
}
