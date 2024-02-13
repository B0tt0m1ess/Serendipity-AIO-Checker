package SerendipityAIOChecker.checker.GUI;

import java.awt.*;

public class Message {
        public static void show(String show) {
            if (SystemTray.isSupported()) {
                switch (show){
                    case "Login" -> loginMessage();
                    case "Microsoft Checker" -> microsoftCheckerRunningMessage();
                    case "Aol VM Checker" -> aolVMCheckerRunningMessage();


                }
            }
        }
        private static void microsoftCheckerRunningMessage(){
            try {
                SystemTray tray = SystemTray.getSystemTray();
                Image image = Toolkit.getDefaultToolkit().createImage(Message.class.getResource("/hexhChecker/checker/Images/My.png"));
                TrayIcon trayIcon = new TrayIcon(image);
                tray.add(trayIcon);
                trayIcon.displayMessage("Hexh Checker Message","Running Microsoft Checker.\nGood luck,"+LoginStage.username+"!!!",TrayIcon.MessageType.NONE);
            } catch (AWTException e) {
                throw new RuntimeException(e);
            }
        }
    private static void aolVMCheckerRunningMessage(){
        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage(Message.class.getResource("/hexhChecker/checker/Images/My.png"));
            TrayIcon trayIcon = new TrayIcon(image);
            tray.add(trayIcon);
            trayIcon.displayMessage("Hexh Checker Message","Running Aol VM Checker.\nGood luck,"+LoginStage.username+"!!!",TrayIcon.MessageType.NONE);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loginMessage(){
            try {
                SystemTray tray = SystemTray.getSystemTray();
                Image image = Toolkit.getDefaultToolkit().createImage(Message.class.getResource("/hexhChecker/checker/Images/My.png"));
                TrayIcon trayIcon = new TrayIcon(image,"Hexh Checker Message");
                trayIcon.setImageAutoSize(true);
                trayIcon.setToolTip("Hexh Checker Message");
                tray.add(trayIcon);
                trayIcon.displayMessage("Dear Hexh Checker User:", "Welcome back," + LoginStage.username
                        + ".\nThank you for supporting my program!\n\t\t\t\tBy Hexh", TrayIcon.MessageType.NONE);
            } catch (AWTException e) {
                throw new RuntimeException(e);
            }

        }
}
