package com.example.miniproyecto3.model.figures2d;

import javafx.scene.Group;
import javafx.scene.layout.Pane;

/**
 * Interface representing a generic ship for a Battleship game.
 */
public interface IShip {
    /**
     * Creates the graphical representation of the ship.
     *
     * @param x The x-coordinate of the ship's position.
     * @param y The y-coordinate of the ship's position.
     * @param isHorizontal True if the ship is horizontal, false if vertical.
     * @return A {@link Group} containing the ship's graphical elements.
     */
    Group createShipShape(double x, double y, boolean isHorizontal);

    /**
     * Adds the ship to the specified pane.
     *
     * @param pane The {@link Pane} where the ship will be added.
     */
    void addToPane(Pane pane);

    /**
     * Gets the size of the ship.
     *
     * @return The size of the ship as an integer.
     */
    int getShipSize();

    /**
     * Gets the unique identifier of the ship.
     *
     * @return A {@link String} representing the ship's ID.
     */
    String getId();

    /**
     * Checks if the ship is oriented horizontally.
     *
     * @return True if the ship is horizontal, false otherwise.
     */
    boolean isHorizontal();

    /**
     * Sets the orientation of the ship.
     *
     * @param isHorizontal True to set the ship as horizontal, false for vertical.
     */
    void setOrientation(boolean isHorizontal);
}
