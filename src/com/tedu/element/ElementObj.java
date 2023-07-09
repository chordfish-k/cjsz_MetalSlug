package com.tedu.element;

import com.tedu.element.component.BoxCollider;
import com.tedu.element.component.ComponentBase;
import com.tedu.element.component.Transform;
import com.tedu.geometry.Polygon;
import com.tedu.geometry.Vector2;
import com.tedu.manager.ElementType;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 所有元素的基类
 * @author LSR
 */
public abstract class ElementObj {
    private int w = 0;
    private int h = 0;
    private ImageIcon sprite = null;
    private ElementState elementState = ElementState.LIVING;
    private List<ElementObj> children = null;
    private ElementObj parent = null;
    private Map<String, ComponentBase> components = null;
    private ElementType elementType = ElementType.DEFAULT;

    public Transform transform = null;

    public ElementObj() {
        components = new HashMap<>();
        children = new ArrayList<>();
        transform = (Transform) addComponent("Transform");
    }

    /**
     * 带参构造函数
     * @param x 左上角x坐标
     * @param y 左上角y坐标
     * @param sprite 元素贴图
     */
    public ElementObj(float x, float y, ImageIcon sprite) {
        this();
        this.w = 0;
        this.h = 0;
        if(this.transform != null) {
            this.transform.setPos(new Vector2(x, y));
        }
        //this.sprite = sprite;
    }

    public ElementObj create() {
        return this.create("");
    }

    /**
     * 工厂模式创建对象（需重写）
     * @param data 必要的数据
     * @return 将数据解析后新建的对象
     */
    public ElementObj create(String data) {
        return this;
    }

    /**
     * 当前元素创建中执行，主要加载Component
     */
    public void onLoad() {
        for(String cpKey : components.keySet()) {
            components.get(cpKey).onLoad();
        }
    }

    /**
     * 当元素创建后执行
     */
    public void onCreate() {
        for(String cpKey : components.keySet()) {
            components.get(cpKey).onCreate();
        }
//        for (ElementObj obj : children) {
//            obj.onCreate();
//        }
    }

    /**
     * 销毁当前对象
     */
    public void destroy(){
        setElementState(ElementState.DIED);
        for (ElementObj obj : children) {
            obj.destroy();
        }
    }

    /**
     * 当监听到键盘按键按下时触发
     * @param key 按键码
     */
    public void onKeyPressed(int key) {
        for(String cpKey : components.keySet()) {
            components.get(cpKey).onKeyPressed(key);
        }
//        for (ElementObj obj : children) {
//            obj.onKeyPressed(key);
//        }
    }

    /**
     * 当监听到键盘按键松开时触发
     * @param key 按键码
     */
    public void onKeyReleased(int key) {
        for(String cpKey : components.keySet()) {
            components.get(cpKey).onKeyReleased(key);
        }
//        for (ElementObj obj : children) {
//            obj.onKeyReleased(key);
//        }
    }

    /**
     * 帧更新
     */
    public void onUpdate(long time) {
        for(String cpKey : components.keySet()) {
            components.get(cpKey).onUpdate();
        }
//        for (ElementObj obj : children) {
//            obj.onUpdate(time);
//        }
    }

    /**
     * 当对象被销毁前触发
     */
    public void onDestroy() {
        for(String cpKey : components.keySet()) {
            components.get(cpKey).onDestroy();
        }
        components.clear();
//        for (ElementObj obj : children) {
//            obj.onDestroy();
//        }
    }

    /**
     * 当触发碰撞后触发
     */
    public void onCollision(ElementObj other) {
        for(String cpKey : components.keySet()) {
            if (!cpKey.equals("BoxCollider"))
                components.get(cpKey).onCollision(other);
        }
//        for (ElementObj obj : children) {
//            obj.onCollision(other);
//        }
    }

    /**
     * 显示元素
     * @param g 当前JPanel的画笔
     */
    public void onDraw(Graphics g) {

    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }

    public ElementState getElementState() {
        return elementState;
    }

    public List<ElementObj> getChildren() {
        return children;
    }

    public Map<String, ComponentBase> getComponents() {
        return components;
    }

    public ComponentBase getComponent(String name) {
        return components.get(name);
    }

    public void setElementState(ElementState elementState) {
        this.elementState = elementState;
    }

    /**
     * 获取本元素的外包围盒
     * @return 元素的碰撞矩形对象
     */
    public Polygon getShape() {
        BoxCollider col = (BoxCollider) getComponent("BoxCollider");
        if (col == null)
            return null;

//        Sprite sp = (Sprite) this.getComponent("Sprite");
//        if (this.transform == null || sp == null)
//            return new Rectangle(0,0,0,0);
//
//        Rectangle r = new Rectangle(Math.round(transform.getX()), Math.round(transform.getY()),
//                Math.round(sp.getWidth() * transform.getScaleX()),
//                Math.round(sp.getHeight() * transform.getScaleY()));
        return col.getShape();
    }

    /**
     * 根据外包围盒，判断两个元素是否有重合（碰撞）
     * @param obj 需要比较的另一个元素
     * @return 是否碰撞的判断
     */
    public boolean checkCollisionWith(ElementObj obj) {
        BoxCollider colA = (BoxCollider) getComponent("BoxCollider");
        BoxCollider colB = (BoxCollider) obj.getComponent("BoxCollider");
        if (colA == null || colB == null)
            return false;

        return colA.checkCollisionWith(colB);
    }
    public ComponentBase addComponent(String name) {
        return addComponent(name, "");
    }

    public ComponentBase addComponent(String name, String data) {
        ComponentBase cb = null;
        try {
            Class<?> c =  Class.forName("com.tedu.element.component."+name);
            cb = ((ComponentBase) c.newInstance()).create(data);
            this.components.put(name, cb);
            cb.setParent(this);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return cb;
    }

    public ElementObj addChild(String name) {
        ElementObj obj =  GameLoad.createElementByName(name);
        return addChild(obj);
    }

    public ElementObj addChild(ElementObj obj) {
        children.add(obj);
        obj.parent = this;
        return obj;
    }

    public ElementObj getParent() {
        return parent;
    }

    public Vector2 calcAbsolutePos() {
        Vector2 vec = new Vector2();
        ElementObj p = this;
        while (p != null) {
            Vector2 tmp = p.transform.getPos().sub(new Vector2(0, 0));
            vec = vec.add(tmp);
            p = p.getParent();
        }
        return vec;
    }
}
