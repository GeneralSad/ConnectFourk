package ConnectFour;

public class ConnectFourDataManager {

	private Disc[][] discLocations;

	public ConnectFourDataManager() {
		this.discLocations = new Disc[7][6];
		resetConnectFourBoard();
	}

	public void resetConnectFourBoard() {
		for (int y = 0; y < 6; y++) {
			for (int x = 0; x < 7; x++) {
				discLocations[x][y] =  Disc.EMPTY;
			}
		}
	}

	public void dropDisc(int xDropLocation, Disc discColor) {
		dropDisc(xDropLocation, 0, discColor);
	}

	public void dropDisc(int xDropLocation, int yDropLocation,Disc discColor) {
		if (yDropLocation >= 6) {
			return;
		}
		if (this.discLocations[xDropLocation][yDropLocation] != Disc.EMPTY) {
			dropDisc(xDropLocation, yDropLocation + 1, discColor);
		} else {
			this.discLocations[xDropLocation][yDropLocation] = discColor;
		}
	}

	public Disc[][] getDiscLocations() {
		return this.discLocations;
	}
}
