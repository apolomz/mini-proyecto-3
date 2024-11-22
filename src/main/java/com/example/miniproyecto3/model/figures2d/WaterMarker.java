package com.example.miniproyecto3.model.figures2d;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class WaterMarker extends Group {
    public WaterMarker() {
        // Crear X
        Line line1 = new Line(5, 5, 25, 25);
        Line line2 = new Line(25, 5, 5, 25);

        line1.setStroke(Color.BLUE);
        line2.setStroke(Color.BLUE);
        line1.setStrokeWidth(2);
        line2.setStrokeWidth(2);

        getChildren().addAll(line1, line2);
    }
}