package com.scnu.element.component;

import com.scnu.element.ElementObj;
import com.scnu.geometry.Polygon;
import com.scnu.geometry.Vector2;
import com.scnu.manager.ElementManager;
import com.scnu.manager.ElementType;
import com.scnu.manager.GameLoad;

import java.util.ArrayList;
import java.util.List;

public class RigidBody extends ComponentBase{
    private long localTime = 0;
    public boolean active = true;

    private Vector2 velocity = new Vector2(); // 物体的速度
    protected float angVel = 0f; // 物体角速度
    private Vector2 force = new Vector2(); // 施加在这个物体中心上的力
    private float mess_rev = 1; // 质量的倒数

    private ElementManager em = null;

    // 相关组件
    private Transform tr = null;
    private BoxCollider bc = null;
    // f = m * a;
    // a = f / m;
    // v' = v + a * dt
    // x = x + (v' + v) * 0.5 * dt


    public RigidBody() {
        this.localTime = System.currentTimeMillis();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        em = ElementManager.getManager();
    }

    @Override
    public void onPhysicsUpdate() {
        super.onUpdate();

        long newTime = System.currentTimeMillis();
        float k = 0.01f;
        float delta = k * (int) (newTime - this.localTime) ;


        if (bc == null) {
            bc = (BoxCollider) parent.getComponent("BoxCollider");
        }


        if (tr != null) {
            Vector2 oldVel = null;
            Vector2 vel = null;

            if (!active)
                return;

            // 计算速度
            oldVel = velocity.clone();
            // 将中心的力加上多边形各顶点上的受力作为合力
            Vector2 jf = new Vector2();
            if (bc != null)
                jf = this.force.add(bc.getShape().calcJoinForce());
            //System.out.println(jf +  ", " + force);
            vel = velocity.add(jf.mul(mess_rev * delta));
            // 将速度应用与Transform

            Vector2 d = this.velocity.add(vel).mul(delta * 0.5f);
            if (Math.abs(vel.x) > Float.MIN_VALUE || Math.abs(vel.y) > Float.MIN_VALUE) {
                if (bc != null)
                    d = testLinear(d, delta);
                tr.setPos(tr.getPos().add(d));
            }

//            // 求角力
//            float af = bc.getShape().calcAngularForce();
//            float newAngVel = angVel + af * delta;
//            // 测试碰撞
//            float dAng = newAngVel - bc.getRotation();
//            dAng = testAngular(dAng, delta);
//            tr.setRotate(bc.getRotation() + dAng);

            this.velocity = vel;
        }
        else {
            tr = parent.transform;
        }

        this.localTime = System.currentTimeMillis();
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getForce() {
        return force;
    }

    public void setForce(Vector2 force) {
        this.force = force;
    }

    public RigidBody setMess(float mess) {
        mess_rev = 1f / mess;
        return this;
    }

    public float getMess() {
        return 1f / mess_rev;
    }


    private Vector2 testLinear(Vector2 d, float delta) {

        Vector2 dvel = d.clone();
        if (dvel.equal(new Vector2(0, 0))) {
            return d;
        }

        boolean flag = true;

        Polygon shape_ = null;

        while (flag) {
            shape_ = bc.getShape().clone();
            shape_.setCenter(shape_.getCenter().add(dvel));

            // TODO: 防止隧穿
//            float l = shape_.projectOn(d);
//            Vector2 nd = d.normalize();
//            System.out.println(l);
//            Vector2 tmp = nd.add(nd.mul(l));
//            System.out.println("tmp:"+tmp);

//            if (parent instanceof Bullet) shape_ = shape_.scaleToward(tmp);

            boolean innerFlag = true;

            if (em == null)
                continue;

            ElementType thisType = parent.getElementType();

            List<BoxCollider> collObj = new ArrayList<>();

            for (ElementType type : em.getGameElements().keySet()) {

                em.setLocked(true);

                List<ElementObj> list =  em.getElementsByType(type);

                for (int i = list.size() - 1; i >= 0; i--){
                    ElementObj obj = list.get(i);
                    if (obj == parent) {
                        continue;
                    }

                    BoxCollider boxCollider = (BoxCollider) obj.getComponent("BoxCollider");

                    ElementType otherType = obj.getElementType();

                    // 按配置文件考虑碰撞
                    if (!GameLoad.colMap.get(thisType).contains(otherType)) {
                        continue;
                    }

                    // 不考虑缺少碰撞器组件的
                    if (boxCollider == null) {
                        continue;
                    }

                    if (boxCollider.getShape().testPolygon(shape_)) {
                        innerFlag = false;
//                        System.out.println(obj.getClass().getName() + " coll");

                        // 调用碰撞器组件的onCollision()方法
//                        bc.onCollision(obj);
//                        boxCollider.onCollision(parent);

                        collObj.add(boxCollider);

                    }
                    //if (!innerFlag) break;
                }


                em.setLocked(false);

                //if (!innerFlag) break;
            }

            // 无任何碰撞
            if (innerFlag) {
                flag = false;
            } else {
                // 处理碰撞
                for (BoxCollider b : collObj) {
                    b.onCollision(parent);
                    bc.onCollision(b.parent);
                }
            }

            // 如果该碰撞器仅仅用作触发，则不处理位移
            if (bc.isTrigger()) {
                return dvel;
            }

            // 计算新的位置
            dvel = dvel.mul(0.5f);

            if (Math.abs(dvel.x) < Float.MIN_VALUE && Math.abs(dvel.y) < Float.MIN_VALUE) {
                flag = false;
            }

        }
        return dvel;
    }


    private float testAngular(float dAng, float delta) {
        float dvel = dAng;
        if (dAng < Float.MIN_VALUE) {
            return dvel;
        }

        boolean flag = true;

        Polygon shape_ = null;

        while (flag) {
            shape_ = bc.getShape().clone();
            shape_.rotateBy(dvel);

            boolean innerFlag = true;

            if (em == null)
                continue;

            ElementType thisType = parent.getElementType();

            for (ElementType type : em.getGameElements().keySet()) {

                em.setLocked(true);

                List<ElementObj> list =  em.getElementsByType(type);
                for (ElementObj obj : list){

                    if (obj == parent) {
                        continue;
                    }

                    BoxCollider boxCollider = (BoxCollider) obj.getComponent("BoxCollider");

                    ElementType otherType = obj.getElementType();

                    // 按配置文件考虑碰撞
                    if (!GameLoad.colMap.get(thisType).contains(otherType)) {
                        continue;
                    }

                    // 不考虑缺少碰撞器组件的
                    if (boxCollider == null) {
                        continue;
                    }

                    // 出现碰撞
                    if (boxCollider.getShape().testPolygon(shape_)) {
//                        if (!bc.isTrigger())
                        innerFlag = false;
//                        System.out.println(obj.getClass().getName() + " coll");

                        // 调用碰撞器组件的onCollision()方法
                        bc.onCollision(obj);
                        boxCollider.onCollision(parent);

                    }
                    if (!innerFlag) break;
                }

                em.setLocked(false);

                if (!innerFlag) break;
            }

            // 无任何碰撞
            if (innerFlag) {
                flag = false;
            }

            // 计算新的位置
            dvel *= 0.5f;

            if (dvel < Float.MIN_VALUE) {
                flag = false;
            }

        }
        return dvel;
    }

}
