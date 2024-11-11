package com.example.miniproyecto3.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeStage extends Stage {

    public WelcomeStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/miniproyecto3/welcome-view.fxml"));
        Parent root = loader.load();
        Scene  scene = new Scene(root, 1000, 600);
        setScene(scene);
        setTitle("Batalla Naval");
        setResizable(false);
        show();
    }

    private static class WelcomeStageHolder{
        private static WelcomeStage INSTANCE;
    }

    public static WelcomeStage getInstance() throws IOException {
        if (WelcomeStageHolder.INSTANCE == null) {
            WelcomeStageHolder.INSTANCE = new WelcomeStage();
        }
        return WelcomeStageHolder.INSTANCE;
    }

    public static void deleteInstance(){
        if (WelcomeStageHolder.INSTANCE != null) {
            WelcomeStageHolder.INSTANCE.close();
            WelcomeStageHolder.INSTANCE = null;
        }
    }


}
