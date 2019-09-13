/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gluonapplication;

import com.app.model.ChatModel;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Icon;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author asusadmin
 */
public class ChatView extends View {

    private Socket socket = BasicView.socket;
    private String username = BasicView.username;

    public ChatView() {
        final TextArea messages = new TextArea();
        final TextField msgField = new TextField();
        Button button = new Button(">>>");
        button.setGraphic(new Icon(MaterialDesignIcon.LANGUAGE));
        button.setOnAction(e -> {

            ChatModel msg = new ChatModel();
            msg.setMessage(msgField.getText());
            msg.setUsername(username);

            try {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(msg);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });

        VBox controls = new VBox(15.0, messages, msgField, button);
        controls.setAlignment(Pos.CENTER);

        setCenter(controls);

        Thread t = new Thread(){

            @Override
            public void run() {
                while(true){
                    try{
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        ChatModel msg = (ChatModel) in.readObject();
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                messages.appendText(msg.getUsername()+" : "+msg.getMessage()+"\n");
                            }
                        });
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            }


        };
        t.setDaemon(true);
        t.start();


    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> System.out.println("Menu")));
        appBar.setTitleText("Basic View");
        appBar.getActionItems().add(MaterialDesignIcon.SEARCH.button(e -> System.out.println("Search")));
    }
}
