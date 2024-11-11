package com.example.miniproyecto3.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameStage extends Stage {

    public GameStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/miniproyecto3/game-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1000, 600);
        setScene(scene);
        setTitle("Batalla Naval");
        setResizable(false);
        show();
    }

    private static class GameStageHolder{
        private static GameStage INSTANCE;
    }

    public static GameStage getInstance() throws IOException {
        if (GameStage.GameStageHolder.INSTANCE == null) {
            GameStage.GameStageHolder.INSTANCE = new GameStage();
        }
        return GameStage.GameStageHolder.INSTANCE;
    }

    public static void deleteInstance(){
        if (GameStage.GameStageHolder.INSTANCE != null) {
            GameStage.GameStageHolder.INSTANCE.close();
            GameStage.GameStageHolder.INSTANCE = null;
        }
    }


}
