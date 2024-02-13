package SerendipityAIOChecker.checker.GUI;

import SerendipityAIOChecker.checker.Auth.GetHWID;
import SerendipityAIOChecker.checker.Auth.UserAuth;
import SerendipityAIOChecker.checker.CUI.CUI;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.*;

public class LoginStage extends Application {
    public static String username;
    public static String password;
    private static String HWID = null;
    @FXML
    private PasswordField pass;
    @FXML
    private TextField user;
    @FXML
    private TextField outHWID;

    @FXML
    private Label logo;
    @FXML
    private Label info;
    LoginStage loginStage;
    private double xOffset = 0;
    private double yOffset = 0;

    private static void readConfig() {
        StringBuilder configInfo = new StringBuilder();
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader("./configs/GlobalConfig.ini"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    configInfo.append(line).append("<>");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                String[] configInfo1 = configInfo.toString().split("<>");
                for (String string : configInfo1) {
                    String[] strings = string.split("\\|");
                    switch (strings[0]) {
                        case "Enable GUI(Y/N)" -> {
                            if (strings[1].equals("Y")) {
                                CUI.enableGUI = true;
                            } else if (!strings[1].equals("N")) {
                                CUI.enableGUI = false;
                            }
                        }
                        case "Info" -> {
                            username = strings[1];
                            password = strings[2];
                        }
                    }
                }
                if (CUI.enableGUI) {
                    CUI.ANSI_RED = "";
                    CUI.ANSI_YELLOW = "";
                    CUI.ANSI_RESET = "";
                    CUI.ANSI_CYAN = "";
                    CUI.ANSI_PURPLE = "";
                    CUI.ANSI_GREEN = "";
                    CUI.ANSI_BRIGHT_CYAN = "";
                    CUI.ANSI_DARK_WHITE = "";
                }
            } catch (NumberFormatException e) {
                System.out.println("[WARNING]" + "Check Your Config File!");
                throw new RuntimeException(e);
            }


        } catch (NumberFormatException e) {
            System.out.println("[WARNING]" + "Check Your Config File!");
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        readConfig();
        if (CUI.enableGUI) {
            Application.launch(args);

        } else {

            CUI.login();
        }


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

    @FXML
    void exit(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    @FXML
    void login(ActionEvent event) throws IOException, InterruptedException {
        if (UserAuth.checkInfo(username, password, HWID, false)) {
            Message.show("Login");
            CUI.dwaosnfo(username, password, HWID);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            Platform.runLater(() -> {
                CheckerGUI secondWindow = new CheckerGUI();
                try {
                    secondWindow.start(new Stage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            System.exit(0);
        }
    }
    @FXML
    void qqGroup() {

    }
    @Override
    public void start(Stage stage) throws Exception {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginStage.fxml"));
        Parent root = loader.load();
        loginStage = loader.getController();
        HWID = GetHWID.getWindowNumber("window");

        Platform.runLater(() -> {
            loginStage.outHWID.setText(HWID);
            loginStage.user.setText(username);
            loginStage.pass.setText(password);
        });
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("HexH Checker Login Stage");
        ConsoleOutput consoleOutput = new ConsoleOutput();
        consoleOutput.start(new Stage());
        stage.show();
    }

    public void initialize() {
        pass.textProperty().addListener((observable, oldValue, newValue) -> password = newValue);
        user.textProperty().addListener((observable, oldValue, newValue) -> username = newValue);
    }
}
