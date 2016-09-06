/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ca1.server;

import java.net.Socket;

/**
 *
 * @author rezalt
 */
public class User
{

    String name;
    Socket socket;
    
    public User(String name, Socket socket)
    {
        name = this.name;
        socket = this.socket;
    }

}
