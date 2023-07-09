package com.tedu.element;

import com.tedu.controller.Direction;
import com.tedu.element.component.RigidBody;
import com.tedu.element.component.Transform;
import com.tedu.element.entity.Hero;
import com.tedu.geometry.Vector2;
import com.tedu.manager.ElementManager;
import com.tedu.manager.ElementType;
import com.tedu.show.GameJFrame;

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

        if (heroX > halfW + halfR || heroX < halfW - halfR) {
            // 跟随
            int dx = (int)(heroX - lastHeroX);
            transform.setX(transform.getX() - dx);
        }
        else {
            lastHeroX = heroX;
        }
    }
}
