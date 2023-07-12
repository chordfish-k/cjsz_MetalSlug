package com.scnu.element.entity;

import com.scnu.anim.AnimationClip;
import com.scnu.controller.Direction;
import com.scnu.element.ElementObj;
import com.scnu.element.bullet.Bullet;
import com.scnu.element.component.*;
import com.scnu.geometry.Vector2;
import com.scnu.manager.ElementManager;
import com.scnu.manager.ElementType;
import com.scnu.manager.GameLoad;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Enemy extends ElementObj {

    private ArrayList<ElementObj> playerList = null;
    private Random ran = null;

    private long localTime = 0;
    private int dt = 0;

    private Direction facing = Direction.LEFT;
    private Vector2 vel = Vector2.ZERO;

    private int sightRange = 300; // 检测该范围内的玩家
    private int walkRange = 50; // 在这个范围内巡逻

    // 巡逻参数
    private int originalX = 0;
    private long lastRanTime = 0;
    private int ranSpan = 50;
    private int switchDelay = 64; // 从巡逻切换至攻击状态的延迟
    private int switchCounter = 0;

    // 攻击参数
    private long lastAtkTime = 0;
    private int atkSpan = 80;

    // 状态
    private boolean isRunning = false;
    private boolean isAttacking = false;

    private Transform attackTarget = null;

    private int speed = 20;

    // 组件
    Sprite sp = null;
    BoxCollider bc = null;
    RigidBody rb = null;
    HealthValue hv = null;


    public Enemy() {
        ran = new Random();

        sp = (Sprite) addComponent("Sprite");
        bc = (BoxCollider) addComponent("BoxCollider","shape:Rectangle,w:35,h:60,offX:-20,offY:-35");
        rb = (RigidBody) addComponent("RigidBody");
        hv = (HealthValue) addComponent("HealthValue");

        sp.setSprite(GameLoad.imgMap.get("enemy_standL000"));
        hv.setMaxHealth(2,true);

        requireAnimations();
    }

    private void requireAnimations() {
        String[] stateList = {"run", "stand", "attack"};
        for (int i=0; i<stateList.length; i++) {
            GameLoad.aniMap.get("enemy_" + stateList[i] + "L00").requireAnime(this);
            GameLoad.aniMap.get("enemy_" + stateList[i] + "L01").requireAnime(this);
            GameLoad.aniMap.get("enemy_" + stateList[i] + "L10").requireAnime(this);
            GameLoad.aniMap.get("enemy_" + stateList[i] + "L11").requireAnime(this);
        }

    }

    @Override
    public ElementObj create(String data) {
        playerList = (ArrayList<ElementObj>) ElementManager.getManager().getElementsByType(ElementType.PLAYER);

        String[] split = data.split(",");
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);
        facing = Direction.valueOf(split[2].toUpperCase());
        transform.setPos(new Vector2(x, y));
        originalX = x;
        return this;
    }

    @Override
    public void onDraw(Graphics g) {
        super.onDraw(g);
        sp.draw(g);

        g.setColor(Color.RED);
        Vector2 p = calcAbsolutePos().clone();
        Vector2 t = p.clone();
        if (this.facing == Direction.LEFT)
            p.x -= sightRange;
        else
            p.x += sightRange;
        g.drawLine((int)t.x, (int)t.y, (int)p.x, (int)p.y);
    }

    @Override
    public void onUpdate(long time) {
        super.onUpdate(time);
        dt = (int)(time - localTime);

        vel = rb.getVelocity();

        ai(time);
        changeSprite(time);

        rb.setVelocity(vel);

        localTime = time;
    }

    private void changeSprite(long time) {
        int dir = this.facing == Direction.LEFT ? 0 : 1;
        if (isRunning) {
            sp.setSprite(GameLoad.aniMap.get("enemy_runL1"+dir).nextFrame(time, this));
        }
        else {
            if (isAttacking) {
                AnimationClip atkAc = GameLoad.aniMap.get("enemy_attackL1"+dir);
                sp.setSprite(atkAc.nextFrame(time, this));
                if (atkAc.getIndex(this) == 2) {
                    oneShoot();
                }
            }
            else {
                if (attackTarget == null) {
                    sp.setSprite(GameLoad.aniMap.get("enemy_standL1"+dir).nextFrame(time, this));
                }
                else {
                    sp.setSprite(GameLoad.imgMap.get("enemy_attackL10"+dir));
                }
            }
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

    /**
     * 敌人AI
     * 通过设定的范围检测玩家后，朝玩家射击
     * @param time
     */
    private void ai(long time) {
        Vector2 ePos = transform.getPos();
        if (attackTarget == null) {
            // 四处巡逻
            if (time - lastRanTime > ranSpan) {
                lastRanTime = time;

                int r = ran.nextInt(100);
                // 移动
                if (r < 10) { // 转向
                    this.facing = this.facing == Direction.RIGHT ? Direction.LEFT : Direction.RIGHT;
                    vel.x = -vel.x;
                }
                else if (r < 60){
                    if (ePos.x > originalX - walkRange && this.facing == Direction.LEFT ||
                            ePos.x < originalX + walkRange && this.facing == Direction.RIGHT) {
                        isRunning = true;
                        vel.x = speed * (this.facing == Direction.LEFT ? -1 : 1);
                    }
                    else {
                        isRunning = false;
                        vel.x = 0;
                        this.facing = this.facing == Direction.RIGHT ? Direction.LEFT : Direction.RIGHT;
                    }

                }
                else {
                    isRunning = false;
                    vel.x = 0;
                }
            }

            // 检测玩家
            int rangeMin = (int)transform.getX();
            int rangeMax = (int)transform.getX();

            if (this.facing == Direction.LEFT) {
                rangeMin -= sightRange;
            } else {
                rangeMax += sightRange;
            }

            if (playerList.size() == 0) return;

            Vector2 pos = playerList.get(0).transform.getPos();

            if (pos.x >= rangeMin && pos.y <= rangeMax) {
                // 发现玩家，并将玩家设为目标
                attackTarget = playerList.get(0).transform;
                switchCounter = 0;

            }
        }
        else {

            if (switchCounter < switchDelay) {
                switchCounter += dt;
            }
            else {
                // 根据自身与玩家的位置，进行攻击
                int dis = (int)(ePos.x - attackTarget.getX());

                // 丢失目标
                if (Math.abs(dis) > sightRange) {
                    attackTarget = null;
                }
                else {
                    boolean isOnLeft = dis > 0;
                    this.facing = isOnLeft ? Direction.LEFT : Direction.RIGHT;

                    if (time - lastAtkTime > atkSpan) {
                        lastAtkTime = time;
                        vel.x = 0; // 站立射击
                        isRunning = false;
                        isAttacking = true;

                        int dir = this.facing == Direction.LEFT ? 0 : 1;
                        GameLoad.aniMap.get("enemy_attackL1"+dir).reset(this);
                    }
                }
            }



        }

    }

    private void oneShoot() {

        int x = (int)transform.getX() + (this.facing == Direction.LEFT ? -100 : 100);
        int y = (int)transform.getY() - 40;
        Bullet b =(Bullet) new Bullet().create("x:" + x + ",y:" + y + ",f:" + facing.name()+",by:ENEMY,speed:30");
        ElementManager.getManager().addElement(b, ElementType.E_BULLET);
        isAttacking = false;
    }
}
