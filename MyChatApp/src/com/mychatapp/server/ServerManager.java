/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mychatapp.server;

import com.mychatapp.dao.ClientDetails;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ahmed
 */
public class ServerManager extends Thread {

    private ServerSocket server;
    private int clientID = 0;
    
    public ServerManager(ServerSocket server){
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket clientSocket = server.accept();
                ClientDetails cd = new ClientDetails(clientSocket, clientID++);
                ChatManager cm = new ChatManager(cd);
                new Thread(cm).start();
            } catch (IOException ex) {
                Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
