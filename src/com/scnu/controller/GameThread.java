package com.scnu.controller;

import com.scnu.element.ElementObj;
import com.scnu.element.ElementState;

import com.scnu.element.component.HealthValue;
import com.scnu.element.component.RigidBody;
import com.scnu.manager.ElementManager;
import com.scnu.manager.ElementType;
import com.scnu.manager.GameLoad;
import com.scnu.manager.UIManager;

import javax.swing.*;
import java.util.List;
import java.util.Map;

/**
 * 游戏主线程 <p>
 * 用于控制游戏加载，关卡加载，运行时自动化，游戏判定，地图切换，资源读取和释放等
 *
 * @author LSR
 */
public class GameThread extends Thread {

    private final ElementManager em;
    private final UIManager um;
    private int gameRunFrameSleep = 16; // 1000 / 16 =  60Hz
    private long gameTime = 0L; // 帧计时器

    private PhysicsThread physicTh = null;

    private boolean isThreadRunning = true; // 主进程是否继续
    private boolean isRunning = false; // 游戏是否继续
    private boolean isWon = false;
    private int levelNum = 1;
    private int sumScore = 0;

    public GameThread() {
        em = ElementManager.getManager();
        um = UIManager.getManager();
    }

    @Override
    public void run() { // 游戏主线程
        while (isThreadRunning) {
            // 游戏开始前：读进度条，加载游戏资源
            gameLoad();
            // 游戏进行时：游戏过程中
            gameRun();
            // 游戏场景结束：游戏资源回收（场景资源）
            gameOver();

            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行游戏的加载
     * 1.资源文件
     * 2.元素类
     * 3.地图
     * 4.其他
     */
    private void gameLoad() {


        GameLoad.loadImage();
        GameLoad.loadAnim();
        GameLoad.loadElement();
        GameLoad.loadRoot();
        GameLoad.loadBackground();
        //GameLoad.LoadMap(this.levelNum);

        GameLoad.loadPlayer();
        GameLoad.loadCollision();

        callOnLoad();

//        physicTh = new PhysicsThread();
//        physicTh.start();

        isRunning = true;
        isWon = false;
        gameTime = 0;

        ((JLabel)um.getUI("settlementLabel")).setText("");
    }

    /**
     * 游戏进行时
     */
    private void gameRun() {
        while (isRunning) {
            if (em != null) {
                Map<ElementType, List<ElementObj>> all = em.getGameElements();

                updateElements(all);

//                // 如果全部敌人都被打败，则胜利并结束
//                if (em.getElementsByType(ElementType.ENEMY).size() == 0) {
//                    isWon = true;
//                    break;
//                }
//                // 如果全部玩家都被打败，则失败并结束
//                else if (em.getElementsByType(ElementType.PLAYER).size() == 0) {
//                    isWon = true;
//                    break;
//                }

                try {
                    sleep(gameRunFrameSleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            gameTime++;
        }
    }


    /**
     * 游戏场景结束
     */
    private void gameOver() {
        // 先进入结算5秒
        if (isWon) {
            // 赢了5秒后进入下一关
            if (levelNum < 2){
                levelNum ++;
            }
            else {
                this.isThreadRunning = false;
            }
            HealthValue hv = (HealthValue) em.getElementsByType(ElementType.PLAYER).get(0).getComponent("HealthValue");
            int score = hv.getHealth();
            sumScore += score;
            ((JLabel)um.getUI("settlementLabel")).setText("your score: "+sumScore+" (+"+score+")");

        } else {
            this.isThreadRunning = false;
            // 输了5秒后退出
            ((JLabel)um.getUI("settlementLabel")).setText("your score: "+sumScore);
        }

        em.cleanAll();

        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void finishGameRun() {
        this.isRunning = false;
    }


    /**
     * 调用所有元素和组件的onLoad
     */
    public void callOnLoad() {
        Map<ElementType, List<ElementObj>> all = em.getGameElements();
        for (ElementType type : ElementType.values()) { // values()按枚举定义顺序返回枚举数组
            List<ElementObj> list = all.get(type);
            for (int i = list.size() - 1; i >= 0; i--) {
                ElementObj obj = list.get(i);
                obj.onLoad();
            }
        }
    }

    public int getGameRunFrameSleep() {
        return gameRunFrameSleep;
    }

    public GameThread setGameRunFrameSleep(int duration) {
        this.gameRunFrameSleep = duration;
        return this;
    }

    /**
     * 逐元素执行帧更新
     * @param all 所有类型元素List的Map集合
     */
    public void updateElements(Map<ElementType, List<ElementObj>> all) {
        for (ElementType type : ElementType.values()) { // values()按枚举定义顺序返回枚举数组
            List<ElementObj> list = all.get(type);
            for (int i = list.size() - 1; i >= 0; i--) {
                ElementObj obj = list.get(i);
                obj.onUpdate(gameTime); // 调用ElementObj的帧更新方法
                RigidBody rb = (RigidBody) obj.getComponent("RigidBody");
                if (rb != null) // 调用所有有RigidBody组件元素的onFixUpdate()方法
                    rb.onPhysicsUpdate();
            }
        }

        for (ElementType type : ElementType.values()) { // values()按枚举定义顺序返回枚举数组
            List<ElementObj> list = all.get(type);
            int p = list.size();
            for (int i = 0; i < p; i++) {
                ElementObj obj = list.get(i);

                if (obj.getElementState() == ElementState.DIED && !em.isLocked()) {
                    obj.onDestroy();
                    list.remove(i);
                    i--;
                    p = list.size();
                }
            }
        }
    }

    /**
     * 元素碰撞检测
     * 暂时先写子弹和敌人的检测
     */
    public void testElementsCollision(List<ElementObj> listA, List<ElementObj> listB) {

        for (int i=0; i<listA.size(); i++) {
            for (int j=0; j<listB.size(); j++) {
                ElementObj a = listA.get(i);
                ElementObj b = listB.get(j);
                if (a.checkCollisionWith(b)) {
                    a.onCollision(b);
                    b.onCollision(a);
                    break;
                }
            }
        }
    }
}
