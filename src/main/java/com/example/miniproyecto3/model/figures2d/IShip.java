package com.example.miniproyecto3.model.figures2d;

import javafx.scene.Group;
import javafx.scene.layout.Pane;

public interface IShip {
    Group createShipShape(double x, double y, boolean isHorizontal);
    void addToPane(Pane pane);
    int getShipSize();
    String getId();
}
