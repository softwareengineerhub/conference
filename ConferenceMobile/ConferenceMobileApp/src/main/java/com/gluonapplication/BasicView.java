package com.gluonapplication;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Icon;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class BasicView extends View {
    public static Socket socket;
    public static String username;
    private GluonApplication gluonApplication;

    public BasicView(GluonApplication gluonApplication) {
        this.gluonApplication = gluonApplication;

        Label label = new Label("Username");
        TextField usernameField = new TextField();
        Button button = new Button("Connect");
        button.setGraphic(new Icon(MaterialDesignIcon.LANGUAGE));
        button.setOnAction(e -> {
            try {
                username = usernameField.getText();
                socket = new Socket("127.0.0.1", 8084);
                gluonApplication.switchView("chatScreen");

            } catch (IOException ex) {
                Logger.getLogger(BasicView.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        VBox controls = new VBox(15.0, label,usernameField, button);
        controls.setAlignment(Pos.CENTER);

        setCenter(controls);
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> System.out.println("Menu")));
        appBar.setTitleText("Basic View");
        appBar.getActionItems().add(MaterialDesignIcon.SEARCH.button(e -> System.out.println("Search")));
    }

}
