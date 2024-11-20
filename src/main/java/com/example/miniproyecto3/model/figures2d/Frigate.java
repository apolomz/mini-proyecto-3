package com.example.miniproyecto3.model.figures2d;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class Frigate implements IShip {
    private Polygon shipShape;
    private Group shipGroup;
    private String id;
    private int size;

    public Frigate(int size){
        this.size = size;
        shipShape = new Polygon();
        shipGroup = new Group();
    }

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

    @Override
    public void addToPane(Pane pane){
        shipGroup.setId(this.id);
        pane.getChildren().add(shipShape);
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public int getShipSize(){
        return size;
    }

}
