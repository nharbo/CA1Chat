
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

    public static Thread thread;

    public TestClient() {

    }

    @BeforeClass
    public static void setUpClass() {
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                TCPServer.main(null);
            }
        });
        thread.start();

    }

    @AfterClass
    public static void tearDownClass() {
        TCPServer.stopServer();
    }

    @Before
    public void setUp() {
        
    }
    @Test
    public void fullTest() throws IOException{
        DummyClient client = new DummyClient("localhost", 4321);
        DummyClient client2 = new DummyClient("localhost", 4321);
        DummyClient client3 = new DummyClient("localhost", 4321);
        
        
        client.send("USER#c1");
        assertThat(client.received(), is("USERLIST#c1"));
        
        client2.send("USER#c2");
        assertThat(client.received(), is("USERLIST#c1,c2"));
        assertThat(client2.received(), is("USERLIST#c1,c2"));
        
        client3.send("USER#c3");
        assertThat(client.received(), is("USERLIST#c1,c2,c3"));
        assertThat(client2.received(), is("USERLIST#c1,c2,c3"));
        assertThat(client3.received(), is("USERLIST#c1,c2,c3"));
        
        client.send("MSG#c2#hej");
        assertThat(client2.received(), is("MSG#c1#hej"));
        
        client.send("MSG#c2,c3#hej");
        assertThat(client2.received(), is("MSG#c1#hej"));
        assertThat(client3.received(), is("MSG#c1#hej"));
        
        client.send("MSG#*#hej");
        assertThat(client2.received(), is("MSG#c1#hej"));
        assertThat(client3.received(), is("MSG#c1#hej"));
       
        client.send("STOP#");
        assertThat(client2.received(), is("USERLIST#c2,c3"));
        assertThat(client3.received(), is("USERLIST#c2,c3"));
    }
    

}
