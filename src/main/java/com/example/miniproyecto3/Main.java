package com.example.miniproyecto3;

import com.example.miniproyecto3.view.WelcomeStage;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException{
        WelcomeStage.getInstance();
    }
}
