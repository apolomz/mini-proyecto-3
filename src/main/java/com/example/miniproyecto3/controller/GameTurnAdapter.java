package com.example.miniproyecto3.controller;

import com.example.miniproyecto3.controller.GameController;
import com.example.miniproyecto3.model.Game;
import com.example.miniproyecto3.model.figures2d.Flame;
import com.example.miniproyecto3.model.figures2d.Bomb;
import com.example.miniproyecto3.model.figures2d.Water;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.Random;

public class GameTurnAdapter{

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
        // Iniciar el turno del jugador
        playButton.setDisable(true); // Desactivar el botón de jugar durante el turno del jugador
        startPlayerTurn();
    }

    private void startPlayerTurn() {
        // Permitir que el jugador dispare al tablero de la computadora
        computerGridPane.setDisable(false); // Habilitar las celdas de la computadora para disparar

        // Configurar el evento de disparo del jugador
        computerGridPane.setOnMouseClicked(event -> handlePlayerShot(event));
    }

    private void handlePlayerShot(javafx.scene.input.MouseEvent event) {
        Button targetButton = (Button) event.getSource();
        int row = GridPane.getRowIndex(targetButton);
        int col = GridPane.getColumnIndex(targetButton);

        // Agregar una StackPane al botón donde se dibujarán las figuras
        StackPane stack = new StackPane();
        stack.getChildren().add(targetButton); // Agregar el botón como fondo

        // Crear las figuras 2D
        Water water = new Water();
        Bomb bomb = new Bomb();
        Flame flame = new Flame();

        // Asegurarse de que el jugador solo dispare a celdas no tocadas aún
        if (game.getComputerGrid()[row][col]) {
            // Si acierta, marca como tocado
            targetButton.setStyle("-fx-background-color: red;");
            stack.getChildren().add(bomb.createBombMark()); // Agregar bomba (tocado)
            game.getComputerGrid()[row][col] = false; // El barco ha sido tocado
            if (controller.checkGameOver()) {
                // Si el jugador hunde todos los barcos de la computadora
                showWinner("¡Ganaste!");
                return;
            }
        } else {
            // Si no acierta, marca como agua
            targetButton.setStyle("-fx-background-color: blue;");
            stack.getChildren().addAll(water.createWaterMark()); // Agregar X (agua)
        }

        // Reemplazar el botón original con el StackPane para que las figuras aparezcan
        computerGridPane.add(stack, col, row);

        // Al final del turno del jugador, el turno pasa a la computadora
        computerGridPane.setDisable(true); // Desactivar el tablero de la computadora mientras la computadora dispara
        startComputerTurn();
    }

    private void startComputerTurn() {
        // La computadora realiza su disparo aleatorio
        Random random = new Random();
        int row, col;
        do {
            row = random.nextInt(10);
            col = random.nextInt(10);
        } while (game.getUserGrid()[row][col] == false); // La computadora no disparará donde ya haya disparado

        // Si acierta, marca como tocado
        Button targetButton = (Button) userGridPane.lookup("#user_cell_" + (row + 1) + "_" + (col + 1));
        StackPane stack = new StackPane();
        stack.getChildren().add(targetButton); // Agregar el botón como fondo

        Bomb bomb = new Bomb();
        Flame flame = new Flame();

        if (game.getUserGrid()[row][col]) {
            targetButton.setStyle("-fx-background-color: red;");
            stack.getChildren().add(bomb.createBombMark()); // Agregar bomba (tocado)
            game.getUserGrid()[row][col] = false; // El barco ha sido tocado
            if (controller.checkGameOver()) {
                // Si la computadora hunde todos los barcos del jugador
                showWinner("¡Perdiste! La computadora ha ganado.");
                return;
            }
        } else {
            targetButton.setStyle("-fx-background-color: blue;");
            stack.getChildren().addAll(new Water().createWaterMark()); // Agregar X (agua)
        }

        userGridPane.add(stack, col, row);

        // Después del turno de la computadora, vuelve el turno al jugador
        computerGridPane.setDisable(false); // Habilitar el tablero del jugador para disparar
    }

    private void showWinner(String message) {
        // Muestra quién ganó el juego
        System.out.println(message);
        // Aquí podrías agregar una ventana emergente o mensaje en la UI para mostrar quién ganó
        // Deshabilitar los botones para evitar más interacción
    }
}
