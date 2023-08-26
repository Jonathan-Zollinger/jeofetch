package com.github.jonathan_zollinger.jeofetch;

import oshi.SystemInfo;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

import static com.github.jonathan_zollinger.jeofetch.utils.Stats.getHardwareProperties;
import static com.github.jonathan_zollinger.jeofetch.utils.Stats.getOsProperties;

@CommandLine.Command(name = "jeofetch",
        mixinStandardHelpOptions = true,
        version = "jeofetch v0.0.1",
        description="jeofetch is a system information tool written in java",
        footer = "written by Jonathan Zollinger")
public class Jeofetch implements Runnable{
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

        printSnapshot(getJeofetchStats());
    }

    private void printSnapshot(Map<String, String> properties) {
        try {
            spec.commandLine().getOut().println((new String(Files.readAllBytes(Paths.get("ascii-art/tie-fighter.ans")))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        OptionalInt formatSize = properties.keySet().stream().mapToInt(String::length).max();
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

    private Map<String, String> getJeofetchStats() {
        return new HashMap<>() {{
            putAll(getHardwareProperties(new SystemInfo().getHardware()));
            putAll(getOsProperties(new SystemInfo().getOperatingSystem()));
        }};

    }

}
