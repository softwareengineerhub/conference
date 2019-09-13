package conferenceFX;

import com.app.model.ChatModel;
import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class MainApp extends Application {
    private Socket socket;
    private String username;

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = new BorderPane();

        final TextArea messages = new TextArea();
        root.setCenter(messages);

        HBox topPane = new HBox(10);
        final TextField usernameField = new TextField();
        Button connectBtn = new Button("Connect");
        connectBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    username = usernameField.getText();
                    socket = new Socket("localhost", 8084);

                    Thread t = new Thread(){

                        public void run(){
                            while(true){
                                try {
                                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                                    final ChatModel chatModel = (ChatModel) in.readObject();
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            messages.appendText(chatModel.getUsername()+" : "+chatModel.getMessage()+"\n");
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    };
                    t.setDaemon(true);
                    t.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        topPane.getChildren().addAll(usernameField, connectBtn);

        root.setTop(topPane);

        HBox bottomPane = new HBox(10);
        final TextField msgField = new TextField();
        Button sendBtn = new Button(">>>");
        sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ChatModel chatModel = new ChatModel();
                chatModel.setUsername(username);
                chatModel.setMessage(msgField.getText());
                try {
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(chatModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        bottomPane.getChildren().addAll(msgField, sendBtn);

        root.setBottom(bottomPane);
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();


    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
