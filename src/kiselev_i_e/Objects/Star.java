package kiselev_i_e.Objects;

import java.awt.*;
import java.util.Random;

public class Star {
    private int x, y;
    private float size;
    private float alpha;
    private float alphaSpeed;
    private boolean increasing;

    private static Random random = new Random();

    public Star(int screenWidth, int maxY) {
        this.x = random.nextInt(screenWidth);
        this.y = random.nextInt(maxY); // Звезды только выше горизонта
        this.size = 1 + random.nextFloat() * 2;
        this.alpha = random.nextFloat();
        this.alphaSpeed = 0.005f + random.nextFloat() * 0.01f;
        this.increasing = random.nextBoolean();
    }

    public void update() {
        if (increasing) {
            alpha += alphaSpeed;
            if (alpha > 1.0f) {
                alpha = 1.0f;
                increasing = false;
            }
        } else {
            alpha -= alphaSpeed;
            if (alpha < 0.1f) {
                alpha = 0.1f;
                increasing = true;
            }
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(new Color(255, 255, 255, (int) (alpha * 255)));
        g2d.fillRect(x, y, (int) size, (int) size);
    }
}