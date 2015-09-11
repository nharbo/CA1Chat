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
import java.util.Properties;
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
    private static boolean keepRunning = true;
    private static final Properties properties = log.Logger.initProperties("server.properties");
    Scanner input;
    PrintWriter output;
    String message;
    String userinput = "";
    String username = "random";
    ClientHandler ch;
    ClientHandler specUser;

    private void startServer() {
        int port = Integer.parseInt(properties.getProperty("port"));
        String ip = properties.getProperty("serverIp");

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
               // output.println("Welcome to CA1 chatzervice ;-)");

                userinput = input.nextLine(); //Important Blocking call 

                //Hvis STOP-command, luk connection til klient
                if (userinput.equals("STOP#")) {
                    closeClient();

                    //Hvis USER-command, fanges username og gemmes ned i et clienthandler objekt, sammen
                    // med socket og serverforbindelsen.
                    // - Derefter startes en tråd af socketobjektet.
                } else if (userinput.contains("USER#")) {
                    String[] data = userinput.split("#");
                    username = data[1];
                    Logger.getLogger(TCPServer.class.getName()).log(Level.INFO, username + " connected!");
                    ch = new ClientHandler(socket, this, username);
                    clientList.add(ch);

                    Thread t1 = new Thread(ch);
                    t1.start();
                    sendClientList(printClientList());


                }

            } while (keepRunning);
        } catch (IOException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void stopServer() {
        keepRunning = false;
    }

    private void closeClient() throws IOException {
        ch.socket.close();
    }

    public void sendAll(String sender, String msg) {
        for (ClientHandler ch : clientList) {
            ch.sendAll(sender, msg);
        }
        Logger.getLogger(TCPServer.class.getName()).log(Level.INFO, sender + ": " + msg);
    }
    
    public void sendClientList(String list){
        for (ClientHandler ch : clientList){
            ch.sendClientList("USERLIST#" + list);
        }
    }

    public String printClientList() {
        String finalList = "";
        for (ClientHandler ch : clientList) {
            finalList = finalList + ch.username + ",";
        }
        finalList = finalList.substring(0, finalList.length()-1);
        return finalList;
    }

    public void sendSpecUser(String sender, ClientHandler client, String msg) {
        ch.sendSpecUser(sender, client, msg);
        System.out.println("SPEC USER I SERVER");
        Logger.getLogger(TCPServer.class.getName()).log(Level.INFO, sender + " sent: " + msg + " to: " + client.username);
    }

    public ClientHandler getUser(String user) {

        for (int i = 0; i < clientList.size(); i++) {
            if (clientList.get(i).username.equals(user)) {
                specUser = clientList.get(i);
            }
        }
        System.out.println(specUser);
        return specUser;
    }

    public static void main(String[] args) {
        try {
            String logFile = properties.getProperty("logFile");
            log.Logger.setLogFile(logFile, TCPServer.class.getName());
            new TCPServer().startServer();
        } finally {
            log.Logger.closeLogger(TCPServer.class.getName());
        }
        
    }

    public void closeCon(ClientHandler client) throws IOException {
        //klienten fjernes fra listen, og en ny liste printes.
        clientList.remove(client);
        System.out.println("Efter remove: "+clientList.size());
        sendClientList(printClientList());
        Logger.getLogger(TCPServer.class.getName()).log(Level.INFO, client.username + " disconnected from server!");
        //connection til socket lukkes.
        client.socket.close();
        

    }

}
