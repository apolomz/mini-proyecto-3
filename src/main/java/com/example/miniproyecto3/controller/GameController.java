package com.example.miniproyecto3.controller;

import com.example.miniproyecto3.model.Game;
import com.example.miniproyecto3.model.GameState;
import com.example.miniproyecto3.model.GameTurnAdapter;
import com.example.miniproyecto3.model.exceptions.InvalidPlacementException;
import com.example.miniproyecto3.model.exceptions.InvalidPlacementOnComputerBoardException;
import com.example.miniproyecto3.model.figures2d.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    private IShip selectedShip;
    private final Game game = new Game();
    private GameTurnAdapter gameAdapter;

    private int carrierCount = 0, destroyerCount = 0, frigateCount = 0, submarineCount = 0;

    @FXML private Button carrierIdHoz, carrierIdVer, destroyerId1Hoz, destroyerIdVer,
            frigateId1Ver, frigateIdVer, submarineIdHoz, submarineIdVer,
            playButton, showComputerBoardButton, loadGameButton, saveGameButton;
    @FXML private GridPane userGridPane, computerGridPane;

    // Inicialización y configuración de la vista
    public void initialize() {
        System.out.println("Controlador juego cargado");
        setupGrid(userGridPane, "user");
        setupGrid(computerGridPane, "computer");
        playButton.setDisable(true);
        gameAdapter = new GameTurnAdapter(game);
        disableComputerBoard(true);
    }

    private void disableComputerBoard(boolean disable) {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                Button cellButton = (Button) computerGridPane.lookup("#computer_cell_" + i + "_" + j);
                if (cellButton != null) {
                    cellButton.setDisable(disable);
                }
            }
        }
    }


    @FXML
    private void handleRestart(ActionEvent event) {
        resetGame();
        gameAdapter = new GameTurnAdapter(game);
        playButton.setDisable(true);
        showComputerBoardButton.setDisable(false);
        disableComputerBoard(true); // Desactivar el tablero al reiniciar
    }


    @FXML void handlePlay(ActionEvent event) {
        disableComputerBoard(false); // Habilitar el tablero
        enableComputerBoardClicks();
        playButton.setDisable(true);
        // Actualizar el estado del juego
        game.getCurrentState().setGameInProgress(true);
    }

    private void enableComputerBoardClicks() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                Button cellButton = (Button) computerGridPane.lookup("#computer_cell_" + i + "_" + j);
                if (cellButton != null) {
                    cellButton.setOnAction(e -> handleShot(cellButton.getId()));
                }
            }
        }
    }

    private void handleShot(String cellId) {
        int[] coords = game.parseCellId(cellId);
        int result = gameAdapter.playerShoot(coords[0], coords[1]);

        Button cellButton = (Button) computerGridPane.lookup("#" + cellId);
        StackPane cellStack = (StackPane) cellButton.getParent();

        if (result != -1) {
            cellStack.getChildren().clear();
            cellStack.getChildren().add(cellButton);

            switch (result) {
                case GameTurnAdapter.WATER:
                    cellButton.setStyle("-fx-background-color: #87CEEB;");
                    cellStack.getChildren().add(new WaterMarker());
                    break;
                case GameTurnAdapter.HIT:
                    cellButton.setStyle("-fx-background-color: #FF4444;");
                    cellStack.getChildren().add(new HitMarker());
                    break;
                case GameTurnAdapter.SUNK:
                    markSunkShip(coords[0], coords[1], computerGridPane);
                    break;
            }

            if (gameAdapter.isGameOver()) {
                showGameOverDialog(gameAdapter.getWinner());
            } else {
                handleComputerTurn();
            }
        }
    }

    private void markSunkShip(int row, int col, GridPane gridPane) {
        String currentCellId = String.format("computer_cell_%d_%d", row, col);
        Button currentButton = (Button) gridPane.lookup("#" + currentCellId);
        if (currentButton != null) {
            StackPane cellStack = (StackPane) currentButton.getParent();
            cellStack.getChildren().clear();
            cellStack.getChildren().add(currentButton);

            currentButton.setStyle("-fx-background-color: #8B0000;");
            cellStack.getChildren().add(new SunkMarker());
        }

        checkAndMarkAdjacentCells(row, col, gridPane);
    }
    private void checkAndMarkAdjacentCells(int row, int col, GridPane gridPane) {
        // Verificar horizontal
        markDirection(row, col, 0, 1, gridPane);  // Derecha
        markDirection(row, col, 0, -1, gridPane); // Izquierda

        // Verificar vertical
        markDirection(row, col, 1, 0, gridPane);  // Abajo
        markDirection(row, col, -1, 0, gridPane); // Arriba
    }
    private void markDirection(int row, int col, int rowDelta, int colDelta, GridPane gridPane) {
        int currentRow = row;
        int currentCol = col;

        while (true) {
            currentRow += rowDelta;
            currentCol += colDelta;

            if (currentRow < 1 || currentRow > 10 || currentCol < 1 || currentCol > 10) {
                break;
            }

            String cellId = String.format("computer_cell_%d_%d", currentRow, currentCol);
            Button button = (Button) gridPane.lookup("#" + cellId);
            StackPane cellStack = button != null ? (StackPane) button.getParent() : null;

            if (button == null || !button.getStyle().contains("-fx-background-color: #FF4444")) {
                break;
            }

            cellStack.getChildren().clear();
            cellStack.getChildren().add(button);
            button.setStyle("-fx-background-color: #8B0000;");
            cellStack.getChildren().add(new SunkMarker());
        }
    }

    private void handleComputerTurn() {
        int[] shotResult = gameAdapter.computerShoot();
        if (shotResult != null) {
            String cellId = "user_cell_" + (shotResult[0] + 1) + "_" + (shotResult[1] + 1);
            Button cellButton = (Button) userGridPane.lookup("#" + cellId);
            StackPane cellStack = (StackPane) cellButton.getParent();

            cellStack.getChildren().clear();
            cellStack.getChildren().add(cellButton);

            switch (shotResult[2]) {
                case GameTurnAdapter.WATER:
                    cellButton.setStyle("-fx-background-color: #87CEEB;");
                    cellStack.getChildren().add(new WaterMarker());
                    break;
                case GameTurnAdapter.HIT:
                    cellButton.setStyle("-fx-background-color: #FF4444;");
                    cellStack.getChildren().add(new HitMarker());
                    break;
                case GameTurnAdapter.SUNK:
                    cellButton.setStyle("-fx-background-color: #8B0000;");
                    cellStack.getChildren().add(new SunkMarker());
                    break;
            }

            if (gameAdapter.isGameOver()) {
                showGameOverDialog(gameAdapter.getWinner());
            }
        }
    }

    // Nuevo método para mostrar el diálogo de fin de juego:
    private void showGameOverDialog(String winner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("¡Juego terminado!");
        alert.setContentText(winner + " ha ganado el juego!");
        alert.showAndWait();
    }

    private void resetGame() {
        userGridPane.getChildren().clear();
        computerGridPane.getChildren().clear();
        game.resetGame();
        setupGrid(userGridPane, "user");
        setupGrid(computerGridPane, "computer");
        resetCountersAndButtons();
    }

    private void resetCountersAndButtons() {
        carrierCount = destroyerCount = frigateCount = submarineCount = 0;
        resetShipButtons();
        playButton.setDisable(true);
        showComputerBoardButton.setDisable(false);
    }

    private void resetShipButtons() {
        carrierIdHoz.setDisable(false);
        carrierIdVer.setDisable(false);
        destroyerId1Hoz.setDisable(false);
        destroyerIdVer.setDisable(false);
        submarineIdHoz.setDisable(false);
        submarineIdVer.setDisable(false);
        frigateId1Ver.setDisable(false);
        frigateIdVer.setDisable(false);
    }

    // Configuración de las celdas del tablero
    private void setupGrid(GridPane gridPane, String player) {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i > 0 && j > 0) {
                    StackPane stack = new StackPane();
                    stack.setPrefSize(30, 30);
                    Button cellButton = new Button();
                    cellButton.setPrefSize(30, 30);
                    cellButton.setStyle("-fx-hgrow: NEVER; -fx-vgrow: NEVER;");
                    cellButton.getStyleClass().add("grid-cell-water");

                    String cellId = player + "_cell_" + i + "_" + j;
                    cellButton.setId(cellId);

                    if (player.equals("computer")) {
                        cellButton.setOnAction(event -> handleShot(cellId));
                        cellButton.setDisable(true);
                    } else {
                        cellButton.setOnAction(event -> handleCellClick(cellId, cellButton, stack));
                    }

                    stack.getChildren().add(cellButton);
                    gridPane.add(stack, j, i);
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
            }
        }
        gridPane.setHgap(0);
        gridPane.setVgap(0);
    }



    private void createGridHeader(GridPane gridPane, int i, int j) {
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

    // Manejo de la colocación de barcos
    private void handleCellClick(String cellId, Button cellButton, StackPane stackPane) {
        if (selectedShip == null) {
            System.out.println("Selecciona un barco primero");
            return;
        }

        try {
            if (cellId.startsWith("computer")) {
                throw new InvalidPlacementOnComputerBoardException("No puedes colocar barcos en el tablero de la computadora.");
            }

            int shipSize = selectedShip.getShipSize();
            boolean isHorizontal = selectedShip.isHorizontal();
            List<String> selectedCells = selectShipCells(cellId, shipSize, isHorizontal);

            if (selectedCells != null && areCellsValid(selectedCells, "user")) {
                game.placeShip(cellId, shipSize, isHorizontal);  // Actualizar el modelo

                // Deshabilitar todas las celdas del barco
                for (String pos : selectedCells) {
                    Button targetCell = (Button) userGridPane.lookup("#" + pos);
                    StackPane targetStack = (StackPane) targetCell.getParent();
                    if (targetCell != null) {
                        targetCell.setStyle("-fx-background-color: #1D3557;");
                        targetCell.setDisable(true);

                        // Solo agregar la figura del barco en la primera celda
                        if (pos.equals(selectedCells.get(0))) {
                            Group shipGroup = selectedShip.createShipShape(0, 0, isHorizontal);
                            targetStack.getChildren().add(shipGroup);
                        }
                    }
                }

                updateShipCounter(selectedShip);
                selectedShip = null;
                checkIfAllShipsPlaced();
            }
        } catch (InvalidPlacementException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Posición inválida");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (InvalidPlacementOnComputerBoardException e) {
            System.out.println(e.getMessage());
        }
    }

    private void placeShip(List<String> selectedCells, StackPane stackPane) {
        for (String pos : selectedCells) {
            Button targetCell = (Button) userGridPane.lookup("#" + pos);
            if (targetCell != null) {
                targetCell.setStyle("-fx-background-color: #1D3557;");
                targetCell.setDisable(true);
            }
        }
        Group shipGroup = selectedShip.createShipShape(0, 0, selectedShip.isHorizontal());
        stackPane.getChildren().add(shipGroup);
    }

    private void updateShipCounter(IShip ship) {
        if (ship instanceof CarrierShip && ++carrierCount >= 1) disableShipButtons(carrierIdHoz, carrierIdVer);
        if (ship instanceof Destroyer && ++destroyerCount >= 3) disableShipButtons(destroyerId1Hoz, destroyerIdVer);
        if (ship instanceof Submarine && ++submarineCount >= 2) disableShipButtons(submarineIdHoz, submarineIdVer);
        if (ship instanceof Frigate && ++frigateCount >= 4) disableShipButtons(frigateId1Ver, frigateIdVer);
    }

    private void disableShipButtons(Button... buttons) {
        for (Button button : buttons) button.setDisable(true);
    }

    private void checkIfAllShipsPlaced() {
        if (carrierCount >= 1 && destroyerCount >= 3 && frigateCount >= 4 && submarineCount >= 2) {
            playButton.setDisable(false);  // Habilita el botón Play cuando todos los barcos estén colocados
        }
    }

    // Lógica de selección y validación de celdas
    public List<String> selectShipCells(String startCellId, int size, boolean isHorizontal) {
        List<String> selectedCells = new ArrayList<>();
        int[] coords = game.parseCellId(startCellId);
        int startRow = coords[0];
        int startCol = coords[1];

        // Verificar límites del tablero antes de agregar celdas
        if (isHorizontal && startCol + size - 1 > 10) return null;
        if (!isHorizontal && startRow + size - 1 > 10) return null;

        for (int i = 0; i < size; i++) {
            int row = isHorizontal ? startRow : startRow + i;
            int col = isHorizontal ? startCol + i : startCol;

            // Verificar que cada celda esté dentro de los límites
            if (row < 1 || row > 10 || col < 1 || col > 10) {
                return null;
            }

            selectedCells.add("user_cell_" + row + "_" + col);
        }
        return selectedCells;
    }



    private boolean areCellsValid(List<String> selectedCells, String player) {
        for (String cellId : selectedCells) {
            Button cellButton = (Button) userGridPane.lookup("#" + cellId);
            if (cellButton == null || cellButton.isDisabled()) {
                return false;
            }
        }
        return true;
    }

    // Selección de barcos
    @FXML void handleCarrierShipHoz(ActionEvent event) { selectShip(new CarrierShip(4), true); }
    @FXML void handleCarrierShipVer(ActionEvent event) { selectShip(new CarrierShip(4), false); }
    @FXML void handleDestroyerHoz(ActionEvent event) { selectShip(new Destroyer(2), true); }
    @FXML void handleDestroyerVer(ActionEvent event) { selectShip(new Destroyer(2), false); }
    @FXML void handleFrigateHoz(ActionEvent event) { selectShip(new Frigate(1), true); }
    @FXML void handleFrigateVer(ActionEvent event) { selectShip(new Frigate(1), false); }
    @FXML void handleSubmarineHoz(ActionEvent event) { selectShip(new Submarine(3), true); }
    @FXML void handleSubmarineVer(ActionEvent event) { selectShip(new Submarine(3), false); }

    private void selectShip(IShip ship, boolean isHorizontal) {
        this.selectedShip = ship;
        this.selectedShip.setOrientation(isHorizontal);
    }

    // Mostrar tablero de la computadora
    @FXML void handleShowComputerBoard(ActionEvent event) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button cellButton = (Button) computerGridPane.lookup("#computer_cell_" + (i + 1) + "_" + (j + 1));
                cellButton.setStyle(game.getComputerGrid()[i][j] ? "-fx-background-color: #FF0000;" : "-fx-background-color: #1D3557;");
            }
        }
        showComputerBoardButton.setDisable(true);
    }

    // Verificación de fin de juego
    public boolean checkGameOver() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (game.getComputerGrid()[i][j]) return false;
            }
        }
        return true;
    }

    @FXML
    private void handleSaveGame() throws IOException {
        try {
            // Guardar el estado actual del juego
            gameAdapter.saveState(game.getCurrentState());
            game.saveGame("battleship_save.dat");
            showAlert("Juego guardado", "El juego se ha guardado correctamente.");
        } catch (IOException e) {
            showAlert("Error", "No se pudo guardar el juego: " + e.getMessage());
        }
    }


    @FXML
    private void handleLoadGame() {
        try {
            game.loadGame("battleship_save.dat");
            GameState loadedState = game.getCurrentState();

            // Restaurar el estado del adaptador
            gameAdapter.loadState(loadedState);

            // Actualizar la interfaz gráfica
            updateGridsFromLoadedGame();

            // Habilitar/deshabilitar el tablero de la computadora según el estado del juego
            disableComputerBoard(!loadedState.isGameInProgress());

            // Habilitar/deshabilitar botones según el estado
            playButton.setDisable(loadedState.isGameInProgress());
            showComputerBoardButton.setDisable(false);

            // Restaurar el estado de los botones de barcos
            updateShipButtonsFromState(loadedState);

            showAlert("Juego cargado", "El juego se ha cargado correctamente.");
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Error", "No se pudo cargar el juego: " + e.getMessage());
        }
    }


    private void updateGridsFromLoadedGame() {
        GameState state = game.getCurrentState();
        boolean[][] userGrid = game.getUserGrid();
        int[][] computerShots = gameAdapter.getComputerShots();
        int[][] playerShots = gameAdapter.getPlayerShots();

        // Actualizar tablero del usuario y recrear las figuras de los barcos
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                String cellId = "user_cell_" + (i+1) + "_" + (j+1);
                Button button = (Button) userGridPane.lookup("#" + cellId);
                StackPane stack = (StackPane) button.getParent();

                if (button != null) {
                    if (userGrid[i][j]) {
                        button.setStyle("-fx-background-color: #1D3557;");
                        button.setDisable(true);

                        // Verificar si este es el inicio de un barco
                        if (isShipStart(userGrid, i, j)) {
                            // Determinar el tipo y orientación del barco
                            ShipInfo shipInfo = determineShipType(userGrid, i, j);
                            if (shipInfo != null) {
                                // Crear y agregar la figura del barco
                                IShip ship = createShip(shipInfo.size);
                                ship.setOrientation(shipInfo.horizontal);
                                Group shipGroup = ship.createShipShape(0, 0, shipInfo.horizontal);
                                stack.getChildren().add(shipGroup);
                            }
                        }
                    }

                    // Mostrar disparos de la computadora
                    if (computerShots[i][j] > 0) {
                        if (userGrid[i][j]) {
                            button.setStyle("-fx-background-color: #FF4444;");
                            button.setText("H");
                        } else {
                            button.setStyle("-fx-background-color: #87CEEB;");
                            button.setText("X");
                        }
                    }
                }
            }
        }

        // Actualizar tablero de la computadora (código existente)
        // ... resto del método se mantiene igual
    }

    // Clase auxiliar para almacenar información del barco
    private static class ShipInfo {
        int size;
        boolean horizontal;

        ShipInfo(int size, boolean horizontal) {
            this.size = size;
            this.horizontal = horizontal;
        }
    }

    // Método para crear el tipo de barco correspondiente
    private IShip createShip(int size) {
        switch (size) {
            case 4: return new CarrierShip(4);
            case 3: return new Submarine(3);
            case 2: return new Destroyer(2);
            case 1: return new Frigate(1);
            default: return null;
        }
    }

    // Método para verificar si una celda es el inicio de un barco
    private boolean isShipStart(boolean[][] grid, int row, int col) {
        // Verificar si hay un barco en la celda actual
        if (!grid[row][col]) return false;

        // Verificar si es el inicio (no hay barco a la izquierda ni arriba)
        boolean noShipLeft = col == 0 || !grid[row][col-1];
        boolean noShipUp = row == 0 || !grid[row-1][col];

        return noShipLeft && noShipUp;
    }

    // Método para determinar el tipo y orientación del barco
    private ShipInfo determineShipType(boolean[][] grid, int startRow, int startCol) {
        // Verificar horizontalmente
        int horizontalSize = 0;
        for (int j = startCol; j < 10 && grid[startRow][j]; j++) {
            horizontalSize++;
        }

        // Verificar verticalmente
        int verticalSize = 0;
        for (int i = startRow; i < 10 && grid[i][startCol]; i++) {
            verticalSize++;
        }

        // Determinar la orientación y tamaño del barco
        if (horizontalSize > verticalSize) {
            return new ShipInfo(horizontalSize, true);
        } else if (verticalSize > horizontalSize) {
            return new ShipInfo(verticalSize, false);
        } else {
            // Si son iguales (caso de barco de tamaño 1)
            return new ShipInfo(1, true);
        }
    }

    private void updateShipButtonsFromState(GameState state) {
        carrierIdHoz.setDisable(state.isShipPlaced("carrier"));
        carrierIdVer.setDisable(state.isShipPlaced("carrier"));
        destroyerId1Hoz.setDisable(state.isShipPlaced("destroyer"));
        destroyerIdVer.setDisable(state.isShipPlaced("destroyer"));
        submarineIdHoz.setDisable(state.isShipPlaced("submarine"));
        submarineIdVer.setDisable(state.isShipPlaced("submarine"));
        frigateId1Ver.setDisable(state.isShipPlaced("frigate"));
        frigateIdVer.setDisable(state.isShipPlaced("frigate"));
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
