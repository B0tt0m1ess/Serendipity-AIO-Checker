package SerendipityAIOChecker.checker.Proxy;

import SerendipityAIOChecker.checker.Aol.AolEmail;
import SerendipityAIOChecker.checker.CUI.CUI;
import SerendipityAIOChecker.checker.GUI.CheckerGUI;
import SerendipityAIOChecker.checker.Microsoft.Microsoft;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxyTester {

    public static List<String> proxies = new ArrayList<>();
    public static volatile boolean updatedProxies = true;
    private static int retryTimes = 1;

    public static List<String> parseProxiesFromSourceCode(String sourceCode) {
        return Arrays.asList(sourceCode.split("\n"));
    }

    public static List<String> readHttpProxiesFromFile() {
        List<String> proxies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("./files/Http_Proxies.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                proxies.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return proxies;
    }

    public static List<String> readSocksProxiesFromFile() {
        List<String> proxies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("./files/Socks_Proxies.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                proxies.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return proxies;
    }

    public static String extractStringBetween(String input, String startMarker, String endMarker) {
        String pattern = Pattern.quote(startMarker) + "(.*?)" + Pattern.quote(endMarker);
        Matcher matcher = Pattern.compile(pattern).matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getWebPageSourceCode(String url) throws IOException {
        int maxRetry = 2;
        int retry = 0;
        String sourceCode = "";

        while (retry < maxRetry) {
            try {
                Connection connection = Jsoup.connect(url).timeout(8000);
                Connection.Response response = connection.execute();
                sourceCode = response.body();
                break;
            } catch (org.jsoup.UncheckedIOException | IOException | UncheckedIOException e) {
                retry++;
            } catch (NoSuchElementException e) {
                break;
            }
        }
        return sourceCode;
    }

    public static void configureProxies(List<String> proxies2, Proxy.Type proxyType) throws InterruptedException {
        for (String proxy1 : proxies2) {
            if (proxy1 != null) {
                String proxy2 = proxy1 + ":" + proxyType;
                proxies.add(proxy2);
            }
        }
        updatedProxies = true;
        if (Microsoft.showUpdateProxiesMessages) {
            System.out.println(CUI.ANSI_PURPLE + "[Logs]" + CUI.ANSI_YELLOW + "Proxies Refreshed, Get " + proxies.size() + " Proxies!");
        }
    }
    public static String microsoftCheckerGetProxyAndCheckProxy(List<String> proxies){
        while (true){
            String status = microsoftCheckerGetProxyAndCheckProxyMethod(proxies);
            if (status != null && !status.equals("no")){
                return status;
            }
        }
    }

    public static void microsoftCheckerGetProxies(){
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        Runnable runnable = ProxyTester::MicrosoftModule_UpdateProxiesAPIs;
        scheduledExecutorService.scheduleAtFixedRate(runnable,0,Microsoft.proxiesRefreshTime, TimeUnit.SECONDS);
    }
    public static String microsoftCheckerGetProxyAndCheckProxyMethod(List<String> proxies) {
        if (!CUI.enableGUI) {
            Microsoft.updateConsoleTitle();
        } else {
            CheckerGUI.updateMicrosoftCheckerInfo();
        }
        if (Microsoft.microsoftChecker_GoodProxies.size() >= Microsoft.minGoodProxiesPool) {
            return MicrosoftModule_GetGoodProxy(Microsoft.microsoftChecker_GoodProxies);
        }
        String proxy = null;
        String[] proxy2;
        Proxy proxy3;
        try {
            proxy = MicrosoftModule_GetRandomProxy(proxies);
            if (proxy == null) {
                proxies.remove(null);
                return "no";
            }
            proxy2 = proxy.split(":");
            if (proxy2.length == 3) {
                try {
                    proxy3 = new Proxy(Proxy.Type.valueOf(proxy2[2]), new InetSocketAddress(proxy2[0], Integer.parseInt(proxy2[1])));
                } catch (IllegalArgumentException e) {
                    if (!proxies.isEmpty()) {
                        proxies.remove(proxy);
                    }
                    return "no";
                }

            } else {
                return "no";
            }
            Connection connection = Jsoup.connect("https://login.live.com/ppsecure/post.srf?contextid=004EDF3AC522A4D3&opid=9E210C77752BBE6B&bk=1690686907&uaid=f615a4d9973c49519f705fcd7bf92faf&pid=0")
                    .header("Cookie", "uaid=f615a4d9973c49519f705fcd7bf92faf; OParams=11O.DcjhOXYjxWmPmSk!uejxLEAMRVGLhTKpA4mjk0O*Oii*YRDHW6WcemZp3IR6ezi1jOG04JZmbnFU9z4Pw0*7KHqqQpyWc0DkkWJE3P8HujTMrRX74n1uPLhWiXPHXNNEOg$$; MSPOK=$uuid-52b04507-6e34-4ce3-b349-b5c4783ceab5$uuid-de9592d5-ac90-4e34-a92a-1cf8cee2a5f8$uuid-3fcbbfc1-0ea9-42b9-b7f7-1cc942cb4d54;")
                    .method(Connection.Method.POST)
                    .timeout(Microsoft.microsoftCheckerTimeout)
                    .requestBody("i13=0&login=hexhchecker&loginfmt=hexhchecker&type=11&LoginOptions=3&lrt=&lrtPartition=&hisRegion=&hisScaleUnit=&passwd=hexhchecker&ps=2&psRNGCDefaultType=&psRNGCEntropy=&psRNGCSLK=&canary=&ctx=&hpgrequestid=&PPFT=-DbJQwq7eLWvNK*aDx!43ZyNG6hVn7HCLR5FaK4Bd6R7oVkBdR9y11ip1S0S0YuwnRYlSelERQVR0P14y0RlFFK5bDiJpbgZ5ipb4xg11dCYzIh*YpRksoCrAZN*IM4Srio5sDAf9nH8WysmQc1zQB7G2hSGIe5y2K4trr6UQ7QCAyGTLIrS642vaoXtNDsDvzT!zKmrl2yri!17SyskAZpk$&PPSX=Passpor&NewUser=1&FoundMSAs=&fspost=0&i21=0&CookieDisclosure=0&IsFidoSupported=0&i2=1&i17=0&i18=&i19=32099")
                    .proxy(proxy3);
            Connection.Response response = connection.execute();
            if (response.body().contains("AR:{\"Username\"")) {
                if (!Microsoft.microsoftChecker_GoodProxies.contains(proxy)){
                    Microsoft.microsoftChecker_GoodProxies.add(proxy);
                }
                return proxy;
            } else {
                Microsoft.microsoftChecker_ProxiesErrors++;
                if (!proxies.isEmpty()) {
                    proxies.remove(proxy);
                }
                return "no";
            }
        } catch (HttpStatusException e) {
            if (e.getStatusCode() != 500 & e.getStatusCode() != 404) {
                Microsoft.microsoftChecker_ProxiesErrors++;
                if (!proxies.isEmpty()) {
                    proxies.remove(proxy);
                }
                return "no";
            }

        } catch (IOException e) {
            if (e instanceof SocketTimeoutException) {
                Microsoft.microsoftChecker_ProxiesErrors++;
                if (!proxies.isEmpty()) {
                    proxies.remove(proxy);
                }
                return "no";
            } else if (e instanceof java.net.SocketException) {
                Microsoft.microsoftChecker_ProxiesErrors++;
                if (!proxies.isEmpty()) {
                    proxies.remove(proxy);
                }
                return "no";
            } else {
                Microsoft.microsoftChecker_ProxiesErrors++;
                if (!proxies.isEmpty()) {
                    proxies.remove(proxy);
                }
                return "no";
            }
        }catch (NoSuchElementException e){
            return "no";
        }
        return "no";
    }

    public static String MicrosoftModule_GetGoodProxy(List<String> proxies) {
        Microsoft.lock.lock();
        try {
            Random random = new Random();
            try {
                int index = random.nextInt(proxies.size());
                if (index <= proxies.size()) {
                    updatedProxies = true;
                    return proxies.get(index);
                } else {
                    return MicrosoftModule_GetGoodProxy(proxies);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } finally {
            Microsoft.lock.unlock();
        }

    }
    public static String AolModule_GetGoodProxy(List<String> proxies) {
        AolEmail.lock.lock();
        try {
            Random random = new Random();
            try {
                int index = random.nextInt(proxies.size());
                if (index <= proxies.size()) {
                    updatedProxies = true;
                    return proxies.get(index);
                } else {
                    return AolModule_GetGoodProxy(proxies);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } finally {
            AolEmail.lock.unlock();
        }

    }

    public static Set<String> readHttpProxyAPIs() {
        String path = "./files/Http_ProxiesAPIs.txt";
        return getApis(path);
    }

    public static Set<String> readSocksProxyAPIs() {
        String path = "./files/Socks_ProxiesAPIs.txt";
        return getApis(path);
    }

    private static Set<String> getApis(String path) {
        Set<String> apis = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                apis.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apis;
    }

    public static String MicrosoftModule_GetRandomProxy(List<String> proxies) {
        Random random = new Random();
        if (Microsoft.enableProxiesChecker){
            Microsoft.lock.lock();
            try {
                if (proxies.size() <= Microsoft.minProxiesPool) {
                    if (Microsoft.showUpdateProxiesMessages) {
                        System.out.println(CUI.ANSI_DARK_WHITE + "[Proxy]" + CUI.ANSI_YELLOW + "Updating Your Proxies,Because Your Proxy That Can Be Used Is 0");
                    }
                    if (Microsoft.enableAPIProxy & retryTimes < Microsoft.apiProxiesRetryTimes) {
                        proxies.addAll(Microsoft.proxies1);
                        retryTimes++;
                    } else if (Microsoft.enableAPIProxy & retryTimes >= Microsoft.apiProxiesRetryTimes) {
                        retryTimes = 1;
                        MicrosoftModule_UpdateProxiesAPIs();
                    } else {
                        try {
                            updatedProxies = false;
                            List<String> proxy = ProxyTester.readHttpProxiesFromFile();
                            ProxyTester.configureProxies(proxy, Proxy.Type.HTTP);
                            List<String> proxy1 = ProxyTester.readSocksProxiesFromFile();
                            ProxyTester.configureProxies(proxy1, Proxy.Type.SOCKS);
                            Microsoft.proxies1.clear();
                            Microsoft.proxies1.addAll(proxies);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } finally {
                Microsoft.lock.unlock();
            }
        }
        try {
            int index = random.nextInt(proxies.size());
            if (index <= proxies.size()) {
                updatedProxies = true;
                return proxies.get(index);
            } else {
                return MicrosoftModule_GetRandomProxy(proxies);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public static String AolModule_GetRandomProxy(List<String> proxies) {
        Random random = new Random();
        AolEmail.lock.lock();
        try {
            if (proxies.size() <= AolEmail.minProxiesPool) {
                if (AolEmail.showProxiesUpdateMessages) {
                    System.out.println(CUI.ANSI_DARK_WHITE + "[Proxy]" + CUI.ANSI_YELLOW + "Updating Your Proxies,Because Your Proxy That Can Be Used Is 0");
                }
                if (AolEmail.aol_EnableUseProxiesAPIs & retryTimes < AolEmail.apiProxiesRetryTimes) {
                    proxies.addAll(AolEmail.proxies);
                    retryTimes++;
                } else if (AolEmail.aol_EnableUseProxiesAPIs & retryTimes >= AolEmail.apiProxiesRetryTimes) {
                    retryTimes = 1;
                    AolModule_UpdateProxiesAPIs();
                } else {
                    try {
                        updatedProxies = false;
                        List<String> proxy = ProxyTester.readHttpProxiesFromFile();
                        ProxyTester.configureProxies(proxy, Proxy.Type.HTTP);
                        List<String> proxy1 = ProxyTester.readSocksProxiesFromFile();
                        ProxyTester.configureProxies(proxy1, Proxy.Type.SOCKS);
                        AolEmail.proxies.clear();
                        AolEmail.proxies.addAll(proxies);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } finally {
            AolEmail.lock.unlock();
        }

        try {
            int index = random.nextInt(proxies.size());
            if (index <= proxies.size()) {
                updatedProxies = true;
                return proxies.get(index);
            } else {
                return AolModule_GetRandomProxy(proxies);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public static void AolModule_UpdateProxiesAPIs() {
        updatedProxies = false;
        Set<String> apis = ProxyTester.readHttpProxyAPIs();
        for (String api : apis) {
            try {
                List<String> proxy = ProxyTester.parseProxiesFromSourceCode(ProxyTester.getWebPageSourceCode(api));
                ProxyTester.configureProxies(proxy, Proxy.Type.HTTP);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        apis = ProxyTester.readSocksProxyAPIs();
        for (String api : apis) {
            try {
                List<String> proxy = ProxyTester.parseProxiesFromSourceCode(ProxyTester.getWebPageSourceCode(api));
                ProxyTester.configureProxies(proxy, Proxy.Type.SOCKS);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        AolEmail.proxies.clear();
        AolEmail.proxies.addAll(proxies);
    }
    public static void MicrosoftModule_UpdateProxiesAPIs() {
        if (!Microsoft.enableProxiesChecker){
            proxies.clear();
        }
        updatedProxies = false;
        Set<String> apis = ProxyTester.readHttpProxyAPIs();
        for (String api : apis) {
            try {
                List<String> proxy = ProxyTester.parseProxiesFromSourceCode(ProxyTester.getWebPageSourceCode(api));
                ProxyTester.configureProxies(proxy, Proxy.Type.HTTP);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        apis = ProxyTester.readSocksProxyAPIs();
        for (String api : apis) {
            try {
                List<String> proxy = ProxyTester.parseProxiesFromSourceCode(ProxyTester.getWebPageSourceCode(api));
                ProxyTester.configureProxies(proxy, Proxy.Type.SOCKS);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Microsoft.proxies1.clear();
        Microsoft.proxies1.addAll(proxies);
    }
}
