package ConnectFour;

import Client.ObjectCommunication.ConnectFourkObjectClient;
import Server.ObjectCommunication.ObjectResponseCallback;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ConnectFourApplication extends Application implements ObjectResponseCallback {
	private BorderPane borderPane;
	private ConnectFourCanvas canvas;
	private ConnectFourDataManager dataManager;

	//should probably be temporary
	private Disc playerColor;

	public ConnectFourApplication() {
		this.borderPane = new BorderPane();
		this.dataManager = new ConnectFourDataManager();
		this.canvas = new ConnectFourCanvas(this.borderPane, this.dataManager.getDiscLocations());
		ConnectFourkObjectClient client = new ConnectFourkObjectClient("localhost", 27272, this);
		client.startClient();
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
				this.dataManager.dropDisc(xValue, this.playerColor);
				this.canvas.updateCanvas();
			});
			hBox.getChildren().add(button);
		}
		this.borderPane.setTop(hBox);
	}

	@Override
	public void objectMessageReceived(Object response) {
		if (response instanceof Disc) {
			this.playerColor = (Disc) response;
		}
	}
}