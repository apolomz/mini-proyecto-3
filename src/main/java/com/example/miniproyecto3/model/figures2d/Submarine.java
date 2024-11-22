package com.example.miniproyecto3.model.figures2d;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

/**
 * Represents a submarine in the Battleship game.
 * The submarine can be displayed on a graphical interface with a specific shape,
 * size, orientation, and a unique identifier.
 */
public class Submarine implements IShip {
    private int size;
    private Polygon shipShape;
    private Circle periscope;
    private Group shipGroup;
    private String id;
    private boolean isHorizontal;

    /**
     * Constructs a new Submarine object with the specified size.
     *
     * @param size The size of the submarine.
     */
    public Submarine(int size) {
        this.size = size;
        shipShape = new Polygon();
        periscope = new Circle();
        shipGroup = new Group();
        this.isHorizontal = true;
    }

    /**
     * Creates the shape of the submarine, including its main body and periscope,
     * and positions it at the specified coordinates.
     *
     * @param x            The x-coordinate of the submarine's initial position.
     * @param y            The y-coordinate of the submarine's initial position.
     * @param isHorizontal True if the submarine is horizontal; false if vertical.
     * @return A {@link Group} containing the graphical representation of the submarine.
     */
    @Override
    public Group createShipShape(double x, double y, boolean isHorizontal) {
        shipShape = new Polygon();
        if (isHorizontal) {
            shipShape.getPoints().addAll(
                    x, y,
                    x + 30 * size, y,
                    x + 25 * size, y + 15,
                    x + 5 * size, y + 15
            );
        } else {
            shipShape.getPoints().addAll(
                    x, y,
                    x, y + 30 * size,
                    x + 15, y + 25 * size,
                    x + 15, y + 5 * size
            );
        }
        shipShape.setFill(Color.web("#024059"));
        shipShape.setStroke(Color.BLACK);

        addPeriscope(x, y, isHorizontal);
        shipGroup.getChildren().addAll(shipShape, periscope);
        return shipGroup;
    }

    /**
     * Adds a periscope to the submarine at the specified position.
     *
     * @param x            The x-coordinate for the periscope's position.
     * @param y            The y-coordinate for the periscope's position.
     * @param isHorizontal True if the submarine is horizontal; false if vertical.
     */
    private void addPeriscope(double x, double y, boolean isHorizontal) {
        periscope.setRadius(5);
        periscope.setFill(Color.GRAY);
        if (isHorizontal) {
            periscope.setCenterX(x + 15);
            periscope.setCenterY(y - 10);
        } else {
            periscope.setCenterX(x + 7.5);
            periscope.setCenterY(y + 15);
        }
    }

    /**
     * Adds the submarine's graphical representation to the specified pane.
     *
     * @param pane The {@link Pane} where the submarine will be added.
     */
    @Override
    public void addToPane(Pane pane) {
        shipGroup.setId(this.id);
        pane.getChildren().add(shipGroup);
    }

    /**
     * Sets the unique identifier for the submarine.
     *
     * @param id The unique identifier for the submarine.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the unique identifier of the submarine.
     *
     * @return The submarine's unique identifier.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Returns the size of the submarine.
     *
     * @return The size of the submarine.
     */
    @Override
    public int getShipSize() {
        return size;
    }

    /**
     * Checks if the submarine is oriented horizontally.
     *
     * @return True if the submarine is horizontal; false if vertical.
     */
    @Override
    public boolean isHorizontal() {
        return isHorizontal;
    }

    /**
     * Sets the orientation of the submarine.
     *
     * @param isHorizontal True to set the submarine as horizontal; false to set it as vertical.
     */
    @Override
    public void setOrientation(boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
    }
}
