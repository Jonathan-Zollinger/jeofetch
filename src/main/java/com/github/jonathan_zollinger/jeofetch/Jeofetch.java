package com.github.jonathan_zollinger.jeofetch;

import com.github.jonathan_zollinger.jeofetch.utils.AsciiArtEnum;
import oshi.SystemInfo;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.github.jonathan_zollinger.jeofetch.utils.Printout.printSnapshot;
import static com.github.jonathan_zollinger.jeofetch.utils.Stats.getHardwareProperties;
import static com.github.jonathan_zollinger.jeofetch.utils.Stats.getOsProperties;

@CommandLine.Command(name = "jeofetch",
        mixinStandardHelpOptions = true,
        version = "jeofetch v0.0.2",
        description="jeofetch is a system information tool written in java",
        footer = "written by Jonathan Zollinger")
public class Jeofetch implements Runnable{
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(
            names = "--properties, -p",
            split = ","
    )
    String[] properties = "os, hostname, uptime, cpu, gpu, ram".split(",");

    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new Jeofetch());
        int exitCode = commandLine.execute(args);
        System.exit(exitCode);
        // the bloody transitive property from oshi needs this property assignment
        System.setProperty("org.slf4j.LoggerFactory", "org.apache.logging.log4j.simple.SimpleLoggerContextFactory");
    }

    @Override
    public void run() {
        printSnapshot(spec, AsciiArtEnum.LAMBDA_SHUTTLE, getJeofetchStats(properties));
    }



    private Map<String, String> getJeofetchStats(String[] properties) {
        return new HashMap<>() {{
            putAll(getHardwareProperties(new SystemInfo().getHardware(properties)));
            putAll(getOsProperties(new SystemInfo().getOperatingSystem(properties)));
        }};

    }

}
