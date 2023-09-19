package com.github.jonathan_zollinger.jeofetch.utils.color;

public class Foreground extends AsciiColor {

    public Foreground(Integer... rgb) {
        super(rgb);
    }

    public Foreground(String hexColor) {
        super(hexColor);
    }

    @Override
    public String render() {
        return foreground.apply(r, g, b);
    }
}
