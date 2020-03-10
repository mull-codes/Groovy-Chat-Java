/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groovychatserver;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerConnection extends Thread {
    Socket socket;
    GroovyChatServer server;
    DataInputStream din;
    DataOutputStream dout;
    boolean shouldRun = true;
    
    public ServerConnection(Socket socket, GroovyChatServer server){
        super("Server connection thread");
        this.socket = socket;
        this.server = server;
    }
    
    //Send data back to one client only
    public void sendStringToClient(String text){
        try {
            System.out.println("Returning: " + text);
            dout.writeUTF(text);
            dout.flush();
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Send data back to all the clients
    public void sendStringToAllClients(String text){
        //Starting at zero iterate through all the server connections
        for(int index = 0; index < server.connections.size(); index++){
            //Temporary server connection variable
            ServerConnection sc = server.connections.get(index);
            sc.sendStringToClient(text);
        }
    }
    
    
    public void run(){
        try {
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());

            while(shouldRun){
                while(din.available() == 0){
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //read from the client
                String textInput = din.readUTF();
                sendStringToAllClients(textInput);
            }
            
            din.close();
            dout.close();
            socket.close();
            
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
