import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * A simple JavaFX Quiz Application.
 * This application loads questions from a text file, randomly selects a subset,
 * and allows the user to answer them while tracking their score.
 *
 * @author Tommy White
 * @author Umanga Bajgai
 *
 * @version 1.0
 */
public class QuizApp extends Application
{
    private static final Path QUIZ_FILE_PATH = Path.of("src/text/quiz.txt");
    private static final int QUIZ_SIZE = 10;
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 300;
    private static final int VBOX_SPACING = 10;
    private static final int INITIAL_SCORE = 0;

    private List<Question> allQuestions;
    private List<Question> quizQuestions;
    private int currentIndex;
    private int score;
    private Label questionLabel;
    private Label scoreLabel;
    private TextField answerField;
    private Button submitButton;
    private Button startButton;

    /**
     * Starts the JavaFX application.
     *
     * @param stage the primary stage
     */
    @Override
    public void start(final Stage stage)
    {
        loadQuestions();

        questionLabel = new Label("Press Start to begin!");
        scoreLabel = new Label("Score: " + INITIAL_SCORE);

        answerField = new TextField();
        answerField.setPromptText("Enter your answer");
        answerField.setDisable(true);

        submitButton = new Button("Submit");
        submitButton.setDisable(true);

        startButton = new Button("Start Quiz");

        submitButton.setOnAction(e -> checkAnswer());
        answerField.setOnAction(e -> checkAnswer());
        startButton.setOnAction(e -> startQuiz());

        final VBox root;

        root = new VBox(VBOX_SPACING,
                        questionLabel,
                        answerField,
                        submitButton,
                        scoreLabel,
                        startButton);

        root.setAlignment(Pos.CENTER);

        final Scene scene;

        scene = new Scene(root,
                          WINDOW_WIDTH,
                          WINDOW_HEIGHT);

        scene.getStylesheets().add("styles.css");

        stage.setTitle("Quiz App");
        stage.setScene(scene);
        stage.show();
    }

    /*
     * Loads questions from the quiz file.
     */
    private void loadQuestions()
    {
        allQuestions = new ArrayList<>();

        try
        {
            final List<String> lines;

            lines = Files.readAllLines(QUIZ_FILE_PATH);

            for (final String line : lines)
            {
                final String[] parts = line.split("\\|");
                if (parts.length == 2)
                {
                    allQuestions.add(new Question(parts[0], parts[1]));
                }
            }
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
    }

    /*
     * Starts a new quiz.
     */
    private void startQuiz()
    {
        Collections.shuffle(allQuestions);
        quizQuestions = allQuestions.subList(0, QUIZ_SIZE);

        currentIndex = 0;
        score = INITIAL_SCORE;

        scoreLabel.setText("Score: " + INITIAL_SCORE);

        answerField.setDisable(false);
        submitButton.setDisable(false);
        startButton.setDisable(true);

        showQuestion();
    }

    /*
     * Displays the current question.
     */
    private void showQuestion()
    {
        if (currentIndex < quizQuestions.size())
        {
            final Question currentQuestion = quizQuestions.get(currentIndex);
            questionLabel.setText(currentQuestion.getQuestion());
            answerField.clear();
        }
        else
        {
            endQuiz();
        }
    }

    /*
     * Checks the user's answer against the correct answer.
     */
    private void checkAnswer()
    {
        final String userAnswer;
        final Question currentQuestion;

        userAnswer = answerField.getText().trim();
        currentQuestion = quizQuestions.get(currentIndex);

        if (userAnswer.equalsIgnoreCase(currentQuestion.getAnswer()))
        {
            score++;
        }

        scoreLabel.setText("Score: " + score);

        currentIndex++;
        showQuestion();
    }

    /*
     * Ends the quiz and displays the final score.
     */
    private void endQuiz()
    {
        questionLabel.setText("Quiz Finished! Final Score: " + score +
                              "/" + QUIZ_SIZE);

        answerField.setDisable(true);
        submitButton.setDisable(true);
        startButton.setDisable(false);
    }

    /**
     * Main method to launch the application.
     *
     * @param args command-line arguments
     */
    public static void main(final String[] args)
    {
        launch(args);
    }
}

/**
 * Represents a quiz question and its answer.
 */
class Question
{
    private final String question;
    private final String answer;

    /**
     * Constructs a Question.
     *
     * @param question the question text
     * @param answer   the correct answer
     */
    public Question(final String question,
                    final String answer)
    {
        this.question = question;
        this.answer = answer;
    }

    /**
     * Gets the question text.
     *
     * @return the question
     */
    public String getQuestion()
    {
        return question;
    }

    /**
     * Gets the correct answer.
     *
     * @return the answer
     */
    public String getAnswer()
    {
        return answer;
    }
}
