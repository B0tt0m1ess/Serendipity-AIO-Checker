package SerendipityAIOChecker.checker.Auth;


import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
public class GetHWID {
    public static String getHardDiskSerialNumber() {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"wmic", "csproduct", "get","uuid"});
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            String shab = sc.next();
            return sc.next();
        } catch (IOException e) {
            throw new RuntimeException("getHardDiskSerialNumber Fail!");
        }
    }
    public static String getCpuSerialNumber(){
        try {
            Process process = Runtime.getRuntime().exec(new String[] { "wmic", "cpu", "get", "ProcessorId" });
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            String shab = sc.next();
            return sc.next();
        } catch (IOException e) {
            throw new RuntimeException("getCpuSerialNumber Fail!");
        }
    }
    public static String getWindowNumber(String type){
        if (Objects.isNull(type)){
            return "";
        }
        String hwids = "";
        if ("window".equals(type)){
            String processorId = getCpuSerialNumber();
            String serialNumber = getHardDiskSerialNumber();
            String uuid = serialNumber.replace('-', 'S');
            String forHwid = processorId + uuid;
            hwids = DigestUtils.md5Hex(forHwid);
        }
        return hwids;
    }
}