package com.example.miniproyecto3.model;

import java.io.Serializable;

/**
 * Represents the complete state of a Battleship game.
 * This class stores all necessary data for saving and restoring the game's progress.
 * Implements {@link Serializable} to support saving and loading the game state.
 */
public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean[][] userGrid;
    private boolean[][] computerGrid;
    private int[][] playerShots;
    private int[][] computerShots;
    private boolean isGameInProgress;
    private int playerHitCount;
    private int computerHitCount;
    private boolean isPlayerTurn;
    private String winner;
    private boolean gameOver;

    // States of ship placement
    private boolean carrierPlaced;
    private boolean destroyerPlaced;
    private boolean submarinePlaced;
    private boolean frigatePlaced;

    /**
     * Constructs a new GameState with default values.
     * Initializes the grids, shot records, and game status.
     */
    public GameState() {
        userGrid = new boolean[10][10];
        computerGrid = new boolean[10][10];
        playerShots = new int[10][10];
        computerShots = new int[10][10];
        isGameInProgress = false;
        playerHitCount = 0;
        computerHitCount = 0;
        isPlayerTurn = true;
        winner = null;
        gameOver = false;
    }

    /**
     * Gets the user's grid representing the positions of their ships.
     *
     * @return A 10x10 boolean array where true indicates a ship position.
     */
    public boolean[][] getUserGrid() {
        return userGrid;
    }

    /**
     * Sets the user's grid with the given 10x10 boolean array.
     *
     * @param grid A 10x10 boolean array representing ship positions.
     */
    public void setUserGrid(boolean[][] grid) {
        this.userGrid = new boolean[10][10];
        for (int i = 0; i < 10; i++) {
            System.arraycopy(grid[i], 0, this.userGrid[i], 0, 10);
        }
    }

    /**
     * Gets the computer's grid representing the positions of its ships.
     *
     * @return A 10x10 boolean array where true indicates a ship position.
     */
    public boolean[][] getComputerGrid() {
        return computerGrid;
    }

    /**
     * Sets the computer's grid with the given 10x10 boolean array.
     *
     * @param grid A 10x10 boolean array representing ship positions.
     */
    public void setComputerGrid(boolean[][] grid) {
        this.computerGrid = new boolean[10][10];
        for (int i = 0; i < 10; i++) {
            System.arraycopy(grid[i], 0, this.computerGrid[i], 0, 10);
        }
    }

    /**
     * Gets the player's shot record.
     *
     * @return A 10x10 integer array where values represent the state of each cell (e.g., hit, miss).
     */
    public int[][] getPlayerShots() {
        return playerShots;
    }

    /**
     * Sets the player's shot record with the given 10x10 integer array.
     *
     * @param shots A 10x10 integer array representing shot outcomes.
     */
    public void setPlayerShots(int[][] shots) {
        this.playerShots = new int[10][10];
        for (int i = 0; i < 10; i++) {
            System.arraycopy(shots[i], 0, this.playerShots[i], 0, 10);
        }
    }

    /**
     * Gets the computer's shot record.
     *
     * @return A 10x10 integer array where values represent the state of each cell (e.g., hit, miss).
     */
    public int[][] getComputerShots() {
        return computerShots;
    }

    /**
     * Sets the computer's shot record with the given 10x10 integer array.
     *
     * @param shots A 10x10 integer array representing shot outcomes.
     */
    public void setComputerShots(int[][] shots) {
        this.computerShots = new int[10][10];
        for (int i = 0; i < 10; i++) {
            System.arraycopy(shots[i], 0, this.computerShots[i], 0, 10);
        }
    }

    /**
     * Checks if the game is in progress.
     *
     * @return True if the game is ongoing, false otherwise.
     */
    public boolean isGameInProgress() {
        return isGameInProgress;
    }

    /**
     * Sets the game progress status.
     *
     * @param gameInProgress True if the game is ongoing, false otherwise.
     */
    public void setGameInProgress(boolean gameInProgress) {
        this.isGameInProgress = gameInProgress;
    }

    /**
     * Gets the player's hit count.
     *
     * @return The number of successful hits made by the player.
     */
    public int getPlayerHitCount() {
        return playerHitCount;
    }

    /**
     * Sets the player's hit count.
     *
     * @param count The number of successful hits.
     */
    public void setPlayerHitCount(int count) {
        this.playerHitCount = count;
    }

    /**
     * Gets the computer's hit count.
     *
     * @return The number of successful hits made by the computer.
     */
    public int getComputerHitCount() {
        return computerHitCount;
    }

    /**
     * Sets the computer's hit count.
     *
     * @param count The number of successful hits.
     */
    public void setComputerHitCount(int count) {
        this.computerHitCount = count;
    }

    /**
     * Checks if it is the player's turn.
     *
     * @return True if it is the player's turn, false otherwise.
     */
    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    /**
     * Sets the turn to the player or computer.
     *
     * @param playerTurn True if it is the player's turn, false otherwise.
     */
    public void setPlayerTurn(boolean playerTurn) {
        this.isPlayerTurn = playerTurn;
    }

    /**
     * Gets the winner of the game.
     *
     * @return The winner's name or null if the game is not over.
     */
    public String getWinner() {
        return winner;
    }

    /**
     * Sets the winner of the game.
     *
     * @param winner The winner's name.
     */
    public void setWinner(String winner) {
        this.winner = winner;
    }

    /**
     * Checks if the game is over.
     *
     * @return True if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Sets the game's over status.
     *
     * @param gameOver True if the game is over, false otherwise.
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * Updates the placement status of a ship.
     *
     * @param shipType The type of ship (e.g., "carrier", "destroyer").
     * @param placed   True if the ship has been placed, false otherwise.
     */
    public void setShipPlaced(String shipType, boolean placed) {
        switch (shipType) {
            case "carrier": carrierPlaced = placed; break;
            case "destroyer": destroyerPlaced = placed; break;
            case "submarine": submarinePlaced = placed; break;
            case "frigate": frigatePlaced = placed; break;
        }
    }

    /**
     * Checks if a ship has been placed on the board.
     *
     * @param shipType The type of ship (e.g., "carrier", "destroyer").
     * @return True if the ship has been placed, false otherwise.
     */
    public boolean isShipPlaced(String shipType) {
        switch (shipType) {
            case "carrier": return carrierPlaced;
            case "destroyer": return destroyerPlaced;
            case "submarine": return submarinePlaced;
            case "frigate": return frigatePlaced;
            default: return false;
        }
    }
}
