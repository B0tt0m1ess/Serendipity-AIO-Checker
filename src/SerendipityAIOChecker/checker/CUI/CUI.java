package SerendipityAIOChecker.checker.CUI;


import SerendipityAIOChecker.checker.Aol.AolEmail;
import SerendipityAIOChecker.checker.Auth.GetHWID;
import SerendipityAIOChecker.checker.Auth.UserAuth;
import SerendipityAIOChecker.checker.GUI.LoginStage;
import SerendipityAIOChecker.checker.GUI.Message;
import SerendipityAIOChecker.checker.Microsoft.Microsoft;
import SerendipityAIOChecker.checker.Microsoft.MicrosoftComboFilter;
import SerendipityAIOChecker.checker.Microsoft.MinecraftHitsEditor;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class CUI {
    public static  String title = "HexH Checker";
    public static  String ANSI_RESET = "\u001B[0m";
    public static  String ANSI_RED = "\u001B[31m";
    public static  String ANSI_GREEN = "\u001B[32m";
    public static  String ANSI_YELLOW = "\u001B[33m";
    public static  String ANSI_PURPLE = "\u001B[35m";
    public static  String ANSI_CYAN = "\u001B[36m";
    public static  String ANSI_BRIGHT_CYAN = "\u001B[96m";

    public static  String ANSI_DARK_WHITE = "\u001B[37m";
    public static boolean enableGUI = false;


    public static void login() throws IOException, InterruptedException {
        logo();
        System.out.println(ANSI_PURPLE + "[HWID]" + ANSI_RED + GetHWID.getWindowNumber("window"));
        while (UserAuth.checkInfo(LoginStage.username, LoginStage.password, GetHWID.getWindowNumber("window"),true)){
            Message.show("Login");
            dwaosnfo(LoginStage.username, LoginStage.password, GetHWID.getWindowNumber("window"));
            Start();
        }
    }
    public static void dwaosnfo(String userNameInfo, String pass, String h){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        // 创建一个Runnable任务，表示您要执行的操作
        Runnable task = () -> {
            try {
                if (!UserAuth.checkInfo(userNameInfo,pass,h,false)){
                    System.exit(0);
                }
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        };
        scheduler.scheduleAtFixedRate(task, 0, 120, TimeUnit.SECONDS);
    }
    public static void Start() throws InterruptedException, IOException {
        logo();
        System.out.println(ANSI_PURPLE + "[Choose]");
        System.out.println(ANSI_PURPLE + "[1]" + ANSI_YELLOW + "Microsoft Module");
        System.out.println(ANSI_PURPLE + "[2]" + ANSI_YELLOW + "Aol Module");
        System.out.println(ANSI_PURPLE + "[0]" + ANSI_YELLOW + "Exit");
        Scanner ss = new Scanner(System.in);
        System.out.print(ANSI_PURPLE + "[Your Choose]" + ANSI_YELLOW);
        if (ss.hasNext()){
            int input = Integer.parseInt(ss.next());
            if (input == 1){
                logo();
                System.out.println(ANSI_PURPLE + "[Choose]");
                System.out.println(ANSI_PURPLE + "[1]" + ANSI_YELLOW + "Microsoft Checker(With Check Minecraft)");
                System.out.println(ANSI_PURPLE + "[2]" + ANSI_YELLOW + "Minecraft Hits Editor");
                System.out.println(ANSI_PURPLE + "[3]" + ANSI_YELLOW + "Combo Filter");
                System.out.println(ANSI_PURPLE + "[0]" + ANSI_YELLOW + "Return");
                Scanner sss = new Scanner(System.in);
                System.out.print(ANSI_PURPLE + "[Your Choose]" + ANSI_YELLOW);
                if (sss.hasNext()){
                    int input1 = Integer.parseInt(sss.next());
                    if (input1 == 1){
                        Microsoft.start();
                        sleep(5000);
                        logo();
                        System.out.println(ANSI_PURPLE + "[Logs]" + ANSI_YELLOW + "Complete the work, thank you for your use！" + ANSI_RESET);
                        sleep(3000);
                        Start();
                    }else if (input1 == 2){
                        logo();
                        MinecraftHitsEditor.starting();
                        sleep(5000);
                        logo();
                        System.out.println(ANSI_PURPLE + "[Logs]" + ANSI_YELLOW + "Complete the work, thank you for your use！" + ANSI_RESET);
                        sleep(3000);
                        Start();
                    }else if (input1 == 3){
                        logo();
                        MicrosoftComboFilter.Start();
                        sleep(5000);
                        logo();
                        System.out.println(ANSI_PURPLE + "[Logs]" + ANSI_YELLOW + "Complete the work, thank you for your use！" + ANSI_RESET);
                        sleep(3000);
                        Start();
                    }
                    else if (input1 == 0){
                        Start();
                    } else {
                        System.out.println(ANSI_RED + "Please enter a legal number!");
                        sleep(2000);
                        Start();
                    }
                } else {
                    System.out.println(ANSI_RED + "Please enter a number!");
                    sleep(2000);
                    Start();
                }
            }else if (input == 2){
                logo();
                System.out.println(ANSI_PURPLE + "[Choose]");
                System.out.println(ANSI_PURPLE + "[1]" + ANSI_YELLOW + "Aol Email VM Checker");
                System.out.println(ANSI_PURPLE + "[0]" + ANSI_YELLOW + "Return");
                Scanner sss = new Scanner(System.in);
                System.out.print(ANSI_PURPLE + "[Your Choose]" + ANSI_YELLOW);
                if (sss.hasNext()){
                    int input1 = sss.nextInt();
                    if (input1 == 1){
                        logo();
                        AolEmail.startChecking();
                        sleep(5000);
                        logo();
                        System.out.println(ANSI_PURPLE + "[Logs]" + ANSI_YELLOW + "Complete the work, thank you for your use！" + ANSI_RESET);
                        sleep(3000);
                        Start();
                    }else if (input1 == 0){
                        Start();
                    }else {
                        System.out.println(ANSI_RED + "Please enter a legal number!");
                        sleep(2000);
                        Start();
                    }
                }
            } else if (input == 0){
                System.exit(0);
            }else {
                System.out.println(ANSI_RED + "Please enter a legal number!");
                sleep(2000);
                Start();
            }
        }else {
            System.out.println(ANSI_RED + "Please enter a number!");
            sleep(2000);
            Start();
        }
        System.exit(0);
    }
    public static void logo(){
        clean();
        System.out.println(ANSI_CYAN + """
    ██╗  ██╗███████╗██╗  ██╗██╗  ██╗
    ██║  ██║██╔════╝╚██╗██╔╝██║  ██║
    ███████║█████╗   ╚███╔╝ ███████║
    ██╔══██║██╔══╝   ██╔██╗ ██╔══██║
    ██║  ██║███████╗██╔╝ ██╗██║  ██║
    ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝
""");
        System.out.println(ANSI_PURPLE + "[Logs]" + ANSI_RED + "Now Time "+ ANSI_YELLOW + time());
        System.out.println(ANSI_PURPLE + "[!!!Coder!!!]" +ANSI_YELLOW + "By HexH QQ:2713845404 && Telegram: @qxb233");
        System.out.println(ANSI_PURPLE + "[Version]" +ANSI_YELLOW + "Beta V.?????? UD.230917");
    }
    public static String time(){
        Date currentTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return simpleDateFormat.format(currentTime);
    }
    public static void clean() {
        try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                new ProcessBuilder("cmd", "/c", "title "+title).inheritIO().start().waitFor();
            } else {
                System.out.println("\033c");
            }
        } catch (Exception ignored) {
        }
    }
}
