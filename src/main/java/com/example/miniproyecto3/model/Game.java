package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.exceptions.InvalidPlacementException;
import com.example.miniproyecto3.model.figures2d.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a battleship game implementation that manages the game state, ship placements,
 * and game operations for both user and computer players.
 *
 * This class implements the IGame interface and provides functionality for:
 * - Ship placement (manual and random)
 * - Game state management
 * - Save/Load game functionality
 * - Grid management for both players
 *
 */
public class Game implements IGame, Serializable {


    /**
     * Serial version UID for serialization compatibility.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Counter for the number of turns played in the game.
     */
    private int turnCount = 0;

    /**
     * Counter for the number of carrier ships placed.
     */
    private int carrierCount = 0;

    /**
     * Counter for the number of destroyer ships placed.
     */
    private int destroyerCount = 0;

    /**
     * Counter for the number of frigate ships placed.
     */
    private int frigateCount = 0;

    /**
     * Counter for the number of submarine ships placed.
     */
    private int submarineCount = 0;

    /**
     * List containing all ships placed by the user.
     */
    private final List<IShip> userShips = new ArrayList<>();

    /**
     * 10x10 grid representing the user's game board.
     * true indicates a ship presence, false indicates empty cell.
     */
    private final boolean[][] userGrid = new boolean[10][10];

    /**
     * 10x10 grid representing the computer's game board.
     * true indicates a ship presence, false indicates empty cell.
     */
    private final boolean[][] computerGrid = new boolean[10][10];

    /**
     * Current state of the game, containing all relevant game information.
     */
    private GameState currentState;

    /**
     * Initializes a new game instance with empty grids and sets up the computer's ships.
     */
    public Game() {
        initializeComputerGrid();
        this.currentState = new GameState();
    }

