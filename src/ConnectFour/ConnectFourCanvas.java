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
	private Ellipse2D[][] ellipse2DS;

	public ConnectFourCanvas(BorderPane borderPane, Disc[][] discLocations){
		this.discLocations = discLocations;
		initEllipses();
		this.canvas = new Canvas(700, 600);

		borderPane.setLeft(this.canvas);

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
				Shape shape = this.ellipse2DS[x][5 - y];
				graphics.setColor(Color.BLACK);
				graphics.draw(shape);
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

	private void initEllipses() {
		this.ellipse2DS = new Ellipse2D[7][6];
		for (int y = 0; y < 6; y++) {
			for (int x = 0; x < 7; x++) {
				this.ellipse2DS[x][5 - y] = new Ellipse2D.Double((12.5 + x * 25) + 75 * x,(12.5 + y * 25) + 75 * y,75,75);
			}
		}
	}

	public void drawWinTekst(String text) {
		FXGraphics2D graphics2D = new FXGraphics2D(this.canvas.getGraphicsContext2D());

		Font font = new Font("Arial", Font.PLAIN, 100);
		Shape shape = font.createGlyphVector(graphics2D.getFontRenderContext(), text).getOutline();
		graphics2D.setColor(Color.BLACK);

		//We check who won
		if (text.equals("RED WON")) {
			shape = AffineTransform.getTranslateInstance(100,this.canvas.getHeight()/2).createTransformedShape(shape);
			graphics2D.setColor(Color.RED);
		} else {
			shape = AffineTransform.getTranslateInstance(10,this.canvas.getHeight()/2).createTransformedShape(shape);
			graphics2D.setColor(Color.YELLOW);
		}
		graphics2D.fill(shape);
		graphics2D.setColor(Color.BLACK);
		graphics2D.draw(shape);

	}



	public void updateDiscLocations(Disc[][] discLocations) {
		this.discLocations = discLocations;
		draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
	}
}
