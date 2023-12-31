package com.scnu.element.entity;

import com.scnu.anim.AnimationClip;
import com.scnu.anim.SpriteImg;
import com.scnu.controller.Direction;
import com.scnu.element.ElementObj;
import com.scnu.element.RootObj;
import com.scnu.element.bullet.Bullet;
import com.scnu.element.component.BoxCollider;
import com.scnu.element.component.RigidBody;
import com.scnu.geometry.Vector2;
import com.scnu.manager.ElementManager;
import com.scnu.manager.ElementType;
import com.scnu.manager.GameLoad;

import java.awt.event.KeyEvent;
import java.util.Map;

public class Hero extends ElementObj {
    private Map<String, SpriteImg> imgMap = null;
    private Map<String, AnimationClip> aniMap = null;

    private RootObj root = null;
    private RigidBody rootRb = null;

    private long localTime = 0;
    private int dt = 0;

    private float speed = 50;
    private boolean isMoving = false;
    private boolean isSquatting = false;
    private boolean isAttacking = false;
    private boolean isJumping = false;

    private int bulletType = 0;
    private long lastAttackTime = 0;

    private float gravity = 50f;
    private int floorY = 0;
    private float jumpSpeed = 130f;
    private float remainAbility = 15f;
    private long lastJumpCalcTime = 0;


    private Vector2 vel = new Vector2();

    private Direction facing = Direction.RIGHT;

    private Vector2[] dirOffset = {
            new Vector2(-20,-5), // 下半朝右站
            new Vector2(-30,-45), // 上半朝右站
            new Vector2(-20,-5), // 下半朝左站
            new Vector2(25, -45), // 上半朝左站

            new Vector2(-20,0), // 下半朝右蹲
            new Vector2(-30,-30), // 上半朝右蹲
            new Vector2(-20,0), // 下半朝左蹲
            new Vector2(25,-30), // 上半朝左蹲

            new Vector2(-30,-5), // 下半朝右跑
            new Vector2(-30,-45), // 上半朝右跑
            new Vector2(-20,-5), // 下半朝左跑
            new Vector2(20, -45), // 上半朝左跑
    };
    private Vector2[] bulletOffset = {
            new Vector2(-40, -25), //朝左站立
            new Vector2(40, -25), //朝右站立
            new Vector2(-40, -10), //朝左下蹲
            new Vector2(40, -10), //朝右下蹲
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
        this.floorY = y;
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

        jump(time);
        move(time);

        attack(time);
        adjustChildPos();
        changeSprite(time);

        localTime = time;
    }

    private void attack(long time) {
        isAttacking = keyOn[KeyEvent.VK_J];
        if (isAttacking) {
            int dir = facing == Direction.RIGHT ? 1 : 0;
            int timeSpan = aniMap.get("attack" + bulletType + dir).getTotalTime();
            if (time > lastAttackTime + timeSpan) {
                lastAttackTime = time;

//                // 正在跳跃，不攻击
//                if (isJumping)
//                    return;

                int x = (int)transform.getX();
                int y = (int)transform.getY();
                int i = dir;
                if (isSquatting)
                    i += 2;
                Vector2 offset = bulletOffset[i];
                x += offset.x;
                y += offset.y;

                Bullet b =(Bullet) new Bullet().create("x:" + x + ",y:" + y + ",f:" + facing.name()+",by:player");
                ElementManager.getManager().addElement(b, ElementType.BULLET);
            }
        }
    }

    private void jump(long time) {
        if (!isJumping && keyOn[KeyEvent.VK_K]) {
            isJumping = true;
            vel.y = -jumpSpeed;

            int dir = facing == Direction.RIGHT ? 1 : 0;
            // 重置动画
            aniMap.get("jump_leg"+dir).reset();
            aniMap.get("jump_body"+bulletType+dir).reset();
        }
        if (isJumping) {
            int accelY = (int)(gravity * dt * 0.1f);
            if (vel.y < 0) {
                if (keyOn[KeyEvent.VK_K]) {
                    // 长按跳跃键滞空能力提升
                    vel.y -= remainAbility * dt * 0.1;
                }
            }
            vel.y += accelY;

            if (transform.getY() > floorY) {
                isJumping = false;
                this.transform.setY(floorY);
                vel.y = 0;
            }
        }
    }

    private void adjustChildPos() {
        if (facing == Direction.RIGHT) {
            if(!isSquatting) {
                if (isMoving && !isJumping) {
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

        if (isJumping) {
            heroDown.sp.setSprite(aniMap.get("jump_leg"+dir).nextFrame(time));
            heroUp.sp.setSprite(aniMap.get("jump_body"+bulletType+dir).nextFrame(time));

        }
        else {
            if (!isMoving) {
                heroDown.sp.setSprite(imgMap.get(squat + "stand"+dir));
            }
            else {

                heroDown.sp.setSprite(aniMap.get(squat + "run"+dir).nextFrame(time));
            }
            if(!isAttacking)
                heroUp.sp.setSprite(imgMap.get("attack" + bulletType + "0" + dir));
        }
        if (isAttacking) {
            heroUp.sp.setSprite(aniMap.get("attack" + bulletType + dir).nextFrame(time));
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
