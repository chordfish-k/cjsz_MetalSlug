package com.scnu.element.bullet;

import com.scnu.controller.Direction;
import com.scnu.element.ElementState;
import com.scnu.element.ElementObj;
import com.scnu.element.component.BoxCollider;
import com.scnu.element.component.HealthValue;
import com.scnu.element.component.RigidBody;
import com.scnu.element.component.Sprite;
import com.scnu.manager.ElementManager;
import com.scnu.manager.ElementType;
import com.scnu.manager.GameLoad;
import com.scnu.show.GameJFrame;
import com.scnu.geometry.Vector2;

import java.awt.*;

/**
 * 子弹类
 */
public class Bullet extends ElementObj {
    private long localTime = 0;
    private float dt = 0f;

    private int damage; // 攻击力
    private int speed; // 移速
    private Direction facing; //朝向
    private int radius = 10; // 子弹半径
    private boolean shouldDie = false;
    private String by = "";
    private int type = 0; //0普通子弹，1榴弹

    BoxCollider bc = null;
    RigidBody rb = null;
    Sprite sp = null;

    public Bullet() {
        bc = (BoxCollider) addComponent(
                "BoxCollider");
        bc.setTrigger(true);
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
                case "speed":
                    this.speed = Integer.parseInt(kv[1]);
                    break;
                case "type":
                    this.type = Integer.parseInt(kv[1]);
                    break;
            }
        }

        this.transform.setPos(new Vector2(x, y));
        this.speed = 50;
        this.radius = 5;


        int dir = this.facing == Direction.LEFT ? 0 : 1;

        sp.setSprite(GameLoad.imgMap.get("bullet"+type+dir));
        if (type == 0) {
            bc.setSize(new Vector2(2*radius, 2*radius));
            this.damage = 1;
        }
        else if (type == 1) {
            bc.setSize(new Vector2(80, 20));
            this.damage = 4;
        }

        ElementManager.eleRoot.addChild(this);
        return this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 设置rb的速度
        Vector2 spd = new Vector2();
        switch (this.facing) {
            case LEFT: spd.x = -speed; break;
            case RIGHT: spd.x = speed; break;
        }

//        if (type == 1) {
//            this.transform.setRotate(-45 * Direction.getHorizontalSign(facing));
//            spd.y = -speed;
//            rb.setForce(new Vector2(0, 20));
//        }

        this.rb.setVelocity(spd);
    }

    @Override
    public void onDraw(Graphics g) {
        super.onDraw(g);
        sp.draw(g);
    }

    @Override
    public void onUpdate(long time) {
        super.onUpdate(time);

        if (localTime == 0) localTime = time;
        dt = (time - localTime) * 0.1f;

        checkDead();
        changeAngle(dt);

        if (shouldDie) destroy();

        localTime = time;
    }

    private void changeAngle(float dt) {
//        if (type != 1) return;
//        this.transform.setRotate(this.transform.getRotate() + 20 * dt * Direction.getHorizontalSign(facing));

    }


    private void checkDead() {
        if (this.getElementState() == ElementState.DIED)
            return;
        Vector2 pos = calcAbsolutePos();
        float x = pos.x;
        float y = pos.y;
        if (x < 0 || y < 0 || x > GameJFrame.SIZE_W || y > GameJFrame.SIZE_H - 80) {
            destroy();
        }
    }

    @Override
    public void onDestroy() {
        if (type == 1) {
            // 产生爆炸
            Vector2 v = transform.getPos();
            int x = (int) v.x;
            int y = 550;
            ElementObj obj = GameLoad.createElementByName("boom", x+","+y);
            ElementManager.getManager().addElement(obj, ElementType.BOOM);
            ElementManager.eleRoot.addChild(obj);
        }
    }

    @Override
    public void onCollision(ElementObj other) {
        // 检测是不是自己人
        if (other.getElementType() == getAttackerType()) {
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

    public ElementType getAttackerType() {
        return ElementType.valueOf(this.by.toUpperCase());
    }

}
