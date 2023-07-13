package com.scnu.element.entity;

import com.scnu.anim.AnimationClip;
import com.scnu.element.ElementObj;
import com.scnu.element.component.BoxCollider;
import com.scnu.element.component.HealthValue;
import com.scnu.element.component.RigidBody;
import com.scnu.element.component.Sprite;
import com.scnu.geometry.Vector2;
import com.scnu.manager.ElementType;
import com.scnu.manager.GameLoad;

import java.awt.*;

public class Boom extends ElementObj {

    private int damage = 4;

    Sprite sp = null;

    public Boom() {
        sp = (Sprite) addComponent("Sprite");
        sp.setSprite(GameLoad.imgMap.get("boom0"));
        GameLoad.aniMap.get("boom").requireAnime(this);
    }

    @Override
    public ElementObj create(String data) {
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
    public void onUpdate(long time) {
        super.onUpdate(time);

        AnimationClip ac = GameLoad.aniMap.get("boom");

        if (ac.getIndex(this) == ac.size()-1) {
            destroy();
        }

        sp.setSprite(ac.nextFrame(time, this));
    }
}
