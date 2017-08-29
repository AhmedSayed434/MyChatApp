/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mychatapp.server;

import com.mychatapp.dao.ClientDetails;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ahmed
 */
public class UpdateClientList extends Thread{

    private Vector<ClientDetails> clientList;
    private static UpdateClientList instance = null;
    
    private UpdateClientList(){}
    
    public static UpdateClientList getInstance(Vector<ClientDetails> clientList){
        if(instance == null){
            instance = new UpdateClientList();
        }
        instance.clientList = clientList;
        return instance;
    }
    
    @Override
    public void run() {
        synchronized(clientList){
            while(true){
                try {
                    this.wait(1000);
                    for(ClientDetails cl : clientList){
                        cl.getOutput().writeUTF(clientList.toString());
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(UpdateClientList.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(UpdateClientList.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
}
