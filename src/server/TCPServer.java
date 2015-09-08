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
    String username = "random";
    ClientHandler ch;

    private void startServer() {
        String ip = "localhost";
        int port = 4321;

        Logger.getLogger(TCPServer.class.getName()).log(Level.INFO, "Server started. Listening on: " + port + ", bound to: " + ip);

        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            //Serveren står hele tiden og venter på at kunne acceptere nye connections
            do {
                Socket socket = serverSocket.accept(); //Important Blocking call
                Logger.getLogger(TCPServer.class.getName()).log(Level.INFO, "Connected to a client");
                input = new Scanner(socket.getInputStream());
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println("Please enter USER#yourname");

                userinput = input.nextLine(); //Important Blocking call
                String command = "";
                String value = "";
                String msg = "";

                //Hvis STOP-command, luk connection til klient
                if (userinput.equals("STOP#")) {
                    closeClient();

                //Hvis USER-command, fanges username og gemmes ned i et clienthandler objekt, sammen
                // med socket og serverforbindelsen.
                // - Derefter startes en tråd af socketobjektet.
                } else if (userinput.contains("USER#")) {
                    String[] data = userinput.split("#");
                    username = data[1];

                    ch = new ClientHandler(socket, this, username);
                    clientList.add(ch);
                    System.out.println(clientList.size());

                    Thread t1 = new Thread(ch);
                    t1.start();

                    System.out.println("Tråd startet");
                    System.out.println("Ny bruger: " + ch.username);

                }

            } while (keepRunning);
        } catch (IOException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void stopServer() {
        keepRunning = false;
    }

    private void closeClient() throws IOException {
        ch.socket.close();
    }

    public void sendAll(String msg) {
        for (ClientHandler ch : clientList) {
            ch.send(msg);
        }
    }

    public static void main(String[] args) {
        new TCPServer().startServer();
    }

}
