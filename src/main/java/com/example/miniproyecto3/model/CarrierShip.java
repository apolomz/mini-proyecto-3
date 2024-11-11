package com.example.miniproyecto3.model;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class CarrierShip implements IShip{
    private int size;
    private Polygon shipShape;
    private Circle[] chimneys;
    private Line shipBase;
    private Group shipGroup;

    public CarrierShip(int size) {
        this.size = size;
        chimneys = new Circle[2];
        shipShape = new Polygon();
        shipGroup = new Group();
    }

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

    public void addBase(double x, double y, boolean isHorizontal) {
        if (isHorizontal) {
            shipBase = new Line(x, y + 15, x + 30 * size, y + 15);
        } else {
            shipBase = new Line(x + 15, y, x + 15, y + 30 * size);
        }
        shipBase.setStroke(Color.BROWN);
        shipBase.setStrokeWidth(5);
    }

    @Override
    public void addToPane(Pane pane){
        pane.getChildren().add(shipGroup);
    }


}
