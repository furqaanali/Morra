import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import java.util.ArrayList;
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

public class TheGameOfMorra extends Application {

	ImageView opponentImage;
	TextArea display;
	HashMap<String, Scene> sceneMap;
	VBox clientBox;
	Scene startScene;
	Client clientConnection;
	Button clientChoice, chooseGameInfoScene, sendBack, playBtn, view, finalizeChoices;
	Text playerPlay;
	TextField ipAddress, portNumber, guessNumber;
	ArrayList<ImageView> fingers;
	ListView<String> listItems, listItems2;

	Text waitingOnOpp = new Text();
	Text waitingOnPlayers = new Text();
	Button playAgain = new Button("Play Again");
	Button quit = new Button("Quit");
	HBox afterGame = new HBox(playAgain, quit);


	boolean madePlay = false;
	int play;
	int guess;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Client");

		this.clientChoice = new Button("Start Client");

		this.clientChoice.setOnAction(e-> {primaryStage.setScene(sceneMap.get("waiting"));
			primaryStage.setTitle("This is a client");
			clientConnection = new Client(data->{
				Platform.runLater(()->{
					listItems2.getItems().add(data.toString());

				});
			}, ipAddress.getText(), Integer.parseInt(portNumber.getText()));

			clientConnection.start();
		});

		portNumber = new TextField("Port Number");
		ipAddress = new TextField("IP Address");
		portNumber.setMaxWidth(100);
		ipAddress.setMaxWidth(100);

		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(20);
		vbox.getChildren().addAll( portNumber, ipAddress, clientChoice);

		startScene = new Scene(vbox, 800,800);

		listItems = new ListView<String>();
		listItems2 = new ListView<String>();

