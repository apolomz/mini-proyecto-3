package com.example.miniproyecto3.controller;

import com.example.miniproyecto3.view.GameStage;
import com.example.miniproyecto3.view.WelcomeStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

/**
 * Controller class for the welcome screen of the Battleship game.
 * Handles initialization and game start actions.
 */
public class WelcomeController {

    /**
     * Handles the play button click event.
     * Creates a new game instance and removes the welcome screen.
     *
     * @param event The ActionEvent triggered by clicking the play button
     * @throws IOException If there's an error creating the game stage
     */
    @FXML
    void HandlePLay(ActionEvent event) throws IOException {
        GameStage.getInstance();
        WelcomeStage.deleteInstance();
    }

    /**
     * Initializes the controller.
     * Called automatically after the FXML file has been loaded.
     */
    public void initialize() {
        System.out.println("Controlador cargado");
    }
}
