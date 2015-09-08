/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nicolaiharbo
 */
public class TCPServer {

    private static Socket socket;
    private static ServerSocket serverSocket;
    ArrayList<ClientHandler> clientList = new ArrayList();
    private boolean keepRunning = true;
    Scanner input;
    PrintWriter output;
    String message;
    String userinput = "";
    String username = "";
    ClientHandler ch;

    private void startServer() {
        String ip = "localhost";
        int port = 4321;

        Logger.getLogger(TCPServer.class.getName()).log(Level.INFO, "Server started. Listening on: " + port + ", bound to: " + ip);

        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            do {
                Socket socket = serverSocket.accept(); //Important Blocking call
                Logger.getLogger(TCPServer.class.getName()).log(Level.INFO, "Connected to a client");
                input = new Scanner(socket.getInputStream());
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println("Please enter USER#yourname");
                
//                //Her startes et nyt objekt af typen clientHandle, som tager en socket, en server og username som argumenter.
//                ClientHandler ch = new ClientHandler(socket, this, username);
//
//                //Her startes en ny tråd af typen clientHandle - dette kan gøres, fordi clienthandle extender Thread.
//                ch.start();

                while (true) {
                    userinput = input.nextLine();
                    String command = "";
                    String value = "";
                    String msg = "";

                    if (userinput.equals("STOP#")) {
                        command = "STOP";
                    } else if (userinput.contains("#")) {
                        String[] data = userinput.split("#");
                        command = data[0];
                        value = data[1];
                        if (data.length > 2) {
                            msg = data[2];
                        }

                    }

                    switch (command) {
                        //Add user to userlist
                        case "USER":
                            ch = new ClientHandler(socket, this, value);

                            clientList.add(ch);
                            break;

                        //Close connection for client
                        case "STOP":
                            socket.close();
                            break;

                        //Sender besked til alle, hvis * er valgt som modtager.    
                        case "MSG":
                            if (value == "*") {
                                for (int i = 0; i < clientList.size(); i++) {

                                    clientList.get(i).send(msg);

                                }
                            }

                            break;

                        default:
                            output.println("Not a valid command, try again!");
                            break;
                    }
                }

            } while (keepRunning);
        } catch (IOException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void stopServer() {
        keepRunning = false;
    }

    private void removeClient() {

    }

    public void send(String msg) {
        output.println(msg);
    }

    public static void main(String[] args) {
        new TCPServer().startServer();
    }

}
