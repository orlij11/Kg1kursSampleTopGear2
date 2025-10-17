package kiselev_i_e.Objects;

import kiselev_i_e.SceneryObject;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class NeonBillboard extends SceneryObject {

    private static final String[] NEON_TEXTS = {
            "RETRO", "NEON", "DREAMS",
            "NIGHT", "CITY", "DRIVE", "SPEED",
            "CYBER","MIAMI", "OUTRUN"
    };

    private String text;
    private Color neonColor;
    private Color glowColor;
    private double waveOffset;

    public NeonBillboard(double t, double offset) {
        super(t, offset);
        this.text = NEON_TEXTS[(int)(Math.random() * NEON_TEXTS.length)];
        this.neonColor = getRandomNeonColor();
        this.glowColor = new Color(neonColor.getRed(), neonColor.getGreen(), neonColor.getBlue(), 80);
        this.waveOffset = Math.random() * Math.PI * 2;
    }

    @Override
    public void drawSelf(Graphics2D g2d, int screenX, int screenY, double scale) {
        int boardWidth = (int) (scale * 70);
        int boardHeight = (int) (scale * 30);
        int poleWidth = Math.max(1, (int)(scale * 2));
        int poleHeight = (int) (scale * 50);

        if (boardWidth < 30) return;

        AffineTransform originalTransform = g2d.getTransform();
        Composite originalComposite = g2d.getComposite();


        double wave = Math.sin(System.currentTimeMillis()) * scale * 3;
        int finalScreenY = screenY + (int)wave;

        g2d.setColor(new Color(80, 80, 80));
        g2d.fillRect(screenX - poleWidth / 2, finalScreenY - poleHeight, poleWidth, poleHeight);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        g2d.setColor(glowColor);
        int glowSize = (int)(scale * 15);
        g2d.fillRoundRect(screenX - boardWidth / 2 - glowSize/2,
                finalScreenY - poleHeight - boardHeight - glowSize/2,
                boardWidth + glowSize, boardHeight + glowSize, 10, 10);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2d.setColor(new Color(10, 10, 20, 200));
        g2d.fillRoundRect(screenX - boardWidth / 2, finalScreenY - poleHeight - boardHeight,
                boardWidth, boardHeight, 8, 8);


        g2d.setColor(neonColor);

        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(screenX - boardWidth / 2, finalScreenY - poleHeight - boardHeight,
                boardWidth, boardHeight, 8, 8);

        int fontSize = Math.max(8, (int)(scale * 10));
        g2d.setFont(new Font("Arial", Font.BOLD, fontSize));


        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textX = screenX - textWidth / 2;
        int textY = finalScreenY - poleHeight - boardHeight / 2 + fm.getAscent() / 2;

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        g2d.setColor(glowColor);
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (Math.abs(i) + Math.abs(j) == 2) { // Только диагональные смещения
                    g2d.drawString(text, textX + i, textY + j);
                }
            }
        }

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2d.setColor(neonColor);
        g2d.drawString(text, textX, textY);


        g2d.setComposite(originalComposite);
        g2d.setTransform(originalTransform);
    }

    private Color getRandomNeonColor() {
        int type = (int)(Math.random() * 6);
        switch (type) {
            case 0: return new Color(255, 0, 255);    // Розовый
            case 1: return new Color(0, 255, 255);    // Голубой
            case 2: return new Color(255, 255, 0);    // Желтый
            case 3: return new Color(0, 255, 0);      // Зеленый
            case 4: return new Color(255, 100, 0);    // Оранжевый
            case 5: return new Color(200, 0, 255);    // Фиолетовый
            default: return new Color(255, 0, 255);
        }
    }
}