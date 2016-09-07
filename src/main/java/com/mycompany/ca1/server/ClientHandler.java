/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ca1.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ProtocolStrings;

/**
 *
 * @author TimmosQuadros
 */
public class ClientHandler extends Thread {

    private Socket socket;
    private Scanner input;
    private PrintWriter writer;
    private String Username;
    private List<ClientHandler> clients;

    public Socket getSocket() {
        return socket;
    }

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        input = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);
        this.clients = EchoServer.clients;
        this.Username = "";
    }

    @Override
    public void run() {
        try {
            Random r = new Random();
            int num = r.nextInt(9999) + 1000;
            while (true) {
                if (num == 3675) {
                    break;
                }
                writer.printf(Integer.toString(num));
                num = r.nextInt(9999) + 1000;
            }
            writer.printf("%n");
            writer.printf("Velkommen til matrix%n");
            //System.out.println(String.format("Received the message: %1$S ", message));
            String message = input.nextLine(); //IMPORTANT blocking call
            while (Username.equalsIgnoreCase("")) {
                String msg[] = message.split(":");
                if (msg[0].equalsIgnoreCase("LOGIN") && msg.length > 1 && !usernameExists(msg[1])) {
                    Username = msg[1];
                    writer.println("Hello " + Username);
                }
                message = input.nextLine(); //IMPORTANT blocking call
            }
            Logger.getLogger(Log.LOG_NAME).log(Level.INFO, String.format("Received the message: %1$S ", message));
//            message = input.nextLine(); //IMPORTANT blocking call
            while (!message.equals(ProtocolStrings.ARGS.LOGOUT + ":")) {
                String msg[] = message.split(":");
                String[] names;
                if (msg[0].equalsIgnoreCase("LOGIN")) {
                    writer.println("Already Logged in");
                } else if (msg[0].equalsIgnoreCase("MSG")) {
                    names = msg[1].split(",");
                    if (names.length == 0) {
                        String[] tmp = {msg[1]};
                        names = tmp;
                    }
                    for (String name : names) {
                        for (ClientHandler client : clients) {
                            if (client.getUsername().equals(name)) {
                                client.sendMessage(Username+" Says:"+msg[2]);
                            }
                        }
                    }
                }else if(message.equalsIgnoreCase("print clients")){
                    printClients();
                }
                //writer.println(message.toUpperCase());
                //System.out.println(String.format("Received the message: %1$S ", message.toUpperCase()));
                Logger.getLogger(Log.LOG_NAME).log(Level.INFO, String.format("Received the message: %1$S ", message.toUpperCase()));
                message = input.nextLine(); //IMPORTANT blocking call
            }
            EchoServer.clients.remove(this);
            printClients();
            writer.println(ProtocolStrings.ARGS.LOGOUT);//Echo the stop message back to the client for a nice closedown
            socket.close();
            //System.out.println("Closed a Connection");
            Logger.getLogger(Log.LOG_NAME).log(Level.INFO, "Closed a Connection");
            super.run(); //To change body of generated methods, choose Tools | Templates.
        } catch (NoSuchElementException | IOException ex) {
            //Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    private boolean usernameExists(String username) {
        for (ClientHandler client : clients) {
            if (username.equalsIgnoreCase(client.getUsername())) {
                return true;
            }
        }
        return false;
    }
    
    private void printClients(){
        for (ClientHandler client : clients) {
            System.out.println(client.getUsername()+"%n");
        }
    }

    public String getUsername() {
        return Username;
    }

    private static void handleClient(Socket socket) throws IOException {

    }
}
