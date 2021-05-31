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
				this.discLocations[x][y] =  Disc.EMPTY;
			}
		}
	}

	public void dropDisc(int xDropLocation, Disc discColor) {
		dropDisc(xDropLocation, 0, discColor);
	}

	private void dropDisc(int xDropLocation, int yDropLocation,Disc discColor) {
		if (yDropLocation >= 6) {
			return;
		}
		if (this.discLocations[xDropLocation][yDropLocation] != Disc.EMPTY) {
			dropDisc(xDropLocation, yDropLocation + 1, discColor);
		} else {
			this.discLocations[xDropLocation][yDropLocation] = discColor;
			winChecker(xDropLocation, yDropLocation, discColor);
		}
	}

	private void winChecker(int xLocation, int yLocation, Disc discColor) {
		int discsWithSameColor = 0;

		//checks from right to left to count the NO same colored discs next to the current one
		for (int i = 0; i < 7; i++) {
			if (i == 3) {
				continue;
			}
			if (!(xLocation + (3 - i) > 6) && !(xLocation + (3 - i) < 0)) {
				if (this.discLocations[xLocation + (3 - i)][yLocation] == discColor) {
					discsWithSameColor++;
					if (discsWithSameColor >= 3) {
						//TODO do something with a win
						System.out.println("WIN WIN WIN: " + discColor);
						return;
					}
				} else {
					if (i < 4) {
						discsWithSameColor = 0;
					} else {
						break;
					}
				}
			}
		}

		discsWithSameColor = 0;
		//checks from top to bottom to count the NO same colored discs next to the current one
		for (int i = 0; i < 7; i++) {
			if (i == 3) {
				continue;
			}
			if (!(yLocation + (3 - i) > 5) && !(yLocation + (3 - i) < 0)) {
				if (this.discLocations[xLocation][yLocation + (3 - i)] == discColor) {
					discsWithSameColor++;
					if (discsWithSameColor >= 3) {
						//TODO do something with a win
						System.out.println("WIN WIN WIN: " + discColor);
						return;
					}
				} else {
					if (i < 4) {
						discsWithSameColor = 0;
					} else {
						break;
					}
				}
			}
		}

		discsWithSameColor = 0;
		//checks from top left to bottom right to count the NO same colored discs next to the current one
		for (int i = 0; i < 7; i++) {
			if (i == 3) {
				continue;
			}
			if (!(xLocation + (3 - i) > 5) && !(xLocation + (3 - i) < 0) && !(yLocation + (3 - i) > 5) && !(yLocation + (3 - i) < 0)) {
				if (this.discLocations[xLocation + (3 - i)][yLocation + (3 - i)] == discColor) {
					discsWithSameColor++;
					if (discsWithSameColor >= 3) {
						//TODO do something with a win
						System.out.println("WIN WIN WIN: " + discColor);
						return;
					}
				} else {
					if (i < 4) {
						discsWithSameColor = 0;
					} else {
						break;
					}
				}
			}
		}

		discsWithSameColor = 0;
		//checks from top left to bottom right to count the NO same colored discs next to the current one
		for (int i = 0; i < 7; i++) {
			if (i == 3) {
				continue;
			}
			if (!(xLocation - (3 - i) > 5) && !(xLocation - (3 - i) < 0) && !(yLocation + (3 - i) > 5) && !(yLocation + (3 - i) < 0)) {
				if (this.discLocations[xLocation - (3 - i)][yLocation + (3 - i)] == discColor) {
					discsWithSameColor++;
					if (discsWithSameColor >= 3) {
						//TODO do something with a win
						System.out.println("WIN WIN WIN: " + discColor);
						return;
					}
				} else {
					if (i < 4) {
						discsWithSameColor = 0;
					} else {
						break;
					}
				}
			}
		}
	}

	public Disc[][] getDiscLocations() {
		return this.discLocations;
	}
}
