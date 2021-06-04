package ConnectFour;

import Client.DataCommunication.ConnectFourkDataClient;
import Client.ObjectCommunication.ConnectFourkObjectClient;
import Server.DataCommunication.DataResponseCallback;
import Server.ObjectCommunication.ObjectResponseCallback;
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
	private Disc turn;
	private ConnectFourDataObject dataObject;
	private ConnectFourkDataClient client;

	private Disc playerColor;
	private TextArea messageArea;
	private Label turnText;

	public ConnectFourDataApplication() {
		this.borderPane = new BorderPane();
		this.dataObject = new ConnectFourDataObject();
		this.dataManager = new ConnectFourDataManager(this.dataObject);
		this.canvas = new ConnectFourCanvas(this.borderPane, this.dataObject.getDiscLocations());
		this.client = new ConnectFourkDataClient("localhost", 27272, this);
		this.client.startClient();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initButtons();
		primaryStage.setScene(new Scene(this.borderPane));
		primaryStage.setTitle("Connect 4");
		primaryStage.setMinWidth(870);
		primaryStage.setMinHeight(600);
		//primaryStage.setResizable(false);
		primaryStage.show();

		this.turnText = new Label("Turn: \n" + this.dataObject.getTurn());
		turnText.setWrapText(true);
		turnText.setPadding(new Insets(0, 10, 0, 10));
		turnText.setFont(new Font(30));

		VBox frame = new VBox();
		frame.setAlignment(Pos.TOP_LEFT);
		frame.getChildren().add(turnText);

		this.messageArea = new TextArea();
		messageArea.setEditable(false);
		messageArea.setWrapText(true);
		messageArea.setMaxWidth(151);
		messageArea.setMinWidth(150);
		frame.getChildren().add(messageArea);


		TextField messageField = new TextField();
		frame.getChildren().add(messageField);

		messageField.setOnKeyPressed(event -> {

			if (event.getCode() == KeyCode.ENTER) {
				this.client.sendMessage("MESSAGE " + this.playerColor + ": " + messageField.getText());
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

					this.client.sendMessage("DROP " + this.playerColor + " " +  xValue);
					setTurn(getOtherPlayerColor(this.playerColor));

				}
			});
			hBox.getChildren().add(button);
		}
		this.borderPane.setTop(hBox);

		Button resetButton = new Button();
		resetButton.setGraphic(new ImageView("\\Revert.png"));
		hBox.getChildren().add(resetButton);

		resetButton.setOnAction(event -> {

			//TODO Send and receive message for reset
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

	public void setTurn(Disc turn) {
		this.dataObject.setTurn(turn);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				turnText.setText("Turn:\n" + turn);
			}
		});
	}

	@Override
	public void stringMessageReceived(String message) {
		String[] commands = message.split(" ");
		System.out.println(Arrays.toString(commands));

		switch (commands[0]) {
			case "DISC":
					this.playerColor = Disc.valueOf(commands[1]);
					System.out.println(this.playerColor);

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
