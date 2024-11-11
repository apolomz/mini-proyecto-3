package com.example.miniproyecto3.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class GameController {

    @FXML
    private GridPane userGridPane;
    @FXML
    private GridPane computerGridPane;
    public void initialize(){
        System.out.println("Controlador juego cargado");

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                Label cell = new Label();

                if (i == 0 && j > 0) {
                    cell.setText(String.valueOf(j));
                } else if (j == 0 && i > 0) {
                    cell.setText(String.valueOf((char) ('A' + i - 1)));
                } else {
                    cell.setText("");
                }

                cell.getStyleClass().add("grid-cell");
                cell.setPrefSize(30, 30);


                //La asignación de los ID no sé si esta correcta para crear la lógica, se pueden tomar por user o computer y el nombre del ID
                String cellId = "cell_" + i + "_" + j;
                cell.setId(cellId); // Asignamos el ID


                Label userCell = new Label(cell.getText());
                userCell.setPrefSize(30, 30);
                userCell.getStyleClass().add("grid-cell");
                userGridPane.add(userCell, j, i);

                Label computerCell = new Label(cell.getText());
                computerCell.setPrefSize(30, 30);
                computerCell.getStyleClass().add("grid-cell");
                computerGridPane.add(computerCell, j, i);
            }
        }
    }
    @FXML
    void handleExit(ActionEvent event) {

    }

    @FXML
    void handleRestart(ActionEvent event) {

    }

}
