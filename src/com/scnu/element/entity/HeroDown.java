package com.scnu.element.entity;

import com.scnu.element.ElementObj;
import com.scnu.element.component.HealthValue;
import com.scnu.element.component.Sprite;
import com.scnu.geometry.Vector2;
import com.scnu.manager.ElementManager;
import com.scnu.manager.GameLoad;

import java.awt.*;

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
        sp.draw(g);
    }
}
