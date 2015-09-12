import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Florent
 */
public class DummyClient {
     private Socket socket;
    private Scanner input;
    private PrintWriter output;

   
    
    public DummyClient(String ip, int port) throws IOException{
        socket = new Socket(ip, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);
    }
    
    public void send(String message){
        output.println(message);
    
    }
    
    public String received(){
    return input.nextLine();
    
    }
    
    
}

