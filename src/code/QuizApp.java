import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class QuizApp extends Application
{
    private List<Question> allQuestions;
    private List<Question> quizQuestions;

    private int currentIndex;
    private int score;

    private Label questionLabel;
    private Label scoreLabel;
    private TextField answerField;
    private Button submitButton;
    private Button startButton;

    @Override
    public void start(Stage stage)
    {
        loadQuestions();

        questionLabel = new Label("Press Start to begin!");
        scoreLabel = new Label("Score: 0");

        answerField = new TextField();
        answerField.setPromptText("Enter your answer");
        answerField.setDisable(true);

        submitButton = new Button("Submit");
        submitButton.setDisable(true);

        startButton = new Button("Start Quiz");

        // Button click
        submitButton.setOnAction(e -> checkAnswer());

        // ENTER key
        answerField.setOnAction(e -> checkAnswer());

        startButton.setOnAction(e -> startQuiz());

        VBox root = new VBox(10, questionLabel, answerField, submitButton, scoreLabel, startButton);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add("styles.css");

        stage.setTitle("Quiz App");
        stage.setScene(scene);
        stage.show();
    }

    private void loadQuestions()
    {
        allQuestions = new ArrayList<>();

        try
        {
            List<String> lines = Files.readAllLines(Paths.get("quiz.txt"));

            for (String line : lines)
            {
                String[] parts = line.split("\\|");
                if (parts.length == 2)
                {
                    allQuestions.add(new Question(parts[0], parts[1]));
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void startQuiz()
    {
        Collections.shuffle(allQuestions);
        quizQuestions = allQuestions.subList(0, 10);

        currentIndex = 0;
        score = 0;

        scoreLabel.setText("Score: 0");

        answerField.setDisable(false);
        submitButton.setDisable(false);
        startButton.setDisable(true);

        showQuestion();
    }

    private void showQuestion()
    {
        if (currentIndex < quizQuestions.size())
        {
            questionLabel.setText(quizQuestions.get(currentIndex).getQuestion());
            answerField.clear();
        }
        else
        {
            endQuiz();
        }
    }

    private void checkAnswer()
    {
        String userAnswer = answerField.getText().trim();

        Question current = quizQuestions.get(currentIndex);

        if (userAnswer.equalsIgnoreCase(current.getAnswer()))
        {
            score++;
        }

        scoreLabel.setText("Score: " + score);

        currentIndex++;
        showQuestion();
    }

    private void endQuiz()
    {
        questionLabel.setText("Quiz Finished! Final Score: " + score + "/10");

        answerField.setDisable(true);
        submitButton.setDisable(true);
        startButton.setDisable(false);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}

/**
 * Question class
 */
class Question
{
    private final String question;
    private final String answer;

    public Question(String question, String answer)
    {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion()
    {
        return question;
    }

    public String getAnswer()
    {
        return answer;
    }
}