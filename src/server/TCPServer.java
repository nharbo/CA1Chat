/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
                //Her startes et nyt objekt af typen clientHandle, som tager en socket og en server som argumenter.
                ClientHandler ch = new ClientHandler(socket, this);
                //Her tilføjes den nye client til listen over tilsluttede brugere.
                clientList.add(ch);
                //Her startes en ny tråd af typen clientHandle - dette kan gøres, fordi clienthandle extender Thread.
                ch.start();

            } while (keepRunning);
        } catch (IOException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void stopServer() {

    }

    private void removeClient() {

    }

    public void send() {

    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
