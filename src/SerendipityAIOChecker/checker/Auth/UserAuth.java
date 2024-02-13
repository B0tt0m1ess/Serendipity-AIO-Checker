package SerendipityAIOChecker.checker.Auth;


import SerendipityAIOChecker.checker.CUI.CUI;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import static java.lang.Thread.sleep;

/**
 * @author qxb
 */
public class UserAuth{
    public static int checkerMembers = 0;
    public static String userLevel = null;
    public static String versionNow= "017";
    private static final String running = "YES";
    public static boolean checkInfo(String userNameInfo, String pass, String h, boolean showMessage) throws IOException, InterruptedException {

        String userInfoNow = userNameInfo + ":" + pass + ":" + h;
        String authInfo = get();
        String[] auth = authInfo.split("===");
        checkerMembers = auth.length - 2;
        if (!auth[1].contains(running)){
            if (showMessage){
                System.out.println(CUI.ANSI_RED + "[Error]" + CUI.ANSI_YELLOW+ "This Program Has Been Closed!");
                sleep(5000);
            }

            return false;
        }else if (!auth[0].contains(versionNow)){
            if (showMessage){
                System.out.println(CUI.ANSI_RED + "[Error]" + CUI.ANSI_YELLOW+ "Have New Version!");
                sleep(5000);
            }

            return false;
        }else if (!authInfo.contains(userInfoNow)){
            if (showMessage){
                System.out.println(CUI.ANSI_PURPLE + "[Error]" + CUI.ANSI_YELLOW + "Your Username,Password Or HWID Is Wrong");
                sleep(5000);
            }

            return false;
        }if (authInfo.contains(userInfoNow)){
            for (String s : auth) {
                if (s.contains(userInfoNow)) {
                    userLevel = s.split(":")[3];
                }
            }
            if (showMessage){
                System.out.println(CUI.ANSI_PURPLE + "[LoginSuccess]" + CUI.ANSI_GREEN + "Welcome Back," + CUI.ANSI_BRIGHT_CYAN + userNameInfo + "!");
                sleep(5000);
            }

            return true;
        }
        return false;
    }
    static String get() throws IOException {
        URL url = new URL("https://pastebin.com/raw/7LPtNq6e");
        URLConnection connection = url.openConnection();
        InputStream is = connection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = br.readLine()) != null){
            builder.append(line);
            builder.append("===");
        }
        br.close();
        isr.close();
        is.close();
        return builder.toString();

    }
}