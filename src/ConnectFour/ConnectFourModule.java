package ConnectFour;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ConnectFourModule extends Application {
	private BorderPane borderPane;
	private ConnectFourCanvas canvas;
	private ConnectFourDataManager dataManager;

	public ConnectFourModule() {
		this.borderPane = new BorderPane();
		this.dataManager = new ConnectFourDataManager();
		this.canvas = new ConnectFourCanvas(this.borderPane, this.dataManager.getDiscLocations());

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initButtons();
		primaryStage.setScene(new Scene(this.borderPane));
		primaryStage.show();
	}

	public void initButtons() {
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER);
		hBox.setSpacing(32);
		for (int i = 0; i < 7; i++) {
			Button button = new Button("drop: " + (i + 1));
			int xValue = i;
			button.setOnAction(event -> {
				this.dataManager.dropDisc(xValue, Disc.RED);
				this.canvas.updateCanvas();
			});
			hBox.getChildren().add(button);
		}

		this.borderPane.setTop(hBox);
	}
}
