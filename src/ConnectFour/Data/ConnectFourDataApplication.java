package ConnectFour.Data;

import Client.DataCommunication.ConnectFourkDataClient;
import ConnectFour.ConnectFourCanvas;
import ConnectFour.ConnectFourDataManager;
import ConnectFour.ConnectFourDataObject;
import ConnectFour.Disc;
import Server.DataCommunication.DataResponseCallback;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Arrays;

public class ConnectFourDataApplication extends Application implements DataResponseCallback {
	private BorderPane borderPane;
	private ConnectFourCanvas canvas;
	private ConnectFourDataManager dataManager;
	private ConnectFourDataObject dataObject;
	private ConnectFourkDataClient client;

	private Disc playerColor;
	private TextArea messageArea;
	private Label turnText;
	private Label playerText;

	public ConnectFourDataApplication() {
		this.borderPane = new BorderPane();
		this.dataObject = new ConnectFourDataObject();
		this.dataManager = new ConnectFourDataManager(this.dataObject);
		this.canvas = new ConnectFourCanvas(this.borderPane, this.dataObject.getDiscLocations());
		this.client = new ConnectFourkDataClient("localhost", 27272, this);
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
		frame.getChildren().add(this.playerText);
		frame.getChildren().add(this.turnText);

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
				this.client.sendMessage("MESSAGE " + this.playerColor + ": " + messageField.getText());
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

					this.client.sendMessage("DROP " + this.playerColor + " " + xValue);
					setTurn(getOtherPlayerColor(this.playerColor));

					if (this.dataObject.getWinner() != Disc.EMPTY) {
						this.client.sendMessage("WIN " + this.playerColor);
					}
				}
			});
			hBox.getChildren().add(dropButton);
		}
		this.borderPane.setTop(hBox);

		Button resetButton = new Button();
		resetButton.setGraphic(new ImageView("\\Revert.png"));
		hBox.getChildren().add(resetButton);

		resetButton.setOnAction(event -> {
			if (this.dataObject.getRequestReset().equals(Disc.EMPTY)) {
				this.client.sendMessage("MESSAGE " + this.playerColor + " asks for a reset!");
				this.dataObject.setRequestReset(this.playerColor);
				this.client.sendMessage("WANTRESET " + this.playerColor);
			} else if (!this.dataObject.getRequestReset().equals(this.playerColor)) {
				this.client.sendMessage("MESSAGE " + this.playerColor + " agreed!");
				this.dataManager.resetConnectFourBoard();
				this.canvas.updateCanvas();
				this.dataObject.resetDataObject();
				this.client.sendMessage("RESET");
			}
		});
	}

	@Override
	public void stop() {
		this.client.close();
	}

	public void setTurn(Disc turn) {
		this.dataObject.setTurn(turn);
		Platform.runLater(() -> this.turnText.setText("Turn: " + turn));
	}

	@Override
	public void stringMessageReceived(String message) {
		String[] commands = message.split(" ");
		System.out.println(Arrays.toString(commands));

		//The first word in a message specifies what kind of data it contains.
		switch (commands[0]) {
			case "DISC":

				this.playerColor = Disc.valueOf(commands[1]);
				Platform.runLater(() -> this.playerText.setText("You are: " + this.playerColor));
				break;
			case "DROP":

				if (!commands[1].equals(this.playerColor.toString())) {
					this.dataManager.dropDisc(Integer.parseInt(commands[2]), Disc.valueOf(commands[1]));
					this.canvas.updateDiscLocations(this.dataObject.getDiscLocations());
					setTurn(this.playerColor);
				}
				break;
			case "MESSAGE":

				this.messageArea.setText(this.messageArea.getText() + message.substring(8) + "\n");
				this.messageArea.positionCaret(this.messageArea.getLength());
				break;
			case "WANTRESET":

				if (!commands[1].equals(this.playerColor.toString())) {
					this.dataObject.setRequestReset(Disc.valueOf(commands[1]));
				}
				break;
			case "RESET":

				this.dataManager.resetConnectFourBoard();
				this.canvas.updateCanvas();
				this.dataObject.resetDataObject();
				setTurn(Disc.RED);
				break;
			case "WIN":

				this.dataObject.setWinner(Disc.valueOf(commands[1]));
				this.messageArea.setText(this.messageArea.getText() + this.dataObject.getWinner() + " has won!\n");
				this.canvas.drawWinTekst(this.dataObject.getWinner() + " WON");
				break;
		}
	}

	private Disc getOtherPlayerColor(Disc playerColor) {
		if (playerColor.equals(Disc.RED)) {
			return Disc.YELLOW;
		} else {
			return Disc.RED;
		}
	}
}
