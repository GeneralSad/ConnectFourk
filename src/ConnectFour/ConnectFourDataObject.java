package ConnectFour;

import java.io.Serializable;

public class ConnectFourDataObject implements Serializable {

	private Disc turn;
	private Disc[][] discLocations;
	Disc requestReset;
	boolean reset;

	public ConnectFourDataObject() {
		this.turn = Disc.RED;
		this.discLocations= new Disc[7][6];
		this.requestReset = null;
		this.reset = false;
	}

	public Disc getRequestReset() {
		return this.requestReset;
	}

	public Disc getTurn() {
		return this.turn;
	}

	public Disc[][] getDiscLocations() {
		return discLocations;
	}

	public void setDiscLocations(Disc[][] discLocations) {
		this.discLocations = discLocations;
	}

	public void setTurn(Disc turn) {
		this.turn = turn;
	}

	public void setRequestReset(Disc requestReset) {
		this.requestReset = requestReset;
	}
}
