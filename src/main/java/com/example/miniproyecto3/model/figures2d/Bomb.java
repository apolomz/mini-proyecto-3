package com.example.miniproyecto3.model.figures2d;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

public class Bomb {

    public Bomb(){}

    public Group createBombMark() {
        Circle circle = new Circle(15, 15, 12); // Círculo representando la bomba
        circle.setFill(Color.BLACK);

        Line fuse = new Line(15, 5, 15, 0); // Línea para el fusible
        fuse.setStroke(Color.GRAY);
        fuse.setStrokeWidth(2);

        Group bombGroup = new Group();
        bombGroup.getChildren().addAll(circle, fuse);
        return bombGroup;
    }

}
