package com.scnu.element.component;

import com.scnu.geometry.Vector2;

import javax.swing.*;

public class Sprite extends ComponentBase{
    private ImageIcon sprite = null;
    private Vector2 center = new Vector2(); // 默认(0, 0)

    public ImageIcon getSprite() {
        return sprite;
    }

    public Sprite setSprite(ImageIcon sprite) {
        this.sprite = sprite;
        return this;
    }

    public int getWidth() {
        return this.sprite.getIconWidth();
    }

    public int getHeight() {
        return this.sprite.getIconHeight();
    }

    /**
     * 设置图片中心点，与图片显示的位置相关
     * @param center 2维向量，x,y分别代表图片左上角到图片中心与左上角左下角的长度比值
     *               以及左上角到图片中心与左上角右上角的长度比值
     */
    public Sprite setCenter(Vector2 center) {
        this.center = center;
        return this;
    }

    public Vector2 getCenter() {
        return center;
    }

}
