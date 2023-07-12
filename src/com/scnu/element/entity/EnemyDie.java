package com.scnu.element.entity;

import com.scnu.anim.AnimationClip;
import com.scnu.controller.Direction;
import com.scnu.element.ElementObj;
import com.scnu.element.component.Sprite;
import com.scnu.geometry.Vector2;
import com.scnu.manager.GameLoad;

import java.awt.*;

public class EnemyDie extends ElementObj {

    private Direction facing = Direction.LEFT;

    Sprite sp = null;

    public EnemyDie() {
        sp = (Sprite) addComponent("Sprite");
        sp.setSprite(GameLoad.imgMap.get("enemy_standL000"));

        GameLoad.aniMap.get("enemy_dieL00").requireAnime(this);
        GameLoad.aniMap.get("enemy_dieL01").requireAnime(this);
    }

    @Override
    public ElementObj create(String data) {
        String[] split = data.split(",");
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);
        facing = Direction.valueOf(split[2].toUpperCase());
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

        changeSprite(time);
    }

    private void changeSprite(long time) {
        int dir = this.facing == Direction.LEFT ? 0 : 1;

        AnimationClip ac = GameLoad.aniMap.get("enemy_dieL0"+dir);
        sp.setSprite(ac.nextFrame(time, this));
        if (ac.getIndex(this) == ac.size() - 1) {
            destroy();
        }
    }
}
