package ConnectFour;

import Client.ObjectCommunication.ConnectFourkObjectClient;
import Server.ObjectCommunication.ObjectResponseCallback;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;

public class ConnectFourApplication extends Application implements ObjectResponseCallback {
	private BorderPane borderPane;
	private ConnectFourCanvas canvas;
	private ConnectFourDataManager dataManager;
	private Disc turn;
	private ConnectFourDataObject dataObject;

	private ConnectFourkObjectClient client;

	//should probably be temporary
	private Disc playerColor;

	public ConnectFourApplication() {
		this.borderPane = new BorderPane();
		this.dataObject = new ConnectFourDataObject();
		this.dataManager = new ConnectFourDataManager(this.dataObject);
		this.canvas = new ConnectFourCanvas(this.borderPane, this.dataObject.getDiscLocations());
		this.client = new ConnectFourkObjectClient("localhost", 27272, this);
		this.client.startClient();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initButtons();
		primaryStage.setScene(new Scene(this.borderPane));
		primaryStage.setTitle("Connect 4");
		primaryStage.setMinWidth(850);
		primaryStage.setMinHeight(600);
		primaryStage.setResizable(false);
		primaryStage.show();

		Label turnText = new Label(turn + "'s up!");
		turnText.setWrapText(true);
		turnText.setAlignment(Pos.TOP_LEFT);
		turnText.setFont(new Font(30));
		borderPane.setRight(turnText);

	}

	public void initButtons() {
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.TOP_LEFT);
		hBox.setPadding(new Insets(10, 0, 10, 25));
		hBox.setSpacing(61);
		for (int i = 0; i < 7; i++) {
			Button button = new Button();
			button.setGraphic(new ImageView("\\ArrowDown.png"));

			int xValue = i;
			button.setOnAction(event -> {
				if (dataObject.getTurn().equals(this.playerColor)) {
					this.dataManager.dropDisc(xValue, this.playerColor);
					this.canvas.updateDiscLocations(dataObject.getDiscLocations());
					this.client.sendObjectMessage(this.dataObject);
				}
			});
			hBox.getChildren().add(button);
		}
		this.borderPane.setTop(hBox);
	}

	public void setTurn(Disc turn) {

		this.turn = Disc.RED;



		TextArea turnText = (TextArea) borderPane.getRight();
		turnText.setText(this.turn + "'s up!");

	}

	@Override
	public void objectMessageReceived(Object response) {
		if (response instanceof Disc) {
			this.playerColor = (Disc) response;
		}

		if (response instanceof ConnectFourDataObject) {
			this.dataObject = (ConnectFourDataObject) response;
			this.dataManager.setDataObject((ConnectFourDataObject) response);
			this.canvas.updateDiscLocations(this.dataObject.getDiscLocations());
		}
	}
}
