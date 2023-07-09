package com.scnu.element.map;

import com.scnu.element.ElementObj;
import com.scnu.element.component.Sprite;
import com.scnu.element.entity.Hero;
import com.scnu.geometry.Vector2;
import com.scnu.manager.GameLoad;
import com.scnu.show.GameJFrame;

import java.awt.*;

public class BackgroundObj extends ElementObj {

    private long localTime = 0;
    private Hero hero = null;

    Sprite sp = null;

    public BackgroundObj() {
        sp = (Sprite) addComponent("Sprite");
        sp.setSprite(GameLoad.imgMap.get("background"));
        sp.setCenter(new Vector2());

    }

    @Override
    public ElementObj create(String data) {
        System.out.println("yes");
        return this;
    }

    @Override
    public void onDraw(Graphics g) {
        Vector2 pos = calcAbsolutePos();

        int h = GameJFrame.SIZE_H;
        int w = sp.getWidth() * h / sp.getHeight();
        g.drawImage(sp.getSprite().getImage(), (int)pos.x, 0, w, h, null);
    }

    @Override
    public void onUpdate(long time) {
        super.onUpdate(time);
        float dt = (int)(time - localTime) * 0.05f;

        // 获取玩家
//        if (hero == null) {
//            hero = (Hero)ElementManager.getManager().getElementsByType(ElementType.PLAYER).get(0);
//        } else {
//            Transform pTr = hero.transform;
//            RigidBody pRb = (RigidBody) hero.getComponent("RigidBody");
//            float vx = pRb.getVelocity().x;
//
//            // 如果玩家坐标距离中心超过一定值
//            int remainWidthHalf = 50;
//            int halfW = GameJFrame.SIZE_W / 2;
//
//            if (pTr.getX() - GameJFrame.CAMERA_X - halfW > remainWidthHalf
//                    && hero.getFacing() == Direction.RIGHT
//                    ) {
//
//                GameJFrame.CAMERA_X += hero.getSpeed() * dt;
//                System.out.println(GameJFrame.CAMERA_X);
//            }
//        }

        localTime = time;
    }
}
