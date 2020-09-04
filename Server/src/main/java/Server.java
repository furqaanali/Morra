import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;

public class Server{

	int count = 1;
	int portNumber;
	int numPlayers = 0;
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;
	MorraInfo currentGameInfo = new MorraInfo();
	boolean p1isSent = false;
	boolean p2isSent = false;

	Server(Consumer<Serializable> call, int portNum){
		callback = call;
		server = new TheServer();
		server.start();
		portNumber = portNum;
	}

	public class TheServer extends Thread{

		public void run() {

			try(ServerSocket mysocket = new ServerSocket( portNumber);){
				System.out.println("Server is waiting for a client!");

				while(true) {

					ClientThread c = new ClientThread(mysocket.accept(), count);
					callback.accept("client has connected to server: " + "client #" + count);
					clients.add(c);
					callback.accept("Number of clients:" + clients.size());

					c.start();

					count++;
					numPlayers++;
				}
			}//end of try
			catch(Exception e) {
				callback.accept("Server socket did not launch");
			}
		}//end of while
	}


	class ClientThread extends Thread{

		Socket connection;
		int count;
		ObjectInputStream in;
		ObjectOutputStream out;

		ClientThread(Socket s, int count){
			this.connection = s;
			this.count = count;
		}

		public void updateClients(MorraInfo data) {
			for(int i = 0; i < clients.size(); i++) {
				ClientThread t = clients.get(i);
				try {
					t.out.writeObject(data);
					t.out.reset();
				}
				catch(Exception e) {}
			}
		}

		public void run(){

			try {
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);
			}
			catch(Exception e) {
				System.out.println("Streams not open");
			}

			currentGameInfo.numPlayers = numPlayers;
			currentGameInfo.message = "new client on server: client #"+count;
			currentGameInfo.p1PlayAgain = currentGameInfo.p2PlayAgain = false;
			currentGameInfo.p1Plays = currentGameInfo.p2Plays = currentGameInfo.p1Guess = currentGameInfo.p2Guess = -1;
			updateClients( currentGameInfo);

			while(true) {
				try {
					MorraInfo data = (MorraInfo) in.readObject();

					if ( data.message.compareTo("reset") == 0) {
						currentGameInfo.p1Plays = currentGameInfo.p2Plays = currentGameInfo.p1Guess = currentGameInfo.p2Guess = -1;
						currentGameInfo.p1Points = currentGameInfo.p2Points = currentGameInfo.sentFromPlayer = 0;
						currentGameInfo.numPlayers++;

						updateClients( currentGameInfo);
						currentGameInfo.message = "";

						if (data.p1PlayAgain == true) {
							callback.accept("Player 1 is Playing Again.");
							data.p1PlayAgain = false;
						}
						else if (data.p2PlayAgain == true) {
							callback.accept("Player 2 is Playing Again.");
							data.p2PlayAgain = false;
						}

					}

					if ( data.message.compareTo("resetPlayers") == 0) {
						currentGameInfo.numPlayers = currentGameInfo.sentFromPlayer = 0;
						updateClients( currentGameInfo);
						currentGameInfo.message = "";
					}

					if ( data.sentFromPlayer == 1) {
						callback.accept("Player 1 Hand:" + data.p1Plays);
						callback.accept("Player 1 Guess:" + data.p1Guess);
						currentGameInfo.p1Plays = data.p1Plays;
						currentGameInfo.p1Guess = data.p1Guess;
						p1isSent = true;
					}
					else if ( data.sentFromPlayer == 2) {
						callback.accept("Player 2 Hand:" + data.p2Plays);
						callback.accept("Player 2 Guess:" + data.p2Guess);
						currentGameInfo.p2Plays = data.p2Plays;
						currentGameInfo.p2Guess = data.p2Guess;
						p2isSent = true;
					}

					if (p1isSent && p2isSent) { // if both were sent
						// eval winner
						int sumOfHands = currentGameInfo.p1Plays + currentGameInfo.p2Plays;

						if (currentGameInfo.p1Guess == sumOfHands && currentGameInfo.p2Guess != sumOfHands) { // p1 won
							callback.accept("Player 1 Won Round.");
							currentGameInfo.p1Points++;
						}
						else if (currentGameInfo.p2Guess == sumOfHands && currentGameInfo.p1Guess != sumOfHands) { // p2 won
							callback.accept("Player 2 Won Round.");
							currentGameInfo.p2Points++;
						}

						callback.accept("Player 1 Points:" + currentGameInfo.p1Points);
						callback.accept("Player 2 Points:" + currentGameInfo.p2Points);

						// check if someone won game
						if (currentGameInfo.p1Points == 2) { // p1 got 2 points
							callback.accept("Player 1 Won Game.");
						}
						else if (currentGameInfo.p1Points == 2) { // p2 got 2 points
							callback.accept("Player 2 Won Game.");
						}

						// reset isSent
						p1isSent = false;
						p2isSent = false;

						currentGameInfo.message = "client #"+count+" said: ";
						updateClients( currentGameInfo);
					}

				}
				catch(Exception e) {
					callback.accept("Client: " + count + " has disconnected");
					numPlayers--;
					currentGameInfo.message = "Client #"+count+" has left the server!";
					updateClients(currentGameInfo);
					clients.remove(this);
					callback.accept("Number of clients:" + clients.size());
					break;
				}
			}
		}//end of run

	}//end of client thread
}