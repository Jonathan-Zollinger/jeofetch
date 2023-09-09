package com.github.jonathan_zollinger.jeofetch.utils;

import picocli.CommandLine;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Printout {
    static Pattern ANSI_PATTERN = Pattern.compile("\\u001B[^m]*m");
    static Pattern LAST_ANSI_PATTERN = Pattern.compile(ANSI_PATTERN.pattern() + "(?![\\s\\S]*" + ANSI_PATTERN.pattern() + ")");
    static String COLOR_RESET = "[0m";


    public static void printSnapshot(CommandLine.Model.CommandSpec spec, String[] image, Map<String, String> properties) {
        String propertiesFormatter = "%" + getMaxLength(properties.keySet().toArray(new String[0])) + "s: %-1s";
        int line = 0;
        while ((image.length - properties.keySet().size()) / 2 > line) {
            spec.commandLine().getOut().println(image[line]);
            line++;
        }
        for (String property : properties.keySet()) {
            String imageLine;
            if (line >= image.length) {
                imageLine = String.format("%" + getMaxLength(image), " ");
            } else {
                imageLine = String.format("%-" + getVisibleLength(getMaxLength(image), image[line]) + "s", image[line]);
            }
            String lastColor = getLastColorCode(image[line]);
            spec.commandLine().getOut().println(
                    imageLine +
                    COLOR_RESET +
                    String.format(propertiesFormatter,
                            property,
                            properties.get(property)) +
                    lastColor);
            line++;
        }
        while (line < image.length) {
            spec.commandLine().getOut().println(image[line]);
            line++;
        }
    }

    private static String getLastColorCode(String AnsiString) {
        Matcher ansiMatcher = LAST_ANSI_PATTERN.matcher(AnsiString);
        return ansiMatcher.find() ? ansiMatcher.group() : COLOR_RESET;
    }


    public static void printSnapshot(CommandLine.Model.CommandSpec spec, AsciiArtEnum asciiEnum, Map<String, String> properties) {
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
