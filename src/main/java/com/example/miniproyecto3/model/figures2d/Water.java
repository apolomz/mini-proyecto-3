package com.example.miniproyecto3.model.figures2d;

import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

public class Water {

    public Water() {}

    public Group createWaterMark() {
        // Crear las líneas que forman la "X"
        Line line1 = new Line(5, 5, 25, 25); // Línea diagonal 1
        line1.setStroke(Color.RED);
        line1.setStrokeWidth(2);

        Line line2 = new Line(5, 25, 25, 5); // Línea diagonal 2
        line2.setStroke(Color.RED);
        line2.setStrokeWidth(2);

        // Empaquetar las líneas en un Group
        Group waterMark = new Group(line1, line2);
        return waterMark;
    }
}
