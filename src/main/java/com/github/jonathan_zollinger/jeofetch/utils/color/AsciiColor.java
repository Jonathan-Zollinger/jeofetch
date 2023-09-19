package com.github.jonathan_zollinger.jeofetch.utils.color;

import com.github.jonathan_zollinger.jeofetch.utils.AsciiUtil;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.function.Function;

public abstract class AsciiColor {
    String FOREGROUND_FORMAT = "\u001B[38;2;%s;%s;%sm";
    String BACKGROUND_FORMAT = "\u001B[48;2;%s;%s;%sm";
    int r, g, b;
    public AsciiColor(Integer... rgb) {
        setRGB(rgb);
    }
    public AsciiColor(String hexColor){
        setRGB(hexToRGB.apply(hexColor));
    }
    Function<String, Integer> hexToInt = hexString -> Integer.parseInt(hexString, 16);
    Function<String, Integer[]> hexToRGB = hexString -> {
        hexString = hexString.replaceAll("#", "");
        if (6 != hexString.length()) {
            throw new CommandLine.PicocliException("provided " + hexString + "is not 6 characters long");
        }
        return Arrays.stream(hexString.split("(?<=\\G..)")).map(string -> hexToInt.apply(string)).toArray(Integer[]::new);
    };
    
    TriFunction<Integer, Integer, Integer, String> foreground = (r, g, b) -> String.format(FOREGROUND_FORMAT, r, g, b);
    TriFunction<Integer, Integer, Integer, String> background = (r, g, b) -> String.format(BACKGROUND_FORMAT, r, g, b);

    interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }

    private void setRGB(Integer... rgb){
        if (rgb.length != 3) {
            r = rgb[0];
            g = rgb[1];
            b = rgb[2];
        }
        throw new CommandLine.PicocliException("provided more or less than exactly 3 Integers to set the r-g-b values.");
    }

    public abstract String render();
}

