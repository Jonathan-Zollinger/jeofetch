package com.github.jonathan_zollinger.jeofetch.utils;

import oshi.hardware.GraphicsCard;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.util.HashMap;
import java.util.Map;

public class Stats {

    public static Map<String, String> getOsProperties(OperatingSystem os) {

        HashMap<String, String> properties = new HashMap<>();
        properties.put("os", os.getFamily() + " " + os.getVersionInfo().getVersion());
        properties.put("hostname", os.getNetworkParams().getHostName());
        properties.put("uptime", getReadableTime(os.getSystemUptime()));
        return properties;
    }

    public static Map<String, String> getHardwareProperties(HardwareAbstractionLayer hardware) {
        Map<String, String> properties = new HashMap<>();
        hardware.getMemory().getTotal();
        properties.put("cpu", hardware.getProcessor().getProcessorIdentifier().getName());
        properties.put("gpu", String.join(", ", hardware.getGraphicsCards()
                .stream()
                .map(GraphicsCard::getName)
                .toArray(String[]::new)));
        float ramUsed = (float) 1.00 - (float) hardware.getMemory().getAvailable() / hardware.getMemory().getTotal();
        properties.put("ram", String.format("%s/%s (%.2f%s)",
                bytesToReadableSize(hardware.getMemory().getTotal() - hardware.getMemory().getAvailable()).split(" ")[0],
                bytesToReadableSize(hardware.getMemory().getTotal()),
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