		sceneMap = new HashMap<String, Scene>();
		sceneMap.put("client",  createClientGui());
		sceneMap.put("gameInfo", createGameInfoGui());
		sceneMap.put("waiting", createWaitingGui());

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});

		sendBack.setOnAction(e->{
			opponentImage.setVisible( false);
			sendBack.setDisable( true);
			waitingOnOpp.setText("");
			clientConnection.data.p1Guess = clientConnection.data.p2Guess = clientConnection.data.p1Plays = clientConnection.data.p2Plays = -1;
			primaryStage.setScene(sceneMap.get("client"));
			display.clear();
		}); // go back to client scene


		playBtn.setOnAction(event -> {
			if ( clientConnection.data.numPlayers > 1) {
				primaryStage.setScene(sceneMap.get("client"));
				waitingOnPlayers.setText("");
			}
			else
				waitingOnPlayers.setText("Waiting for more players...");
		});

		playAgain.setOnAction(event -> {
			opponentImage.setVisible( false);
			if (clientConnection.player == 1) {
				clientConnection.data.p1PlayAgain = true;
			}
			else if (clientConnection.player == 2) {
				clientConnection.data.p2PlayAgain = true;
			}
			clientConnection.data.message = "reset";
			clientConnection.player = -1;
			sendBack.setDisable( true);
			waitingOnOpp.setText("");
			clientConnection.send(clientConnection.data);
			display.clear();
			afterGame.setVisible( false);
			primaryStage.setScene(sceneMap.get("waiting"));
		});

		quit.setOnAction(event -> {
			
			System.exit(0);
		});

		chooseGameInfoScene.setOnAction(e->{
			view.setDisable(false);
			if (clientConnection.data.p1Guess >= 0 && clientConnection.data.p2Guess >= 0
					&& clientConnection.data.p1Plays >= 0 && clientConnection.data.p2Plays >= 0) {
				finalizeChoices.setDisable(false);
				playerPlay.setDisable(false);
				playerPlay.setText("You Played: ?");
				guessNumber.setDisable(false);
				guessNumber.setText("Enter Guess");
				chooseGameInfoScene.setVisible(false);
				primaryStage.setScene(sceneMap.get("gameInfo"));
			}
			else {
				waitingOnOpp.setText("Waiting on opponent move...");
			}
		}); // set new scene

		primaryStage.setScene(startScene);
		primaryStage.show();

	}

	public Scene createGameInfoGui() {

		sendBack = new Button("Return To Game");
		sendBack.setDisable( true);
		view = new Button("View Game Information");
		display = new TextArea();
		display.setVisible(false);
		display.setEditable(false);

		ArrayList<Image> imageArray = new ArrayList<>();
		imageArray.add(new Image("zeroFinger.png", 100, 100, false, true));
		imageArray.add(new Image("oneFinger.png", 100, 100, false, true));
		imageArray.add(new Image("twoFinger.png", 100, 100, false, true));
		imageArray.add(new Image("threeFinger.png", 100, 100, false, true));
		imageArray.add(new Image("fourFinger.png", 100, 100, false, true));
		imageArray.add(new Image("fiveFinger.png", 100, 100, false, true));

		opponentImage = new ImageView();
		Text t1 = new Text("Opponent Play: ");

		view.setOnAction(e->{
			opponentImage.setVisible( true);
			view.setDisable(true);
			display.setVisible(true);
			sendBack.setDisable( false);
			// display points and opponent image hand and guess
			if (clientConnection.player == 1) {
				if (clientConnection.data.p1Guess == clientConnection.data.p1Plays + clientConnection.data.p2Plays &&
					clientConnection.data.p2Guess != clientConnection.data.p1Plays + clientConnection.data.p2Plays) {
					display.appendText("YOU WON THIS ROUND!\n");
				}
				else if (clientConnection.data.p1Guess != clientConnection.data.p1Plays + clientConnection.data.p2Plays &&
						clientConnection.data.p2Guess == clientConnection.data.p1Plays + clientConnection.data.p2Plays) {
					display.appendText("YOU LOST THIS ROUND!\n");
				}
				else {
					display.appendText("IT WAS A TIE!\n");
				}
				display.appendText("Your Points: " + clientConnection.data.p1Points + "\n");
				display.appendText("Opponents Points: " + clientConnection.data.p2Points + "\n");
				display.appendText("Opponents Guess: " + clientConnection.data.p2Guess + "\n");
				opponentImage.setImage(imageArray.get(clientConnection.data.p2Plays));
			}
			else if (clientConnection.player == 2) {
				if (clientConnection.data.p2Guess == clientConnection.data.p1Plays + clientConnection.data.p2Plays &&
					clientConnection.data.p1Guess != clientConnection.data.p1Plays + clientConnection.data.p2Plays) {
					display.appendText("YOU WON THIS ROUND!\n");
				}
				else if (clientConnection.data.p2Guess != clientConnection.data.p1Plays + clientConnection.data.p2Plays &&
					clientConnection.data.p1Guess == clientConnection.data.p1Plays + clientConnection.data.p2Plays) {
					display.appendText("YOU LOST THIS ROUND!\n");
				}
				else {
					display.appendText("IT WAS A TIE!\n");
				}
				display.appendText("Your Points: " + clientConnection.data.p2Points + "\n");
				display.appendText("Opponents Points: " + clientConnection.data.p1Points + "\n");
				display.appendText("Opponents Guess: " + clientConnection.data.p1Guess + "\n");
				opponentImage.setImage(imageArray.get(clientConnection.data.p1Plays));
			}

			if (clientConnection.data.p1Points == 2) {
				sendBack.setDisable(true);
				if (clientConnection.player == 1) {
					display.appendText("YOU WON THE GAME!\n");
					clientConnection.data.message = "resetPlayers";
					clientConnection.send(clientConnection.data);
				}
				else if (clientConnection.player == 2) {
					display.appendText("YOU LOST THE GAME!\n");
					clientConnection.data.message = "resetPlayers";
					clientConnection.send(clientConnection.data);
				}
				afterGame.setVisible(true);
			}
			else if (clientConnection.data.p2Points == 2) {
				sendBack.setDisable(true);
				if (clientConnection.player == 2) {
					display.appendText("YOU WON THE GAME!\n");
					clientConnection.data.message = "resetPlayers";
					clientConnection.send(clientConnection.data);
				}
				else if (clientConnection.player == 1) {
					display.appendText("YOU LOST THE GAME!\n");
					clientConnection.data.message = "resetPlayers";
					clientConnection.send(clientConnection.data);
				}
				afterGame.setVisible(true);
			}

		});

		afterGame.setVisible(false);
		VBox clientBox = new VBox(view, display, t1, opponentImage, sendBack, afterGame);
		return new Scene(clientBox, 400, 300);

	}

	public Scene createWaitingGui() {
		playBtn = new Button("Play");
		VBox vbox = new VBox( playBtn, waitingOnPlayers);
		vbox.setSpacing(20);
		vbox.setAlignment(Pos.CENTER);
		return new Scene(vbox, 300, 300);
	}

	public Scene createClientGui() {
		fingers = new ArrayList<>();
		chooseGameInfoScene = new Button("Continue");
		chooseGameInfoScene.setVisible(false);
		ImageView zeroFingerImage = new ImageView( new Image("zeroFinger.png", 100, 100, false, true));
		ImageView oneFingerImage = new ImageView( new Image("oneFinger.png", 100, 100, false, true));
		ImageView twoFingerImage = new ImageView( new Image("twoFinger.png", 100, 100, false, true));
		ImageView threeFingerImage = new ImageView( new Image("threeFinger.png", 100, 100, false, true));
		ImageView fourFingerImage = new ImageView( new Image("fourFinger.png", 100, 100, false, true));
		ImageView fiveFingerImage = new ImageView( new Image("fiveFinger.png", 100, 100, false, true));
		fingers.add(zeroFingerImage);
		fingers.add( oneFingerImage);
		fingers.add( twoFingerImage);
		fingers.add( threeFingerImage);
		fingers.add( fourFingerImage);
		fingers.add( fiveFingerImage);
		HBox fingerOptions = new HBox();
		fingerOptions.getChildren().addAll(zeroFingerImage, oneFingerImage, twoFingerImage, threeFingerImage, fourFingerImage, fiveFingerImage);
		fingerOptions.setAlignment(Pos.BOTTOM_CENTER);
		fingerOptions.setSpacing(10);
		playerPlay = new Text( "You Played: ?");
		finalizeChoices = new Button("Finalize Choices");
		guessNumber = new TextField( "Enter Guess");
		HBox buttons = new HBox();
		buttons.getChildren().addAll( playerPlay, guessNumber, finalizeChoices);
		buttons.setAlignment(Pos.CENTER);
		buttons.setSpacing(10);

		for (int i = 0; i < fingers.size(); ++i) {
			int finalI = i;
			fingers.get(i).setOnMouseClicked(event -> {
				if ( finalizeChoices.isDisable() == false) {
					playerPlay.setText("You Played: " + (finalI));
					madePlay = true;
					play = finalI;
				}
			});
		}

		finalizeChoices.setOnAction( event -> {
			if ( madePlay && Integer.parseInt( guessNumber.getText()) < 11 && Integer.parseInt( guessNumber.getText()) >= 0) {
				guess = Integer.parseInt( guessNumber.getText());
				guessNumber.setDisable( true);
				finalizeChoices.setDisable( true);
				playerPlay.setDisable( true);

				if ( clientConnection.player == 1) {
					clientConnection.data.p1Plays = play;
					clientConnection.data.p1Guess = guess;
					clientConnection.data.sentFromPlayer = 1;
					System.out.println("PLAYER 1 update ENTERED");
				}
				else if ( clientConnection.player == 2) {
					clientConnection.data.p2Plays = play;
					clientConnection.data.p2Guess = guess;
					clientConnection.data.sentFromPlayer = 2;
					System.out.println("PLAYER 2 update ENTERED");
				}

				clientConnection.send( clientConnection.data);
				chooseGameInfoScene.setVisible(true);
			}
			else
				guessNumber.setText("Enter valid guess");

		});

		if ( finalizeChoices.isDisable() == true) {
			if (clientConnection.data.p1Guess > 1 && clientConnection.data.p2Guess > 1) {
				finalizeChoices.setDisable(false);
			}
		}

		clientBox = new VBox(10,listItems2, fingerOptions, buttons, chooseGameInfoScene, waitingOnOpp);
		return new Scene(clientBox, 400, 300);

	}

}
