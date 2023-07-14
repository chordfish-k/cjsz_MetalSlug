package com.scnu.element.map;

import com.scnu.element.ElementObj;
import com.scnu.element.component.Sprite;
import com.scnu.element.entity.Hero;
import com.scnu.game.Game;
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
    }

    @Override
    public ElementObj create(String data) {
        int levelNum = Game.getInstance().getGameLevel();
        sp.setSprite(GameLoad.imgMap.get("background"+levelNum));
        int h = GameJFrame.SIZE_H;
        int w = sp.getWidth() * h / sp.getHeight();
        Game.getInstance().setMapSize(new Vector2(w, h));
        return this;
    }

    @Override
    public void onDraw(Graphics g) {
        Vector2 pos = calcAbsolutePos();
        Vector2 size = Game.getInstance().getMapSize();
        g.drawImage(sp.getSprite().getImage(), (int)pos.x, 0, (int)size.x, (int)size.y, null);
    }

    @Override
    public void onUpdate(long time) {
        super.onUpdate(time);
        float dt = (int)(time - localTime) * 0.05f;

        localTime = time;
    }
}
