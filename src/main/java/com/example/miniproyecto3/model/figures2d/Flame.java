package com.example.miniproyecto3.model.figures2d;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;


public class Flame {

    public Flame(){}

    public Polygon createFlameMark() {
        Polygon flame = new Polygon();
        flame.getPoints().addAll(15.0, 0.0, 20.0, 10.0, 10.0, 15.0, 20.0, 30.0, 5.0, 25.0);
        flame.setFill(Color.ORANGE);
        return flame;
    }

}
