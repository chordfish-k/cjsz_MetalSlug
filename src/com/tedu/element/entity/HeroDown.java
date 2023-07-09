package com.tedu.element.entity;

import com.tedu.anim.AnimationClip;
import com.tedu.element.ElementObj;
import com.tedu.element.component.BoxCollider;
import com.tedu.element.component.HealthValue;
import com.tedu.element.component.Sprite;
import com.tedu.geometry.Vector2;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class HeroDown extends ElementObj{
    private ElementManager em = null;

    HealthValue hv = null;
    Sprite sp = null;

    public HeroDown() {
        em = ElementManager.getManager();


        hv = (HealthValue) addComponent("HealthValue");
        sp = (Sprite) addComponent("Sprite");
        sp.setSprite(GameLoad.imgMap.get("stand1"));
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
                (int)pos.x,
                (int)pos.y,
                sp.getWidth(), sp.getHeight(), null);
    }
}
