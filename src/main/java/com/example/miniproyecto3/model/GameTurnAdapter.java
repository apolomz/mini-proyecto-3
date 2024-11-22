package com.example.miniproyecto3.model;


import java.io.Serializable;

public class GameTurnAdapter implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Game game;
    private boolean isPlayerTurn;
    private boolean gameOver;
    private String winner;

    public static final int WATER = 0;
    public static final int HIT = 1;
    public static final int SUNK = 2;

    private final int[][] playerShots = new int[10][10];
    private final int[][] computerShots = new int[10][10];

    // Añadimos contadores para llevar el registro de barcos hundidos
    private int playerHitCount = 0;
    private int computerHitCount = 0;
    private static final int TOTAL_SHIP_CELLS = 20; // Total de celdas ocupadas por barcos

    public GameTurnAdapter(Game game) {
        this.game = game;
        this.isPlayerTurn = true;
        this.gameOver = false;
        this.winner = null;
    }

    public int playerShoot(int row, int col) {
        if (gameOver || playerShots[row-1][col-1] != 0) {
            return -1;
        }

        boolean[][] computerGrid = game.getComputerGrid();
        int result;

        if (computerGrid[row-1][col-1]) {
            playerShots[row-1][col-1] = HIT;
            playerHitCount++;
            result = checkIfSunk(row-1, col-1, computerGrid, playerShots) ? SUNK : HIT;

            if (playerHitCount >= TOTAL_SHIP_CELLS) {
                gameOver = true;
                winner = "¡Jugador";
            }
        } else {
            playerShots[row-1][col-1] = WATER;
            result = WATER;
        }

        return result;
    }

    public int[] computerShoot() {
        if (gameOver) {
            return null;
        }

        int[] target = findBestTarget();
        int row = target[0];
        int col = target[1];
        boolean[][] userGrid = game.getUserGrid();
        int[] result;

        if (userGrid[row][col]) {
            computerShots[row][col] = HIT;
            computerHitCount++;

            if (checkIfSunk(row, col, userGrid, computerShots)) {
                result = new int[]{row, col, SUNK};
            } else {
                result = new int[]{row, col, HIT};
            }

            if (computerHitCount >= TOTAL_SHIP_CELLS) {
                gameOver = true;
                winner = "¡Computadora";
            }
        } else {
            computerShots[row][col] = WATER;
            result = new int[]{row, col, WATER};
        }

        return result;
    }

    private int[] findBestTarget() {
        // Primero, buscar celdas adyacentes a hits previos
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (computerShots[i][j] == HIT) {
                    int[][] adjacentCells = {
                            {i-1, j}, {i+1, j}, {i, j-1}, {i, j+1}
                    };

                    for (int[] cell : adjacentCells) {
                        if (isValidCell(cell[0], cell[1]) && computerShots[cell[0]][cell[1]] == 0) {
                            return new int[]{cell[0], cell[1]};
                        }
                    }
                }
            }
        }

        // Si no hay hits previos, elegir una celda aleatoria no disparada
        int row, col;
        do {
            row = (int) (Math.random() * 10);
            col = (int) (Math.random() * 10);
        } while (computerShots[row][col] != 0);

        return new int[]{row, col};
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < 10 && col >= 0 && col < 10;
    }

    private boolean checkIfSunk(int row, int col, boolean[][] grid, int[][] shots) {
        int shipCells = countShipCells(row, col, grid);
        int hitCells = countHitCells(row, col, grid, shots);

        if (shipCells == hitCells) {
            markShipAsSunk(row, col, grid, shots);
            return true;
        }
        return false;
    }

    private int countShipCells(int row, int col, boolean[][] grid) {
        int count = 0;
        // Contar horizontal
        int left = col;
        while (left >= 0 && grid[row][left]) {
            count++;
            left--;
        }
        int right = col + 1;
        while (right < 10 && grid[row][right]) {
            count++;
            right++;
        }

        // Si no hay barco horizontal, contar vertical
        if (count == 1) {
            count = 0;
            int up = row;
            while (up >= 0 && grid[up][col]) {
                count++;
                up--;
            }
            int down = row + 1;
            while (down < 10 && grid[down][col]) {
                count++;
                down++;
            }
        }

        return count;
    }

    private int countHitCells(int row, int col, boolean[][] grid, int[][] shots) {
        int count = 0;
        // Contar hits horizontales
        int left = col;
        while (left >= 0 && grid[row][left]) {
            if (shots[row][left] >= HIT) count++;
            left--;
        }
        int right = col + 1;
        while (right < 10 && grid[row][right]) {
            if (shots[row][right] >= HIT) count++;
            right++;
        }

        // Si no hay barco horizontal, contar vertical
        if (count <= 1) {
            count = 0;
            int up = row;
            while (up >= 0 && grid[up][col]) {
                if (shots[up][col] >= HIT) count++;
                up--;
            }
            int down = row + 1;
            while (down < 10 && grid[down][col]) {
                if (shots[down][col] >= HIT) count++;
                down--;
            }
        }

        return count;
    }

    private void markShipAsSunk(int row, int col, boolean[][] grid, int[][] shots) {
        // Marcar horizontal
        int left = col;
        while (left >= 0 && grid[row][left]) {
            shots[row][left] = SUNK;
            left--;
        }
        int right = col + 1;
        while (right < 10 && grid[row][right]) {
            shots[row][right] = SUNK;
            right++;
        }

        // Si no hay barco horizontal, marcar vertical
        if (left == col - 1 && right == col + 1) {
            int up = row;
            while (up >= 0 && grid[up][col]) {
                shots[up][col] = SUNK;
                up--;
            }
            int down = row + 1;
            while (down < 10 && grid[down][col]) {
                shots[down][col] = SUNK;
                down++;
            }
        }
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getWinner() {
        return winner;
    }

    public int[][] getPlayerShots() {
        return playerShots;
    }

    public int[][] getComputerShots() {
        return computerShots;
    }
    public void saveState(GameState state) {
        state.setPlayerShots(playerShots);
        state.setComputerShots(computerShots);
        state.setPlayerHitCount(playerHitCount);
        state.setComputerHitCount(computerHitCount);
        state.setPlayerTurn(isPlayerTurn);
        state.setWinner(winner);
        state.setGameOver(gameOver);
        state.setGameInProgress(true);
    }
    public void loadState(GameState state) {
        int[][] loadedPlayerShots = state.getPlayerShots();
        int[][] loadedComputerShots = state.getComputerShots();

        for (int i = 0; i < 10; i++) {
            System.arraycopy(loadedPlayerShots[i], 0, playerShots[i], 0, 10);
            System.arraycopy(loadedComputerShots[i], 0, computerShots[i], 0, 10);
        }

        playerHitCount = state.getPlayerHitCount();
        computerHitCount = state.getComputerHitCount();
        isPlayerTurn = state.isPlayerTurn();
        winner = state.getWinner();
        gameOver = state.isGameOver();
    }
}