package com.tedu.element.map;

import com.tedu.element.ElementObj;
import com.tedu.element.component.BoxCollider;
import com.tedu.geometry.Vector2;

import java.awt.*;

public class BarrierObj extends ElementObj {

    private BoxCollider bc = null;

    public BarrierObj() {
        bc = (BoxCollider) addComponent("BoxCollider");
    }

    @Override
    public void onDraw(Graphics g) {

    }

    public BarrierObj setRect(Rectangle rect) {
        Vector2 pos = new Vector2(rect.x, rect.y);
        Vector2 size = new Vector2(rect.width, rect.height);
        bc.getShape().setSize(size);
        bc.setOffset(new Vector2(size.x * 0.5f, size.y * 0.5f));
        transform.setPos(pos);
        return this;
    }
}
