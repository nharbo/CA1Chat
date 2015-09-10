
import client.TCPClient;
import server.TCPServer;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Test;

public class TestClient {

    public TestClient() {

    }

    @BeforeClass
    public static void setUpClass() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                TCPServer.main(null);
            }
        }).start();
    }

    @AfterClass
    public static void tearDownClass() {
        TCPServer.stopServer();
    }

    @Before
    public void setUp() {

    }

    @Test
    public void send() throws IOException {
        TCPClient client = new TCPClient();
        client.connect("localhost", 4321);
        client.send("USER#nico");
        assertEquals("USERLIST#nico,", client.received());
    }

}
