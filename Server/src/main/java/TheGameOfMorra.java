import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.text.Text;

public class TheGameOfMorra extends Application {


	TextField c1, portNumber;
	Button serverChoice, b1;
	HashMap<String, Scene> sceneMap;
	Scene startScene;
	Server serverConnection;
	ListView<String> listItems, listItems2;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("The Networked Client/Server GUI Example");

		this.serverChoice = new Button("Start Server");

		this.serverChoice.setOnAction(e->{ primaryStage.setScene(sceneMap.get("server"));
			primaryStage.setTitle("This is the Server");
			serverConnection = new Server(data -> {
				Platform.runLater(()->{
					listItems.getItems().add(data.toString());
				});

			}, Integer.parseInt( portNumber.getText()));

		});

		portNumber = new TextField("Port Number");
		portNumber.setMaxWidth(100);

		VBox vbox = new VBox();
		vbox.getChildren().addAll( portNumber, serverChoice);
		vbox.setSpacing(10);
		vbox.setAlignment(Pos.CENTER);

		startScene = new Scene(vbox, 800,800);

		listItems = new ListView<String>();
		listItems2 = new ListView<String>();

		c1 = new TextField();
		b1 = new Button("Send");

		sceneMap = new HashMap<String, Scene>();
		sceneMap.put("server",  createServerGui());

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});

		primaryStage.setScene(startScene);
		primaryStage.show();
	}

	public Scene createServerGui() {

		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-background-color: #C4C4C4");
		pane.setCenter(listItems);
		return new Scene(pane, 500, 400);
	}

}
