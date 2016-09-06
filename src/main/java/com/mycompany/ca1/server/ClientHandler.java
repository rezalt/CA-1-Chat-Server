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
public class ClientHandler extends Thread{

    private Socket socket;
    private Scanner input;
    private PrintWriter writer;
    
    public Socket getSocket(){
        return socket;
    }
            
    public ClientHandler(Socket socket) throws IOException {
        this.socket=socket;
        input = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);

        
    }

    @Override
    public void run() {
        try {
            String message = input.nextLine(); //IMPORTANT blocking call
            //System.out.println(String.format("Received the message: %1$S ", message));
            Logger.getLogger(Log.LOG_NAME).log(Level.INFO,String.format("Received the message: %1$S ", message));
            while (!message.equals(ProtocolStrings.ARGS.STOP)) {
                writer.println(message.toUpperCase());
                //System.out.println(String.format("Received the message: %1$S ", message.toUpperCase()));
                Logger.getLogger(Log.LOG_NAME).log(Level.INFO,String.format("Received the message: %1$S ", message.toUpperCase()));
                message = input.nextLine(); //IMPORTANT blocking call
            }
            writer.println(ProtocolStrings.ARGS.STOP);//Echo the stop message back to the client for a nice closedown
            socket.close();
            //System.out.println("Closed a Connection");
            Logger.getLogger(Log.LOG_NAME).log(Level.INFO,"Closed a Connection");
            super.run(); //To change body of generated methods, choose Tools | Templates.
        } catch (NoSuchElementException|IOException ex) {
            //Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    private static void handleClient(Socket socket) throws IOException {
        
    }
}
