package ru.vsu.cs.kiselev;

import java.awt.Graphics2D;

public abstract class SceneryObject {
    protected double t;
    protected double offset;

    public SceneryObject(double t, double offset) {
        this.t = t;
        this.offset = offset;
    }

    public void draw(Graphics2D g2d, double cameraPosition, DisplayContext context) {
        double distanceToCamera = this.t - cameraPosition;

        if (distanceToCamera < -2.0 || distanceToCamera > 40.0) {
            return;
        }

        double scale = context.getPerspectiveScale(distanceToCamera);

        int screenY = (int) (context.getHorizonY() +
                (context.getScreenHeight() - context.getHorizonY()) * 0.8 *
                        (1.0 - distanceToCamera / 40.0) - 225);

        double roadWidthAtHorizon = 80;
        double roadWidthAtBottom = context.getScreenWidth() * 0.25;

        double currentRoadWidth = roadWidthAtHorizon +
                (roadWidthAtBottom - roadWidthAtHorizon) * (1.0 - distanceToCamera / 40.0);

        double roadEdge = currentRoadWidth / 2 + 20;
        int screenX = (int) (context.getScreenWidth() / 2.0 +
                this.offset * roadEdge * scale);

        drawSelf(g2d, screenX, screenY, scale, context);
    }

    public void update(double distanceTravelled) {
        this.t -= distanceTravelled;
    }

    public abstract void drawSelf(Graphics2D g2d, int screenX, int screenY, double scale, DisplayContext context);

    public double getT() {
        return t;
    }
}