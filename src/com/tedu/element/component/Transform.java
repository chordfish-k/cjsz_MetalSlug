package com.tedu.element.component;

import com.tedu.geometry.Vector2;

public class Transform extends ComponentBase{
    protected Vector2 pos = new Vector2();
    protected Vector2 scale = new Vector2(1, 1);
    protected float rotate = 0f; // 角度制

    private BoxCollider col = null;

    public Transform() {

    }

    public Transform create(String data) {

        return this;
    }

    public Vector2 getPos() {
        return pos;
    }

    public Transform setPos(Vector2 pos) {
        this.pos.x = pos.x;
        this.pos.y = pos.y;
        if (col != null) {
            col.setCenter(pos);
        }
        return this;
    }

    public float getX() {
        return this.pos.x;
    }

    public float getY() {
        return this.pos.y;
    }

    public Transform setX(float x) {
        return setPos(new Vector2(x, this.pos.y));
    }

    public Transform setY(float y) {
        return setPos(new Vector2(this.pos.x, y));
    }

    public float getScaleX() {
        return this.scale.x;
    }

    public float getScaleY() {
        return this.scale.y;
    }

    public Vector2 getScale() {
        return scale;
    }

    public Transform setScale(Vector2 scale) {
        this.scale.x = scale.x;
        this.scale.y = scale.y;
        return this;
    }

    public float getRotate() {
        return rotate;
    }

    public Transform setRotate(float rotate) {
        float oldRot = this.rotate;
        this.rotate = rotate;
        return this;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (col == null)
            col = (BoxCollider) parent.getComponent("BoxCollider");
            if (col != null)
                col.setCenter(pos);
    }
}
