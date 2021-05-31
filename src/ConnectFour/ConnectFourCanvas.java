package ConnectFour;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class ConnectFourCanvas {
	private Canvas canvas;
	private Disc[][] discLocations;

	public ConnectFourCanvas(BorderPane borderPane, Disc[][] discLocations){
		this.discLocations = discLocations;
		this.canvas = new Canvas(700, 600);

		borderPane.setCenter(this.canvas);

		draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
	}

	public void updateCanvas() {
		draw(new FXGraphics2D(this.canvas.getGraphicsContext2D()));
	}

	private void draw(FXGraphics2D graphics) {
		graphics.setTransform(new AffineTransform());
		graphics.setBackground(Color.BLUE);
		graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

		for (int y = 0; y < 6; y++) {
			for (int x = 0; x < 7; x++) {
				Shape shape = new Ellipse2D.Double((12.5 + x * 25) + 75 * x,(12.5 + y * 25) + 75 * y,75,75);
				graphics.setColor(Color.BLACK);
				graphics.draw(shape);

				//The disk array is in Y X format due to that making more sense for checking where a disk will drop!!
				switch (this.discLocations[x][5 - y]) {
					case EMPTY:
						graphics.setColor(Color.white);
						break;
					case RED:
						graphics.setColor(Color.red);
						break;
					case YELLOW:
						graphics.setColor(Color.yellow);
						break;
				}
				graphics.fill(shape);
			}
		}
	}

	public void setDiscLocations(Disc[][] discLocations) {
		this.discLocations = discLocations;
	}
}
