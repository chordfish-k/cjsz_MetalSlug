package com.scnu.element.entity;

import com.scnu.anim.AnimationClip;
import com.scnu.element.ElementObj;
import com.scnu.element.component.*;
import com.scnu.geometry.Vector2;
import com.scnu.manager.ElementManager;
import com.scnu.manager.ElementType;
import com.scnu.manager.GameLoad;

import java.awt.*;
import java.util.ArrayList;

public class Boss extends ElementObj {
    private ArrayList<ElementObj> playerList = null;
    private Transform foundPlayer = null;

    private int sightRange = 400; // 检测该范围内的玩家
    private int speed = 30;

    private long lastSpawnTime = 0;
    private int spawnSpan = 80;
    private boolean isSpawning = false;

    Sprite sp = null;
    BoxCollider bc = null;
    RigidBody rb = null;
    HealthValue hv = null;

    public Boss() {
        sp = (Sprite) addComponent("Sprite");
        bc = (BoxCollider) addComponent("BoxCollider");
        rb = (RigidBody) addComponent("RigidBody");
        hv = (HealthValue) addComponent("HealthValue");

        hv.setMaxHealth(20, true);
        sp.setSprite(GameLoad.imgMap.get("boss0"));
        bc.setSize(new Vector2(60, 70));
        bc.setOffset(new Vector2(-10, -40));

        requireAnimations();
    }

    private void requireAnimations() {
        GameLoad.aniMap.get("boss").requireAnime(this);
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
    public void onDestroy() {
        super.onDestroy();
        // 爆炸特效
    }

    @Override
    public void onUpdate(long time) {
        super.onUpdate(time);

        ai(time);
        changeSprite(time);
    }

    private void changeSprite(long time) {
        AnimationClip ac = GameLoad.aniMap.get("boss");
        sp.setSprite(ac.nextFrame(time, this));
    }

    private void ai(long time) {
        // 检测玩家
        int rangeMin = (int)transform.getX() - sightRange;
        int rangeMax = (int)transform.getX();

        if (playerList.size() == 0) return;

        Vector2 pos = playerList.get(0).transform.getPos();

        if (pos.x >= rangeMin && pos.y <= rangeMax) {
            // 发现玩家，进入phase1
            foundPlayer = playerList.get(0).transform;

            isSpawning = true;
        }
        else {

        }

        if (isSpawning) {
            if (time > lastSpawnTime + spawnSpan) {
                spawnEnemy();
                lastSpawnTime = time;
            }
        }

    }

    private void spawnEnemy() {
        int x = (int)transform.getX();
        int y = 550;

        ElementObj enemy1 = GameLoad.createElementByName("enemy", (x-30) + ","+ y + ",left");
        ElementManager.getManager().addElement(enemy1, ElementType.ENEMY);
        ElementManager.eleRoot.addChild(enemy1);
    }
}
