package ru.vsu.cs.kiselev.objects;

import ru.vsu.cs.kiselev.DisplayContext;
import ru.vsu.cs.kiselev.GameConstants;

import java.awt.*;
import java.util.Random;

public class Road {
    private Random random = new Random();
    private double scrollOffset = 0;

    public void update(double speed) {
        scrollOffset += speed * 100;
    }

    public void draw(Graphics2D g2d, DisplayContext context) {
        drawGrass(g2d, context);
        drawRoadSurface(g2d, context);
        drawRoadMarkings(g2d, context);
    }

    private void drawGrass(Graphics2D g2d, DisplayContext context) {
        g2d.setColor(GameConstants.GRASS_COLOR);
        g2d.fillRect(0, context.getHorizonY(),
                context.getScreenWidth(),
                context.getScreenHeight() - context.getHorizonY());
    }

    private void drawRoadSurface(Graphics2D g2d, DisplayContext context) {
        int roadWidthAtBottom = context.getRoadWidthAtBottom();
        int roadWidthAtTop = context.getRoadWidthAtTop();
        int roadBottomY = context.getScreenHeight();
        int roadTopY = context.getHorizonY();

        int[] roadXPoints = {
                (context.getScreenWidth() - roadWidthAtBottom) / 2,
                (context.getScreenWidth() + roadWidthAtBottom) / 2,
                (context.getScreenWidth() + roadWidthAtTop) / 2,
                (context.getScreenWidth() - roadWidthAtTop) / 2
        };

        int[] roadYPoints = {
                roadBottomY,
                roadBottomY,
                roadTopY,
                roadTopY
        };

        g2d.setColor(GameConstants.TRACK_COLOR);
        g2d.fillPolygon(roadXPoints, roadYPoints, 4);
    }

    private void drawRoadMarkings(Graphics2D g2d, DisplayContext context) {
        long currentTime = System.currentTimeMillis();
        final int patternHeight = 80;
        double currentScrollOffset = scrollOffset % patternHeight;

        final int maxMarkerWidth = 15;
        final int maxMarkerHeight = 30;

        for (int y = -patternHeight; y < context.getScreenHeight(); y += patternHeight) {
            int currentY = y + (int)currentScrollOffset;

            if (currentY < context.getHorizonY()) {
                continue;
            }

            double perspectiveFactor = (double)(currentY - context.getHorizonY()) /
                    (context.getScreenHeight() - context.getHorizonY());
            int currentMarkerWidth = Math.max(1, (int)(maxMarkerWidth * perspectiveFactor));
            int currentMarkerHeight = Math.max(1, (int)(maxMarkerHeight * perspectiveFactor));

            int markerX = context.getScreenWidth() / 2 - currentMarkerWidth / 2;

            double stripeFlicker = (Math.sin(currentTime * 0.003 + currentY * 0.1) + 1.0) / 2.0;
            int stripeAlpha = 80 + (int)(175 * stripeFlicker);
            stripeAlpha = Math.max(60, Math.min(255, stripeAlpha));

            g2d.setColor(new Color(
                    GameConstants.ROAD_MARKING.getRed(),
                    GameConstants.ROAD_MARKING.getGreen(),
                    GameConstants.ROAD_MARKING.getBlue(),
                    stripeAlpha
            ));

            g2d.fillRect(markerX, currentY, currentMarkerWidth, currentMarkerHeight);
        }
    }
}