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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nicolaiharbo
 */
public class ClientHandler implements Runnable {

    Socket socket;
    TCPServer server;
    Scanner input;
    PrintWriter output;
    String userinput;
    String username;
    ClientHandler specUser;

    ClientHandler(Socket socket, TCPServer server, String username) throws IOException {
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);
        this.socket = socket;
        this.server = server;
        this.username = username;
    }

    public void sendAll(String message) {
        output.println(message);
    }
    
    public void sendSpecUser(String message){
        output.println(message);
    }
    

    @Override
    public void run() {

        while (true) {
            userinput = input.nextLine();
            String command = "";
            String value = "";
            String msg = "";

            String[] data = userinput.split("#");
            data[0] = command;
            data[1] = value;
            data[2] = msg;

            switch (command) {

                case "STOP": {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

//                    if (data.length > 2) {
//                        msg = data[2];
//                    }
                    
                //Sender besked til alle, hvis * er valgt som modtager.    
                case "MSG":
                    if (value == "*") {
                        server.sendAll(msg);
//                        for (int i = 0; i < clientList.size(); i++) {
//                            clientList.get(i).send(msg);
                    } else {
                        specUser = server.getUser(value);
                        specUser.sendSpecUser(msg);
                    }
                    break;

                default:
                    output.println("Not a valid command, try again!");
                    break;
            }

        }
    }


}
