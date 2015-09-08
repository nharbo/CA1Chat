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
public class ClientHandler implements Runnable {

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
    }

    public void send(String message) {
        output.println(message);
    }

    @Override
    public void run() {
//        switch (command) {
//            //Add user to userlist
//            case "USER":
//
//                break;
//
//            //Close connection for client
//            case "STOP":
//                socket.close();
//                break;
//
//                if (data.length > 2) {
//                    msg = data[2];
//                }
//            //Sender besked til alle, hvis * er valgt som modtager.    
//            case "MSG":
//                if (value == "*") {
//                    for (int i = 0; i < clientList.size(); i++) {
//
//                        clientList.get(i).send(msg);
//
//                    }
//                }
//
//                break;
//
//            default:
//                output.println("Not a valid command, try again!");
//                break;
//        }
        while (true) {
            message = input.nextLine();
            
            switch (message) {
                //bla bla bla.. her skal stå hvad der skal ske, afhængig af input..
            }
            
            server.sendAll(message);

        }
    }

}
