/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Scanner;
import server.ClientHandler;

/**
 *
 * @author nicolaiharbo
 */
public class TCPClient extends Observable implements Runnable {

    Socket socket;
    private Scanner input;
    private PrintWriter output;
    String message = "";

//    public TCPClient(Socket socket) {
//        this.socket = socket;
//    }
    public void connect(String ip, int port) throws IOException {

        socket = new Socket(ip, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);

        Thread t1 = new Thread(this);
        t1.start();

    }

    // Sender til clienthandlerens output.
    public void send(String message) {
        output.println(message);
    }

    @Override
    public void run() {
        while (true) {
            message = input.nextLine();
            setChanged();
            notifyObservers(message);
        }
    }
    
    public String received () {
        return message;
    }

}
