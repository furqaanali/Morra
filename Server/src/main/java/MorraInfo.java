import java.io.Serializable;

public class MorraInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    int p1Points;
    int p2Points;
    int p1Plays;
    int p2Plays;
    int p1Guess;
    int p2Guess;
    int numPlayers;
    int sentFromPlayer;
    String message;
    boolean p1PlayAgain;
    boolean p2PlayAgain;
}
