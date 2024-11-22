package com.example.miniproyecto3.model.figures2d;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.LineTo;

public class SunkMarker extends Group {
    public SunkMarker() {
        // Crear llama
        Path flame = new Path();
        flame.setFill(Color.DARKRED);

        flame.getElements().addAll(
                new MoveTo(15, 5),
                new LineTo(20, 15),
                new LineTo(25, 10),
                new LineTo(20, 25),
                new LineTo(15, 20),
                new LineTo(10, 25),
                new LineTo(5, 10),
                new LineTo(10, 15),
                new LineTo(15, 5)
        );

        getChildren().add(flame);
    }
}