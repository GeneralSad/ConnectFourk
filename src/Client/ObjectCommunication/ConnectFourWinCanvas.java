package Client.ObjectCommunication;

import ConnectFour.ConnectFourCanvas;
import ConnectFour.ConnectFourDataManager;
import ConnectFour.ConnectFourDataObject;
import ConnectFour.Disc;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ConnectFourWinCanvas {

    private Disc playerColor;
    private ConnectFourDataObject dataObject;
    private ConnectFourDataManager dataManager;
    private ConnectFourkObjectClient client;
    private ConnectFourCanvas canvas;

    private BorderPane winPane;

    private TextArea winMessageArea;

    public ConnectFourWinCanvas(Disc playerColor, ConnectFourDataObject dataObject, ConnectFourDataManager dataManager, ConnectFourkObjectClient client, ConnectFourCanvas canvas) {
        this.playerColor = playerColor;
        this.dataObject = dataObject;
        this.dataManager = dataManager;
        this.client = client;
        this.canvas = canvas;
    }

    public void construct() {
        BorderPane winPane;

        winPane = new BorderPane();

        Label winLabel = new Label();

        if (playerColor == dataObject.getWinner()) {
            winLabel.setText("You won!");
        } else {
            winLabel.setText("You lost!");
        }

        Button rematchButton = new Button("Rematch");

        rematchButton.setOnAction(event -> {

            if (!dataObject.getRequestReset().equals(Disc.EMPTY) && dataObject.getRequestReset().equals(playerColor)) {
                dataManager.resetConnectFourBoard();
                canvas.updateDiscLocations(dataObject.getDiscLocations());
                dataObject.setRequestReset(Disc.EMPTY);
                client.sendObjectMessage(dataObject);
                client.sendObjectMessage(playerColor + " agreed to the reset!");
            } else {
                dataObject.setRequestReset(playerColor);
                client.sendObjectMessage(dataObject);
                client.sendObjectMessage(playerColor + " asks for a reset!");
            }

        });

        this.winMessageArea = new TextArea();
        TextField winMessageField = new TextField();

        VBox vBox = new VBox();
        vBox.getChildren().add(this.winMessageArea);
        vBox.getChildren().add(winMessageField);

        winPane.setRight(vBox);
        winPane.setCenter(rematchButton);
        winPane.setTop(winLabel);

        winMessageField.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ENTER) {

                //TODO send messages?
                client.sendObjectMessage(playerColor + ": " + winMessageField.getText());
                winMessageField.clear();

            }

        });

        this.winPane = winPane;
    }

    public BorderPane getWinPane() {
        return winPane;
    }

    public TextArea getMessageArea() {
        return  this.winMessageArea;
    }
}
