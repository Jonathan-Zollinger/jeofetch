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
        for (String property : properties.keySet()) {
            Matcher ansiMatcher = ANSI_PATTERN.matcher(image[line]);
            int imageLength = getMaxLength(image);
            while(ansiMatcher.find()){
                imageLength += ansiMatcher.group().length();
            }
            String imageLine = String.format("%-" + imageLength + "s", image[line]);

            spec.commandLine().getOut().println(imageLine + String.format(propertiesFormatter, property, properties.get(property)));
            line++;
        }
        while (line < image.length) {
            spec.commandLine().getOut().println(image[line]);
            line++;
        }
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
