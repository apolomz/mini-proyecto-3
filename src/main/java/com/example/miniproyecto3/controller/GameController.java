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

/**
 * JHOAN FERNANDEZ - DANIEL CARDENAS
 * Controller class for managing a Battleship game interface.
 * Handles game logic, user interactions, and view updates.
 */
public class GameController {
    /** Currently selected ship for placement */
    private IShip selectedShip;

    /** Main game model instance */
    private final Game game = new Game();

    /** Adapter for managing game turns */
    private GameTurnAdapter gameAdapter;

    /** Counters for tracking ship placements */
    private int carrierCount = 0, destroyerCount = 0, frigateCount = 0, submarineCount = 0;

    // FXML Injected Components

    /** Ship placement buttons for different types and orientations */
    @FXML private Button carrierIdHoz, carrierIdVer, destroyerId1Hoz, destroyerIdVer,
            frigateId1Ver, frigateIdVer, submarineIdHoz, submarineIdVer;

    /** Game control buttons */
    @FXML private Button playButton, showComputerBoardButton, loadGameButton, saveGameButton;

    /** Game boards for user and computer */
    @FXML private GridPane userGridPane, computerGridPane;

    /**
     * Initializes the game controller and sets up the initial game state.
     * This method is automatically called by JavaFX after FXML loading.
     */
    public void initialize() {
        System.out.println("Game controller loaded");
        setupGrid(userGridPane, "user");
        setupGrid(computerGridPane, "computer");
        playButton.setDisable(true);
        gameAdapter = new GameTurnAdapter(game);
        disableComputerBoard(true);
    }

    /**
     * Enables or disables all cells in the computer's board.
     * @param disable true to disable the board, false to enable it
     */
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

    /**
     * Handles the restart game action.
     * Resets both boards and game state to initial conditions.
     * @param event The action event triggered by the restart button
     */
    @FXML
    private void handleRestart(ActionEvent event) {
        resetGame();
        gameAdapter = new GameTurnAdapter(game);
        playButton.setDisable(true);
        showComputerBoardButton.setDisable(false);
        disableComputerBoard(true);
    }

    /**
     * Handles the play button action.
     * Enables the computer board and starts the game.
     * @param event The action event triggered by the play button
     */
    @FXML
    private void handlePlay(ActionEvent event) {
        disableComputerBoard(false);
        enableComputerBoardClicks();
        playButton.setDisable(true);
        game.getCurrentState().setGameInProgress(true);
    }

    /**
     * Enables click events on all cells of the computer's board.
     */
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

    /**
     * Processes a shot at a specific cell on the computer's board.
     * Updates the visual state and handles the result of the shot.
     * @param cellId The ID of the cell that was shot at
     */
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

    /**
     * Marks a ship as sunk on the board and updates adjacent cells.
     * @param row Row coordinate of the sunk ship
     * @param col Column coordinate of the sunk ship
     * @param gridPane The grid pane containing the board
     */
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

    /**
     * Checks and marks adjacent cells of a sunk ship.
     * @param row Starting row coordinate
     * @param col Starting column coordinate
     * @param gridPane The grid pane containing the board
     */
    private void checkAndMarkAdjacentCells(int row, int col, GridPane gridPane) {
        markDirection(row, col, 0, 1, gridPane);  // Right
        markDirection(row, col, 0, -1, gridPane); // Left
        markDirection(row, col, 1, 0, gridPane);  // Down
        markDirection(row, col, -1, 0, gridPane); // Up
    }

    /**
     * Marks cells in a specific direction from a starting point.
     * @param row Starting row coordinate
     * @param col Starting column coordinate
     * @param rowDelta Row direction (-1, 0, or 1)
     * @param colDelta Column direction (-1, 0, or 1)
     * @param gridPane The grid pane containing the board
     */
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

    /**
     * Processes the computer's turn.
     * Updates the user's board based on the computer's shot.
     */
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

    /**
     * Shows the game over dialog with the winner's name.
     * @param winner The name of the winning player
     */
    private void showGameOverDialog(String winner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Game Over!");
        alert.setContentText(winner + " has won the game!");
        alert.showAndWait();
    }

    /**
     * Resets the game to its initial state.
     * Clears both boards and resets all game variables.
     */
    private void resetGame() {
        userGridPane.getChildren().clear();
        computerGridPane.getChildren().clear();
        game.resetGame();
        setupGrid(userGridPane, "user");
        setupGrid(computerGridPane, "computer");
        resetCountersAndButtons();
    }

    /**
     * Resets all ship counters and button states.
     */
    private void resetCountersAndButtons() {
        carrierCount = destroyerCount = frigateCount = submarineCount = 0;
        resetShipButtons();
        playButton.setDisable(true);
        showComputerBoardButton.setDisable(false);
    }

    /**
     * Resets all ship placement buttons to their initial state.
     */
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

    /**
     * Sets up the game grid with cells and headers.
     * @param gridPane The grid pane to set up
     * @param player The player identifier ("user" or "computer")
     */
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

