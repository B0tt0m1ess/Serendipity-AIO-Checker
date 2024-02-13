package SerendipityAIOChecker.checker.GUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.OutputStream;
import java.io.PrintStream;

public class ConsoleOutput {
    private static int number = 0;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private TextArea output;
    private static ConsoleOutput consoleOutput;
    @FXML
    void exit(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void clear(ActionEvent event) {
        Platform.runLater(() -> consoleOutput.output.clear());
    }

    @FXML
    void minimize(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void move(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    @FXML
    void onMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConsoleOutput.fxml"));
        Parent root = fxmlLoader.load();
        consoleOutput = fxmlLoader.getController();
        Platform.runLater(() -> consoleOutput.output.clear());
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("HexH Checker Console Output Stage");
        stage.show();
        OutputStream outStream = new TextAreaOutputStream(consoleOutput.output);
        PrintStream printStream = new PrintStream(outStream, true);
        System.setOut(printStream);
        OutputStream errStream = new TextAreaOutputStream(consoleOutput.output);
        PrintStream errPrintStream = new PrintStream(errStream, true);
        System.setErr(errPrintStream);
    }

    public static class TextAreaOutputStream extends OutputStream {
        private final TextArea textArea;

        public TextAreaOutputStream(TextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) {
            Platform.runLater(() -> textArea.appendText(String.valueOf((char) b)));
            number++;
            if (number >= 6000){
                Platform.runLater(textArea::clear);
                number = 0;
            }
        }
    }

}