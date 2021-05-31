package ConnectFour;

import Client.ObjectCommunication.ConnectFourkObjectClient;
import Server.ObjectCommunication.ObjectResponseCallback;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

		Label turnText = new Label("Turn: \n" + Disc.RED);
		turnText.setWrapText(true);
		turnText.setPadding(new Insets(0, 10, 0, 10));
		turnText.setFont(new Font(30));

		VBox frame = new VBox();
		frame.setAlignment(Pos.TOP_LEFT);
		frame.getChildren().add(turnText);

		TextArea messageArea = new TextArea();
		messageArea.setEditable(false);
		frame.getChildren().add(messageArea);


		TextField messageField = new TextField();
		frame.getChildren().add(messageField);

		messageField.setOnKeyPressed(event -> {

			if (event.getCode() == KeyCode.ENTER) {

				//TODO send messages?
				messageArea.setText(messageArea.getText() + playerColor + ": " + messageField.getText() + "\n");
				messageField.clear();

			}

		});

		borderPane.setRight(frame);

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
				if (this.dataObject.getTurn().equals(this.playerColor)) {
					this.dataManager.dropDisc(xValue, this.playerColor);
					this.canvas.updateDiscLocations(this.dataObject.getDiscLocations());
					this.client.sendObjectMessage(this.dataObject);
				}
			});
			hBox.getChildren().add(button);
		}
		this.borderPane.setTop(hBox);

		Button resetButton = new Button();
		resetButton.setGraphic(new ImageView("\\Revert.png"));
		hBox.getChildren().add(resetButton);

		resetButton.setOnAction(event -> {

			VBox scoreBox = (VBox) borderPane.getRight();
			TextArea textArea = (TextArea)scoreBox.getChildren().get(1);
			textArea.setText(textArea.getText() + playerColor + " asks for a reset!\n");
			textArea.setText(textArea.getText() + playerColor + " agrees!\n");

		});

	}

	public void setTurn(Disc turn) {

		this.turn = turn;

		Label turnText = (Label) borderPane.getRight();
		turnText.setText("Turn:\n" + this.turn);

	}

	@Override
	public void objectMessageReceived(Object response) {
		if (response instanceof Disc) {
			this.playerColor = (Disc) response;
		}

		if (response instanceof ConnectFourDataObject) {
			if (this.dataObject.getWinner().equals(Disc.EMPTY)) {
				this.dataObject = (ConnectFourDataObject) response;
				this.dataManager.setDataObject((ConnectFourDataObject) response);
				this.canvas.updateDiscLocations(this.dataObject.getDiscLocations());
			}
		}
	}
}
