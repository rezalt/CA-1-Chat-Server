/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ca1.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ProtocolStrings;

/**
 *
 * @author TimmosQuadros
 */
public class ClientHandler extends Thread
{

    private Socket socket;
    private Scanner input;
    private PrintWriter writer;
    private String username;
    private ProtocolStrings protocol;

    public Socket getSocket()
    {
        return socket;
    }

    public ClientHandler(Socket socket) throws IOException
    {
        this.socket = socket;
        input = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);
        //this.clients = EchoServer.clients;
        this.username = "";
        
        protocol = new ProtocolStrings(socket);
    }

    @Override
    public void run()
    {
        try
        {

            username = protocol.login();
            EchoServer.clients.add(this);
            sendClientList(); //Send the updated clientlist to all clients
            System.out.println("ClientList echo: " + EchoServer.clients);
            protocol.message(getUsername());

            //  message = input.nextLine(); //IMPORTANT blocking call

            EchoServer.clients.remove(this);
            sendClientList();
            writer.println(ProtocolStrings.ARGS.LOGOUT);//Echo the stop message back to the client for a nice closedown
            socket.close();
            //System.out.println("Closed a Connection");
            Logger.getLogger(Log.LOG_NAME).log(Level.INFO, "Closed a Connection");
            super.run(); //To change body of generated methods, choose Tools | Templates.
        }
        catch (NoSuchElementException | IOException ex)
        {
            EchoServer.clients.remove(this);
            sendClientList();

            try
            {
                socket.close();
            }
            catch (IOException ex1)
            {
                Logger.getLogger(Log.LOG_NAME).log(Level.INFO, ex1 + " Removed missing client from list and resent list to all other clients.");
            }
           Logger.getLogger(Log.LOG_NAME).log(Level.INFO, ex + " Removed missing client from list and resent list to all other clients.");
        }
    }

    public void sendMessage(String message)
    {
        writer.println(message);
    }

    public void sendClientList()
    {
        String clientList = ProtocolStrings.ARGS.CLIENTLIST + ":";
        for (ClientHandler client : EchoServer.clients)
        {
            clientList += client.getUsername() + ",";
        }
        clientList = clientList.substring(0, clientList.length() - 1);
       
        for (ClientHandler client : EchoServer.clients)
        {
            client.sendMessage(clientList);
        }
    }

    private void printClients()
    {
        for (ClientHandler client : EchoServer.clients)
        {
            System.out.println(username + "%n");
        }
    }

    public String getUsername()
    {
        return username;
    }
}
