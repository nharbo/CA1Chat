/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author nicolaiharbo
 */
public class ClientHandler extends Thread {

    Socket socket;
    TCPServer server;
    Scanner input;
    PrintWriter output;
    String message;
    String username;
    

    ClientHandler(Socket socket, TCPServer server, String username) throws IOException {
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);
        this.socket = socket;
        this.server = server;
        this.username = username;
        this.message = message;
    }
    
    public void send(String message){
        output.println(message);
    }
    

    public void recieved() throws IOException {
        input = new Scanner(socket.getInputStream());
        message = input.nextLine();
        output.println(message);
    }

    @Override
    public void run() {
        while(true){
        message = input.nextLine();
        server.send(message);
        }
    }

}
