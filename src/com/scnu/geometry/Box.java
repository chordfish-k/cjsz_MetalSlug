package com.scnu.geometry;

import java.awt.*;

public class Box extends Polygon{
    protected int w;
    protected int h;

    public Box() {
        this(new Vector2(), 0, 0);
    }

    public Box(Vector2 center, int w, int h) {
        super(center);

        float cx = center.x;
        float cy = center.y;
        float hw = w / 2f;
        float hh = h / 2f;

        addPoint(new Vector2(cx - hw, cy + hh));
        addPoint(new Vector2(cx + hw, cy + hh));
        addPoint(new Vector2(cx + hw, cy - hh));
        addPoint(new Vector2(cx - hw, cy - hh));
    }

    public void setSize(Vector2 size) {
        w = (int) size.x;
        h = (int) size.y;
        float cx = center.x;
        float cy = center.y;
        float hw = w / 2f;
        float hh = h / 2f;

        this.clear();
        this.addPoint(new Vector2(cx - hw, cy + hh));
        this.addPoint(new Vector2(cx + hw, cy + hh));
        this.addPoint(new Vector2(cx + hw, cy - hh));
        this.addPoint(new Vector2(cx - hw, cy - hh));
    }

    /**
     * 获取盒子大小
     * @return (宽，高)的二维向量
     */
    public Vector2 getSize() {
        return new Vector2(w, h);
    }

    public Shape getShape() {
        float cx = center.x;
        float cy = center.y;
        float hw = w / 2f;
        float hh = h / 2f;
        Rectangle rect = new Rectangle(
                (int)(cx - hw), (int)(cy - hh), w, h
        );
        return (Shape)(rect);
    }

    @Override
    public void setCenter(Vector2 center) {
        super.setCenter(center);
    }

    @Override
    public String toString() {
        return "Box{" +
                "x=" + this.points.get(0).x +
                ",y=" +this.points.get(0).y +
                ",w=" + w +
                ", h=" + h +
                '}';
    }
}
