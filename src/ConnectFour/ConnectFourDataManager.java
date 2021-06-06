package ConnectFour;

public class ConnectFourDataManager {

	private ConnectFourDataObject dataObject;

	public ConnectFourDataManager(ConnectFourDataObject dataObject) {
		this.dataObject = dataObject;
		resetConnectFourBoard();
	}

	public void resetConnectFourBoard() {
		Disc[][] discLocations = this.dataObject.getDiscLocations();
		for (int y = 0; y < 6; y++) {
			for (int x = 0; x < 7; x++) {
				discLocations[x][y] =  Disc.EMPTY;
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
		Disc[][] discLocations = this.dataObject.getDiscLocations();

		if (discLocations[xDropLocation][yDropLocation] != Disc.EMPTY) {
			//If there already is a disc at this location we try again but 1 Y higher.
			dropDisc(xDropLocation, yDropLocation + 1, discColor);
		} else {
			discLocations[xDropLocation][yDropLocation] = discColor;
			winChecker(xDropLocation, yDropLocation, discColor);
		}
	}

	private void winChecker(int xLocation, int yLocation, Disc discColor) {
		int discsWithSameColor = 0;
		Disc[][] discLocations = this.dataObject.getDiscLocations();

		//The program checks for a win by looking 3 to the right and 3 to the left of the recently dropped disc
		//it will count how many of the discs are next to the dropped disc. If that number is higher than 3 it has found a win.
		//There are 4 pretty similar for loops due to needing to check every direction with a different calculation.

		//checks from right to left to count the NO same colored discs next to the current one
		for (int i = 0; i < 7; i++) {
			if (i == 3) {
				continue;
			}
			if (!(xLocation + (3 - i) > 6) && !(xLocation + (3 - i) < 0)) {
				if (discLocations[xLocation + (3 - i)][yLocation] == discColor) {
					discsWithSameColor++;
					if (discsWithSameColor >= 3) {
						this.dataObject.setWinner(discColor);
						System.out.println("WINNER: " + discColor);
						return;
					}
				} else {
					//if i is smaller than 4 finding a disc of another color does not mean there aren't 4 discs of the sane
					//color next to each other on this angle, but it does reset the NO discs with the same color next to main disc.
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
				if (discLocations[xLocation][yLocation + (3 - i)] == discColor) {
					discsWithSameColor++;
					if (discsWithSameColor >= 3) {
						this.dataObject.setWinner(discColor);
						System.out.println("WINNER: " + discColor);
						return;
					}
				} else {
					//if i is smaller than 4 finding a disc of another color does not mean there aren't 4 discs of the sane
					//color next to each other on this angle, but it does reset the NO discs with the same color next to main disc.
					if (i < 4) {
						discsWithSameColor = 0;
					} else {
						break;
					}
				}
			}
		}

		discsWithSameColor = 0;
		//checks from top right to bottom left to count the NO same colored discs next to the current one.
		for (int i = 0; i < 7; i++) {
			if (i == 3) {
				continue;
			}
			if (!(xLocation + (3 - i) > 5) && !(xLocation + (3 - i) < 0) && !(yLocation + (3 - i) > 5) && !(yLocation + (3 - i) < 0)) {
				if (discLocations[xLocation + (3 - i)][yLocation + (3 - i)] == discColor) {
					discsWithSameColor++;
					if (discsWithSameColor >= 3) {
						this.dataObject.setWinner(discColor);
						System.out.println("WINNER: " + discColor);
						return;
					}
				} else {
					//if i is smaller than 4 finding a disc of another color does not mean there aren't 4 discs of the sane
					//color next to each other on this angle, but it does reset the NO discs with the same color next to main disc.
					if (i < 4) {
						discsWithSameColor = 0;
					} else {
						break;
					}
				}
			}
		}

		discsWithSameColor = 0;
		//checks from top left to bottom right to count the NO same colored discs next to the current one.
		for (int i = 0; i < 7; i++) {
			if (i == 3) {
				continue;
			}
			if (!(xLocation - (3 - i) > 5) && !(xLocation - (3 - i) < 0) && !(yLocation + (3 - i) > 5) && !(yLocation + (3 - i) < 0)) {
				if (discLocations[xLocation - (3 - i)][yLocation + (3 - i)] == discColor) {
					discsWithSameColor++;
					if (discsWithSameColor >= 3) {
						this.dataObject.setWinner(discColor);
						System.out.println("WINNER: " + discColor);
						return;
					}
				} else {
					//if i is smaller than 4 finding a disc of another color does not mean there aren't 4 discs of the sane
					//color next to each other on this angle, but it does reset the NO discs with the same color next to main disc.
					if (i < 4) {
						discsWithSameColor = 0;
					} else {
						break;
					}
				}
			}
		}
	}

	public void setDataObject(ConnectFourDataObject dataObject) {
		this.dataObject = dataObject;
	}
}
