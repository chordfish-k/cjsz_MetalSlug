package com.scnu.element.entity;

import com.scnu.controller.Direction;
import com.scnu.element.ElementObj;
import com.scnu.element.component.BoxCollider;
import com.scnu.element.component.HealthValue;
import com.scnu.element.component.RigidBody;
import com.scnu.element.component.Sprite;
import com.scnu.geometry.Vector2;
import com.scnu.manager.ElementManager;
import com.scnu.manager.ElementType;
import com.scnu.manager.GameLoad;

import java.awt.*;

public class Enemy extends ElementObj {

    private long localTime = 0;
    private int dt = 0;

    private Direction facing = Direction.LEFT;
    private Vector2 vel = Vector2.ZERO;
    private boolean isRunning = true;

    private int speed = 20;

    Sprite sp = null;
    BoxCollider bc = null;
    RigidBody rb = null;
    HealthValue hv = null;

    public Enemy() {
        sp = (Sprite) addComponent("Sprite");
        bc = (BoxCollider) addComponent("BoxCollider","shape:Rectangle,w:35,h:60,offX:-20,offY:-5");
        rb = (RigidBody) addComponent("RigidBody");
        hv = (HealthValue) addComponent("HealthValue");

        sp.setSprite(GameLoad.imgMap.get("enemy_standL000"));
        hv.setMaxHealth(2,true);
    }

    @Override
    public ElementObj create(String data) {
        String[] split = data.split(",");
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);
        facing = Direction.valueOf(split[2].toUpperCase());
        transform.setPos(new Vector2(x, y));
        return this;
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
        dt = (int)(time - localTime);

        vel = rb.getVelocity();

        changeSprite(time);

//        vel.x = -speed;

        rb.setVelocity(vel);

        localTime = time;
    }

    private void changeSprite(long time) {
        if (isRunning) {
            sp.setSprite(GameLoad.aniMap.get("enemy_runL00").nextFrame(time));
        }
        else {
            sp.setSprite(GameLoad.aniMap.get("enemy_standL00").nextFrame(time));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        int x = (int)transform.getX();
        int y = (int)transform.getY()+20;
        ElementObj obj = GameLoad.createElementByName("enemy_die", x+","+y+","+this.facing.name());
        ElementManager.eleRoot.addChild(obj);
        ElementManager.getManager().addElement(obj, ElementType.EFFECT);
    }
}
