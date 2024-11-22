package com.example.miniproyecto3.model.exceptions;

/**
 * This exception is thrown when a ship is placed in an invalid position
 * on the computer's game board.
 */
public class InvalidPlacementOnComputerBoardException extends Exception {

    /**
     * Constructs a new InvalidPlacementOnComputerBoardException with a specified detail message.
     *
     * @param message A detailed message describing the cause of the exception.
     */
    public InvalidPlacementOnComputerBoardException(String message) {
        super(message);
    }
}
