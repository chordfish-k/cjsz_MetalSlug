package com.scnu.element.component;

import com.scnu.anim.SpriteImg;
import com.scnu.geometry.Vector2;

import javax.swing.*;
import java.awt.*;

public class Sprite extends ComponentBase{
    private SpriteImg sprite = null;

    public ImageIcon getSprite() {
        return sprite.getSprite();
    }

    public Sprite setSprite(SpriteImg sprite) {
        this.sprite = sprite;
        return this;
    }

    public int getWidth() {
        return this.sprite.getSprite().getIconWidth();
    }

    public int getHeight() {
        return this.sprite.getSprite().getIconHeight();
    }

    /**
     * 设置图片中心点，与图片显示的位置相关
     * @param center 2维向量，x,y分别代表图片左上角到图片中心与左上角左下角的长度比值
     *               以及左上角到图片中心与左上角右上角的长度比值
     */
    public Sprite setCenter(Vector2 center) {
        this.sprite.setCenter(center);
        return this;
    }

    public Vector2 getCenter() {
        return this.sprite.getCenter();
    }

    public Vector2 getOffset() {
        return new Vector2(
                getWidth() * getCenter().x,
                getWidth() * getCenter().y
        );
    }

    @Override
    public void onDraw(Graphics g) {
        //super.onDraw(g);
        g.setColor(Color.RED);
        Vector2 v = parent.calcAbsolutePos().add(getCenter());
        g.drawOval((int)v.x-1, (int)v.y-1, 2, 2);
    }
}
