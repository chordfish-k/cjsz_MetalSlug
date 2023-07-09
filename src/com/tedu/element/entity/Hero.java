package com.tedu.element.entity;

import com.tedu.anim.AnimationClip;
import com.tedu.controller.Direction;
import com.tedu.element.ElementObj;
import com.tedu.element.RootObj;
import com.tedu.element.component.BoxCollider;
import com.tedu.element.component.RigidBody;
import com.tedu.geometry.Vector2;
import com.tedu.manager.ElementManager;
import com.tedu.manager.ElementType;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class Hero extends ElementObj {
    private Map<String, ImageIcon> imgMap = null;
    private Map<String, AnimationClip> aniMap = null;

    private RootObj root = null;
    private RigidBody rootRb = null;

    private long localTime = 0;
    private int dt = 0;

//    private int spriteChangeSpan = 4;
//    private long lastSpriteChangeTime = 0;

    private float speed = 50;
    private boolean isMoving = false;
    private boolean isSquatting = false;


    private Vector2 vel = new Vector2();
    private Direction facing = Direction.RIGHT;
    private Vector2[] dirOffset = {
            new Vector2(-20,-5), // 下半朝右站
            new Vector2(-30,-45), // 上半朝右站
            new Vector2(-20,-5), // 下半朝左站
            new Vector2(-65, -45), // 上半朝左站

            new Vector2(-20,0), // 下半朝右蹲
            new Vector2(-30,-30), // 上半朝右蹲
            new Vector2(-20,0), // 下半朝左蹲
            new Vector2(-55,-30), // 上半朝左蹲

            new Vector2(-30,-5), // 下半朝右跑
            new Vector2(-30,-45), // 上半朝右跑
            new Vector2(-20,-5), // 下半朝左跑
            new Vector2(-55, -45), // 上半朝左跑
    };

    HeroUp heroUp = null;
    HeroDown heroDown = null;
    RigidBody rigidBody = null;
    BoxCollider boxCollider = null;

    boolean[] keyOn = new boolean[255];

    public Hero() {
        imgMap = GameLoad.imgMap;
        aniMap = GameLoad.aniMap;

        root = ElementManager.eleRoot;
        rootRb = (RigidBody) root.getComponent("RigidBody");


        heroDown = (HeroDown) addChild("heroDown");
        heroUp = (HeroUp) addChild("heroUp");

        heroUp.transform.setPos(new Vector2());

        boxCollider = (BoxCollider) addComponent("BoxCollider", "shape:Rectangle,w:30,h:55");//,offX:60,offY:40
        rigidBody = (RigidBody) addComponent("RigidBody");
    }

    public Hero create(String data) {
        String[] sp = data.split(",");
        int x = Integer.parseInt(sp[0]);
        int y = Integer.parseInt(sp[1]);
        this.transform.setPos(new Vector2(x, y));
        return this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ElementManager.getManager().addElement(heroUp, getElementType());
        ElementManager.getManager().addElement(heroDown, getElementType());
    }

    @Override
    public void onKeyPressed(int key) {
        super.onKeyPressed(key);
        this.keyOn[key] = true;
        if (key == KeyEvent.VK_D) {
            this.keyOn[KeyEvent.VK_A] = false;
        } else if (key == KeyEvent.VK_A) {
            this.keyOn[KeyEvent.VK_D] = false;
        }
    }

    @Override
    public void onKeyReleased(int key) {
        super.onKeyReleased(key);
        this.keyOn[key] = false;
    }

    @Override
    public void onUpdate(long time) {
        super.onUpdate(time);
        dt = (int)(time - localTime);

        move(time);
        adjustChildPos();
        changeSprite(time);

        localTime = time;
    }

    private void adjustChildPos() {
        if (facing == Direction.RIGHT) {
            if(!isSquatting) {
                if (isMoving) {
                    heroDown.transform.setPos(dirOffset[8]);
                    heroUp.transform.setPos(dirOffset[9]);
                }
                else {
                    heroDown.transform.setPos(dirOffset[0]);
                    heroUp.transform.setPos(dirOffset[1]);
                }

            }
            else {
                heroDown.transform.setPos(dirOffset[4]);
                heroUp.transform.setPos(dirOffset[5]);
            }
        }
        else if (facing == Direction.LEFT) {
            if(!isSquatting) {
                if (isMoving) {
                    heroDown.transform.setPos(dirOffset[10]);
                    heroUp.transform.setPos(dirOffset[11]);
                }
                else {
                    heroDown.transform.setPos(dirOffset[2]);
                    heroUp.transform.setPos(dirOffset[3]);
                }
            }
            else {
                heroDown.transform.setPos(dirOffset[6]);
                heroUp.transform.setPos(dirOffset[7]);
            }
        }
    }

    public void move(long time) {
        isMoving = true;
        isSquatting = keyOn[KeyEvent.VK_CONTROL];

        if (keyOn[KeyEvent.VK_D]) {
            vel.x = speed;
            this.facing = Direction.RIGHT;
        } else if (keyOn[KeyEvent.VK_A]) {
            vel.x = -speed;
            this.facing = Direction.LEFT;
        }
        else {
            vel.x = 0;
            isMoving = false;
        }

        rigidBody.setVelocity(vel);
    }

    public void changeSprite(long time) {
        int dir = facing == Direction.RIGHT ? 1 : 0;
        heroUp.sp.setSprite(imgMap.get("attack00"+dir));

        String squat = isSquatting ? "squat_" : "";

        if (!isMoving) {
            heroDown.sp.setSprite(imgMap.get(squat + "stand"+dir));
        }
        else {

            heroDown.sp.setSprite(aniMap.get(squat + "run"+dir).nextFrame(time));
        }
    }

    public Direction getFacing() {
        return facing;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }


}
