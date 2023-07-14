package com.scnu.element.component;

import com.scnu.element.ElementObj;
import com.scnu.geometry.Box;
import com.scnu.geometry.Vector2;

import java.awt.*;


/**
 * 碰撞器组件
 * 默认是矩形碰撞箱
 * @author LSR
 */
public class BoxCollider extends ComponentBase{

    private Box shape = null;
    private float rotation = 0f;
    private Vector2 center = new Vector2();
    private Vector2 offset = new Vector2();
    private boolean isTrigger = false;

    public BoxCollider() {
        shape = new Box(new Vector2(0, 0), 10, 10);
    }

    @Override
    public ComponentBase create(String data) {
        String[] split = data.split(",");
        String shape = "Rectangle";
        float offX = 0f;
        float offY = 0f;
        int w = 10;
        int h = 10;
        for(String sp : split) {
            String[] kv = sp.split(":");

            switch (kv[0]) {
                case "offX":
                    offX = Float.parseFloat(kv[1]);
                    break;
                case "offY":
                    offY = Float.parseFloat(kv[1]);
                    break;
                case "w":
                    w = Integer.parseInt(kv[1]);
                    break;
                case "h":
                    h = Integer.parseInt(kv[1]);
                    break;
                case "shape":
                    shape = kv[1];
                    break;
            }
        }
        if (shape .equals("Rectangle")) {
            this.shape.setSize(new Vector2(w, h));
            this.setOffset(new Vector2(offX, offY));
        }

        return this;
    }

    public BoxCollider setSize(Vector2 size) {
        this.shape.setSize(new Vector2(size.x, size.y));
        return this;
    }

    public Vector2 getSize() {
        Vector2 size = this.shape.getSize();
        int h = (int)size.y;
        int w = (int)size.x;
        return new Vector2(w, h);
    }

    public Box getShape() {
        if (shape == null) return null;
        ElementObj p = parent;
        Vector2 vec = parent.calcAbsolutePos();
//        while (p != null) {
//            Vector2 tmp = p.transform.getPos().sub(new Vector2(0, 0));
//            vec = vec.add(tmp);
//            p = p.getParent();
//        }
        shape.setCenter(vec.add(offset));
        return shape;
    }

    /**
     * 设置碰撞形状的原点
     * @param center 原点坐标
     * @return 碰撞组件自身
     */
    public BoxCollider setCenter(Vector2 center) {

        ElementObj parent = getParent();
        Transform tr = parent.transform;
        Vector2 oldCenter = this.center.clone();
        this.center = center.clone();

        if (tr == null)
            return this;

        if (center.equal(oldCenter))
            return this;

        float dx = center.x - oldCenter.x;
        float dy = center.y - oldCenter.y;


        this.moveBy(new Vector2(dx, dy));

        return this;
    }

    public Vector2 getCenter() {
        return this.center;
    }

    public Vector2 getOffset() {
        return offset;
    }

    public boolean isTrigger() {
        return isTrigger;
    }

    public BoxCollider setTrigger(boolean trigger) {
        isTrigger = trigger;
        return this;
    }

    public float getRotation() {
        return rotation;
    }

    /**
     * 设置碰撞形状距离原点的偏移量
     * @param offset 偏移量
     * @return 碰撞组件自身
     */
    public BoxCollider setOffset(Vector2 offset) {
        this.offset = offset;
        //this.moveBy(offset);
        return this;
    }

    public BoxCollider moveBy(Vector2 vec) {
        Vector2 loc = this.getShape().getCenter();
        this.getShape().setCenter(new Vector2(loc.x + vec.x, loc.y + vec.y));
        return this;
    }

    public BoxCollider move(Vector2 pos) {
        return setCenter(pos);
    }

    @Override
    public void onDraw(Graphics g) {
        super.onDraw(g);
        g.setColor(Color.RED);
        Graphics2D g2d = (Graphics2D)g;
//        g2d.draw(getShape().getShape());

    }

    public boolean checkCollisionWith(BoxCollider colB) {

        return this.getShape().testPolygon(colB.getShape());
    }

    public void onCollision(ElementObj other) {
        parent.onCollision(other);
    }

//    @Override
//    public void onPhysicsUpdate() {
//        Transform tr = parent.transform;
//        if (tr != null) {
//            float nowAng = tr.getRotate();
//            float dr = nowAng - rotation;
//            this.shape.rotateBy(dr); // 将形状旋转
//            this.rotation = nowAng;
//        }
//        super.onPhysicsUpdate();
//    }
}
