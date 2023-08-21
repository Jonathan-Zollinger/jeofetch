package com.github.jonathan_zollinger.jeofetch;

import com.sun.management.OperatingSystemMXBean;
import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        Map<String, String> properties = new HashMap<>(Map.of(
                "Hostname", SystemUtils.getHostName(),
                "OS Name", osBean.getName(),
                "OS Version", osBean.getVersion()
        ));
        if (SystemUtils.IS_OS_LINUX) {
            properties.putAll(getLinuxDistroInfo());
        }
        for (String property : properties.keySet()) {
            System.out.printf("%30s = %-20s%n", property, properties.get(property));
        }
    }

    static Map<String, String> getLinuxDistroInfo() throws IOException {
        Path osRelease = Paths.get("/etc/os-release");
        if (!Files.exists(osRelease)) {
            throw new RuntimeException("Bad argument." + osRelease + "does not exist.");
        }
        if (!Files.isRegularFile(osRelease)) {
            throw new RuntimeException(osRelease + " is not a normal file.");

        }
        if (!Files.isReadable(osRelease)) {
            throw new RuntimeException("cannot read " + osRelease);
        }
        Scanner osScanner = new Scanner(osRelease);
        Map<String, String> properties = new HashMap<>();
        while (osScanner.hasNextLine()) {
            String[] lines = Arrays.stream(osScanner.nextLine().split("="))
                    .map(s -> s.replace("\"", ""))
                    .toArray(String[]::new);
            properties.put(lines[0], lines[1]);
        }
        return properties;
    }
}
