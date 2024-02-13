package SerendipityAIOChecker.checker.GUI;

import SerendipityAIOChecker.checker.Aol.AolEmail;
import SerendipityAIOChecker.checker.Auth.UserAuth;
import SerendipityAIOChecker.checker.Microsoft.Microsoft;
import SerendipityAIOChecker.checker.Microsoft.MicrosoftComboFilter;
import SerendipityAIOChecker.checker.Microsoft.MinecraftHitsEditor;
import SerendipityAIOChecker.checker.Proxy.ProxyTester;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class CheckerGUI{
    private static CheckerGUI checkerGUI;
    private final double originalModulePaneOpacity = 0.9;
    private final double originalCheckerInfoOpacity = 1;
    private final double originalModuleFunctionPanesOpacity = 0.8;
    @FXML
    public Label bads;
    @FXML
    public Label checkerVersion;
    @FXML
    public Label cpm;
    @FXML
    private Label VM_Registered0;

    @FXML
    private Label VM_Registered1;

    @FXML
    private Label VM_UnRegistered0;

    @FXML
    private Label VM_UnRegistered1;

    @FXML
    private Label VM_Unknown0;

    @FXML
    private Label VM_Unknown1;
    @FXML
    public Label hasmc;
    @FXML
    private AnchorPane aolModulePane;

    @FXML
    private AnchorPane aolMoudulePane1;
    @FXML
    public Label hits;
    @FXML
    public Label members;
    @FXML
    public Label process;
    @FXML
    public Label proxypool;
    @FXML
    public Label superhits;
    @FXML
    public Label tfas;
    @FXML
    private AnchorPane microsoftMoudulePane1;
    @FXML
    private AnchorPane microsoftMoudulePane2;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private Button MicrosoftComboFilterbutton;
    @FXML
    private Button MinecraftHitsEditorbutton;
    @FXML
    private AnchorPane info;
    @FXML
    private Label level;
    @FXML
    private AnchorPane microsoftcheckerinfo;
    @FXML
    private AnchorPane microsoftMoudule;
    @FXML
    private AnchorPane aolVMCheckerInfo;
    @FXML
    private Button microsoftcheckerstartbutton;
    @FXML
    private Button minecraftcheckerreturnbutton;

    @FXML
    private Label username;
    @FXML
    private Label proxyerrors;
    @FXML
    private Label aolVMChecker_CPM;

    @FXML
    private Label aolVMChecker_Cancelled;

    @FXML
    private Label aolVMChecker_Process;

    @FXML
    private Label aolVMChecker_ProxyErrors;
    @FXML
    private Button aolVMCheckerButton;

    @FXML
    private Label aolVMChecker_ProxyPool;

    @FXML
    private Label aolVMChecker_Registered;

    @FXML
    private Label aolVMChecker_Unregistered;


    public static void isEnableVMCheckerLabel(boolean isEnable){
        if (isEnable){
            checkerGUI.VM_Registered0.setVisible(true);
            checkerGUI.VM_Registered1.setVisible(true);
            checkerGUI.VM_UnRegistered0.setVisible(true);
            checkerGUI.VM_UnRegistered1.setVisible(true);
            checkerGUI.VM_Unknown0.setVisible(true);
            checkerGUI.VM_Unknown1.setVisible(true);
        }else {
            checkerGUI.VM_Registered0.setVisible(false);
            checkerGUI.VM_Registered1.setVisible(false);
            checkerGUI.VM_UnRegistered0.setVisible(false);
            checkerGUI.VM_UnRegistered1.setVisible(false);
            checkerGUI.VM_Unknown0.setVisible(false);
            checkerGUI.VM_Unknown1.setVisible(false);
        }
    }
    public static void updateMicrosoftVMCheckerInfo() {
            Platform.runLater(() -> {
                checkerGUI.VM_Registered1.setText(String.valueOf(Microsoft.microsoftChecker_vmhits));
                checkerGUI.VM_UnRegistered1.setText(String.valueOf(Microsoft.microsoftChecker_vmbad));
                checkerGUI.VM_Unknown1.setText(String.valueOf(Microsoft.microsoftChecker_vmUnknown));
                checkerGUI.proxypool.setText(Microsoft.microsoftChecker_GoodProxies.size() + "/" + ProxyTester.proxies.size());
                checkerGUI.cpm.setText(String.valueOf(Microsoft.microsoftChecker_cpm));
                checkerGUI.proxyerrors.setText(String.valueOf(Microsoft.microsoftChecker_ProxiesErrors));
                checkerGUI.process.setText(Microsoft.microsoftChecker_process + "/" + Microsoft.microsoftChecker_combo);
            });
    }
    public static void updateMicrosoftCheckerInfo() {
        Platform.runLater(() -> {
            checkerGUI.hits.setText(String.valueOf(Microsoft.microsoftChecker_hits));
            checkerGUI.hasmc.setText(String.valueOf(Microsoft.microsoftChecker_hasMC));
            checkerGUI.superhits.setText(String.valueOf(Microsoft.microsoftChecker_superHits));
            checkerGUI.tfas.setText(String.valueOf(Microsoft.microsoftChecker_tfa));
            checkerGUI.bads.setText(String.valueOf(Microsoft.microsoftChecker_bad));
            checkerGUI.proxypool.setText(Microsoft.microsoftChecker_GoodProxies.size() + "/" + ProxyTester.proxies.size());
            checkerGUI.cpm.setText(String.valueOf(Microsoft.microsoftChecker_cpm));
            checkerGUI.proxyerrors.setText(String.valueOf(Microsoft.microsoftChecker_ProxiesErrors));
            checkerGUI.process.setText(Microsoft.microsoftChecker_process + "/" + Microsoft.microsoftChecker_combo);
        });
    }
    public static void updateAolVMCheckerInfo() {
        Platform.runLater(() -> {
            checkerGUI.aolVMChecker_Registered.setText(String.valueOf(AolEmail.aol_VmRegistered));
            checkerGUI.aolVMChecker_Unregistered.setText(String.valueOf(AolEmail.aol_VmUnregistered));
            checkerGUI.aolVMChecker_Cancelled.setText(String.valueOf(AolEmail.aol_VmCancelled));
            checkerGUI.aolVMChecker_CPM.setText(String.valueOf(AolEmail.aol_CPM));
            checkerGUI.aolVMChecker_ProxyPool.setText(AolEmail.aol_GoodProxies.size() + "/" + ProxyTester.proxies.size());
            checkerGUI.aolVMChecker_Process.setText(AolEmail.aol_Process+ "/" + AolEmail.aol_CombosNumber);
            checkerGUI.aolVMChecker_ProxyErrors.setText(String.valueOf(AolEmail.aol_ProxyErrors));
        });
    }
    @FXML
    void aolModule(ActionEvent event) {
        if (aolModulePane.getLayoutX() == 63){
            aolModulePane.setLayoutX(aolModulePane.getLayoutX() - 922);
        }
        if (info.isVisible()){
            info.setVisible(false);
        }else if (microsoftMoudule.isVisible()){
            microsoftMoudule.setVisible(false);
        }
        aolModulePane.setOpacity(originalModulePaneOpacity);
        aolModulePane.setVisible(true);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.2), aolModulePane);
        transition.setFromX(0);
        transition.setToX(922);
        transition.play();
    }

    @FXML
    void MicrosoftComboFilter(ActionEvent event) {
        MicrosoftComboFilterbutton.setDisable(true);
        Thread taskThread = new Thread(() -> {

            MicrosoftComboFilter.Start();
            Platform.runLater(() -> MicrosoftComboFilterbutton.setDisable(false));
        });
        taskThread.start();

    }

    @FXML
    void MinecraftHitsEditor(ActionEvent event) {
        MinecraftHitsEditorbutton.setDisable(true); // Disable the button during task execution
        Thread taskThread = new Thread(() -> {

            MinecraftHitsEditor.starting();
            Platform.runLater(() -> MinecraftHitsEditorbutton.setDisable(false));
        });
        taskThread.start();
    }

    @FXML
    void noPane(ActionEvent event) {
        if (info.isVisible()) {
            info.setOpacity(originalModulePaneOpacity);
            Timeline fadeInTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(0.2),
                            new KeyValue(info.opacityProperty(), 0.0))
            );
            fadeInTimeline.setOnFinished(e -> info.setVisible(false));
            fadeInTimeline.play();
        } else if (microsoftMoudule.isVisible()) {
            microsoftMoudule.setOpacity(originalModulePaneOpacity);
            Timeline fadeInTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(0.2),
                            new KeyValue(microsoftMoudule.opacityProperty(), 0.0))
            );
            fadeInTimeline.setOnFinished(e -> microsoftMoudule.setVisible(false));
            fadeInTimeline.play();
        }else if (aolModulePane.isVisible()){
            aolModulePane.setOpacity(originalModulePaneOpacity);
            Timeline fadeInTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(0.2),
                            new KeyValue(aolModulePane.opacityProperty(), 0.0))
            );
            fadeInTimeline.setOnFinished(e -> aolModulePane.setVisible(false));
            fadeInTimeline.play();
        }

    }

    @FXML
    void microsoftchecker(ActionEvent event) {
        microsoftMoudulePane1.setOpacity(originalModuleFunctionPanesOpacity);
        microsoftMoudulePane2.setOpacity(originalModuleFunctionPanesOpacity);
        Timeline fadeInTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.2),
                        new KeyValue(microsoftMoudulePane1.opacityProperty(), 0.0))
        );
        Timeline fadeInTimeline2 = new Timeline(
                new KeyFrame(Duration.seconds(0.2),
                        new KeyValue(microsoftMoudulePane2.opacityProperty(), 0.0))
        );
        fadeInTimeline2.setOnFinished(e -> {
            microsoftMoudulePane1.setVisible(false);
            microsoftMoudulePane2.setVisible(false);
            microsoftcheckerinfo.setOpacity(0.0);
            microsoftcheckerinfo.setVisible(true);
            Timeline fadeInTimeline1 = new Timeline(
                    new KeyFrame(Duration.seconds(0.2),
                            new KeyValue(microsoftcheckerinfo.opacityProperty(), originalCheckerInfoOpacity))
            );
            fadeInTimeline1.setOnFinished(e1 -> {
                microsoftcheckerstartbutton.setVisible(true);
                minecraftcheckerreturnbutton.setVisible(true);

            });
            fadeInTimeline1.play();
        });
        fadeInTimeline.play();
        fadeInTimeline2.play();
    }
    @FXML
    void aolVMChecker(ActionEvent event){
        aolMoudulePane1.setOpacity(originalModuleFunctionPanesOpacity);
        Timeline fadeInTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.2),
                        new KeyValue(aolMoudulePane1.opacityProperty(), 0.0))
        );
        fadeInTimeline.setOnFinished(event1 -> {
            aolMoudulePane1.setVisible(false);
            aolVMCheckerInfo.setVisible(true);
            aolVMCheckerInfo.setOpacity(originalCheckerInfoOpacity);
        });
        fadeInTimeline.play();

    }
    @FXML
    void aolVMcheckerreturn(ActionEvent event) {
        aolVMCheckerInfo.setOpacity(originalCheckerInfoOpacity);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.2),
                    new KeyValue(aolVMCheckerInfo.opacityProperty(),0.0)
        ));
        timeline.setOnFinished(event1 -> {
            aolVMCheckerInfo.setVisible(false);
            aolMoudulePane1.setVisible(true);
            aolMoudulePane1.setOpacity(originalModulePaneOpacity);
        });
        timeline.play();
    }


    @FXML
    void aolVMcheckerstart(ActionEvent event) {
        updateAolVMCheckerInfo();
        aolVMCheckerButton.setDisable(true); // Disable the button during task execution
        Thread taskThread = new Thread(() -> {
            try {
                AolEmail.startChecking();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(() -> aolVMCheckerButton.setDisable(false));
        });
        taskThread.start();
    }

    @FXML
    void microsoftcheckerstart(ActionEvent event) {
        updateMicrosoftCheckerInfo();
        microsoftcheckerstartbutton.setDisable(true); // Disable the button during task execution
        Thread taskThread = new Thread(() -> {
            try {
                Microsoft.start();
                Platform.runLater(() -> microsoftcheckerstartbutton.setDisable(false));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        taskThread.start();
    }

    @FXML
    void minecraftcheckerreturn(ActionEvent event) {
        microsoftcheckerstartbutton.setVisible(false);
        minecraftcheckerreturnbutton.setVisible(false);
        microsoftcheckerinfo.setOpacity(originalCheckerInfoOpacity);
        Timeline fadeInTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.2),
                        new KeyValue(microsoftcheckerinfo.opacityProperty(), 0.0))
        );
        fadeInTimeline.setOnFinished(e -> {
            microsoftcheckerinfo.setVisible(false);
            microsoftMoudulePane1.setOpacity(0.0);
            microsoftMoudulePane2.setOpacity(0.0);
            microsoftMoudulePane1.setVisible(true);
            microsoftMoudulePane2.setVisible(true);
            Timeline fadeInTimeline1 = new Timeline(
                    new KeyFrame(Duration.seconds(0.2),
                            new KeyValue(microsoftMoudulePane1.opacityProperty(), originalModuleFunctionPanesOpacity))
            );
            Timeline fadeInTimeline2 = new Timeline(
                    new KeyFrame(Duration.seconds(0.2),
                            new KeyValue(microsoftMoudulePane2.opacityProperty(), originalModuleFunctionPanesOpacity))
            );
            fadeInTimeline1.play();
            fadeInTimeline2.play();
        });
        fadeInTimeline.play();
    }

    @FXML
    void microsoftmodulebutton(ActionEvent event) {
        if (microsoftMoudule.getLayoutX() == 63){
            microsoftMoudule.setLayoutX(microsoftMoudule.getLayoutX() - 922);
        }

        if (info.isVisible()){
            info.setVisible(false);
        }else if (aolModulePane.isVisible()){
            aolModulePane.setVisible(false);
        }
        microsoftMoudule.setOpacity(originalModulePaneOpacity);
        microsoftMoudule.setVisible(true);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.2), microsoftMoudule);
        transition.setFromX(0);
        transition.setToX(922);
        transition.play();
    }

    @FXML
    void minimize(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }


    @FXML
    void infobutton(ActionEvent event) {
        if (info.getLayoutX() == 63){
            info.setLayoutX(info.getLayoutX() - 922);
        }

        if (microsoftMoudule.isVisible()){
            microsoftMoudule.setVisible(false);
        }else if (aolModulePane.isVisible()){
            aolModulePane.setVisible(false);
        }
        info.setOpacity(originalModulePaneOpacity);
        info.setVisible(true);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.2), info);
        transition.setFromX(0);
        transition.setToX(922);
        transition.play();
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
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CheckerGUI.fxml"));
        Parent root = fxmlLoader.load();
        checkerGUI = fxmlLoader.getController();
        Platform.runLater(() -> {
            checkerGUI.members.setText(String.valueOf(UserAuth.checkerMembers));
            checkerGUI.username.setText(LoginStage.username);
            checkerGUI.level.setText(UserAuth.userLevel);
            checkerGUI.checkerVersion.setText("Beta V." + UserAuth.versionNow);
        });
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("HexH Checker Stage");
        stage.show();
        checkerGUI.info.setOpacity(0.0);
        checkerGUI.info.setVisible(true);
        Timeline fadeInTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1.5),
                        new KeyValue(checkerGUI.info.opacityProperty(), originalModulePaneOpacity))
        );
        fadeInTimeline.play();
    }
}
