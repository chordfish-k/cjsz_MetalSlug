package com.scnu.element;

import java.awt.*;

public class TextObj extends ElementObj{
    public String text = "";
    public Color fontColor = Color.RED;
    public Font font = null;

    public TextObj(String fontName, int fontStyle, int fontSize) {
        super.onCreate();
        font = new Font(fontName, fontStyle, fontSize);
    }

    @Override
    public void onDraw(Graphics g) {
        super.onDraw(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(font);
        Color c = g2.getColor();
        g2.setColor(fontColor);
        int x = (int)transform.getX();
        int y = (int)transform.getY();
        g2.drawString(text, x, y);
        g2.setColor(c);
    }
}
