package com.example.miniproyecto3.model;

import java.io.Serializable;

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

    // Estado de los botones de barcos
    private boolean carrierPlaced;
    private boolean destroyerPlaced;
    private boolean submarinePlaced;
    private boolean frigatePlaced;

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

    // Getters y setters
    public boolean[][] getUserGrid() { return userGrid; }
    public void setUserGrid(boolean[][] grid) {
        this.userGrid = new boolean[10][10];
        for (int i = 0; i < 10; i++) {
            System.arraycopy(grid[i], 0, this.userGrid[i], 0, 10);
        }
    }

    public boolean[][] getComputerGrid() { return computerGrid; }
    public void setComputerGrid(boolean[][] grid) {
        this.computerGrid = new boolean[10][10];
        for (int i = 0; i < 10; i++) {
            System.arraycopy(grid[i], 0, this.computerGrid[i], 0, 10);
        }
    }

    public int[][] getPlayerShots() { return playerShots; }
    public void setPlayerShots(int[][] shots) {
        this.playerShots = new int[10][10];
        for (int i = 0; i < 10; i++) {
            System.arraycopy(shots[i], 0, this.playerShots[i], 0, 10);
        }
    }

    public int[][] getComputerShots() { return computerShots; }
    public void setComputerShots(int[][] shots) {
        this.computerShots = new int[10][10];
        for (int i = 0; i < 10; i++) {
            System.arraycopy(shots[i], 0, this.computerShots[i], 0, 10);
        }
    }

    public boolean isGameInProgress() { return isGameInProgress; }
    public void setGameInProgress(boolean gameInProgress) {
        this.isGameInProgress = gameInProgress;
    }

    public int getPlayerHitCount() { return playerHitCount; }
    public void setPlayerHitCount(int count) { this.playerHitCount = count; }

    public int getComputerHitCount() { return computerHitCount; }
    public void setComputerHitCount(int count) { this.computerHitCount = count; }

    public boolean isPlayerTurn() { return isPlayerTurn; }
    public void setPlayerTurn(boolean playerTurn) { this.isPlayerTurn = playerTurn; }

    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }

    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

    public void setShipPlaced(String shipType, boolean placed) {
        switch (shipType) {
            case "carrier": carrierPlaced = placed; break;
            case "destroyer": destroyerPlaced = placed; break;
            case "submarine": submarinePlaced = placed; break;
            case "frigate": frigatePlaced = placed; break;
        }
    }

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