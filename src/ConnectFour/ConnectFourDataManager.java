package ConnectFour;

public class ConnectFourDataManager {

	private Disk[][] diskLocations;

	public ConnectFourDataManager() {
		//The disk array is in Y X format due to that making more sense for checking where a disk will drop!!
		this.diskLocations = new Disk[7][6];
		resetConnectFourBoard();
	}

	public void resetConnectFourBoard() {
		for (int y = 0; y < 6; y++) {
			for (int x = 0; x < 7; x++) {
				diskLocations[x][y] =  Disk.EMPTY;
			}
		}
	}

	public Disk[][] getDiskLocations() {
		return this.diskLocations;
	}
}
