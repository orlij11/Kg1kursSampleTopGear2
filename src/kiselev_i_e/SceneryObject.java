package kiselev_i_e;

import kiselev_i_e.Objects.DistantCity;

import java.awt.Graphics2D;

public abstract class SceneryObject {

    protected double t; // Расстояние вдоль трассы
    protected double offset; // Боковое смещение

    public SceneryObject(double t, double offset) {
        this.t = t;
        this.offset = offset;
    }

    public void draw(Graphics2D g2d, double cameraPosition) {
        double distanceToCamera = this.t - cameraPosition;

        // Пропускаем объекты, которые слишком далеко или позади
        if (distanceToCamera < -2.0 || distanceToCamera > 40.0) {
            return;
        }

        // Вычисляем масштаб в зависимости от расстояния
        double scale = 1.0 / (distanceToCamera + 0.001);

        // НОВЫЙ РАСЧЕТ: баннеры параллельно дороге
        // Позиция Y - привязываем к горизонту и дороге
        int screenY = (int) (GameConstants.HORIZON_Y + (GameConstants.SCREEN_HEIGHT - GameConstants.HORIZON_Y) * 0.8 * (1.0 - distanceToCamera / 40.0) - 225);

        // НОВЫЙ РАСЧЕТ: позиция X привязана к ширине дороги на текущей дистанции
        // Ширина дороги меняется с расстоянием (перспектива)
        double roadWidthAtHorizon = 80; // Ширина дороги у горизонта
        double roadWidthAtBottom = GameConstants.SCREEN_WIDTH * 0.25; // Ширина дороги внизу

        // Интерполируем ширину дороги в зависимости от расстояния
        double currentRoadWidth = roadWidthAtHorizon +
                (roadWidthAtBottom - roadWidthAtHorizon) * (1.0 - distanceToCamera / 40.0);

        // Позиция X - по бокам дороги с небольшим отступом
        double roadEdge = currentRoadWidth / 2 + 20; // +20 пикселей отступ от края дороги
        int screenX = (int) (GameConstants.SCREEN_WIDTH / 2.0 +
                this.offset * roadEdge * scale);

        // Вызываем конкретную реализацию отрисовки
        drawSelf(g2d, screenX, screenY, scale);
    }

    public void update(double distanceTravelled) {
        // Объекты движутся навстречу камере
        this.t -= distanceTravelled;
    }

    public abstract void drawSelf(Graphics2D g2d, int screenX, int screenY, double scale);

    public double getDistanceToCamera(DistantCity.Track track) {
        return this.t - track.getCameraPosition();
    }

    public double getT() {
        return t;
    }
}