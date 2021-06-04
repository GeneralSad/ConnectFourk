package ConnectFour;

import java.io.Serializable;

public class ConnectFourDataObject implements Serializable {

	private Disc turn;
	private Disc[][] discLocations;
	private Disc requestReset;
	private Disc winner;
	private boolean reset;

	public ConnectFourDataObject() {
		this.turn = Disc.RED;
		this.discLocations= new Disc[7][6];
		this.requestReset = Disc.EMPTY;
		this.reset = false;
		this.winner = Disc.EMPTY;
	}

	public void resetDataObject() {
		this.turn = Disc.RED;
		this.requestReset = Disc.EMPTY;
		this.reset = false;
		this.winner = Disc.EMPTY;
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

	public Disc getWinner() {
		return this.winner;
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

	public void setWinner(Disc winner) {
		this.winner = winner;
	}
}

