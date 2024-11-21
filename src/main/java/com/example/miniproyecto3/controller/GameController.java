package com.example.miniproyecto3.controller;

import com.example.miniproyecto3.model.Game;
import com.example.miniproyecto3.model.exceptions.InvalidPlacementException;
import com.example.miniproyecto3.model.figures2d.*;
import com.example.miniproyecto3.view.GameStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
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
                    // Crear StackPane para apilar botón y figura 2D
                    StackPane stack = new StackPane();
                    stack.setPrefSize(30, 30);

                    Button cellButton = new Button();
                    cellButton.setPrefSize(30, 30);
                    cellButton.setStyle("-fx-hgrow: NEVER; -fx-vgrow: NEVER;");
                    cellButton.getStyleClass().add("grid-cell-water");

                    String cellId = player + "_cell_" + i + "_" + j;
                    cellButton.setId(cellId);
                    cellButton.setOnAction(event -> handleCellClick(cellId, cellButton, stack));

                    stack.getChildren().add(cellButton); // Colocamos el botón en la celda
                    gridPane.add(stack, j, i);
                } else {
                    // Crear encabezados (letras y números)
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
            }
        }
        gridPane.setHgap(0);
        gridPane.setVgap(0);
    }


    @FXML
    private void handleCellClick(String cellId, Button cellButton, StackPane stackPane) {
        if (selectedShip == null) {
            System.out.println("Selecciona un barco primero");
            return;
        }

        try {
            int shipSize = selectedShip.getShipSize();
            boolean isHorizontal = true; // Aquí podrías usar un botón o lógica para cambiar la orientación

            // Lógica de selección de celdas
            List<String> selectedCells = selectShipCells(cellId, shipSize, isHorizontal);

            // Validar que las celdas están disponibles y son consecutivas
            if (selectedCells != null && areCellsValid(selectedCells, "user")) {
                for (String pos : selectedCells) {
                    Button targetCell = (Button) userGridPane.lookup("#" + pos);
                    if (targetCell != null) {
                        targetCell.setStyle("-fx-background-color: #1D3557;"); // Marca la celda ocupada
                        targetCell.setDisable(true); // Desactiva el botón para que no se pueda interactuar nuevamente
                    }
                }

                // Lógica para colocar el barco después de la selección de celdas
                for (String pos : selectedCells) {
                    Button targetCell = (Button) userGridPane.lookup("#" + pos);
                    if (targetCell != null) {
                        Group shipGroup = selectedShip.createShipShape(targetCell.getLayoutX(), targetCell.getLayoutY(), isHorizontal);
                        stackPane.getChildren().add(shipGroup);
                    }
                }

                // Actualizar el estado del barco
                selectedShip = null;
                game.incrementTurnForShip(selectedShip); // Aumenta el turno del barco que se acaba de colocar
            } else {
                throw new InvalidPlacementException("El barco no se puede colocar en esas celdas.");
            }
        } catch (InvalidPlacementException e) {
            System.out.println("Error al colocar el barco: " + e.getMessage());
        }
    }


    public List<String> selectShipCells(String startCellId, int size, boolean isHorizontal) {
        List<String> selectedCells = new ArrayList<>();
        int startRow = extractRow(startCellId);
        int startCol = extractColumn(startCellId);

        for (int i = 0; i < size; i++) {
            int row = isHorizontal ? startRow : startRow + i;
            int col = isHorizontal ? startCol + i : startCol;

            if (isValidCell(row, col)) {
                selectedCells.add("user_cell_" + row + "_" + col);
            } else {
                return null; // Si alguna celda no es válida, retorna null
            }
        }

        return selectedCells;
    }


    private int extractRow(String cellId) {
        String[] parts = cellId.split("_");
        return Integer.parseInt(parts[2]) - 1; // Devuelve la fila ajustada al índice de matriz (empieza desde 0)
    }
    private int extractColumn(String cellId) {
        String[] parts = cellId.split("_");
        return Integer.parseInt(parts[3]) - 1; // Devuelve la columna ajustada al índice de matriz (empieza desde 0)
    }
    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < 10 && col >= 0 && col < 10; // Asegura que la celda esté dentro de los límites
    }

    private boolean areCellsValid(List<String> selectedCells, String player) {
        for (String cellId : selectedCells) {
            Button cellButton = (Button) userGridPane.lookup("#" + cellId);
            if (cellButton == null || cellButton.isDisabled()) {
                return false; // Si la celda ya está ocupada o está deshabilitada, es inválida
            }
        }
        return true;
    }







    @FXML
    void handleCarrierShip(ActionEvent event) {
        if (game.getCarrierCount() >= 1) {
            System.out.println("Ya se ha colocado el Portaaviones.");
            carrierId.setDisable(true); // Desactiva el botón cuando el contador alcanza el límite
            return;
        }
        selectedShip = new CarrierShip(4);
        carrierId.setDisable(true); // Desactiva el botón después de la selección
        System.out.println("Seleccionado: Portaaviones (4 casillas)");
    }


    @FXML
    void handleDestroyer(ActionEvent event) {
        if (game.getDestroyerCount() >= 3) {
            System.out.println("Ya se han colocado todos los Destructores.");
            return;
        }
        selectedShip = new Destroyer(2);
        destroyerId.setDisable(true); // Desactiva el botón después de la selección
        System.out.println("Seleccionado: Destructor (2 casillas)");
    }

    @FXML
    void handleFrigate(ActionEvent event) {
        if (game.getFrigateCount() >= 1) {
            System.out.println("Ya se ha colocado la Fragata.");
            return;
        }
        selectedShip = new Frigate(1);
        frigateId.setDisable(true); // Desactiva el botón después de la selección
        System.out.println("Seleccionado: Fragata (1 casilla)");
    }

    @FXML
    void handleSubmarine(ActionEvent event) {
        if (game.getSubmarineCount() >= 2) {
            System.out.println("Ya se han colocado todos los Submarinos.");
            return;
        }
        selectedShip = new Submarine(3);
        submarineId.setDisable(true); // Desactiva el botón después de la selección
        System.out.println("Seleccionado: Submarino (3 casillas)");
    }



}
