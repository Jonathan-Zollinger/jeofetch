package com.github.jonathan_zollinger.jeofetch.utils;

public enum CANVAS {
    foreground("fg"),
    background("bg");

    private final String canvas;

    CANVAS(String canvas) {
        this.canvas = canvas;
    }

    @Override
    public String toString() {
        return canvas;
    }
}
