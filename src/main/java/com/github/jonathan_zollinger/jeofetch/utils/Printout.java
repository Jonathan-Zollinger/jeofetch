package com.github.jonathan_zollinger.jeofetch.utils;

import picocli.CommandLine;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Printout {
    static Pattern ANSI_PATTERN = Pattern.compile("\\u001B[^m]*m");

    public static void printSnapshot(CommandLine.Model.CommandSpec spec, String[] image, Map<String, String> properties) {
        String propertiesFormatter = "%" + getMaxLength(properties.keySet().toArray(new String[0])) + "s: %-1s";
        int line = 0;
        while ((image.length - properties.keySet().size()) / 2 > line) {
            spec.commandLine().getOut().println(image[line]);
            line ++;
        }
        for (String property : properties.keySet()) {
            String imageLine;
            if (line >= image.length) {
                imageLine = String.format("%" + getMaxLength(image), " ");
            } else {
                imageLine = String.format("%-" + getVisibleLength(getMaxLength(image), image[line]) + "s", image[line]);
            }
            spec.commandLine().getOut().println(imageLine + String.format(propertiesFormatter, property, properties.get(property)));
            line++;
        }
        while (line < image.length) {
            spec.commandLine().getOut().println(image[line]);
            line++;
        }
    }

    public static void printSnapshot(CommandLine.Model.CommandSpec spec, AsciiArtEnum asciiEnum, Map<String, String> properties){
        printSnapshot(spec, asciiEnum.artPiece.split("\n"), properties);
    }

    private static int getVisibleLength(int maxLengthSansAnsiSequence, String string) {
        Matcher ansiMatcher = ANSI_PATTERN.matcher(string);
        while (ansiMatcher.find()) {
            maxLengthSansAnsiSequence += ansiMatcher.group().length();
        }
        return maxLengthSansAnsiSequence;
    }

    static int getMaxLength(String[] strings) {
        return Arrays.stream(strings)
                .mapToInt(string -> ANSI_PATTERN
                        .matcher(string)
                        .replaceAll("")
                        .length())
                .max()
                .orElse(5);
    }

}
