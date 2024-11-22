package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.exceptions.InvalidPlacementException;
import com.example.miniproyecto3.model.figures2d.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Game implements IGame, Serializable {

    private static final long serialVersionUID = 1L;
    private int turnCount = 0;
    private int carrierCount = 0;
    private int destroyerCount = 0;
    private int frigateCount = 0;
    private int submarineCount = 0;

    private final List<IShip> userShips = new ArrayList<>();
    private final boolean[][] userGrid = new boolean[10][10];
    private final boolean[][] computerGrid = new boolean[10][10];
    private GameState currentState;

    public Game() {
        initializeComputerGrid();
        this.currentState = new GameState();
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void saveGame(String fileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            currentState.setUserGrid(userGrid);
            currentState.setComputerGrid(computerGrid);
            oos.writeObject(currentState);
        }
    }

    public void loadGame(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            GameState loadedState = (GameState) ois.readObject();
            boolean[][] loadedUserGrid = loadedState.getUserGrid();
            boolean[][] loadedComputerGrid = loadedState.getComputerGrid();

            // Copiar los estados cargados
            for (int i = 0; i < 10; i++) {
                System.arraycopy(loadedUserGrid[i], 0, userGrid[i], 0, 10);
                System.arraycopy(loadedComputerGrid[i], 0, computerGrid[i], 0, 10);
            }
            this.currentState = loadedState;
        }
    }

    public boolean[][] getComputerGrid() {
        return computerGrid;
    }
    public boolean[][] getUserGrid() {
        return userGrid;
    }

    @Override
    public void initializeComputerGrid() {
        clearGrid(computerGrid);  // Limpiar el tablero de la computadora
        placeShipRandomly(4);     // Portaaviones
        placeShipRandomly(3);     // Submarinos
        placeShipRandomly(3);
        placeShipRandomly(2);     // Destructores
        placeShipRandomly(2);
        placeShipRandomly(2);
        placeShipRandomly(1);     // Fragatas
        placeShipRandomly(1);
        placeShipRandomly(1);
        placeShipRandomly(1);
    }


    @Override
    public void resetGame(){
        clearGrid(userGrid);
        clearGrid(computerGrid);
        initializeComputerGrid();
        this.currentState = new GameState();
    }

    @Override
    public void placeShip(String startCellId, int shipSize, boolean isHorizontal) throws InvalidPlacementException {
        int[] coords = parseCellId(startCellId);
        int startRow = coords[0] - 1;  // Ajustar para índices base-0
        int startCol = coords[1] - 1;

        // Verificar límites del tablero
        if (isHorizontal) {
            if (startCol + shipSize > 10) {
                throw new InvalidPlacementException("El barco se sale del tablero horizontalmente");
            }
        } else {
            if (startRow + shipSize > 10) {
                throw new InvalidPlacementException("El barco se sale del tablero verticalmente");
            }
        }

        // Verificar superposición con otros barcos
        for (int i = 0; i < shipSize; i++) {
            int row = isHorizontal ? startRow : startRow + i;
            int col = isHorizontal ? startCol + i : startCol;

            if (row >= 10 || col >= 10 || userGrid[row][col]) {
                throw new InvalidPlacementException("Posición ocupada o inválida");
            }
        }

        // Colocar el barco
        for (int i = 0; i < shipSize; i++) {
            int row = isHorizontal ? startRow : startRow + i;
            int col = isHorizontal ? startCol + i : startCol;
            userGrid[row][col] = true;
        }
    }

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

    public IShip findShipById(String shipId) {
        for (IShip ship : userShips) {
            if (ship.getId().equals(shipId)) {
                return ship;
            }
        }
        return null;
    }


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


    @Override
    public int[] parseCellId(String cellId) {
        String[] parts = cellId.split("_");
        int row = Integer.parseInt(parts[2]);  // Ya no restamos 1
        int col = Integer.parseInt(parts[3]);
        return new int[]{row, col};
    }


    private void clearGrid(boolean[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = false;
            }
        }
    }

    public boolean isPlacementValid(String cellId, int shipSize, boolean isHorizontal, String player) {
        // Lógica de validación para el placement del barco
        return true; // Retornar true si es válido, false si no lo es
    }

}
