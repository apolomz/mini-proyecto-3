package com.example.miniproyecto3.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Represents the primary stage for the Battleship game.
 * Implements a singleton pattern to ensure only one game stage exists at any time.
 */
public class GameStage extends Stage {

    /**
     * Constructs a new GameStage and initializes the game view.
     *
     * @throws IOException If the FXML resource cannot be loaded.
     */
    public GameStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/miniproyecto3/game-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1000, 700);
        setScene(scene);
        setTitle("Batalla Naval");
        setResizable(false);
        show();
    }

    /**
     * Holder for the singleton instance of {@code GameStage}.
     * Ensures lazy initialization and thread safety.
     */
    private static class GameStageHolder {
        private static GameStage INSTANCE;
    }

    /**
     * Retrieves the singleton instance of the {@code GameStage}.
     * If the instance does not exist, it is created.
     *
     * @return The singleton {@code GameStage} instance.
     * @throws IOException If the FXML resource cannot be loaded during instance creation.
     */
    public static GameStage getInstance() throws IOException {
        if (GameStage.GameStageHolder.INSTANCE == null) {
            GameStage.GameStageHolder.INSTANCE = new GameStage();
        }
        return GameStage.GameStageHolder.INSTANCE;
    }

    /**
     * Deletes the singleton instance of {@code GameStage}, closing the current stage.
     */
    public static void deleteInstance() {
        if (GameStage.GameStageHolder.INSTANCE != null) {
            GameStage.GameStageHolder.INSTANCE.close();
            GameStage.GameStageHolder.INSTANCE = null;
        }
    }
}
