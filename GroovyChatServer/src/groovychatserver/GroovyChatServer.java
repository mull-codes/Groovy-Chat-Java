/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groovychatserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mull
 */
public class GroovyChatServer {

    boolean shouldRun = true;
    ServerSocket ss;
    ArrayList<ServerConnection> connections = new ArrayList<ServerConnection>();
    
    int clientNo = 1; // The number of a client
    
    public static void main(String[] args) {
        new GroovyChatServer();
    }
    
    public GroovyChatServer(){
        System.out.println("Server started....awaiting connections");
        try {
            ss = new ServerSocket(8000);
            while(shouldRun){
                Socket s = ss.accept();
                ServerConnection sc = new ServerConnection(s, this);
                sc.start();
                connections.add(sc);
                
                // Print the new connect number on the console
                System.out.println("Start thread for client " + clientNo);

                // Find the client's host name, and IP address
                InetAddress clientInetAddress = s.getInetAddress();
                System.out.println("Client " + clientNo + "'s host name is " + clientInetAddress.getHostName());
                System.out.println("Client " + clientNo + "'s IP Address is " + clientInetAddress.getHostAddress());

                
                clientNo++;// Increment clientNo
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(GroovyChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
