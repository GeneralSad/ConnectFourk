package ConnectFour;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ConnectFourModule extends Application {
	private BorderPane borderPane;
	private ConnectFourCanvas canvas;
	private ConnectFourDataManager dataManager;

	public ConnectFourModule() {
		this.borderPane = new BorderPane();
		this.dataManager = new ConnectFourDataManager();
		this.canvas = new ConnectFourCanvas(this.borderPane, this.dataManager.getDiskLocations());

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setScene(new Scene(this.borderPane));
		primaryStage.show();
	}
}
