package com.example.miniproyecto3.model.figures2d;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Represents a graphical marker for water in the Battleship game.
 * The marker is displayed as an "X" in blue color.
 */
public class WaterMarker extends Group {

    /**
     * Creates a new water marker with an "X" shape.
     */
    public WaterMarker() {
        // Create the "X" shape
        Line line1 = new Line(5, 5, 25, 25);
        Line line2 = new Line(25, 5, 5, 25);

        line1.setStroke(Color.BLUE);
        line2.setStroke(Color.BLUE);
        line1.setStrokeWidth(2);
        line2.setStrokeWidth(2);

        getChildren().addAll(line1, line2);
    }
}
