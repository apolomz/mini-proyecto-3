package com.example.miniproyecto3.model.figures2d;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Destroyer implements IShip {
    private int size;
    private Polygon shipShape;
    private Group shipGroup;
    private String id;

    public Destroyer(int size){
        this.size = size;
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
