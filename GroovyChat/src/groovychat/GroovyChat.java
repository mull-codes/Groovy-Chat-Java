/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groovychat;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mull
 */
public class GroovyChat {

    ClientConnection cc;
    
    public static void main(String[] args) {
        new GroovyChat();
    }
    
    public GroovyChat(){
        try {
            Socket s = new Socket("localhost", 8000);
            cc = new ClientConnection(s, this);
            cc.start();
            
            
            listenForInput();
            
        } catch (IOException ex) {
            Logger.getLogger(GroovyChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void listenForInput(){
        Scanner console = new Scanner(System.in);
        System.out.println("Enter a chat: ");
        while(true){
            
            while(!console.hasNextLine()){
                try{
                    Thread.sleep(1);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            
            String input = console.nextLine();
            if(input.toLowerCase().equals("-1")){
                break;
            }else if(input.equals(null) || input.equals("")){
                System.out.println("Error: Invalid input please try again ...");
                System.out.print("\nEnter a chat: ");
            }
            
            cc.sendStringToServer(input);
        }
        cc.close();
    }
    
}
