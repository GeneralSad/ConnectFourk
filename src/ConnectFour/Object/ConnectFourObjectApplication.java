package ConnectFour.Object;

import Client.ObjectCommunication.ConnectFourkObjectClient;
import ConnectFour.ConnectFourCanvas;
import ConnectFour.ConnectFourDataManager;
import ConnectFour.ConnectFourDataObject;
import ConnectFour.Disc;
import Server.ObjectCommunication.ObjectResponseCallback;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ConnectFourObjectApplication extends Application implements ObjectResponseCallback {
	private BorderPane borderPane;
	private ConnectFourCanvas canvas;
	private ConnectFourDataManager dataManager;
	private ConnectFourDataObject dataObject;
	private ConnectFourkObjectClient client;

	private Disc playerColor;
	private TextArea messageArea;
	private Label turnText;
	private Disc turn;
	private Label playerText;

	public ConnectFourObjectApplication() {
		this.borderPane = new BorderPane();
		this.dataObject = new ConnectFourDataObject();
		this.dataManager = new ConnectFourDataManager(this.dataObject);
		this.playerColor = Disc.EMPTY;
		this.canvas = new ConnectFourCanvas(this.borderPane, this.dataObject.getDiscLocations());
		this.client = new ConnectFourkObjectClient("localhost", 27272, this);
		this.client.startClient();
	}

	@Override
	public void start(Stage primaryStage) {
		initButtons();
		primaryStage.setScene(new Scene(this.borderPane));
		primaryStage.setTitle("Connect 4");
		primaryStage.setMinWidth(870);
		primaryStage.setMinHeight(600);
		primaryStage.setResizable(false);
		primaryStage.show();

		this.playerText = new Label("You are: " + this.playerColor);
		this.playerText.setPadding(new Insets(0, 10, 0, 10));
		this.playerText.setFont(new Font(15));

		this.turnText = new Label("Turn: " + this.dataObject.getTurn());
		this.turnText.setPadding(new Insets(0, 10, 5, 10));
		this.turnText.setFont(new Font(15));

		VBox frame = new VBox();
		frame.setAlignment(Pos.TOP_LEFT);
		frame.getChildren().add(playerText);
		frame.getChildren().add(turnText);

		this.messageArea = new TextArea();
		this.messageArea.setEditable(false);
		this.messageArea.setWrapText(true);
		this.messageArea.setMaxWidth(151);
		this.messageArea.setMinWidth(150);

		frame.getChildren().add(this.messageArea);

		TextField messageField = new TextField();
		frame.getChildren().add(messageField);

		messageField.setOnKeyPressed(event -> {

			if (event.getCode() == KeyCode.ENTER) {
				this.client.sendObjectMessage(this.playerColor + ": " + messageField.getText());
				messageField.clear();
			}
		});

		this.borderPane.setRight(frame);
	}

	public void initButtons() {
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.TOP_LEFT);
		hBox.setPadding(new Insets(10, 0, 10, 25));
		hBox.setSpacing(61);

		for (int i = 0; i < 7; i++) {
			Button dropButton = new Button();
			dropButton.setGraphic(new ImageView("\\ArrowDown.png"));

			int xValue = i;
			dropButton.setOnAction(event -> {
				if (this.dataObject.getTurn().equals(this.playerColor) && this.dataObject.getWinner().equals(Disc.EMPTY)) {
					this.dataManager.dropDisc(xValue, this.playerColor);
					this.canvas.updateDiscLocations(this.dataObject.getDiscLocations());
					this.client.sendObjectMessage(this.dataObject);


				}
			});
			hBox.getChildren().add(dropButton);
		}
		this.borderPane.setTop(hBox);

		Button resetButton = new Button();
		resetButton.setGraphic(new ImageView("\\Revert.png"));
		hBox.getChildren().add(resetButton);

		resetButton.setOnAction(event -> {

			if (!this.dataObject.getRequestReset().equals(this.playerColor)) {
				if (!this.dataObject.getRequestReset().equals(Disc.EMPTY)) {
					this.dataManager.resetConnectFourBoard();
					this.dataObject.resetDataObject();
					this.canvas.updateDiscLocations(this.dataObject.getDiscLocations());

					this.client.sendObjectMessage(this.playerColor + " agreed to the reset!");
					this.client.sendObjectMessage(this.dataObject);
				} else {
					this.dataObject.setRequestReset(this.playerColor);
					this.client.sendObjectMessage(this.dataObject);
					this.client.sendObjectMessage(this.playerColor + " asks for a reset!");
				}
			}
		});
	}

	@Override
	public void stop() {
		this.client.close();
	}

	@Override
	public void objectMessageReceived(Object response) {
		System.out.println(this.playerColor + " received: " + response);
		if (response instanceof Disc) {
			this.playerColor = (Disc) response;

			Platform.runLater(() -> this.playerText.setText("You are: " + response));
		}

		if (response instanceof ConnectFourDataObject) {
			this.dataObject = ((ConnectFourDataObject) response);
			this.dataManager.setDataObject(this.dataObject);

			if (this.dataObject.getWinner().equals(Disc.EMPTY)) {
				this.canvas.updateDiscLocations(this.dataObject.getDiscLocations());
				this.turn = this.dataObject.getTurn();

				Platform.runLater(() -> this.turnText.setText("Turn: " + this.turn));

			} else {
				this.canvas.updateDiscLocations(this.dataObject.getDiscLocations());

				if (this.dataObject.getWinner() != this.playerColor && !this.dataObject.isGameDone()) {
					this.client.sendObjectMessage(this.dataObject.getWinner() + " has won!");
				}
				this.dataObject.setGameDone(true);
				this.canvas.drawWinTekst(this.dataObject.getWinner() + " WON");
			}
		}

		if (response instanceof String) {
			this.messageArea.setText(this.messageArea.getText() + response + "\n");
			this.messageArea.selectPositionCaret(this.messageArea.getLength());
		}
	}

}
