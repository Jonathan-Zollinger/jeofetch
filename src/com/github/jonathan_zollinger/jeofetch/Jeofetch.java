package com.github.jonathan_zollinger.jeofetch;

import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.platform.windows.WindowsHardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import picocli.CommandLine;

import java.util.HashMap;
import java.util.Map;

@CommandLine.Command(name = "jeofetch",
        mixinStandardHelpOptions = true,
        version = "jeofetch v0.0.0-SNAPSHOT",
        description="jeofetch is a system information tool written in java",
        footer = "written by Jonathan Zollinger")
public class Jeofetch implements Runnable{
    static HardwareAbstractionLayer HARDWARE;
    static OperatingSystem OS;

    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new Jeofetch());
        int exitCode = commandLine.execute(args);
        System.exit(exitCode);
        // the bloody transitive property from oshi needs this property assignment
        System.setProperty("org.slf4j.LoggerFactory", "org.apache.logging.log4j.simple.SimpleLoggerContextFactory");
    }

    @Override
    public void run() {
        HARDWARE = new SystemInfo().getHardware();
        OS = new SystemInfo().getOperatingSystem();
        Map<String, String> properties = getHardwareProperties();
        properties.putAll(getOsProperties());
    }

    private static Map<String, String> getOsProperties() {
        return new HashMap<>();
    }

    private static Map<String, String> getHardwareProperties() {
        Map<String, String> properties = new HashMap<>();
        properties.put("cpu:", HARDWARE.getProcessor().getProcessorIdentifier().getName());
        properties.put("gpu:", String.join(", ", HARDWARE.getGraphicsCards()
                .stream()
                .map(GraphicsCard::getName)
                .toArray(String[]::new)));
        properties.put("ram:", bytesToReadableSize(((WindowsHardwareAbstractionLayer) HARDWARE)
                .createMemory()
                .getTotal()));
        return properties;
    }

    public static String bytesToReadableSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int z = (63 - Long.numberOfLeadingZeros(bytes)) / 10;
        return String.format("%.1f %sB", (double)bytes / (1L << (z * 10)), " KMGTPE".charAt(z));
    }
}
