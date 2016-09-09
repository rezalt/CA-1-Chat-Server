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

    EchoClient client;
    EchoClient client1;

    public ClientServerIntegrationTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] args = new String[2];
                args[0] = "localhost";
                args[1] = "7777";
                EchoServer.main(args);
            }
        }).start();
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
    public void testSendClientList() throws IOException {
        EchoClient client = new EchoClient();
        Echoclient1 = new EchoClient();
        client.send("LOGIN:U1");
        assertEquals("CLIENTLIST:U1", client.receive());
        client1.send("LOGIN:U2");
        assertEquals("CLIENTLIST:U1,U2", client.receive());
    }

//    @Test
//    public void testSendMessage() throws IOException {
//        EchoClient client0 = new EchoClient();
//        EchoClient client1 = new EchoClient();
//        client0.connect("localhost", 7777);
//        client1.connect("localhost", 7777);
//        client0.send("LOGIN:Abe1");
//        client1.send("LOGIN:Monkey");
//        client0.receive();
//        client1.receive();
//        client0.receive();
//        client1.send("MSG:Abe1:Hej");
//        assertEquals("MSGRES:Monkey:Hej", client0.receive());
//
//    }
}
