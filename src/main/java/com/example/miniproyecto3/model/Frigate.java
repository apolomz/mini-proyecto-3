package com.example.miniproyecto3.model;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Frigate implements IShip{
    private Rectangle shipShape;
    private Group shipGroup;

    public Frigate(){
        shipShape = new Rectangle();
        shipGroup = new Group();
    }

    @Override
    public Group createShipShape(double x, double y, boolean isHorizontal){
        shipShape = new Rectangle(x,y,30,30);
        shipShape.setFill(Color.web("#8C503A"));
        shipShape.setStroke(Color.BLACK);

        shipGroup.getChildren().add(shipShape);
        return shipGroup;
    }

    @Override
    public void addToPane(Pane pane){
        pane.getChildren().add(shipShape);
    }

}
