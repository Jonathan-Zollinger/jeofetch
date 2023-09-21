package com.github.jonathan_zollinger.jeofetch;

import com.github.jonathan_zollinger.jeofetch.assets.AsciiArtEnum;
import com.github.jonathan_zollinger.jeofetch.utils.AsciiUtil;
import lombok.Data;
import oshi.SystemInfo;
import picocli.CommandLine;

import java.util.HashMap;
import java.util.Map;

import static com.github.jonathan_zollinger.jeofetch.utils.Stats.getHardwareProperties;
import static com.github.jonathan_zollinger.jeofetch.utils.Stats.getOsProperties;

@CommandLine.Command(name = "jeofetch",
        mixinStandardHelpOptions = true,
        version = "jeofetch v0.0.2",
        description = "Jeofetch is a system information tool written in java",
        footer = "Jeofetch is written by Jonathan Zollinger")
public class Jeofetch implements Runnable {
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
        AsciiUtil asciiUtil = new AsciiUtil();
        asciiUtil.printSnapshot(spec, AsciiArtEnum.LAMBDA_SHUTTLE, getJeofetchStats());
    }

    private Map<String, String> getJeofetchStats() {
        return new HashMap<>() {{
            SystemInfo systemInfo = new SystemInfo();
            putAll(getHardwareProperties(systemInfo.getHardware()));
            putAll(getOsProperties(systemInfo.getOperatingSystem()));
        }};
    }
}