    /**
     * Creates a header cell in the game grid.
     * @param gridPane The grid pane where the header will be added
     * @param i Row index for the header
     * @param j Column index for the header
     */
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

    /**
     * Handles the ship placement when a cell is clicked.
     * Validates the placement and updates both the model and UI accordingly.
     *
     * @param cellId The unique identifier of the clicked cell
     * @param cellButton The button representing the clicked cell
     * @param stackPane The stack pane containing the cell button
     * @throws InvalidPlacementException If the ship placement is invalid
     * @throws InvalidPlacementOnComputerBoardException If attempting to place on computer's board
     */
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

    /**
     * Places a ship on the selected cells and updates the UI.
     *
     * @param selectedCells List of cell IDs where the ship will be placed
     * @param stackPane The stack pane where the ship's visual representation will be added
     */
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

    /**
     * Updates the counter for placed ships and disables ship selection buttons when limit is reached.
     *
     * @param ship The ship that was just placed
     */
    private void updateShipCounter(IShip ship) {
        if (ship instanceof CarrierShip && ++carrierCount >= 1) disableShipButtons(carrierIdHoz, carrierIdVer);
        if (ship instanceof Destroyer && ++destroyerCount >= 3) disableShipButtons(destroyerId1Hoz, destroyerIdVer);
        if (ship instanceof Submarine && ++submarineCount >= 2) disableShipButtons(submarineIdHoz, submarineIdVer);
        if (ship instanceof Frigate && ++frigateCount >= 4) disableShipButtons(frigateId1Ver, frigateIdVer);
    }


    /**
     * Disables the specified ship selection buttons.
     *
     * @param buttons Variable number of buttons to disable
     */
    private void disableShipButtons(Button... buttons) {
        for (Button button : buttons) button.setDisable(true);
    }

    /**
     * Checks if all ships have been placed and enables the Play button if true.
     */
    private void checkIfAllShipsPlaced() {
        if (carrierCount >= 1 && destroyerCount >= 3 && frigateCount >= 4 && submarineCount >= 2) {
            playButton.setDisable(false);  // Habilita el botón Play cuando todos los barcos estén colocados
        }
    }

    /**
     * Selects cells for ship placement based on starting position and orientation.
     *
     * @param startCellId The cell ID where the ship starts
     * @param size The size of the ship
     * @param isHorizontal The orientation of the ship
     * @return List of selected cell IDs, or null if placement is invalid
     */
    public List<String> selectShipCells(String startCellId, int size, boolean isHorizontal) {
        List<String> selectedCells = new ArrayList<>();
        int[] coords = game.parseCellId(startCellId);
        int startRow = coords[0];
        int startCol = coords[1];

        if (isHorizontal && startCol + size - 1 > 10) return null;
        if (!isHorizontal && startRow + size - 1 > 10) return null;

        for (int i = 0; i < size; i++) {
            int row = isHorizontal ? startRow : startRow + i;
            int col = isHorizontal ? startCol + i : startCol;


            if (row < 1 || row > 10 || col < 1 || col > 10) {
                return null;
            }

            selectedCells.add("user_cell_" + row + "_" + col);
        }
        return selectedCells;
    }

    /**
     * Validates if the selected cells are available for ship placement.
     *
     * @param selectedCells List of cell IDs to validate
     * @param player The player's board being validated ("user" or "computer")
     * @return true if all cells are valid for placement, false otherwise
     */
    private boolean areCellsValid(List<String> selectedCells, String player) {
        for (String cellId : selectedCells) {
            Button cellButton = (Button) userGridPane.lookup("#" + cellId);
            if (cellButton == null || cellButton.isDisabled()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Event handlers for ship selection buttons.
     * Each method creates and selects a specific type of ship with given orientation.
     */
    @FXML void handleCarrierShipHoz(ActionEvent event) { selectShip(new CarrierShip(4), true); }
    @FXML void handleCarrierShipVer(ActionEvent event) { selectShip(new CarrierShip(4), false); }
    @FXML void handleDestroyerHoz(ActionEvent event) { selectShip(new Destroyer(2), true); }
    @FXML void handleDestroyerVer(ActionEvent event) { selectShip(new Destroyer(2), false); }
    @FXML void handleFrigateHoz(ActionEvent event) { selectShip(new Frigate(1), true); }
    @FXML void handleFrigateVer(ActionEvent event) { selectShip(new Frigate(1), false); }
    @FXML void handleSubmarineHoz(ActionEvent event) { selectShip(new Submarine(3), true); }
    @FXML void handleSubmarineVer(ActionEvent event) { selectShip(new Submarine(3), false); }

    /**
     * Sets the currently selected ship and its orientation.
     *
     * @param ship The ship to be selected
     * @param isHorizontal The orientation to set for the ship
     */
    private void selectShip(IShip ship, boolean isHorizontal) {
        this.selectedShip = ship;
        this.selectedShip.setOrientation(isHorizontal);
    }


    /**
     * Reveals the computer's board by showing all ship positions.
     * Disables the show board button after revealing.
     *
     * @param event The action event triggering the reveal
     */
    @FXML void handleShowComputerBoard(ActionEvent event) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button cellButton = (Button) computerGridPane.lookup("#computer_cell_" + (i + 1) + "_" + (j + 1));
                cellButton.setStyle(game.getComputerGrid()[i][j] ? "-fx-background-color: #FF0000;" : "-fx-background-color: #1D3557;");
            }
        }
        showComputerBoardButton.setDisable(true);
    }


