package com.github.jonathan_zollinger.jeofetch;

import com.github.jonathan_zollinger.jeofetch.utils.AsciiArtEnum;
import oshi.SystemInfo;
import picocli.CommandLine;

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

    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new Jeofetch());
        int exitCode = commandLine.execute(args);
        System.exit(exitCode);
        // the bloody transitive property from oshi needs this property assignment
        System.setProperty("org.slf4j.LoggerFactory", "org.apache.logging.log4j.simple.SimpleLoggerContextFactory");
    }

    @Override
    public void run() {
        printSnapshot(spec, AsciiArtEnum.LAMBDA_SHUTTLE, getJeofetchStats());
    }



    private Map<String, String> getJeofetchStats() {
        return new HashMap<>() {{
            putAll(getHardwareProperties(new SystemInfo().getHardware()));
            putAll(getOsProperties(new SystemInfo().getOperatingSystem()));
        }};

    }

}