    /**
     * Retrieves the current state of the game.
     *
     * @return The current GameState object
     */
    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * Saves the current game state to a file.
     *
     * @param fileName The name of the file to save the game state
     * @throws IOException If there's an error writing to the file
     */
    public void saveGame(String fileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            currentState.setUserGrid(userGrid);
            currentState.setComputerGrid(computerGrid);
            oos.writeObject(currentState);
        }
    }

    /**
     * Loads a previously saved game state from a file.
     *
     * @param fileName The name of the file to load the game state from
     * @throws IOException If there's an error reading the file
     * @throws ClassNotFoundException If the saved game state class cannot be found
     */
    public void loadGame(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            GameState loadedState = (GameState) ois.readObject();
            boolean[][] loadedUserGrid = loadedState.getUserGrid();
            boolean[][] loadedComputerGrid = loadedState.getComputerGrid();

            for (int i = 0; i < 10; i++) {
                System.arraycopy(loadedUserGrid[i], 0, userGrid[i], 0, 10);
                System.arraycopy(loadedComputerGrid[i], 0, computerGrid[i], 0, 10);
            }
            this.currentState = loadedState;
        }
    }
    /**
     * Gets the computer's grid representation.
     *
     * @return A 2D boolean array representing the computer's grid
     */
    public boolean[][] getComputerGrid() {
        return computerGrid;
    }
    /**
     * Gets the user's grid representation.
     *
     * @return A 2D boolean array representing the user's grid
     */
    public boolean[][] getUserGrid() {
        return userGrid;
    }

    /**
     * Initializes the computer's grid by randomly placing all required ships.
     * Places ships of different sizes according to game rules:
     * - 1 carrier (size 4)
     * - 2 submarines (size 3)
     * - 3 destroyers (size 2)
     * - 4 frigates (size 1)
     */
    @Override
    public void initializeComputerGrid() {
        clearGrid(computerGrid);
        placeShipRandomly(4);
        placeShipRandomly(3);
        placeShipRandomly(3);
        placeShipRandomly(2);
        placeShipRandomly(2);
        placeShipRandomly(2);
        placeShipRandomly(1);
        placeShipRandomly(1);
        placeShipRandomly(1);
        placeShipRandomly(1);
    }


    /**
     * Resets the game to its initial state, clearing both grids and reinitializing
     * the computer's ship placements.
     */
    @Override
    public void resetGame(){
        clearGrid(userGrid);
        clearGrid(computerGrid);
        initializeComputerGrid();
        this.currentState = new GameState();
    }


    /**
     * Places a ship on the user's grid at the specified position.
     *
     * @param startCellId The starting cell ID in format "user_cell_X_Y"
     * @param shipSize The size of the ship to place
     * @param isHorizontal True if the ship should be placed horizontally, false for vertical
     * @throws InvalidPlacementException If the placement is invalid (out of bounds or overlapping)
     */
    @Override
    public void placeShip(String startCellId, int shipSize, boolean isHorizontal) throws InvalidPlacementException {
        int[] coords = parseCellId(startCellId);
        int startRow = coords[0] - 1;
        int startCol = coords[1] - 1;
        if (isHorizontal) {
            if (startCol + shipSize > 10) {
                throw new InvalidPlacementException("El barco se sale del tablero horizontalmente");
            }
        } else {
            if (startRow + shipSize > 10) {
                throw new InvalidPlacementException("El barco se sale del tablero verticalmente");
            }
        }

        for (int i = 0; i < shipSize; i++) {
            int row = isHorizontal ? startRow : startRow + i;
            int col = isHorizontal ? startCol + i : startCol;

            if (row >= 10 || col >= 10 || userGrid[row][col]) {
                throw new InvalidPlacementException("Posición ocupada o inválida");
            }
        }

        for (int i = 0; i < shipSize; i++) {
            int row = isHorizontal ? startRow : startRow + i;
            int col = isHorizontal ? startCol + i : startCol;
            userGrid[row][col] = true;
        }
    }

    /**
     * Calculates all positions that a ship would occupy given a starting position and orientation.
     *
     * @param startCellId The starting cell ID in format "user_cell_X_Y"
     * @param shipSize The size of the ship
     * @param isHorizontal True if the ship should be placed horizontally, false for vertical
     * @return List of cell IDs that the ship would occupy
     * @throws InvalidPlacementException If the calculated positions would be invalid
     */
    public List<String> calculatePositions(String startCellId, int shipSize, boolean isHorizontal) throws InvalidPlacementException {
        int[] coords = parseCellId(startCellId);
        int startRow = coords[0];
        int startCol = coords[1];

        List<String> positions = new ArrayList<>();
        for (int i = 0; i < shipSize; i++) {
            int row = isHorizontal ? startRow : startRow + i;
            int col = isHorizontal ? startCol + i : startCol;

            if (row >= 10 || col >= 10) {
                throw new InvalidPlacementException("El barco no cabe en la posición inicial: " + startCellId);
            }
            positions.add("user_cell_" + (row + 1) + "_" + (col + 1));
        }
        return positions;
    }

    /**
     * Finds a ship by its ID in the user's fleet.
     *
     * @param shipId The ID of the ship to find
     * @return The found IShip object, or null if not found
     */
    public IShip findShipById(String shipId) {
        for (IShip ship : userShips) {
            if (ship.getId().equals(shipId)) {
                return ship;
            }
        }
        return null;
    }


    /**
     * Places a ship randomly on the computer's grid.
     *
     * @param shipSize The size of the ship to place
     */
    @Override
    public void placeShipRandomly(int shipSize) {
        boolean placed = false;
        while (!placed) {
            int row = (int) (Math.random() * 10);
            int col = (int) (Math.random() * 10);
            boolean isHorizontal = Math.random() < 0.5;

            try {
                List<int[]> positions = new ArrayList<>();
                for (int i = 0; i < shipSize; i++) {
                    int newRow = isHorizontal ? row : row + i;
                    int newCol = isHorizontal ? col + i : col;

                    if (newRow >= 10 || newCol >= 10 || computerGrid[newRow][newCol]) {
                        throw new InvalidPlacementException("Posición inválida para barco.");
                    }
                    positions.add(new int[]{newRow, newCol});
                }

                for (int[] pos : positions) {
                    computerGrid[pos[0]][pos[1]] = true;
                }
                placed = true;
            } catch (InvalidPlacementException ignored) {
            }
        }
    }


    /**
     * Parses a cell ID string to get its grid coordinates.
     *
     * @param cellId The cell ID in format "user_cell_X_Y"
     * @return An integer array containing the [row, column] coordinates
     */
    @Override
    public int[] parseCellId(String cellId) {
        String[] parts = cellId.split("_");
        int row = Integer.parseInt(parts[2]);  // Ya no restamos 1
        int col = Integer.parseInt(parts[3]);
        return new int[]{row, col};
    }


    /**
     * Clears all cells in the specified grid by setting them to false.
     * This method is used when resetting the game or initializing a new grid.
     *
     * @param grid The 2D boolean array representing the grid to clear
     */
    private void clearGrid(boolean[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = false;
            }
        }
    }

    /**
     * Checks if a ship placement would be valid for the specified player.
     *
     * @param cellId The starting cell ID
     * @param shipSize The size of the ship
     * @param isHorizontal The orientation of the ship
     * @param player The player making the placement ("user" or "computer")
     * @return True if the placement would be valid, false otherwise
     */
    public boolean isPlacementValid(String cellId, int shipSize, boolean isHorizontal, String player) {
        // Lógica de validación para el placement del barco
        return true; // Retornar true si es válido, false si no lo es
    }

}
