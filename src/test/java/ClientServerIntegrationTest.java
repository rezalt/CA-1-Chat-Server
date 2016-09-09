/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mycompany.ca1.client.EchoClient;
import com.mycompany.ca1.server.EchoServer;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author TimmosQuadros
 */
public class ClientServerIntegrationTest {
    static EchoClient client1;
    static EchoClient client2;

    public ClientServerIntegrationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] args = new String[2];
                args[0] = "localhost";
                args[1] = "7777";
                EchoServer.main(args);
            }
        }).start();
        client1= new EchoClient();
        client2= new EchoClient();
        client1.connect("localhost", 7777);
        client1.send("LOGIN:U1");
        client1.receive();
        client2.connect("localhost", 7777);
        client2.send("LOGIN:U2");
        client1.receive();
        client2.receive();
    }

    @AfterClass
    public static void tearDownClass() {
        EchoServer.stopServer();
    }

    @Before
    public void setUp() {
        
    }

    @After
    public void tearDown() {
    }

//    @Test
//    public void send() throws IOException {
//        EchoClient client = new EchoClient();
//        client.connect("localhost", 7777);
//        client.send("Hello");
//        assertEquals("HELLO", client.receive());
//        client.stop();
//    }
//
//    @Test
//    public void stop() throws IOException {
//        EchoClient client = new EchoClient();
//        client.connect("localhost", 7777);
//        client.send("Hello");
//        assertEquals("HELLO", client.receive());
//        client.stop();
//        client.receive(); //We have to try to receive something in order to see if the client has closed its conecction!
//        assertTrue(client.isStopped());
//    }
    @Test
    public void testLogin() throws IOException {
        EchoClient client3 = new EchoClient();
        client3.connect("localhost", 7777);
        client3.send("LOGIN:U3");
        assertEquals("CLIENTLIST:U1,U2,U3", client3.receive());
        client3.send("LOGOUT:");
    }

    @Test
    public void testSendMessage() throws IOException {
        client1.receive();
        client2.receive();
        client2.receive();
        client1.send("MSG:U2:Hej");
        assertEquals("MSGRESP:U1:Hej",client2.receive());
    }
//    
    @Test
    public void testLogout() throws IOException{
        client1.send("LOGOUT:");
        assertEquals("CLIENTLIST:U2",client2.receive());
    }
}
