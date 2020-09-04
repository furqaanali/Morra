import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread{

	MorraInfo data;
	int player = -1;
	String host;
	int portNumber;
	boolean isReady = false;

	Socket socketClient;
	ObjectOutputStream out;
	ObjectInputStream in;

	private Consumer<Serializable> callback;

	Client(Consumer<Serializable> call, String hostString, int portNum){

		callback = call;
		host = hostString;
		portNumber = portNum;
	}

	public void run() { //"127.0.0.1"

		try {
			socketClient= new Socket(host,portNumber);
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {}

		while(true) {

			try {

				data = (MorraInfo)in.readObject();

				if (player == 1) {
					callback.accept("-----------------------");
					callback.accept("Your points: " + data.p1Points);
					callback.accept("Your play: " + data.p1Plays);
					callback.accept("Your guess: " + data.p1Guess);
					callback.accept("Opponent points: " + data.p2Points);
					callback.accept("Opponent play: " + data.p2Plays);
					callback.accept("Opponent guess: " + data.p2Guess);
				}
				else if (player == 2) {
					callback.accept("-----------------------");
					callback.accept("Your points: " + data.p2Points);
					callback.accept("Your play: " + data.p2Plays);
					callback.accept("Your guess: " + data.p2Guess);
					callback.accept("Opponent points: " + data.p1Points);
					callback.accept("Opponent play: " + data.p1Plays);
					callback.accept("Opponent guess: " + data.p1Guess);
				}

				if ( player == -1 ) {
					if ( data.numPlayers == 1)
						player = 1;
					else if ( data.numPlayers == 2)
						player = 2;
				}
				isReady = true;

			}
			catch(Exception e) {
				System.out.println("Exc entered");
			}
		}

	}

	public void send(MorraInfo data) {

		try {
			out.writeObject(data);
			out.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
