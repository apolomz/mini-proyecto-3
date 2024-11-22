package com.example.miniproyecto3.model.figures2d;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 * Represents a Frigate ship with a custom shape, size, and orientation.
 */
public class Frigate implements IShip {
    private Polygon shipShape;
    private Group shipGroup;
    private String id;
    private int size;
    private boolean isHorizontal;

    /**
     * Constructs a new Frigate with a given size.
     *
     * @param size the size of the Frigate
     */
    public Frigate(int size){
        this.size = size;
        shipShape = new Polygon();
        shipGroup = new Group();
        isHorizontal = true;
    }

    /**
     * Creates a visual representation of the Frigate at the given coordinates
     * and orientation.
     *
     * @param x the X-coordinate of the Frigate
     * @param y the Y-coordinate of the Frigate
     * @param isHorizontal true if the ship is horizontal, false otherwise
     * @return a {@link Group} containing the ship's graphical elements
     */
    @Override
    public Group createShipShape(double x, double y, boolean isHorizontal){
        shipShape = new Polygon();
        if (isHorizontal) {
            shipShape.getPoints().addAll(x, y, x + 20, y, x + 15, y + 5, x + 5, y + 5);
        } else {
            shipShape.getPoints().addAll(x, y, x, y + 20, x + 5, y + 15, x + 5, y + 5);
        }
        shipShape.setFill(Color.web("#8C503A"));
        shipShape.setStroke(Color.BLACK);

        shipGroup.getChildren().add(shipShape);
        return shipGroup;
    }
    /**
     * Adds the Frigate to the specified {@link Pane}.
     *
     * @param pane the Pane to add the ship to
     */
    @Override
    public void addToPane(Pane pane){
        shipGroup.setId(this.id);
        pane.getChildren().add(shipShape);
    }
    /**
     * Sets the unique identifier for the Frigate.
     *
     * @param id the identifier for the ship
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * Gets the unique identifier of the Frigate.
     *
     * @return the identifier of the ship
     */
    public String getId(){
        return id;
    }

    /**
     * Gets the size of the Frigate.
     *
     * @return the size of the ship
     */
    public int getShipSize(){
        return size;
    }

    /**
     * Checks whether the Frigate is in horizontal orientation.
     *
     * @return true if the ship is horizontal, false otherwise
     */
    @Override
    public boolean isHorizontal() {
        return isHorizontal;
    }

    /**
     * Sets the orientation of the Frigate.
     *
     * @param isHorizontal true if the ship should be horizontal, false otherwise
     */
    @Override
    public void setOrientation(boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
    }
}
