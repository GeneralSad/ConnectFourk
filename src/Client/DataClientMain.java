package Client;

import java.util.Scanner;

public class DataClientMain
{

	public static void main(String[] args) {
//		System.out.println("What is your name?");
//		Scanner reader = new Scanner(System.in);
//		String name = reader.nextLine();

		ConnectFourkDataClient connectFourkDataClient = new ConnectFourkDataClient("localhost", 27272);

		connectFourkDataClient.startClient();
	}
}
