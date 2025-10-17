package kiselev_i_e.objects;

import kiselev_i_e.GameConstants;

import java.awt.*;
import java.util.Random;

public class DistantCity {
    private final Color[] BUILDING_COLORS = {
            new Color(15, 0, 25),
            new Color(25, 0, 35),
            new Color(35, 0, 45)
    };

    private final Color WINDOW_COLOR = new Color(255, 100, 255, 200);
    private final Color BRIGHT_WINDOW_COLOR = new Color(255, 150, 255, 255);

    private Random random = new Random();
    private int[] buildingHeights;
    private int[] buildingWidths;
    private int[] buildingPositions;
    private boolean[][] windowLights;
    private int buildingCount;
    private long lastUpdateTime = 0;

    public DistantCity() {
        initializeCity();
    }

    private void initializeCity() {
        buildingCount = 8 + random.nextInt(8);
        buildingHeights = new int[buildingCount];
        buildingWidths = new int[buildingCount];
        buildingPositions = new int[buildingCount];
        windowLights = new boolean[buildingCount][50]; // Максимум 50 окон на здание

        int totalWidth = 0;
        for (int i = 0; i < buildingCount; i++) {
            buildingHeights[i] = 80 + random.nextInt(120);
            buildingWidths[i] = 30 + random.nextInt(50);
            buildingPositions[i] = totalWidth + random.nextInt(20);
            totalWidth += buildingWidths[i] + 10 + random.nextInt(20);


            for (int j = 0; j < 10; j++) {
                windowLights[i][j] = random.nextDouble() > 0.4;
            }
        }
    }

    public void draw(Graphics2D g2d, int screenWidth, int screenHeight) {
        int cityBaseY = GameConstants.HORIZON_Y;

        updateWindowFlicker();

        for (int i = 0; i < buildingCount; i++) {
            drawSkyscraper(g2d, screenWidth, cityBaseY, i);
        }

        drawHorizonGlow(g2d, screenWidth);
    }

    private void updateWindowFlicker() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdateTime > 5) {
            lastUpdateTime = currentTime;

            for (int i = 0; i < buildingCount; i++) {
                for (int j = 0; j < 28; j++) {
                    if (random.nextInt(72) < 1) {
                        windowLights[i][j] = !windowLights[i][j];
                    }
                }
            }
        }
    }

    private void drawSkyscraper(Graphics2D g2d, int screenWidth, int cityBaseY, int buildingIndex) {
        int buildingWidth = buildingWidths[buildingIndex];
        int buildingHeight = buildingHeights[buildingIndex];
        int buildingX = buildingPositions[buildingIndex];

        int totalCityWidth = buildingPositions[buildingCount - 1] + buildingWidths[buildingCount - 1];
        int cityStartX = (screenWidth - totalCityWidth) / 2;
        int finalBuildingX = cityStartX + buildingX;
        int buildingY = cityBaseY - buildingHeight;

        Color buildingColor = BUILDING_COLORS[random.nextInt(BUILDING_COLORS.length)];

        g2d.setColor(buildingColor);
        g2d.fillRect(finalBuildingX, buildingY, buildingWidth, buildingHeight);

        drawBuildingDetails(g2d, finalBuildingX, buildingY, buildingWidth, buildingHeight, buildingColor);
        drawBuildingWindows(g2d, finalBuildingX, buildingY, buildingWidth, buildingHeight, buildingIndex);
    }

    private void drawBuildingDetails(Graphics2D g2d, int x, int y, int width, int height, Color baseColor) {
        g2d.setColor(baseColor.brighter());
        int topHeight = Math.max(5, height / 20);
        g2d.fillRect(x, y, width, topHeight);

        g2d.setColor(new Color(baseColor.getRed() + 10, baseColor.getGreen(), baseColor.getBlue() + 10));
        for (int i = 0; i < width; i += width / 4) {
            if (i < width - 2) {
                g2d.fillRect(x + i, y + topHeight, 2, height - topHeight);
            }
        }

        g2d.setColor(baseColor.darker());
        int baseHeight = Math.max(8, height / 15);
        g2d.fillRect(x - 2, y + height - baseHeight, width + 4, baseHeight);
    }

    private void drawBuildingWindows(Graphics2D g2d, int x, int y, int width, int height, int buildingIndex) {
        int windowSize = Math.max(2, width / 8);
        int windowSpacing = windowSize + 2;

        int startX = x + 4;
        int startY = y + 15;

        int windowCounter = 0;

        for (int wx = startX; wx < x + width - 4 && windowCounter < 50; wx += windowSpacing) {
            for (int wy = startY; wy < y + height - 10 && windowCounter < 50; wy += windowSpacing) {

                if (windowLights[buildingIndex][windowCounter]) {
                    Color windowColor = (random.nextDouble() > 0.6) ? BRIGHT_WINDOW_COLOR : WINDOW_COLOR;
                    g2d.setColor(windowColor);
                    g2d.fillRect(wx, wy, windowSize, windowSize);
                }
                windowCounter++;
            }
        }
    }

    private void drawHorizonGlow(Graphics2D g2d, int screenWidth) {
        GradientPaint horizonGlow = new GradientPaint(
                0, GameConstants.HORIZON_Y - 10, new Color(180, 50, 220, 100),
                0, GameConstants.HORIZON_Y + 20, new Color(180, 50, 220, 0)
        );

        g2d.setPaint(horizonGlow);
        g2d.fillRect(0, GameConstants.HORIZON_Y - 10, screenWidth, 30);

        // Линия горизонта
        g2d.setColor(new Color(200, 80, 240));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(0, GameConstants.HORIZON_Y, screenWidth, GameConstants.HORIZON_Y);
        g2d.setStroke(new BasicStroke(1));
    }

    public static class Track {
        private Random random = new Random();

        public Track() {
        }

        public void update() {
        }

        public void drawStaticRoad(Graphics2D g2d, int screenWidth, int screenHeight) {
            g2d.setColor(GameConstants.GRASS_COLOR);
            g2d.fillRect(0, GameConstants.HORIZON_Y, screenWidth, screenHeight - GameConstants.HORIZON_Y);

            int roadWidthAtBottom = (int)(screenWidth * 0.4);
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

            drawFlickeringMarkings(g2d, screenWidth, screenHeight);
        }

        private void drawFlickeringMarkings(Graphics2D g2d, int screenWidth, int screenHeight) {
            long currentTime = System.currentTimeMillis();

            double flicker = (Math.sin(currentTime * 0.004) + 1.0) / 2.0;
            int baseAlpha = 120 + (int)(135 * flicker);

            for (int y = GameConstants.HORIZON_Y + 50; y < screenHeight; y += 70) {
                double stripeFlicker = (Math.sin(currentTime * 0.005 + y * 0.05) + 1.0) / 2.0;
                int stripeAlpha = 100 + (int)(155 * stripeFlicker);

                g2d.setColor(new Color(255, 0, 255, stripeAlpha));

                int markerWidth = 4;
                int markerHeight = 35;
                int markerX = screenWidth / 2 - markerWidth / 2;
                g2d.fillRect(markerX, y, markerWidth, markerHeight);
            }
        }

        public double getCameraPosition() {
            return 0.0; // Статичная камера
        }
    }
}