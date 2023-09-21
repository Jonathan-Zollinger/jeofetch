package com.github.jonathan_zollinger.jeofetch.utils;

import lombok.Setter;
import picocli.CommandLine;

import java.util.Arrays;

@Setter
public class SprayPaint {
    private STYLE[] styles;
    private final RGB color;

    public SprayPaint(RGB color) {
        this.color = color;
    }

    public String tag(String text){
        return CommandLine.Help.Ansi.AUTO.string(String.format("@|%s,%s %s|@",
                String.join(",", Arrays.toString(styles)), color.ize(), text));
    }

}

