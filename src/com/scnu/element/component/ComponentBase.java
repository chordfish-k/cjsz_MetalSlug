package com.scnu.element.component;

import com.scnu.element.ElementObj;

import java.awt.*;

public abstract class ComponentBase{
    protected ElementObj parent = null;
    protected boolean active = true;

    public ComponentBase() {
        this("");
    }

    public ComponentBase(String data) {
        onCreate();
    }

    public ComponentBase create(String data) {
        return this;
    }

    public void setParent(ElementObj parent) {
        this.parent = parent;
    }

    public ElementObj getParent() {
        return parent;
    }

    public void onLoad() {}

    public void onCreate() {}

    public void onDestroy() {}

    public void onUpdate() {}

    /**
     * 当物理更新时触发
     */
    public void onPhysicsUpdate() {}

    /**
     * 当监听到键盘按键按下时触发
     * @param key 按键码
     */
    public void onKeyPressed(int key) { }

    /**
     * 当监听到键盘按键松开时触发
     * @param key 按键码
     */
    public void onKeyReleased(int key) { }

    /**
     * 当该帧执行渲染时执行，可以重写，用于显示测试用图像
     */
    public void onDraw(Graphics g) {}

    public void onCollision(ElementObj other) {
    }

    public boolean isActive(){
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
