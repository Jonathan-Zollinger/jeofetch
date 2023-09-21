package com.github.jonathan_zollinger.jeofetch.utils;

public class RGB extends Color {
    int r, g, b;

    public RGB(int r, int g, int b) {
        super();
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    String ize() {
        canvas = null == canvas ? CANVAS.foreground: canvas;
        return String.format("%s(%d;%d;%d)", canvas, r, g, b);
    }
}
