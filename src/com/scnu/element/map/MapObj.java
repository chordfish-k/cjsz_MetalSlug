package com.scnu.element.map;

import com.scnu.element.ElementObj;
import com.scnu.element.component.BoxCollider;
import com.scnu.element.component.HealthValue;
import com.scnu.element.component.Sprite;
import com.scnu.geometry.Vector2;

import javax.swing.*;
import java.awt.*;

public class MapObj extends ElementObj {
    HealthValue hv = null;
    Sprite sp = null;
    BoxCollider col = null;

    public MapObj() {
        hv = (HealthValue) addComponent("HealthValue");
        sp = (Sprite) addComponent("Sprite");
        col = (BoxCollider) addComponent("BoxCollider", "shape:Rectangle,offX:10,offY:10,w:20,h:20");
    }

    @Override
    public void onDraw(Graphics g) {
        if (sp == null || sp.getSprite() == null)
            return;
        Vector2 center = sp.getCenter();
        g.drawImage(sp.getSprite().getImage(),
                Math.round(transform.getX() - sp.getWidth() * transform.getScaleX() * center.x),
                Math.round(transform.getY() - sp.getWidth() * transform.getScaleX() * center.y),
                getW(), getH(), null);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onCollision(ElementObj other) {
        super.onCollision(other);
    }

    @Override
    public ElementObj create(String data) {
        hv.setMaxHealth(1, true);

        String[] arr = data.split(",");

        ImageIcon sprite = null;

        switch (arr[0]) {
            case "GRASS":
                sprite = new ImageIcon("image/wall/grass.png");
                break;
            case "BRICK":
                sprite = new ImageIcon("image/wall/brick.png");
                break;
            case "RIVER":
                sprite = new ImageIcon("image/wall/river.png");
                hv.setDamageable(false);
                break;
            case "IRON":
                sprite = new ImageIcon("image/wall/iron.png");
                hv.setMaxHealth(2, true);
                break;
        }
        int x = Integer.parseInt(arr[1]);
        int y = Integer.parseInt(arr[2]);
        assert sprite != null;
        int w = sprite.getIconWidth();
        int h = sprite.getIconHeight();
        this.setH(h);
        this.setW(w);
        this.transform.setPos(new Vector2(x, y));
        sp.setSprite(sprite);
        return this;
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
