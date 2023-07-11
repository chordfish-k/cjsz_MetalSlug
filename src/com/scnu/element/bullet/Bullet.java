package com.scnu.element.bullet;

import com.scnu.controller.Direction;
import com.scnu.element.ElementState;
import com.scnu.element.ElementObj;
import com.scnu.element.component.BoxCollider;
import com.scnu.element.component.HealthValue;
import com.scnu.element.component.RigidBody;
import com.scnu.element.component.Sprite;
import com.scnu.manager.ElementManager;
import com.scnu.manager.GameLoad;
import com.scnu.show.GameJFrame;
import com.scnu.geometry.Vector2;

import java.awt.*;

/**
 * 子弹类
 */
public class Bullet extends ElementObj {
    private int damage; // 攻击力
    private int speed; // 移速
    private Direction facing; //朝向
    private int radius = 10; // 子弹半径
    private boolean shouldDie = false;
    private String by = "";

    BoxCollider col = null;
    RigidBody rb = null;
    Sprite sp = null;

    public Bullet() {
        col = (BoxCollider) addComponent(
                "BoxCollider",
                "shape:Rectangle,offX:10,w:"+2 * radius +",h:"+2 * radius);
        col.setTrigger(true);
        rb = (RigidBody) addComponent("RigidBody");
        sp = (Sprite) addComponent("Sprite");
    }

    @Override
    public ElementObj create(String data) {
        // 解析数据
        String[] split = data.split(",");
        float x = 0f;
        float y = 0f;

        for (String piece : split) {
            String[] kv = piece.split(":");

            switch (kv[0]) {
                case "x":
                    x = Float.parseFloat(kv[1]);
                    break;
                case "y":
                    y = Float.parseFloat(kv[1]);
                    break;
                case "f":
                    this.facing = Direction.valueOf(kv[1]);
                    break;
                case "by":
                    this.by = kv[1];
                    break;
            }
        }

        this.setW(10);
        this.setH(10);
        this.transform.setPos(new Vector2(x, y));
        this.speed = 50;
        this.damage = 1;
        this.radius = 5;

        int dir = this.facing == Direction.LEFT ? 0 : 1;
        sp.setSprite(GameLoad.imgMap.get("bullet0"+dir));

        ElementManager.eleRoot.addChild(this);
        return this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 设置rb的速度
        Vector2 spd = new Vector2();
        switch (this.facing) {
            case UP: spd.y = -speed; break;
            case DOWN: spd.y = speed; break;
            case LEFT: spd.x = -speed; break;
            case RIGHT: spd.x = speed; break;
        }
        this.rb.setVelocity(spd);
    }

    @Override
    public void onDraw(Graphics g) {
        super.onDraw(g);
        Vector2 pos = calcAbsolutePos();
        g.drawImage(
                sp.getSprite().getImage(),
                (int)(pos.x - sp.getWidth() * sp.getCenter().x),
                (int)(pos.y - sp.getWidth() * sp.getCenter().y),
                sp.getWidth(), sp.getHeight(), null);
    }

    @Override
    public void onUpdate(long time) {
        super.onUpdate(time);
        checkDead();
        if (shouldDie) destroy();
    }


    private void checkDead() {
        if (this.getElementState() == ElementState.DIED)
            return;
        Vector2 pos = calcAbsolutePos();
        float x = pos.x;
        float y = pos.y;
        if (x < 0 || y < 0 || x > GameJFrame.SIZE_W || y > GameJFrame.SIZE_H) {
            destroy();
        }
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onCollision(ElementObj other) {
        // 检测是不是自己人
        if (by.equals(other.getElementType().name())) {
            return;
        }
        // 被子弹打到的元素，如果有HealthValue组件，则扣血
        HealthValue hv = (HealthValue) other.getComponent("HealthValue");
        if (hv != null) {
            hv.damageBy(getDamage());
        }
        destroy();
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
