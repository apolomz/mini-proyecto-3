package com.example.miniproyecto3.model.figures2d;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Flame {

    public Flame() {}

    public Group createFlameMark() {
        // Crear el polígono en forma de llama
        Polygon flame = new Polygon();
        flame.getPoints().addAll(15.0, 0.0, 20.0, 10.0, 10.0, 15.0, 20.0, 30.0, 5.0, 25.0);
        flame.setFill(Color.ORANGE);

        // Empaquetar el polígono en un Group
        Group flameMark = new Group(flame);
        return flameMark;
    }
}
