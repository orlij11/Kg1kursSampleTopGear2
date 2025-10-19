package ru.vsu.cs.kiselev;

import java.awt.*;

public class DisplayContext {
    private final int screenWidth;
    private final int screenHeight;
    private final int horizonY;

    public DisplayContext(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.horizonY = calculateHorizonY();
    }

    private int calculateHorizonY() {
        return (int)(screenHeight * 280.0 / 600.0);
    }

    public int getScreenWidth() { return screenWidth; }
    public int getScreenHeight() { return screenHeight; }
    public int getHorizonY() { return horizonY; }

    public int getRoadWidthAtBottom() {
        return (int)(screenWidth * 0.7);
    }

    public int getRoadWidthAtTop() {
        return 80;
    }

    public int getCarBaseY() {
        return screenHeight - 95;
    }

    public double getPerspectiveScale(double distance) {
        return 1.0 / (distance + 0.001);
    }
}