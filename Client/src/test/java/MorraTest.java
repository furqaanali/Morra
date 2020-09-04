import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MorraTest {

	@Test
	void initTest() {
		Client clientConnection = new Client(null, null, 0);
		assertEquals("Client", clientConnection.getClass().getName(), "clientConnection not properly created");
	}

	@Test
    void clientInitNotNull() {
		Client clientConnection = new Client(null, null, 0);
        assertNotNull(clientConnection, "clientConnection not properly created: Null Value");
    }

	@Test
	void portNumberTest() {
		Client clientConnection = new Client(null, null, 5555);
		assertEquals(5555, clientConnection.portNumber, "port number not working");
	}

	@Test
	void ipAddressTest() {
		Client clientConnection = new Client(null, "127.0.0.1", 0);
		assertEquals("127.0.0.1", clientConnection.host, "ip address not working");
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
