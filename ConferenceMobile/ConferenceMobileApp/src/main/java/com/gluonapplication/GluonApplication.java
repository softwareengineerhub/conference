package com.gluonapplication;

import com.gluonapplication.BasicView;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GluonApplication extends MobileApplication {

    @Override
    public void init() {
        addViewFactory(HOME_VIEW, ()->{
            return new BasicView(this);
        });
        addViewFactory("chatScreen", ()->{
            return new ChatView();
        });
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.BLUE.assignTo(scene);

        ((Stage) scene.getWindow()).getIcons().add(new Image(GluonApplication.class.getResourceAsStream("/icon.png")));
    }
}
