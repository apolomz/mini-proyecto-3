package com.example.miniproyecto3.model;

public class Player {
    protected boolean[][] grid;

    public Player(int gridSize) {
        this.grid = new boolean[gridSize][gridSize];
    }

    public boolean isShipSunk(int row, int col) {
        // Implementación lógica para verificar si un barco está completamente hundido
        return !grid[row][col];
    }

    public void markCellAsHit(int row, int col) {
        grid[row][col] = false;
    }

    public boolean[][] getGrid() {
        return grid;
    }
}
