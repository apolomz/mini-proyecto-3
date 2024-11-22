package com.example.miniproyecto3;

import com.example.miniproyecto3.view.WelcomeStage;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JHOAN FERNANDEZ - DANIEL CARDENAS
 * Main application class for the Battleship game.
 * Extends JavaFX Application to provide the entry point for the game.
 */
public class Main extends Application {

    /**
     * Main entry point for the application.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the JavaFX application by creating the welcome screen.
     * @param primaryStage The primary stage for the application
     * @throws IOException If there's an error creating the welcome stage
     */
    @Override
    public void start(Stage primaryStage) throws IOException{
        WelcomeStage.getInstance();
    }
}
