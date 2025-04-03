package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NumberGuessingGame extends Application {
    private int targetNumber;
    private int attempts;
    private int maxAttempts;
    private int score = 0;
    private List<Integer> guessHistory = new ArrayList<>();

    // UI Components
    private Label messageLabel;
    private Label attemptsLabel;
    private Label scoreLabel;
    private Label historyLabel;
    private TextField guessField;
    private Button submitButton;
    private Button restartButton;
    private ProgressBar progressBar;
    private VBox gameUI;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showDifficultySelection();
    }

    private void showDifficultySelection() {
        VBox difficultyBox = new VBox(20);
        difficultyBox.setAlignment(Pos.CENTER);
        difficultyBox.setPadding(new Insets(25));
        difficultyBox.setStyle("-fx-background-color: linear-gradient(to bottom, #E0EAFC, #CFDEF3);");

        Label titleLabel = new Label("Number Guessing Game");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.DARKRED); // Changed from gradient to solid color

        Label instructionLabel = new Label("Select Difficulty Level");
        instructionLabel.setFont(Font.font("Arial", 16));
        instructionLabel.setTextFill(Color.DARKSLATEGRAY);

        Button easyButton = createDifficultyButton("Easy (10 guesses)", 10);
        Button mediumButton = createDifficultyButton("Medium (7 guesses)", 7);
        Button hardButton = createDifficultyButton("Hard (5 guesses)", 5);

        difficultyBox.getChildren().addAll(titleLabel, instructionLabel, easyButton, mediumButton, hardButton);

        Scene scene = new Scene(difficultyBox, 450, 350);
        primaryStage.setTitle("Number Guessing Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Button createDifficultyButton(String text, int attempts) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 200px;");
        button.setOnAction(e -> {
            this.maxAttempts = attempts;
            startGame();
        });
        return button;
    }

    private void startGame() {
        gameUI = createGameUI();
        Scene gameScene = new Scene(gameUI, 450, 450);
        primaryStage.setScene(gameScene);
        startNewGame();
    }


    private VBox createGameUI() {
        Label titleLabel = new Label("Guess the Number!");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.DARKBLUE); // Changed from gradient to solid color


        Label instructionLabel = new Label("I'm thinking of a number between 1 and 100");
        instructionLabel.setFont(Font.font("Arial", 16));
        instructionLabel.setTextFill(Color.DARKSLATEGRAY);

        scoreLabel = new Label("Score: " + score);
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        scoreLabel.setTextFill(Color.PURPLE);

        messageLabel = new Label();
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        attemptsLabel = new Label();
        attemptsLabel.setFont(Font.font("Arial", 14));

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);
        progressBar.setStyle("-fx-accent: #4CAF50;");

        guessField = new TextField();
        guessField.setPromptText("Enter number (1-100)");
        guessField.setMaxWidth(200);
        guessField.setFont(Font.font(16));
        guessField.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");

        guessField.setOnAction(e -> checkGuess());

        submitButton = new Button("Submit Guess");
        submitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        submitButton.setOnAction(e -> checkGuess());

        restartButton = new Button("New Game");
        restartButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
        restartButton.setOnAction(e -> showDifficultySelection());

        guessField.setTextFormatter(new TextFormatter<>(change ->
                change.getText().matches("[0-9]*") ? change : null));


        // History display
        historyLabel = new Label("Previous guesses: ");
        historyLabel.setFont(Font.font("Arial", 14));
        historyLabel.setWrapText(true);
        historyLabel.setMaxWidth(300);

        VBox historyBox = new VBox(5, new Label("Guess History:"), historyLabel);
        historyBox.setPadding(new Insets(10));
        historyBox.setStyle("-fx-background-color: #f8f8f8; -fx-border-color: #ddd; -fx-border-radius: 5;");

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #E0EAFC, #CFDEF3);");

        HBox buttonBox = new HBox(15, submitButton, restartButton);
        restartButton.setVisible(false); // Hide at start


        root.getChildren().addAll(
                titleLabel,
                instructionLabel,
                scoreLabel,
                messageLabel,
                attemptsLabel,
                progressBar,
                guessField,
                buttonBox,
                historyBox
        );

        return root;
    }

    private void startNewGame() {
        Random random = new Random();
        targetNumber = random.nextInt(100) + 1;
        attempts = 0;
        guessHistory.clear();
        updateUIForNewGame();
    }

    private void updateUIForNewGame() {
        messageLabel.setText("Take a guess!");
        messageLabel.setTextFill(Color.BLACK);
        attemptsLabel.setText("Attempts: 0/" + maxAttempts);
        progressBar.setProgress(0);
        guessField.clear();
        guessField.setDisable(false);
        submitButton.setDisable(false);
        restartButton.setVisible(false);
        updateHistoryDisplay();
    }

    private void checkGuess() {

        if (guessField.getText().trim().isEmpty()) {
            messageLabel.setText("Please enter a number between 1-100!");
            messageLabel.setTextFill(Color.RED);
            return; // Exit method to prevent further execution
        }

        try {
            int userGuess = Integer.parseInt(guessField.getText());
            if (userGuess < 1 || userGuess > 100) {
                throw new NumberFormatException();
            }

            attempts++;
            guessHistory.add(userGuess);
            attemptsLabel.setText("Attempts: " + attempts + "/" + maxAttempts);
            progressBar.setProgress((double) attempts / maxAttempts);
            updateHistoryDisplay();

            if (userGuess == targetNumber) {
                score += (maxAttempts - attempts + 1) * 10;
                scoreLabel.setText("Score: " + score);
                endGame(true);
            } else if (attempts >= maxAttempts) {
                endGame(false);
            } else {
                String hint = userGuess < targetNumber ? "higher" : "lower";
                messageLabel.setText("Try again! Go " + hint);
                messageLabel.setTextFill(userGuess < targetNumber ? Color.BLUE : Color.ORANGE);
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Please enter a number between 1-100!");
            messageLabel.setTextFill(Color.RED);
        } finally {
            guessField.clear();
        }
    }

    private void updateHistoryDisplay() {
        StringBuilder historyText = new StringBuilder();
        for (int i = 0; i < guessHistory.size(); i++) {
            int guess = guessHistory.get(i);
            String comparison = guess < targetNumber ? "↑" : guess > targetNumber ? "↓" : "✓";
            historyText.append(guess).append(" ").append(comparison);
            if (i < guessHistory.size() - 1) {
                historyText.append(", ");
            }
        }
        historyLabel.setText(historyText.toString());
    }

    private void endGame(boolean won) {
        if (won) {
            messageLabel.setText("Congratulations! You guessed it in " + attempts + " tries!");
            messageLabel.setTextFill(Color.GREEN);
        } else {
            messageLabel.setText("Game over! The number was " + targetNumber);
            messageLabel.setTextFill(Color.RED);
        }
        guessField.setDisable(true);
        submitButton.setDisable(true);
        restartButton.setVisible(true);
    }

}