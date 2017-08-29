/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mychatapp.server;

import com.mychatapp.dao.ClientDetails;
import com.mychatapp.dao.Message;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ahmed
 */
public class ChatManager implements Runnable {

    private ClientDetails client;
    private Vector<ClientDetails> clientList = new Vector<>();
    private Thread clientListThread;

    public ChatManager(ClientDetails client) {
        this.client = client;
        clientListThread = UpdateClientList.getInstance(clientList);
        if (!clientListThread.isAlive()) {
            clientListThread.start();
        }
    }

    @Override
    public void run() {
        synchronized (clientList) {
            clientList.addElement(this.client);
            try {
                while (true) {
                    Message msg = (Message) this.client.getInput().readObject();
                    send(msg);
                }
            } catch (IOException ex) {
                Logger.getLogger(ChatManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ChatManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void send(Message msg) {
        synchronized (clientList) {
            try {
                ClientDetails receiver = search(msg.getReceiverID());
                receiver.getOutput().writeObject(msg);
                receiver.getOutput().flush();
            } catch (IOException ex) {
                Logger.getLogger(ChatManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullPointerException ex) {
                Logger.getLogger(ChatManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public ClientDetails search(int receiverID) {
        synchronized (clientList) {
            for (ClientDetails cl : clientList) {
                if (cl.getId() == receiverID) {
                    return cl;
                }
            }
            return null;
        }
    }

}
