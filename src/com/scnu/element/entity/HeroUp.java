package com.scnu.element.entity;

import com.scnu.anim.AnimationClip;
import com.scnu.element.ElementObj;
import com.scnu.element.component.HealthValue;
import com.scnu.element.component.Sprite;
import com.scnu.geometry.Vector2;
import com.scnu.manager.ElementManager;
import com.scnu.manager.GameLoad;

import java.awt.*;
import java.util.Map;

public class HeroUp extends ElementObj {
    private ElementManager em = null;
    private Map<String, AnimationClip> aniMap = null;

    HealthValue hv = null;
    Sprite sp = null;

    public HeroUp() {
        em = ElementManager.getManager();
        aniMap = GameLoad.aniMap;

        hv = (HealthValue) addComponent("HealthValue");
        sp = (Sprite) addComponent("Sprite");
        sp.setSprite(GameLoad.imgMap.get("attack001"));


    }

    @Override
    public ElementObj create(String data) {
        return this;
    }

    @Override
    public void onDraw(Graphics g) {
        super.onDraw(g);
        Vector2 pos = calcAbsolutePos();
//        if (getParent() != null) {
//            // 子元素的坐标以父元素的坐标为原点
//            pos = pos.add(getParent().transform.getPos());
//        }
        g.drawImage(
                sp.getSprite().getImage(),
                (int)(pos.x - sp.getOffset().x),
                (int)(pos.y - sp.getOffset().y),
                sp.getWidth(), sp.getHeight(), null);
    }
}
