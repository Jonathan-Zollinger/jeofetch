package com.github.jonathan_zollinger.jeofetch;

import org.apache.commons.lang3.SystemUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Map<String, String[]> properties = new HashMap<>();
        if (SystemUtils.IS_OS_LINUX) {
            properties.putAll(getLinuxDistroInfo());
        } else if (SystemUtils.IS_OS_WINDOWS) {
            properties.putAll(getWindowsInfo("systeminfo"));
        }
        for (String property : properties.keySet()) {
            System.out.printf("%30s = %-20s%n", property, String.join("\n\t", properties.get(property)));
        }
    }

    static Map<String, String[]> getLinuxDistroInfo() throws IOException {
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
        Map<String, String[]> properties = new HashMap<>();
        while (osScanner.hasNextLine()) {
            String[] lines = Arrays.stream(osScanner.nextLine().split("="))
                    .map(s -> s.replace("\"", ""))
                    .toArray(String[]::new);
            properties.put(lines[0], new String[]{lines[1]});
        }
        return properties;
    }

    static Map<String, String[]> getWindowsInfo(String... command) {
        Map<String, String[]> windowsProperties = new HashMap<>();
        for (String thisCommand : command) {

            Process process;
            try {
                process = Runtime.getRuntime().exec("cmd /c " + thisCommand);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String lastKey = "";
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.isEmpty()) {
                        continue;
                    }
                    System.out.println(line);
                    String[] lines = Arrays.stream(line.split(":",2)).toArray(String[]::new);
                    if (lines.length != 1 && !lines[0].startsWith(" ")) {
                        windowsProperties.put(lines[0].trim(), new String[]{lines[1].trim()});
                        lastKey = lines[0].trim();
                        continue;
                    }
                    windowsProperties.put(lastKey, Arrays
                            .stream(new String[][]{windowsProperties
                                    .get(lastKey), new String[]{lines[0]
                                    .trim()}})
                            .flatMap(Arrays::stream)
                            .toArray(String[]::new));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return windowsProperties;
    }
}
