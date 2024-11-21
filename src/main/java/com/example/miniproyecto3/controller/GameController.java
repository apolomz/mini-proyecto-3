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
    private Button carrierIdHoz;
    @FXML
    private Button carrierIdVer;
    @FXML
    private Button destroyerId1Hoz;
    @FXML
    private Button destroyerIdVer;
    @FXML
    private Button frigateId1Ver;
    @FXML
    private Button frigateIdVer;
    @FXML
    private Button submarineIdHoz;
    @FXML
    private Button submarineIdVer;


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


    private void handleCellClick(String cellId, Button cellButton, StackPane stackPane) {
        if (selectedShip == null) {
            System.out.println("Selecciona un barco primero");
            return;
        }

        try {
            int shipSize = selectedShip.getShipSize();
            boolean isHorizontal = selectedShip.isHorizontal(); // Toma la orientación del barco seleccionado

            // Seleccionar las celdas
            List<String> selectedCells = selectShipCells(cellId, shipSize, isHorizontal);

            if (selectedCells != null && areCellsValid(selectedCells, "user")) {
                for (String pos : selectedCells) {
                    Button targetCell = (Button) userGridPane.lookup("#" + pos);
                    if (targetCell != null) {
                        targetCell.setStyle("-fx-background-color: #1D3557;"); // Marca la celda como ocupada
                        targetCell.setDisable(true); // Desactiva la celda
                    }
                }

                // Colocar la figura 2D del barco en la orientación seleccionada
                Group shipGroup = selectedShip.createShipShape(0, 0, isHorizontal);
                stackPane.getChildren().add(shipGroup);

                // Actualizar el estado del barco
                disableShipButton(selectedShip);
                selectedShip = null;
            } else {
                throw new InvalidPlacementException("El barco no se puede colocar en esas celdas.");
            }
        } catch (InvalidPlacementException e) {
            System.out.println("Error al colocar el barco: " + e.getMessage());
        }
    }


    private void disableShipButton(IShip ship) {
        if (ship instanceof CarrierShip) {
            if (ship.isHorizontal()) {
                carrierIdHoz.setDisable(true);
            } else {
                carrierIdVer.setDisable(true);
            }
        } else if (ship instanceof Destroyer) {
            if (ship.isHorizontal()) {
                destroyerId1Hoz.setDisable(true);
            } else {
                destroyerIdVer.setDisable(true);
            }
        } else if (ship instanceof Submarine) {
            if (ship.isHorizontal()) {
                submarineIdHoz.setDisable(true);
            } else {
                submarineIdVer.setDisable(true);
            }
        } else if (ship instanceof Frigate) {
            if (ship.isHorizontal()) {
                frigateId1Ver.setDisable(true);
            } else {
                frigateIdVer.setDisable(true);
            }
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
        return Integer.parseInt(parts[2]); // Ajuste directo
    }
    private int extractColumn(String cellId) {
        String[] parts = cellId.split("_");
        return Integer.parseInt(parts[3]); // Ajuste directo
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
    void handleCarrierShipHoz(ActionEvent event) {
        selectShip(new CarrierShip(4), true);
    }

    @FXML
    void handleCarrierShipVer(ActionEvent event) {
        selectShip(new CarrierShip(4), false);
    }

    @FXML
    void handleDestroyerHoz(ActionEvent event) {
        selectShip(new Destroyer(2), true);
    }

    @FXML
    void handleDestroyerVer(ActionEvent event) {
        selectShip(new Destroyer(2), false);
    }

    @FXML
    void handleFrigateHoz(ActionEvent event) {
        selectShip(new Frigate(1), true);
    }

    @FXML
    void handleFrigateVer(ActionEvent event) {
        selectShip(new Frigate(1), false);
    }

    @FXML
    void handleSubmarineHoz(ActionEvent event) {
        selectShip(new Submarine(3), true);
    }

    @FXML
    void handleSubmarineVer(ActionEvent event) {
        selectShip(new Submarine(3), false);
    }

    private void selectShip(IShip ship, boolean isHorizontal) {
        this.selectedShip = ship;
        this.selectedShip.setOrientation(isHorizontal);
    }

}
