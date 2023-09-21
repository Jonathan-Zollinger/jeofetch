package com.github.jonathan_zollinger.jeofetch.utils;

public enum STYLE {
    BOLD("bold"),
    FAINT("faint"),
    UNDERLINE("underline"),
    ITALIC("italic"),
    BLINK("blink"),
    REVERSE("reverse"),
    RESET("reset");

    private final String style;

    STYLE(String style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return style;
    }
}
