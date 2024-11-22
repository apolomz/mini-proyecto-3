package com.example.miniproyecto3.model;

import java.io.Serializable;

/**
 * JHOAN FERNANDEZ - DANIEL CARDENAS
 * Adapter class that manages the game turns and shooting mechanics in the Battleship game.
 * This class handles the logic for both player and computer shots, tracks game state,
 * and determines when ships are sunk.
 */
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
    private int playerHitCount = 0;
    private int computerHitCount = 0;
    private static final int TOTAL_SHIP_CELLS = 20;

    /**
     * Constructs a new GameTurnAdapter.
     * @param game The main game instance to adapt
     */
    public GameTurnAdapter(Game game) {
        this.game = game;
        this.isPlayerTurn = true;
        this.gameOver = false;
        this.winner = null;
    }

    /**
     * Processes a player's shot at the specified coordinates.
     * @param row The row coordinate (1-based indexing)
     * @param col The column coordinate (1-based indexing)
     * @return Result of the shot: -1 for invalid shot, WATER for miss, HIT for hit, SUNK for sinking a ship
     */
    public int playerShoot(int row, int col) {
        if (gameOver || !isPlayerTurn) {
            return -1;
        }

        row--;
        col--;

        if (playerShots[row][col] != 0) {
            return -1;
        }

        boolean[][] computerGrid = game.getComputerGrid();
        int result;

        if (computerGrid[row][col]) {
            playerShots[row][col] = HIT;
            playerHitCount++;
            result = checkIfSunk(row, col, computerGrid, playerShots) ? SUNK : HIT;

            if (playerHitCount >= TOTAL_SHIP_CELLS) {
                gameOver = true;
                winner = "¡Jugador";
            }
        } else {
            playerShots[row][col] = WATER;
            result = WATER;
            isPlayerTurn = false;
        }

        return result;
    }

    /**
     * Executes the computer's turn to shoot.
     * @return An array containing [row, col, result] of the shot, or null if it's not computer's turn
     */
    public int[] computerShoot() {
        if (gameOver || isPlayerTurn) {
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
            isPlayerTurn = true;
        }

        return result;
    }

    /**
     * Determines the best target for the computer's next shot.
     * @return An array containing the [row, col] coordinates for the next shot
     */
    private int[] findBestTarget() {
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

        int row, col;
        do {
            row = (int) (Math.random() * 10);
            col = (int) (Math.random() * 10);
        } while (computerShots[row][col] != 0);

        return new int[]{row, col};
    }

    /**
     * Validates if the given coordinates are within the game grid.
     */
    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < 10 && col >= 0 && col < 10;
    }

    /**
     * Checks if a ship has been sunk at the given coordinates.
     */
    private boolean checkIfSunk(int row, int col, boolean[][] grid, int[][] shots) {
        int shipCells = countShipCells(row, col, grid);
        int hitCells = countHitCells(row, col, grid, shots);

        if (shipCells == hitCells) {
            markShipAsSunk(row, col, grid, shots);
            return true;
        }
        return false;
    }

    /**
     * Counts the total cells occupied by a ship at the given coordinates.
     */
    private int countShipCells(int row, int col, boolean[][] grid) {
        int count = 0;
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

    /**
     * Counts how many cells of a ship have been hit.
     */
    private int countHitCells(int row, int col, boolean[][] grid, int[][] shots) {
        int count = 0;
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

    /**
     * Marks all cells of a sunk ship in the shots grid.
     */
    private void markShipAsSunk(int row, int col, boolean[][] grid, int[][] shots) {
        boolean isHorizontal = false;
        int left = col;
        while (left >= 0 && grid[row][left]) {
            shots[row][left] = SUNK;
            left--;
            isHorizontal = true;
        }
        int right = col + 1;
        while (right < 10 && grid[row][right]) {
            shots[row][right] = SUNK;
            right++;
            isHorizontal = true;
        }

        if (!isHorizontal) {
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

    /**
     * @return true if it's the player's turn
     */
    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    /**
     * @return true if the game has ended
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * @return The winner of the game, or null if game is not over
     */
    public String getWinner() {
        return winner;
    }

    /**
     * @return Grid recording all player shots
     */
    public int[][] getPlayerShots() {
        return playerShots;
    }

    /**
     * @return Grid recording all computer shots
     */
    public int[][] getComputerShots() {
        return computerShots;
    }

    /**
     * Saves the current game state.
     * @param state The GameState object to save to
     */
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

    /**
     * Loads a saved game state.
     * @param state The GameState object to load from
     */
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