package com.example.miniproyecto3.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Represents the welcome stage of the Battleship game.
 * Implements the Singleton pattern to ensure only one welcome stage exists at any time.
 */
public class WelcomeStage extends Stage {

    /**
     * Constructs a new WelcomeStage and initializes the welcome view.
     *
     * @throws IOException If the FXML resource cannot be loaded.
     */
    public WelcomeStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/miniproyecto3/welcome-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1000, 700);
        setScene(scene);
        setTitle("Batalla Naval");
        setResizable(false);
        show();
    }

    /**
     * Holder for the singleton instance of {@code WelcomeStage}.
     * Ensures lazy initialization and thread safety.
     */
    private static class WelcomeStageHolder {
        private static WelcomeStage INSTANCE;
    }

    /**
     * Retrieves the singleton instance of the {@code WelcomeStage}.
     * If the instance does not exist, it is created.
     *
     * @return The singleton {@code WelcomeStage} instance.
     * @throws IOException If the FXML resource cannot be loaded during instance creation.
     */
    public static WelcomeStage getInstance() throws IOException {
        if (WelcomeStageHolder.INSTANCE == null) {
            WelcomeStageHolder.INSTANCE = new WelcomeStage();
        }
        return WelcomeStageHolder.INSTANCE;
    }

    /**
     * Deletes the singleton instance of {@code WelcomeStage}, closing the current stage.
     */
    public static void deleteInstance() {
        if (WelcomeStageHolder.INSTANCE != null) {
            WelcomeStageHolder.INSTANCE.close();
            WelcomeStageHolder.INSTANCE = null;
        }
    }
}
