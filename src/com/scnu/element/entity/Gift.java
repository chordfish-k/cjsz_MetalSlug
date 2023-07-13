package com.scnu.element.entity;

import com.scnu.element.ElementObj;
import com.scnu.element.component.BoxCollider;
import com.scnu.element.component.RigidBody;
import com.scnu.element.component.Sprite;
import com.scnu.geometry.Vector2;
import com.scnu.manager.GameLoad;

import java.awt.*;

public class Gift extends ElementObj {

    Sprite sp = null;
    BoxCollider bc = null;
    RigidBody rb = null;

    public Gift() {
        sp = (Sprite) addComponent("Sprite");
        bc = (BoxCollider) addComponent("BoxCollider");
        rb = (RigidBody) addComponent("RigidBody");

        sp.setSprite(GameLoad.imgMap.get("gift"));
        sp.setCenter(new Vector2(0.5f, 0.5f));
        bc.setSize(new Vector2(30, 30));
        bc.setTrigger(true);
    }

    @Override
    public ElementObj create(String data) {
        String[] split = data.split(",");
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);
        transform.setPos(new Vector2(x, y));
        return this;
    }

    @Override
    public void onDraw(Graphics g) {
        super.onDraw(g);
        sp.draw(g);
    }

    @Override
    public void onCollision(ElementObj other) {
        super.onCollision(other);

        if (other instanceof Hero) {
            Hero hero = (Hero) other;
            hero.changeBulletType();

            destroy();
        }
    }
}
