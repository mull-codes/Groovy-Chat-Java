/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groovychat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mull
 */
public class ClientConnection extends Thread {
    Socket s;
    DataInputStream din;
    DataOutputStream dout;
    boolean shouldRun = true;
    
    String reply;
    
    public ClientConnection(Socket socket, GroovyChat client){
        s = socket;    
    }
    
    public void sendStringToServer(String text){
        try {
            dout.writeUTF(text);
            dout.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
            close();
        }
    }
    
    public void run(){
        try {
            din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());
            
            while(shouldRun){
                try {
                    while(din.available() == 0){
                        //listenForFileUpdate();
                        try{
                            Thread.sleep(1);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                    }

                    reply = din.readUTF();
                    System.out.println("Message: " + reply);
                    System.out.print("\nEnter a chat: ");
                    
                } catch (IOException ex) {
                    Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
                    close();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
            close();
        }   
    }
    
    //Note* not needed was testing if the Client was listening to the server
    public void listenForFileUpdate(){
        try {
            if(din.available() == 0){
                reply = din.readUTF();
                System.out.println("Message: " + reply);
                System.out.println("\nEnter a chat ...: ");
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void close(){
        try {
            din.close();
            dout.close();
            s.close(); 
        } catch (IOException ex) {
            Logger.getLogger(GroovyChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}