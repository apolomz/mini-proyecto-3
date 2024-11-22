package com.example.miniproyecto3.model.exceptions;

/**
 * This exception is thrown when a ship is placed in an invalid position
 * on the player's game board.
 */
public class InvalidPlacementException extends Exception {

    /**
     * Constructs a new InvalidPlacementException with a specified detail message.
     *
     * @param message A detailed message describing the cause of the exception.
     */
    public InvalidPlacementException(String message) {
        super(message);
    }
}