    /**
     * Checks if the game is over by verifying if all computer ships have been sunk.
     *
     * @return true if game is over, false otherwise
     */
    public boolean checkGameOver() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (game.getComputerGrid()[i][j]) return false;
            }
        }
        return true;
    }

    /**
     * Saves the current game state to a file.
     *
     * @throws IOException If there's an error saving the game
     */
    @FXML
    private void handleSaveGame() throws IOException {
        try {
            gameAdapter.saveState(game.getCurrentState());
            game.saveGame("battleship_save.dat");
            showAlert("Juego guardado", "El juego se ha guardado correctamente.");
        } catch (IOException e) {
            showAlert("Error", "No se pudo guardar el juego: " + e.getMessage());
        }
    }


    /**
     * Loads a saved game state from a file and updates the UI accordingly.
     *
     * @throws IOException If there's an error reading the save file
     * @throws ClassNotFoundException If there's an error deserializing the game state
     */
    @FXML
    private void handleLoadGame() {
        try {
            game.loadGame("battleship_save.dat");
            GameState loadedState = game.getCurrentState();

            gameAdapter.loadState(loadedState);

            updateGridsFromLoadedGame();

            disableComputerBoard(!loadedState.isGameInProgress());

            playButton.setDisable(loadedState.isGameInProgress());
            showComputerBoardButton.setDisable(false);

            updateShipButtonsFromState(loadedState);

            showAlert("Juego cargado", "El juego se ha cargado correctamente.");
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Error", "No se pudo cargar el juego: " + e.getMessage());
        }
    }



    /**
     * Updates the game grids with loaded game state data.
     * Recreates ship positions, shot markers, and updates UI elements.
     */
    private void updateGridsFromLoadedGame() {
        GameState state = game.getCurrentState();
        boolean[][] userGrid = game.getUserGrid();
        int[][] computerShots = gameAdapter.getComputerShots();
        int[][] playerShots = gameAdapter.getPlayerShots();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                String cellId = "user_cell_" + (i+1) + "_" + (j+1);
                Button button = (Button) userGridPane.lookup("#" + cellId);
                StackPane stack = (StackPane) button.getParent();

                if (button != null) {
                    if (userGrid[i][j]) {
                        button.setStyle("-fx-background-color: #1D3557;");
                        button.setDisable(true);


                        if (isShipStart(userGrid, i, j)) {

                            ShipInfo shipInfo = determineShipType(userGrid, i, j);
                            if (shipInfo != null) {

                                IShip ship = createShip(shipInfo.size);
                                ship.setOrientation(shipInfo.horizontal);
                                Group shipGroup = ship.createShipShape(0, 0, shipInfo.horizontal);
                                stack.getChildren().add(shipGroup);
                            }
                        }
                    }

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

    }

    /**
     * Helper class to store ship information during game state loading.
     */
    private static class ShipInfo {
        int size;
        boolean horizontal;

        ShipInfo(int size, boolean horizontal) {
            this.size = size;
            this.horizontal = horizontal;
        }
    }

    /**
     * Creates a ship instance based on the specified size.
     *
     * @param size The size of the ship to create
     * @return A new ship instance of the appropriate type, or null if size is invalid
     */
    private IShip createShip(int size) {
        switch (size) {
            case 4: return new CarrierShip(4);
            case 3: return new Submarine(3);
            case 2: return new Destroyer(2);
            case 1: return new Frigate(1);
            default: return null;
        }
    }

    /**
     * Checks if a cell is the starting position of a ship.
     *
     * @param grid The game grid to check
     * @param row The row to check
     * @param col The column to check
     * @return true if the cell is the start of a ship, false otherwise
     */
    private boolean isShipStart(boolean[][] grid, int row, int col) {

        if (!grid[row][col]) return false;

        boolean noShipLeft = col == 0 || !grid[row][col-1];
        boolean noShipUp = row == 0 || !grid[row-1][col];

        return noShipLeft && noShipUp;
    }

    /**
     * Creates a new ShipInfo instance.
     *
     * @param "size" The size of the ship
     * @param "horizontal" The orientation of the ship
     */
    private ShipInfo determineShipType(boolean[][] grid, int startRow, int startCol) {

        int horizontalSize = 0;
        for (int j = startCol; j < 10 && grid[startRow][j]; j++) {
            horizontalSize++;
        }


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

    /**
     * Updates ship selection buttons based on the loaded game state.
     *
     * @param state The loaded game state
     */
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

    /**
     * Displays an alert dialog with the specified title and content.
     *
     * @param title The title of the alert dialog
     * @param content The message to display in the alert
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
