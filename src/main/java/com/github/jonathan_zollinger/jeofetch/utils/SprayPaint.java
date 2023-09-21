package com.github.jonathan_zollinger.jeofetch.utils;

import lombok.Setter;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Setter
public class SprayPaint {
    private STYLE[] styles;
    private final RGB color;

    public SprayPaint(RGB color) {
        this.color = color;
    }

    public String tag(String text){
        if (null != styles){
            return CommandLine.Help.Ansi.AUTO.string(String.format("@|%s,%s %s|@",
                    Arrays.stream(styles)
                            .map(Objects::toString)
                            .collect(Collectors.joining(",")), color.ize(), text));
        }
        return CommandLine.Help.Ansi.AUTO.string(String.format("@|%s %s|@", color.ize(), text));
    }

}

