package com.openlib.market.frontend;

import com.openlib.market.frontend.app.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class OpenLibApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneManager.initialize(primaryStage);
        SceneManager.navigateTo("login");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
