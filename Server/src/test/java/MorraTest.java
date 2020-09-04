import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import javafx.scene.control.ListView;
import javafx.application.Platform;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;

class MorraTest {


	@Test
	void initTest() {
		Server serverConnection = new Server(null, 0);
		assertEquals("Server", serverConnection.getClass().getName(), "serverConnection not properly created");
	}

	@Test
    void serverInitNotNull() {
		Server serverConnection = new Server(null, 0);
        assertNotNull( serverConnection, "serverConnection not properly created: Null Value");
    }

	@Test
	void portNumberTest() {
		Server serverConnection = new Server(null, 5555);
		assertEquals(5555, serverConnection.portNumber, "port number not working");
	}

	@Test
	void dataTest() {
		MorraInfo data = new MorraInfo();
		assertEquals("MorraInfo", data.getClass().getName(), "morraInfo not properly created");
	}

	@Test
	void dataNotNullTest() {
		MorraInfo data = new MorraInfo();
        assertNotNull(data, "morrainfo not properly created: Null Value");
	}

	@Test
	void testPoints() {
		MorraInfo data = new MorraInfo();
		data.p1Points = 1;
		assertEquals(1, data.p1Points, "morrainfo error");
	}

	@Test
	void testSentFromPlayer() {
		MorraInfo data = new MorraInfo();
		assertEquals(0, data.sentFromPlayer, "morrainfo error");
	}

	@Test
	void testPlays() {
		MorraInfo data = new MorraInfo();
		data.p1Plays = 10;
		assertEquals(10, data.p1Plays, "morrainfo error");
	}

	@Test
	void testGuess() {
		MorraInfo data = new MorraInfo();
		data.p1Guess = 5;
		assertEquals(5, data.p1Guess, "morrainfo error");
	}

	@Test
	void testNumOfPlayers() {
		MorraInfo data = new MorraInfo();
		data.numPlayers++;
		assertEquals(1, data.numPlayers, "morrainfo error");	
	}

}
