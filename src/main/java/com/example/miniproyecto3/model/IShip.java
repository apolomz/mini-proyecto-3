package com.example.miniproyecto3.model;

import javafx.scene.Group;
import javafx.scene.layout.Pane;

public interface IShip {
    Group createShipShape(double x, double y, boolean isHorizontal);
    void addToPane(Pane pane);
}
