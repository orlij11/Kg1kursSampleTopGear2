package kiselev_i_e;

import kiselev_i_e.Objects.Car;
import kiselev_i_e.Objects.DistantCity;
import kiselev_i_e.Objects.NeonBillboard;
import kiselev_i_e.Objects.Star;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class RacingPanel extends JPanel implements ActionListener {

    private Timer timer;
    private DistantCity.Track track;
    private Car car;
    private DistantCity distantCity;
    private List<SceneryObject> sceneryObjects;
    private List<Star> stars;
    private Random random = new Random();


    private double carPosition = 0.0;

    public RacingPanel() {
        setPreferredSize(new Dimension(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT));
        setBackground(Color.BLACK);

        track = new DistantCity.Track();
        car = new Car();
        distantCity = new DistantCity();
        sceneryObjects = new ArrayList<>();
        stars = new ArrayList<>();

        initializeScenery();
        initializeStars();

        timer = new Timer(GameConstants.ANIMATION_DELAY, this);
        timer.start();
    }

    private void initializeStars() {
        for (int i = 0; i < 150; i++) {
            stars.add(new Star(GameConstants.SCREEN_WIDTH, GameConstants.HORIZON_Y));
        }
    }

    private void initializeScenery() {
        for (int i = 0; i < 25; i++) {
            double offset = random.nextBoolean() ? 1.1 + random.nextDouble() * 0.4 : -1.1 - random.nextDouble() * 0.4;
            double t = 3 + i * 4.0;
            sceneryObjects.add(new NeonBillboard(t, offset));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        carPosition += GameConstants.TRACK_SPEED * 35;

        for (SceneryObject obj : sceneryObjects) {
            obj.update(GameConstants.TRACK_SPEED * 35);
        }


        sceneryObjects.removeIf(obj -> obj.getT() < -2.0);

        while (sceneryObjects.size() < 25) {
            double offset = random.nextBoolean() ? 1.1 + random.nextDouble() * 0.4 : -1.1 - random.nextDouble() * 0.4;


            double maxT = -2.0;
            for (SceneryObject obj : sceneryObjects) {
                if (obj.getT() > maxT) {
                    maxT = obj.getT();
                }
            }


            double t = maxT + 5.0 + random.nextDouble() * 3.0;
            sceneryObjects.add(new NeonBillboard(t, offset));
        }


        for (Star star : stars) {
            star.update();
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);


        drawSky(g2d);


        drawStars(g2d);


        distantCity.draw(g2d, getWidth(), getHeight());


        drawStaticRoadWithFlickeringMarkings(g2d);

        drawNeonBillboards(g2d);


        drawMovingCar(g2d);
    }

    private void drawSky(Graphics2D g2d) {
        GradientPaint skyGradient = new GradientPaint(
                0, 0, GameConstants.SKY_COLOR_TOP,
                0, GameConstants.HORIZON_Y,
                GameConstants.SKY_COLOR_BOTTOM
        );
        g2d.setPaint(skyGradient);
        g2d.fillRect(0, 0, getWidth(), GameConstants.HORIZON_Y);
    }

    private void drawStars(Graphics2D g2d) {
        for (Star star : stars) {
            star.draw(g2d);
        }
    }

    private void drawStaticRoadWithFlickeringMarkings(Graphics2D g2d) {
        int screenWidth = getWidth();
        int screenHeight = getHeight();


        g2d.setColor(GameConstants.GRASS_COLOR);
        g2d.fillRect(0, GameConstants.HORIZON_Y, screenWidth, screenHeight - GameConstants.HORIZON_Y);


        int roadWidthAtBottom = (int)(screenWidth * 0.7);
        int roadWidthAtTop = 80;

        int roadBottomY = screenHeight;
        int roadTopY = GameConstants.HORIZON_Y;

        int[] roadXPoints = {
                (screenWidth - roadWidthAtBottom) / 2,
                (screenWidth + roadWidthAtBottom) / 2,
                (screenWidth + roadWidthAtTop) / 2,
                (screenWidth - roadWidthAtTop) / 2
        };

        int[] roadYPoints = {
                roadBottomY,
                roadBottomY,
                roadTopY,
                roadTopY
        };


        g2d.setColor(GameConstants.TRACK_COLOR);
        g2d.fillPolygon(roadXPoints, roadYPoints, 4);

        drawFlickeringRoadMarkings(g2d, screenWidth, screenHeight);
    }

    private void drawFlickeringRoadMarkings(Graphics2D g2d, int screenWidth, int screenHeight) {
        long currentTime = System.currentTimeMillis();

        final int patternHeight = 80;
        double scrollOffset = (Math.abs(carPosition) * 100) % patternHeight;


        final int maxMarkerWidth = 15;
        final int maxMarkerHeight = 30;
        // -----------------------------------------------------------

        for (int y = -patternHeight; y < screenHeight; y += patternHeight) {
            int currentY = y + (int)scrollOffset;

            if (currentY < GameConstants.HORIZON_Y) {
                continue;
            }


            double perspectiveFactor = (double)(currentY - GameConstants.HORIZON_Y) / (screenHeight - GameConstants.HORIZON_Y);

            int currentMarkerWidth = (int)(maxMarkerWidth * perspectiveFactor);
            int currentMarkerHeight = (int)(maxMarkerHeight * perspectiveFactor);

            if (currentMarkerWidth < 1) currentMarkerWidth = 1;
            if (currentMarkerHeight < 1) currentMarkerHeight = 1;

            int markerX = screenWidth / 2 - currentMarkerWidth / 2;
            // -----------------------------------------------------------

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

    private void drawNeonBillboards(Graphics2D g2d) {

        sceneryObjects.sort(Comparator.comparingDouble(obj -> -obj.getT()));

        for (SceneryObject obj : sceneryObjects) {
            obj.draw(g2d, carPosition);
        }
    }

    private void drawMovingCar(Graphics2D g2d) {
        int carX = getWidth() / 2;
        int carY = getHeight() - 95;

        drawHeadlights(g2d, carX, carY);

        drawCarBody(g2d, carX, carY);
    }

    private void drawHeadlights(Graphics2D g2d, int carX, int carY) {
        int scale = 4;
        int lightBaseY = carY + 15 * scale;
        int lightTopY = GameConstants.HORIZON_Y + 60;

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

    private void drawCarBody(Graphics2D g2d, int carX, int carY) {
        int scale = 4;

        AffineTransform originalTransform = g2d.getTransform();

        double time = System.currentTimeMillis();
        double bobbing = Math.sin(time / 200.0) * 2;
        double movementBobbing = Math.sin(carPosition * 8) * 1.0;

        g2d.translate(carX, carY + bobbing + movementBobbing);

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
        if (System.currentTimeMillis() % 200 > 100) {
            g2d.fillRect(-12 * scale, 20 * scale, 4 * scale, 2 * scale);
            g2d.fillRect(8 * scale, 20 * scale, 4 * scale, 2 * scale);
        }

        g2d.setTransform(originalTransform);
    }
}