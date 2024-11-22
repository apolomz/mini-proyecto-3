package com.example.miniproyecto3.model.figures2d;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
/**
 * Represents a CarrierShip with a custom shape, size, and orientation.
 * It includes visual features like chimneys and a base line.
 */
public class CarrierShip implements IShip {
    private int size;
    private Polygon shipShape;
    private Circle[] chimneys;
    private Line shipBase;
    private Group shipGroup;
    private String id;
    private boolean isHorizontal;

    /**
     * Constructs a new CarrierShip with a given size.
     *
     * @param size the size of the CarrierShip
     */
    public CarrierShip(int size) {
        this.size = size;
        chimneys = new Circle[2];
        shipShape = new Polygon();
        shipGroup = new Group();
        this.isHorizontal = true;
    }

    /**
     * Creates a visual representation of the CarrierShip at the given coordinates
     * and orientation.
     *
     * @param x the X-coordinate of the CarrierShip
     * @param y the Y-coordinate of the CarrierShip
     * @param isHorizontal true if the ship is horizontal, false otherwise
     * @return a {@link Group} containing the ship's graphical elements
     */
    @Override
    public Group createShipShape(double x, double y, boolean isHorizontal) {
        shipShape = new Polygon();
        if (isHorizontal) {
            shipShape.getPoints().addAll(x, y, x + 30 * size, y, x + 20 * size, y + 15, x + 10 * size, y + 15);
        } else {
            shipShape.getPoints().addAll(x, y, x, y + 30 * size, x + 15, y + 20 * size, x + 15, y + 10 * size);
        }
        shipShape.setFill(Color.web("#023E73"));
        shipShape.setStroke(Color.BLACK);

        addChimneys(x,y,isHorizontal);
        addBase(x, y, isHorizontal);

        shipGroup.getChildren().addAll(shipShape, chimneys[0], chimneys[1], shipBase);
        return shipGroup;
    }

    /**
     * Adds chimneys to the CarrierShip based on its orientation.
     *
     * @param x the X-coordinate for the chimneys
     * @param y the Y-coordinate for the chimneys
     * @param isHorizontal true if the ship is horizontal, false otherwise
     */
    public void addChimneys(double x, double y, boolean isHorizontal) {
        if (isHorizontal) {
            chimneys[0] = new Circle(x + 20, y+ 5, 5);
            chimneys[1] = new Circle(x + 35, y + 5, 5);
        } else {
            chimneys[0] = new Circle(x + 5, y + 20, 5);
            chimneys[1] = new Circle(x + 5, y + 35, 5);
        }
        for (Circle chimney : chimneys) {
            chimney.setFill(Color.GRAY);
        }
    }

    /**
     * Adds a base line to the CarrierShip based on its orientation.
     *
     * @param x the X-coordinate of the base line
     * @param y the Y-coordinate of the base line
     * @param isHorizontal true if the ship is horizontal, false otherwise
     */
    public void addBase(double x, double y, boolean isHorizontal) {
        if (isHorizontal) {
            shipBase = new Line(x, y + 15, x + 30 * size, y + 15);
        } else {
            shipBase = new Line(x + 15, y, x + 15, y + 30 * size);
        }
        shipBase.setStroke(Color.BROWN);
        shipBase.setStrokeWidth(5);
    }
    /**
     * Adds the CarrierShip to the specified {@link Pane}.
     *
     * @param pane the Pane to add the ship to
     */
    @Override
    public void addToPane(Pane pane){
        shipGroup.setId(this.id);
        pane.getChildren().add(shipGroup);
    }
    /**
     * Sets the unique identifier for the CarrierShip.
     *
     * @param id the identifier for the ship
     */
    public void setId(String id){
        this.id = id;
    }
    /**
     * Gets the unique identifier of the CarrierShip.
     *
     * @return the identifier of the ship
     */
    public String getId(){
        return id;
    }
    /**
     * Gets the size of the CarrierShip.
     *
     * @return the size of the ship
     */
    public int getShipSize(){
        return size;
    }
    /**
     * Checks whether the CarrierShip is in horizontal orientation.
     *
     * @return true if the ship is horizontal, false otherwise
     */
    @Override
    public boolean isHorizontal() {
        return isHorizontal;
    }
    /**
     * Sets the orientation of the CarrierShip.
     *
     * @param isHorizontal true if the ship should be horizontal, false otherwise
     */
    @Override
    public void setOrientation(boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

}
