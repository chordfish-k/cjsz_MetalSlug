package com.scnu.geometry;

import java.awt.*;

public class Vector2{
    public static final Vector2 ZERO = new Vector2();

    public float x;
    public float y;

    public Vector2() {
        this(0, 0);
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public Point toPoint() {
        return new Point((int)this.x, (int)this.y);
    }

    public boolean equal(Vector2 b) {
        return x == b.x && y == b.y;
    }

    public Vector2 add(Vector2 b) {
        return new Vector2(x + b.x, y + b.y);
    }

    public Vector2 sub(Vector2 b) {
        return new Vector2(x - b.x, y - b.y);
    }

    public Vector2 mul(float k) {
        return new Vector2(x * k, y * k );
    }

    public Vector2 div(float k) {
        return new Vector2(x / k, y / k );
    }

    public float dotProduct(Vector2 b) {
        return x * b.x + y * b.y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2 vec) {
        set(vec.x, vec.y);
    }

    /**
     * 求两个向量的距离
     * @param b 另一个向量
     * @return 两个向量的距离
     */
    public float distanceTo(Vector2 b) {
        return (float) Math.sqrt(Math.pow(x - b.x, 2) + Math.pow(y - b.y, 2));
    }

    /**
     * 向量到原点的距离
     * @return 向量的模
     */
    public float norm() {
        return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    /**
     * 求两个向量距离的平方
     * @param b 另一个向量
     * @return 两个向量距离的平方
     */
    public float distanceRawTo(Vector2 b) {
        return (float) Math.pow(x - b.x, 2) + (float) Math.pow(y - b.y, 2);
    }

    /**
     * 向量单位化
     * @return 单位化后的新向量
     */
    public Vector2 normalize() {
        return this.div(this.norm());
    }

    /**
     * 向量在另一个向量上的投影
     * @return 所求投影
     */
    public Vector2 projectOn(Vector2 v) {
        float d2 = this.distanceRawTo(Vector2.ZERO);
        if (d2 < Float.MIN_VALUE) {
            return new Vector2();
        }
        float k = v.dotProduct(this) / this.distanceRawTo(Vector2.ZERO);
        System.out.println("k:"+k);
        return v.mul(k);
    }

    /**
     * 求一条垂直于该向量的向量
     * @return 所求向量
     */
    public Vector2 perpendicular() {
        return new Vector2(y, -x);
    }


    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public Vector2 clone() {
        return new Vector2(x, y);
    }
}

