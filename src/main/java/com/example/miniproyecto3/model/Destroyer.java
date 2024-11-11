package com.example.miniproyecto3.model;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Destroyer implements IShip{
    private int size = 2;
    private Polygon shipShape;
    private Group shipGroup;

    public Destroyer(){
        shipShape = new Polygon();
        shipGroup = new Group();
    }

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

    @Override
    public void addToPane(Pane pane){
        pane.getChildren().add(shipShape);
    }

}
