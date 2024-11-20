package com.example.miniproyecto3.model;

import com.example.miniproyecto3.model.exceptions.InvalidPlacementException;

public interface IGame {
    void initializeComputerGrid();
    void resetGame();
    void placeShip(String startCellId, int shipSize, boolean isHorinzontal) throws InvalidPlacementException;
    void placeShipRandomly(int shipSize);
    int[] parseCellId(String cellId);
}
