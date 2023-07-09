package com.scnu.element.component;

import com.scnu.element.ElementObj;
import com.scnu.element.map.MapObj;
import com.scnu.geometry.Vector2;

import java.awt.event.KeyEvent;

public class Jumpable extends ComponentBase{

    private boolean jumping = true;
    private RigidBody rb = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        rb = (RigidBody) parent.getComponent("RigidBody");
    }

    @Override
    public void onKeyPressed(int key) {
        System.out.println(key);
        if (key == KeyEvent.VK_J && !jumping) {
            jumping = true;
            if (rb == null) return;
            rb.setVelocity(new Vector2(rb.getVelocity().x, -70f));
        }
        super.onKeyPressed(key);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (jumping) {
            rb.setForce(new Vector2(0, 50f));
        }
    }

    @Override
    public void onCollision(ElementObj other) {
        super.onCollision(other);
        if (other instanceof MapObj) {
            jumping = false;
            rb.setForce(new Vector2(0, 0));
        }
    }
}
