package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.exceptions.InvalidPlacementException;

/**
 * Represents the Interface for game logic in a Battleship game.
 */
public interface IGame {

    /**
     * Initializes the computer's grid with random ship placements.
     */
    void initializeComputerGrid();

    /**
     * Resets the game state, clearing all grids and player statistics.
     */
    void resetGame();

    /**
     * Places a ship on the player's grid based on the starting cell and orientation.
     *
     * @param startCellId   The ID of the starting cell (e.g., "A1").
     * @param shipSize      The size of the ship to place.
     * @param isHorizontal  True if the ship is placed horizontally, false otherwise.
     * @throws InvalidPlacementException If the placement is invalid (e.g., out of bounds, overlapping).
     */
    void placeShip(String startCellId, int shipSize, boolean isHorizontal) throws InvalidPlacementException;

    /**
     * Places a ship randomly on the player's grid.
     *
     * @param shipSize The size of the ship to place.
     */
    void placeShipRandomly(int shipSize);

    /**
     * Parses a cell ID into its row and column indices.
     *
     * @param cellId The ID of the cell (e.g., "A1").
     * @return An array where the first element is the row index and the second is the column index.
     */
    int[] parseCellId(String cellId);
}
