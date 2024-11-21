package com.example.miniproyecto3.controller;

import com.example.miniproyecto3.model.Game;
import com.example.miniproyecto3.model.figures2d.Flame;
import com.example.miniproyecto3.model.figures2d.Bomb;
import com.example.miniproyecto3.model.figures2d.Water;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.Random;

public class GameTurnAdapter {
    private final Game game;
    private final GridPane userGridPane;
    private final GridPane computerGridPane;
    private final Button playButton;
    private final GameController controller;

    public GameTurnAdapter(Game game, GridPane userGridPane, GridPane computerGridPane, Button playButton, GameController controller) {
        this.game = game;
        this.userGridPane = userGridPane;
        this.computerGridPane = computerGridPane;
        this.playButton = playButton;
        this.controller = controller;
    }

    public void handlePlay() {
        playButton.setDisable(true);
        startPlayerTurn();
    }

    private void startPlayerTurn() {
        // Habilitar el tablero de la computadora y deshabilitar la interacción del jugador hasta que dispare.
        computerGridPane.setDisable(false);
        computerGridPane.setOnMouseClicked(this::handlePlayerShot);
    }

    private void handlePlayerShot(MouseEvent event) {
        Button targetButton = (Button) event.getTarget();
        int col = GridPane.getColumnIndex(targetButton);
        int row = GridPane.getRowIndex(targetButton);

        // Verificar si hay un barco en la posición seleccionada
        if (game.getComputerGrid()[row][col]) {
            targetButton.setStyle("-fx-background-color: red;"); // Indicar que el barco ha sido tocado
            StackPane stack = new StackPane(targetButton);
            stack.getChildren().add(new Bomb().createBombMark());
            computerGridPane.add(stack, col, row);

            game.getComputerGrid()[row][col] = false; // Eliminar barco de la casilla

            if (controller.checkGameOver()) { // Verificar si el jugador ha ganado
                showAlert("¡Ganaste!", "Todos los barcos de la computadora han sido hundidos.");
                return;
            }
        } else {
            targetButton.setStyle("-fx-background-color: blue;"); // Indicar que el disparo fue en agua
            StackPane stack = new StackPane(targetButton);
            stack.getChildren().add(new Water().createWaterMark());
            computerGridPane.add(stack, col, row);
            computerGridPane.setDisable(true); // Deshabilitar tablero de la computadora después del disparo

            startComputerTurn(); // Hacer que la computadora dispare
        }
    }

    private void startComputerTurn() {
        Random random = new Random();
        int row, col;
        do {
            row = random.nextInt(10); // Elegir fila aleatoria
            col = random.nextInt(10); // Elegir columna aleatoria
        } while (!game.getUserGrid()[row][col]); // Asegurarse de que el disparo es sobre una casilla con barco

        Button targetButton = (Button) userGridPane.lookup("#user_cell_" + (row + 1) + "_" + (col + 1));
        if (targetButton == null) return;

        // La computadora dispara
        if (game.getUserGrid()[row][col]) {
            targetButton.setStyle("-fx-background-color: red;"); // Indicar que el barco fue tocado
            StackPane stack = new StackPane(targetButton);
            stack.getChildren().add(new Bomb().createBombMark());
            userGridPane.add(stack, col, row);
            game.getUserGrid()[row][col] = false; // Eliminar barco de la casilla

            if (controller.checkGameOver()) { // Verificar si la computadora ha ganado
                showAlert("¡Perdiste!", "Todos tus barcos han sido hundidos.");
                return;
            }
        } else {
            targetButton.setStyle("-fx-background-color: blue;"); // Indicar que el disparo fue en agua
            StackPane stack = new StackPane(targetButton);
            stack.getChildren().add(new Water().createWaterMark());
            userGridPane.add(stack, col, row);
        }

        // Reactivar turno del jugador
        computerGridPane.setDisable(false);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
