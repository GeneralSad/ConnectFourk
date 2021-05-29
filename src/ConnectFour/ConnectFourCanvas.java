package ConnectFour;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class ConnectFourCanvas {
	private Canvas canvas;
	private Disk[][] diskLocations;

	public ConnectFourCanvas(BorderPane borderPane, Disk[][] diskLocations){
		this.diskLocations = diskLocations;
		this.canvas = new Canvas(700, 600);

		borderPane.setCenter(this.canvas);

		draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
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
				switch (this.diskLocations[x][y]) {
					case EMPTY:
						graphics.setColor(Color.white);
						break;
					case RED:
						graphics.setColor(Color.red);
						break;
					case BLUE:
						graphics.setColor(Color.blue);
						break;
				}
				graphics.fill(shape);
			}
		}


	}
}
