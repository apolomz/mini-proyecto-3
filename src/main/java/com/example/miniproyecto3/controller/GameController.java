package com.example.miniproyecto3.controller;

import com.example.miniproyecto3.model.Game;
import com.example.miniproyecto3.model.exceptions.InvalidPlacementException;
import com.example.miniproyecto3.model.figures2d.*;
import com.example.miniproyecto3.view.GameStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class GameController {

    @FXML
    private Button carrierId;

    @FXML
    private Button destroyerId;

    @FXML
    private Button frigateId;

    @FXML
    private Button submarineId;
    @FXML
    private VBox shipVBox;
    @FXML
    private GridPane userGridPane;
    @FXML
    private GridPane computerGridPane;

    private IShip selectedShip;
    private final Game game = new Game();

    public void initialize(){
        System.out.println("Controlador juego cargado");

        setupGrid(userGridPane, "user");
        setupGrid(computerGridPane, "computer");

    }
    @FXML
    void handleExit(ActionEvent event) {
        GameStage.deleteInstance();
    }

    @FXML
    void handleRestart(ActionEvent event) {
        userGridPane.getChildren().clear();
        computerGridPane.getChildren().clear();
        game.resetGame();
        setupGrid(userGridPane, "user");
        setupGrid(computerGridPane, "computer");
    }

    private void setupGrid(GridPane gridPane, String player) {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i > 0 && j > 0) {
                    // Crear botón para las celdas de juego
                    Button cell = new Button();
                    cell.setPrefSize(30, 30);
                    cell.setStyle("-fx-hgrow: NEVER; -fx-vgrow: NEVER;");

                    cell.getStyleClass().add("grid-cell-water");
                    String cellId = player + "_cell_" + i + "_" + j;
                    cell.setId(cellId);
                    cell.setOnAction(event -> handleCellClick(cellId, cell));

                    gridPane.add(cell, j, i);
                } else {
                    Label header = new Label();
                    header.setPrefSize(30, 30);
                    header.setStyle("-fx-hgrow: NEVER; -fx-vgrow: NEVER;");

                    if (i == 0) {
                        header.setText(j > 0 ? String.valueOf(j) : "");
                        header.getStyleClass().add("grid-cell-header-number");
                    } else {
                        header.setText(String.valueOf((char) ('A' + i - 1)));
                        header.getStyleClass().add("grid-cell-header-letter");
                    }

                    gridPane.add(header, j, i);
                }
                gridPane.setHgap(0);
                gridPane.setVgap(0);
            }
        }
    }





    private void handleCellClick(String cellId, Button cell) {
        if (selectedShip == null) {
            System.out.println("Selecciona un barco primero");
            return;
        }

        try {
            int shipSize = selectedShip.getShipSize();
            boolean isHorizontal = true; // Por defecto, podrías alternar entre horizontal y vertical con un botón o lógica
            if (!game.isPlacementValid(cellId, shipSize, isHorizontal, "user")) {
                throw new InvalidPlacementException("El barco no se puede colocar en esa posición.");
            }

            List<String> positions = game.calculatePositions(cellId, shipSize, isHorizontal);
            for (String pos : positions) {
                Button targetCell = (Button) userGridPane.lookup("#" + pos);
                if (targetCell != null) {
                    targetCell.setStyle("-fx-background-color: #1D3557;"); // Color para indicar la colocación
                }
            }

            selectedShip = null;
        } catch (InvalidPlacementException e) {
            System.out.println("Error al colocar el barco: " + e.getMessage());
        }
    }


    private void addShipsToVBox() {
        IShip[] ships = {
                new CarrierShip(4),
                new Submarine(3),
                new Destroyer(2),
                new Frigate(1)
        };

        for (IShip ship : ships) {
            ship.addToPane(shipVBox);
        }
    }

    private void setupShipSelection() {
        shipVBox.getChildren().forEach(node -> {
            if (node instanceof Button shipButton) {
                shipButton.setOnAction(event -> {
                    selectedShip = game.findShipById(shipButton.getId());
                    System.out.println("Barco seleccionado: " + selectedShip.getId());
                });
            }
        });
    }


    @FXML
    void handleCarrierShip(ActionEvent event) {
        selectedShip = new CarrierShip(4);
        System.out.println("Seleccionado: Portaaviones (4 casillas)");
    }

    @FXML
    void handleDestroyer(ActionEvent event) {
        selectedShip = new Destroyer(2);
        System.out.println("Seleccionado: Destructor (2 casillas)");
    }

    @FXML
    void handleFrigate(ActionEvent event) {
        selectedShip = new Frigate(1);
        System.out.println("Seleccionado: Fragata (1 casilla)");
    }

    @FXML
    void handleSubmarine(ActionEvent event) {
        selectedShip = new Submarine(3);
        System.out.println("Seleccionado: Submarino (3 casillas)");
    }



}
