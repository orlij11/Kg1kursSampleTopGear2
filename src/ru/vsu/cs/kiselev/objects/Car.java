package ru.vsu.cs.kiselev.objects;

import ru.vsu.cs.kiselev.DisplayContext;
import ru.vsu.cs.kiselev.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Car {
    public void draw(Graphics2D g2d, DisplayContext context, double curvature) {
        int carX = context.getScreenWidth() / 2;
        int carY = context.getCarBaseY();
        int scale = 4;

        drawHeadlights(g2d, carX, carY, scale, context);
        drawCarBody(g2d, carX, carY, scale);
    }

    private void drawHeadlights(Graphics2D g2d, int carX, int carY, int scale, DisplayContext context) {
        int lightBaseY = carY + 15 * scale;
        int lightTopY = context.getHorizonY() + 60;

        int lightBottomLeftX = carX - 13 * scale;
        int lightBottomRightX = carX + 13 * scale;

        int topWidth = 40;
        int lightTopLeftX = carX - topWidth / 2;
        int lightTopRightX = carX + topWidth / 2;

        int[] lightX = { lightBottomLeftX, lightBottomRightX, lightTopRightX, lightTopLeftX };
        int[] lightY = { lightBaseY, lightBaseY, lightTopY, lightTopY };

        GradientPaint lightGradient = new GradientPaint(
                carX, carY, GameConstants.CAR_HEADLIGHT_NEON,
                carX, lightTopY, new Color(0, 255, 255, 0)
        );
        g2d.setPaint(lightGradient);
        g2d.fillPolygon(lightX, lightY, 4);
    }

    private void drawCarBody(Graphics2D g2d, int carX, int carY, int scale) {
        AffineTransform originalTransform = g2d.getTransform();

        double time = System.currentTimeMillis();
        double bobbing = Math.sin(time / 200.0) * 2;
        g2d.translate(carX, carY + bobbing);

        Color body = new Color(130, 20, 180);
        Color darkBody = new Color(80, 0, 100);
        Color glass = new Color(0, 255, 255, 100);
        Color lights = new Color(255, 0, 255);
        Color exhaust = new Color(255, 255, 0);

        g2d.setColor(body);
        g2d.fillRect(-16 * scale, 5 * scale, 32 * scale, 15 * scale);

        g2d.setColor(darkBody);
        g2d.fillRect(-14 * scale, 0 * scale, 28 * scale, 5 * scale);

        g2d.setColor(glass);
        g2d.fillRect(-13 * scale, 2 * scale, 26 * scale, 3 * scale);

        g2d.setColor(lights.brighter());
        g2d.fillRect(-14 * scale, 8 * scale, 8 * scale, 5 * scale);
        g2d.fillRect(6 * scale, 8 * scale, 8 * scale, 5 * scale);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(-18 * scale, 18 * scale, 8 * scale, 5 * scale);
        g2d.fillRect(10 * scale, 18 * scale, 8 * scale, 5 * scale);

        g2d.setColor(exhaust);
        g2d.fillRect(-10 * scale, 19 * scale, 3 * scale, 3 * scale);
        g2d.fillRect(7 * scale, 19 * scale, 3 * scale, 3 * scale);

        g2d.setTransform(originalTransform);
    }
}