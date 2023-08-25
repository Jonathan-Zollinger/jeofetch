package com.github.jonathan_zollinger.jeofetch;

import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

@CommandLine.Command(name = "jeofetch",
        mixinStandardHelpOptions = true,
        version = "jeofetch v0.0.1",
        description="jeofetch is a system information tool written in java",
        footer = "written by Jonathan Zollinger")
public class Jeofetch implements Runnable{
    static HardwareAbstractionLayer HARDWARE;
    static OperatingSystem OS;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

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
        OptionalInt formatSize = properties.keySet().stream().mapToInt(String::length).max();
        try {
            spec.commandLine().getOut().println((new String(Files.readAllBytes(Paths.get("/home/jonathan/go/src/github.com/jonathan-zollinger/redhat/src/blueprints/baseline-workstation/assets/ascii/tie-fighter.ans")))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String formatter = String.format("%s%d%s: %s",
                "%",
                formatSize.isPresent()? formatSize.getAsInt(): 5,
                "s",
                "%-1s");
        for (String property: properties.keySet()) {
            spec.commandLine().getOut().println( String.format(formatter,
                    property, properties.get(property)));
        }

    }

    private static Map<String, String> getOsProperties() {

        HashMap<String, String> properties = new HashMap<>();
        properties.put("os", OS.getFamily() + " " + OS.getVersionInfo().getVersion());
        properties.put("hostname", OS.getNetworkParams().getHostName());
        properties.put("uptime", getReadableTime(OS.getSystemUptime()));
        return properties;
    }

    private static Map<String, String> getHardwareProperties() {
        Map<String, String> properties = new HashMap<>();
        HARDWARE.getMemory().getTotal();
        properties.put("cpu", HARDWARE.getProcessor().getProcessorIdentifier().getName());
        properties.put("gpu", String.join(", ", HARDWARE.getGraphicsCards()
                .stream()
                .map(GraphicsCard::getName)
                .toArray(String[]::new)));
        float ramUsed = (float) 1.00 - (float) HARDWARE.getMemory().getAvailable() / HARDWARE.getMemory().getTotal();
        properties.put("ram", String.format("%s/%s (%.2f%s)",
                bytesToReadableSize(HARDWARE.getMemory().getTotal() - HARDWARE.getMemory().getAvailable()).split(" ")[0],
                bytesToReadableSize(HARDWARE.getMemory().getTotal()),
                ramUsed * 100,
                "%"));
        return properties;
    }

    static String bytesToReadableSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int z = (63 - Long.numberOfLeadingZeros(bytes)) / 10;
        return String.format("%.1f %sB", (double)bytes / (1L << (z * 10)), " KMGTPE".charAt(z));
    }

    static String getReadableTime(Long seconds) {
        long hours = seconds / 3600;
        seconds = seconds % 3600;
        long minutes = seconds / 60;
        seconds = seconds % 60;

        return hours + "h " + minutes + "m " + seconds + "s";
    }
}
