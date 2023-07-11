package com.scnu.anim;

import com.scnu.element.component.Sprite;
import com.scnu.geometry.Vector2;

import javax.swing.*;

public class SpriteImg {
    private ImageIcon sprite = null;

    private Vector2 center = new Vector2(); // 默认(0, 0)

    public SpriteImg(ImageIcon sprite, Vector2 center) {
        this.sprite = sprite;
        this.center = center;
    }


    public ImageIcon getSprite() {
        return sprite;
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
    public void setCenter(Vector2 center) {
        this.center = center;
    }

    public Vector2 getCenter() {
        return center;
    }
}
