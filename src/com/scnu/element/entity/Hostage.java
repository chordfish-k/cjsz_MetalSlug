package com.scnu.element.entity;

import com.scnu.anim.AnimationClip;
import com.scnu.element.ElementObj;
import com.scnu.element.bullet.Bullet;
import com.scnu.element.component.*;
import com.scnu.geometry.Vector2;
import com.scnu.manager.ElementManager;
import com.scnu.manager.ElementType;
import com.scnu.manager.GameLoad;

import java.awt.*;
import java.util.ArrayList;

public class Hostage extends ElementObj {
    private ArrayList<ElementObj> playerList = null;
    private Transform foundPlayer = null;

    private int phase = 0;
    private int sightRange = 300; // 检测该范围内的玩家
    private int speed = 30;
    private Vector2 vel;

    // 组件
    Sprite sp = null;
    BoxCollider bc = null;
    RigidBody rb = null;


    public Hostage() {
        sp = (Sprite) addComponent("Sprite");
        bc = (BoxCollider) addComponent("BoxCollider", "shape:Rectangle,w:35,h:60,offX:-20,offY:-35");
        rb = (RigidBody) addComponent("RigidBody");

        sp.setSprite(GameLoad.imgMap.get("hostage0"));

        requireAnimations();
    }

    private void requireAnimations() {
        for (int i = 0; i < 5; i++) {
            GameLoad.aniMap.get("hostage_phase" + i).requireAnime(this);
        }
    }

    @Override
    public ElementObj create(String data) {
        playerList = (ArrayList<ElementObj>) ElementManager.getManager().getElementsByType(ElementType.PLAYER);

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

        if (other instanceof Bullet && phase <= 2) {
            Bullet b = (Bullet) other;
            ElementType atk = b.getElementType();
            if (atk == ElementType.P_BULLET) {
                // 如果是玩家的子弹
                phase = 3;
                // 清除碰撞器
                bc.setActive(false);
            }
        }
    }

    @Override
    public void onUpdate(long time) {
        super.onUpdate(time);

        vel = rb.getVelocity();

        checkOutside();
        ai(time);
        changeSprite(time);

        rb.setVelocity(vel);
    }

    private void checkOutside() {
        if (transform.getX() < 0 && phase == 4) {
            destroy();
        }
    }

    private void ai(long time) {
        if (phase == 0) {
            // 检测玩家
            int rangeMin = (int)transform.getX() - sightRange;
            int rangeMax = (int)transform.getX();

            if (playerList.size() == 0) return;

            Vector2 pos = playerList.get(0).transform.getPos();

            if (pos.x >= rangeMin && pos.y <= rangeMax) {
                // 发现玩家，进入phase1
                phase = 1;
                foundPlayer = playerList.get(0).transform;
            }
        }
        else if (phase == 1 || phase == 2) {
            int dis = (int)(transform.getX() - foundPlayer.getX());

            // 丢失目标
            if (Math.abs(dis) > sightRange) {
                phase = 0;
            }
        }
    }

    private void changeSprite(long time) {
        AnimationClip ac = GameLoad.aniMap.get("hostage_phase" + phase);
        sp.setSprite(ac.nextFrame(time, this));
        if (ac.getIndex(this) == ac.size() - 1) {
            if (phase == 1) {
                phase = 2;
            }
            else if (phase == 3) {
                phase = 4;
                vel.x = -speed;
                // 掉落道具
                dropGift();
            }
        }
    }

    private void dropGift() {
        int x = (int)transform.getX();
        int y = (int)transform.getY();
        ElementObj obj = GameLoad.createElementByName("gift", x+","+y);
        ElementManager.getManager().addElement(obj, ElementType.GIFT);
        ElementManager.eleRoot.addChild(obj);
    }
}
