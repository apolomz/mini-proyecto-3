package com.example.miniproyecto3.model.figures2d;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class HitMarker extends Group {
    public HitMarker() {
        // Crear explosión
        Circle outer = new Circle(15, 15, 12);
        outer.setFill(Color.TRANSPARENT);
        outer.setStroke(Color.RED);
        outer.setStrokeWidth(2);

        Circle inner = new Circle(15, 15, 6);
        inner.setFill(Color.RED);

        // Rayos de la explosión
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4;
            Line ray = new Line(
                    15 + Math.cos(angle) * 6,
                    15 + Math.sin(angle) * 6,
                    15 + Math.cos(angle) * 12,
                    15 + Math.sin(angle) * 12
            );
            ray.setStroke(Color.RED);
            ray.setStrokeWidth(2);
            getChildren().add(ray);
        }

        getChildren().addAll(outer, inner);
    }
}