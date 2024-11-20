package com.example.miniproyecto3.model.figures2d;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class Submarine implements IShip {
    private int size;
    private Polygon shipShape;
    private Circle periscope;
    private Group shipGroup;
    private String id;

    public Submarine(int size){
        this.size = size;
        shipShape = new Polygon();
        periscope = new Circle();
        shipGroup = new Group();
    }

    @Override
    public Group createShipShape(double x, double y, boolean isHorizontal){
        shipShape = new Polygon();
        if(isHorizontal){
            shipShape.getPoints().addAll(x,y,x+30*size,y,x+25*size,y+15,x+5*size, y + 15);
        }else {
            shipShape.getPoints().addAll(x,y,x,y+30*size,x+15,y+25*size,x+15,y+5*size);
        }
        shipShape.setFill(Color.web("#024059"));
        shipShape.setStroke(Color.BLACK);

        addPeriscope(x,y,isHorizontal);
        shipGroup.getChildren().addAll(shipShape,periscope);
        return shipGroup;
    }

    private void addPeriscope(double x, double y, boolean isHorizontal){
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

    @Override
    public void addToPane(Pane pane){
        shipGroup.setId(this.id);
        pane.getChildren().add(shipGroup);
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
