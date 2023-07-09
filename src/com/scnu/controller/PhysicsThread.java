package com.scnu.controller;

import com.scnu.element.ElementObj;
import com.scnu.element.component.RigidBody;
import com.scnu.manager.ElementManager;
import com.scnu.manager.ElementType;

import java.util.List;

public class PhysicsThread extends Thread{

    private ElementManager em = null;

    public PhysicsThread() {
        em = ElementManager.getManager();
    }

    @Override
    public void run() {
        while (true) {
            if (em != null && !em.isLocked()) {
                em.setLocked(true);
                for (ElementType type : ElementType.values()) { // values()按枚举定义顺序返回枚举数组
                    List<ElementObj> list = em.getGameElements().get(type);
                    for (int i = list.size() - 1; i >= 0; i--) {
                        ElementObj obj = list.get(i);
                        RigidBody rb = (RigidBody) obj.getComponent("RigidBody");

                        if (rb != null) // 调用所有有RigidBody组件元素的onFixUpdate()方法
                            rb.onPhysicsUpdate();
                    }
                }
                em.setLocked(false);
                try {
                    sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
