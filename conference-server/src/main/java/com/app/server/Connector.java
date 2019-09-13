package com.app.server;

import com.app.model.ChatModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Connector extends Thread {
    private Socket socket;
    private Server server;

    public Connector(Socket socket, Server server) {
        this.socket = socket;
        this.server=server;
    }

    public void run(){
        while(true){
            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ChatModel msg = (ChatModel) in.readObject();
                publishToAll(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void publishToAll(ChatModel msg){
        List<Connector> sessions =  server.getSessions();
        for(Connector c: sessions){
            try {
                ObjectOutputStream out = new ObjectOutputStream(c.socket.getOutputStream());
                out.writeObject(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
