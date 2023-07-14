package com.scnu.element;

import com.scnu.element.component.RigidBody;
import com.scnu.element.component.Transform;
import com.scnu.element.entity.Hero;
import com.scnu.game.Game;
import com.scnu.manager.ElementManager;
import com.scnu.manager.ElementType;
import com.scnu.manager.GameLoad;
import com.scnu.show.GameJFrame;

import java.awt.*;

/**
 * 元素的根节点，单例
 */
public class RootObj extends ElementObj{

    RigidBody rb = null;

    private Hero hero = null;
    private Transform heroTr = null;
    private float lastHeroX = 0f;
    private RigidBody heroRb = null;
    private int xDis = 0;
    private int halfW = 0;
    private final int halfR = 50;

    public RootObj create(String data) {
        return this;
    }

    public RootObj() {
        halfW = (int)(GameJFrame.SIZE_W * 0.5f);
        rb = (RigidBody) addComponent("RigidBody");
    }

    @Override
    public void onUpdate(long time) {
        super.onUpdate(time);

        if (heroTr == null) {
            hero = (Hero) ElementManager.getManager().getElementsByType(ElementType.PLAYER).get(0);
            heroTr = (Transform) hero.getComponent("Transform");
            heroRb = (RigidBody) hero.getComponent("RigidBody");

        }

        // 在halfR的范围内镜头跟随玩家
        int heroX = (int)hero.calcAbsolutePos().x;
        int rootX = (int)transform.getX();

        if (lastHeroX == 0) {
            lastHeroX = heroX;
            return;
        }

        int dx = (int)(heroX - lastHeroX);

        int nextX = heroX + dx;
        if ((nextX > halfW + halfR && dx > 0|| nextX < halfW - halfR && dx < 0)
            && ((rootX - dx + (int) Game.getInstance().getMapSize().x) >=  GameJFrame.SIZE_W
                && rootX-dx <= 0)) {
            // 跟随
            transform.setX(rootX - dx);
        }
        else {
            lastHeroX = heroX;
        }
    }

//    public Rectangle getCameraRange() {
//        return new Rectangle();
//    }
}
