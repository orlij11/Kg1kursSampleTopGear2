package ru.vsu.cs.kiselev;

import ru.vsu.cs.kiselev.objects.Car;
import ru.vsu.cs.kiselev.objects.DistantCity;
import ru.vsu.cs.kiselev.objects.NeonBillboard;
import ru.vsu.cs.kiselev.objects.Road;
import ru.vsu.cs.kiselev.objects.Star;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class RacingPanel extends JPanel implements ActionListener {

    private Timer timer;
    private Road road;
    private Car car;
    private DistantCity distantCity;
    private List<SceneryObject> sceneryObjects;
    private List<Star> stars;
    private Random random = new Random();
    private DisplayContext displayContext;

    private double carPosition = 0.0;

    public RacingPanel() {
        setPreferredSize(new Dimension(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT));
        setBackground(Color.BLACK);

        displayContext = new DisplayContext(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        road = new Road();
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
            stars.add(new Star(displayContext));
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

        road.update(GameConstants.TRACK_SPEED * 35);

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

        displayContext = new DisplayContext(getWidth(), getHeight());

        drawSky(g2d, displayContext);
        drawStars(g2d, displayContext);
        distantCity.draw(g2d, displayContext);
        road.draw(g2d, displayContext);
        drawNeonBillboards(g2d, displayContext);
        drawMovingCar(g2d, displayContext);
    }

    private void drawSky(Graphics2D g2d, DisplayContext context) {
        GradientPaint skyGradient = new GradientPaint(
                0, 0, GameConstants.SKY_COLOR_TOP,
                0, context.getHorizonY(),
                GameConstants.SKY_COLOR_BOTTOM
        );
        g2d.setPaint(skyGradient);
        g2d.fillRect(0, 0, context.getScreenWidth(), context.getHorizonY());
    }

    private void drawStars(Graphics2D g2d, DisplayContext context) {
        for (Star star : stars) {
            star.draw(g2d);
        }
    }

    private void drawNeonBillboards(Graphics2D g2d, DisplayContext context) {
        sceneryObjects.sort(Comparator.comparingDouble(obj -> -obj.getT()));

        for (SceneryObject obj : sceneryObjects) {
            obj.draw(g2d, carPosition, context);
        }
    }

    private void drawMovingCar(Graphics2D g2d, DisplayContext context) {
        car.draw(g2d, context, 0.0);
    }
}