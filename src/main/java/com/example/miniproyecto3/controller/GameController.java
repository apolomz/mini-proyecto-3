package com.example.miniproyecto3.controller;

import com.example.miniproyecto3.model.CarrierShip;
import com.example.miniproyecto3.model.Destroyer;
import com.example.miniproyecto3.model.Frigate;
import com.example.miniproyecto3.model.Submarine;
import com.example.miniproyecto3.view.GameStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class GameController {

    @FXML
    private VBox shipVBox;
    @FXML
    private GridPane userGridPane;
    @FXML
    private GridPane computerGridPane;


    public void initialize(){
        System.out.println("Controlador juego cargado");
        setupGrid(userGridPane, "user");
        setupGrid(computerGridPane, "computer");

        CarrierShip ship = new CarrierShip(4);
        ship.createShipShape(0,0,true);
        ship.addToPane(shipVBox);
        Submarine submarine = new Submarine();
        submarine.createShipShape(0,0,false);
        submarine.addToPane(shipVBox);
        Destroyer destroyer = new Destroyer();
        destroyer.createShipShape(0,0,true);
        destroyer.addToPane(shipVBox);
        Frigate frigate = new Frigate();
        frigate.createShipShape(0,0,true);
        frigate.addToPane(shipVBox);
    }
    @FXML
    void handleExit(ActionEvent event) {
        GameStage.deleteInstance();
    }

    @FXML
    void handleRestart(ActionEvent event) {

    }

    private void setupGrid(GridPane gridPane, String player) {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                Label cell = new Label();
                cell.setPrefSize(30, 30);

                if (i > 0 && j > 0) {
                    cell.getStyleClass().add("grid-cell-water");
                } else {
                    cell.getStyleClass().add("grid-cell");
                }

                if (i == 0 && j > 0) {
                    cell.setText(String.valueOf(j));
                } else if (j == 0 && i > 0) {
                    cell.setText(String.valueOf((char) ('A' + i - 1)));
                }

                String cellId = player + "_cell_" + i + "_" + j;
                cell.setId(cellId);
                gridPane.add(cell, j, i);
            }
        }
    }
}
