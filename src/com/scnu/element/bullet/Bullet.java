package com.scnu.element.bullet;

import com.scnu.controller.Direction;
import com.scnu.element.ElementState;
import com.scnu.element.ElementObj;
import com.scnu.element.component.BoxCollider;
import com.scnu.element.component.HealthValue;
import com.scnu.element.component.RigidBody;
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

    public Bullet() {
        col = (BoxCollider) addComponent(
                "BoxCollider",
                "shape:Rectangle,w:"+2 * radius +",h:"+2 * radius);
        col.setTrigger(true);
        rb = (RigidBody) addComponent("RigidBody");
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
        if (transform == null)
            return;

        g.setColor(Color.red);

        int x = Math.round(transform.getX()) - radius;
        int y = Math.round(transform.getY()) - radius;

        g.fillOval(x, y, 2 * radius, 2 * radius);
    }

    @Override
    public void onUpdate(long time) {
        super.onUpdate(time);
        checkDead();
        if (shouldDie) destroy();
    }


    private void checkDead() {
        if (transform == null || this.getElementState() != ElementState.DIED)
            return;
        float x = transform.getX();
        float y = transform.getY();
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
