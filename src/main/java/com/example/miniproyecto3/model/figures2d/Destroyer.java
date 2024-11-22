package com.example.miniproyecto3.model.figures2d;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * Represents a Destroyer ship with a custom shape, size, and orientation.
 */
public class Destroyer implements IShip {
    private int size;
    private Polygon shipShape;
    private Group shipGroup;
    private String id;
    private boolean isHorizontal;

    /**
     * Constructs a new Destroyer with a given size.
     *
     * @param size the size of the Destroyer
     */
    public Destroyer(int size){
        this.size = size;
        shipShape = new Polygon();
        shipGroup = new Group();
        this.isHorizontal = true;
    }
    /**
     * Creates a visual representation of the Destroyer at the given coordinates
     * and orientation.
     *
     * @param x the X-coordinate of the Destroyer
     * @param y the Y-coordinate of the Destroyer
     * @param isHorizontal true if the ship is horizontal, false otherwise
     * @return a {@link Group} containing the ship's graphical elements
     */
    @Override
    public Group createShipShape(double x, double y, boolean isHorizontal){
        shipShape = new Polygon();
        if(isHorizontal){
            shipShape.getPoints().addAll(x,y,x+30*size,y,x+20*size,y+15,x+10*size,y+15);
        }else {
            shipShape.getPoints().addAll(x,y,x,y+30*size,x+15,y+20*size,x+15,y+10*size);
        }
        shipShape.setFill(Color.web("#BF6415"));
        shipShape.setStroke(Color.BLACK);

        shipGroup.getChildren().add(shipShape);
        return shipGroup;
    }
    /**
     * Adds the Destroyer to the specified {@link Pane}.
     *
     * @param pane the Pane to add the ship to
     */
    @Override
    public void addToPane(Pane pane){
        shipGroup.setId(this.id);
        pane.getChildren().add(shipShape);
    }
    /**
     * Sets the unique identifier of the Destroyer.
     *
     * @return the identifier of the ship
     */
    public void setId(String id){
        this.id = id;
    }
    /**
     * Gets the unique identifier of the Destroyer.
     *
     * @return the identifier of the ship
     */
    public String getId(){
        return id;
    }
    /**
     * Gets the size of the Destroyer.
     *
     * @return the size of the ship
     */
    public int getShipSize(){
        return size;
    }

    /**
     * Checks whether the Destroyer is in horizontal orientation.
     *
     * @return true if the ship is horizontal, false otherwise
     */
    @Override
    public boolean isHorizontal() {
        return isHorizontal;
    }
    /**
     * Sets the orientation of the Destroyer.
     *
     * @param isHorizontal true if the ship should be horizontal, false otherwise
     */

    @Override
    public void setOrientation(boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

}
