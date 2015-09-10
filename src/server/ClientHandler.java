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

    ClientHandler(Socket socket, TCPServer server, String username) throws IOException {
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);
        this.socket = socket;
        this.server = server;
        this.username = username;
    }

    public void sendAll(String sender, String message) {
        output.println("MSG#" + sender + "#" + message);
    }

    public void sendClientList(String list) {
        output.println(list);
    }

    public void sendSpecUser(String sender, ClientHandler client, String message) {
        client.output.println("MSG#" + sender + "#" + message);
    }

    @Override
    public void run() {

        while (true) {
            //Modtages fra TCPClients output
            userinput = input.nextLine();
            String command = "";
            String value = "";
            String msg = "";

            String[] data = userinput.split("#");

            if (data.length > 2) {
                command = data[0];
                value = data[1];
                msg = data[2];
            } else {
                command = data[0];
            }

            switch (command) {

                case "STOP": {
                    try {

                        server.closeCon(this);
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
                    //Sender til alle
                    if (value.equals("*")) {
                        server.sendAll(this.username, msg);

                    //Sender til flere, sepereret med komma.
                    } else if (value.contains(",")) {
                        String[] names = value.split(",");
                        
                        for (int i = 0; i < names.length; i++) {
                            Object tempuser = server.getUser(names[i]);
                            if (server.getUser(names[i]).username.equals(names[i])) {

                                if (server.getUser(names[i]) == tempuser) {
                                    String tempUserName = names[i];
                                    server.sendSpecUser(this.username, server.getUser(tempUserName), msg);
                                    output.println("MSG#" + this.username + "#" + msg);
                                }
                            } else {
                                output.println(names[i] + " is an unknown user - try again!");
                            }

                        }
                        
                    //Sender til en specifik bruger.
                    } else if (value.equals(server.getUser(value).username)) {
                        ClientHandler singleUser = server.getUser(value);
                        output.println("MSG#" + singleUser.username + "#" + msg);
                        server.sendSpecUser(this.username, singleUser, msg);      
                    } else {
                        output.println("Unknown user. Try again");
                    }
                    
                    break;

                    //Hvis ingen af ovenstående opfyldes.
                default:
                    output.println("Not a valid command, try again!");
                    break;
            }

        }
    }

    @Override
    public String toString() {
        return "ClientHandler: " + "username = " + username;
    }

}
