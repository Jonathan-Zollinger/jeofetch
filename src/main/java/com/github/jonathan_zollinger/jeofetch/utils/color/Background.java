package com.github.jonathan_zollinger.jeofetch.utils.color;

public class Background extends AsciiColor {

    public Background(Integer... rgb) {
        super(rgb);
    }

    public Background(String hexColor) {
        super(hexColor);
    }

    @Override
    public String render() {
        return background.apply(r, g, b);
    }
}
