package com.app.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
    private ServerSocket serverSocket;
    private List<Connector> sessions;

    public Server(){
        try {
            sessions = new ArrayList<Connector>();
            serverSocket = new ServerSocket(8084);
            System.out.println("-------SERVER STARTED--------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void  run(){
        while(true){
            try {
                Socket socket = serverSocket.accept();
                Connector c = new Connector(socket, this);
                sessions.add(c);
                c.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Connector> getSessions() {
        return sessions;
    }
}
