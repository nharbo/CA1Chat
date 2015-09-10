
import client.TCPClient;
import server.TCPServer;
import java.io.IOException;
import static org.hamcrest.CoreMatchers.is;
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
     TCPClient client = new TCPClient();
     TCPClient client2 = new TCPClient();
     TCPClient client3 = new TCPClient();
     TCPClient client4 = new TCPClient();
    
    @Test
    public void Connect() throws IOException {

        String exM = "USERLIST#nico";
        client.connect("localhost", 4321);
        client.send("USER#nico");
        assertThat(exM, is(client.received(exM)));
    }

    @Test
    public void sendMSingle() throws IOException {
       

        client.connect("localhost", 4321);
        client2.connect("localhost", 4321);

        client.send("USER#nico");
        client2.send("USER#flo");
        client2.send("MSG#nico#hej");

        assertThat("MSG#flo#hej", is(client.received("MSG#flo#hej")));
    }

    @Test
    public void sendMSelected() throws IOException {
        String exM = "MSG#nico#hejsa drenge";

        client.connect("localhost", 4321);
        client2.connect("localhost", 4321);
        client3.connect("localhost", 4321);
        client4.connect("localhost", 4321);

        client.send("USER#nico");
        client2.send("USER#flo");
        client3.send("USER#hiller");
        client4.send("USER#zeus");

        client.send("MSG#flo,hiller,zeus#hejsa drenge");

        assertThat(exM, is(client2.received(exM)));
        assertThat(exM, is(client3.received(exM)));
        assertThat(exM, is(client4.received(exM)));

    }

    @Test
    public void sendMAll() throws IOException {
        String exM = "MSG#nico#hej alle users";
        
        client.connect("localhost", 4321);
        client2.connect("localhost", 4321);
        client3.connect("localhost", 4321);
        client4.connect("localhost", 4321);

        client.send("USER#nico");
        client2.send("USER#flo");
        client3.send("USER#hiller");
        client4.send("USER#zeus");

        client.send("MSG#*#hej alle users");
        assertThat(exM, is(client2.received(exM)));
        assertThat(exM, is(client3.received(exM)));
        assertThat(exM, is(client4.received(exM)));
        //Checker userliste efter flere folk er connected
        assertThat("USERLIST#nico,flo,hiller,zeus", is(client4.received("USERLIST#nico,flo,hiller,zeus")));
    }
}
