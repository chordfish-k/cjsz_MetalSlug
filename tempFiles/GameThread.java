package com.tedu.controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import com.tedu.element.ElementObj;
import com.tedu.element.Enemy;
import com.tedu.element.GameOver;
import com.tedu.element.Play;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameScore;

/**
 * @author renjj
 * @说明 游戏的主线程，用于控制游戏加载，游戏关卡，游戏运行时自动化
 * 游戏判定；游戏地图切换 资源释放和重新读取。。。
 * @继承 使用继承的方式实现多线程(一般建议使用接口实现)
 */
public class GameThread extends Thread {
    private static int IndexOfMap = 4;
    private ElementManager em;
    private GameScore gs;
    public GameThread() {
        em = ElementManager.getManager();
        gs = GameScore.getGameScore();
    }

    @Override
    public void run() {//游戏的run方法  主线程
        boolean contain = true;
        while (contain) { //扩展,可以讲true变为一个变量用于控制结束
//		游戏开始前   读进度条，加载游戏资源(场景资源)
            gameLoad(IndexOfMap);
//		游戏进行时   游戏过程中
            boolean isWin = gameRun();
//		游戏场景结束  游戏资源回收(场景资源)
            contain = gameOver(isWin);
            if (!contain) {
                try {
                    sleep(5000);
                    System.out.println("游戏结束");
                    System.exit(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * 游戏的加载
     */
    private void gameLoad(int IndexOfMap) {
        GameLoad.loadImg(); //加载图片
        GameLoad.MapLoad(IndexOfMap);//可以变为 变量，每一关重新加载  加载地图
//		加载主角
        GameLoad.loadPlay();//也可以带参数，单机还是2人
//		加载敌人NPC等

//		全部加载完成，游戏启动
    }

    /**
     * @说明 游戏进行时
     * @任务说明 游戏过程中需要做的事情：1.自动化玩家的移动，碰撞，死亡
     * 2.新元素的增加(NPC死亡后出现道具)
     * 3.暂停等等。。。。。
     * 先实现主角的移动
     */
    private long gameTime = 0L; //记录程序开始运行的时间

    private boolean gameRun() {
//		long gameTime=0L;//给int类型就可以啦
        while (true) {// 预留扩展   true可以变为变量，用于控制管关卡结束等
            Map<GameElement, List<ElementObj>> all = em.getGameElements();
            List<ElementObj> enemys = em.getElementsByKey(GameElement.ENEMY);
            List<ElementObj> files = em.getElementsByKey(GameElement.PLAYFILE);
            List<ElementObj> plays = em.getElementsByKey(GameElement.PLAY);
            List<ElementObj> maps = em.getElementsByKey(GameElement.MAPS);
            moveAndUpdate(all, gameTime);//	游戏元素自动化方法
//			ElementPK(enemys,files);
//			ElementPK(files,maps);

            PlayPk(maps, plays);//检测人物与地图之间的碰撞
            PlayPk(enemys, plays);//检测人物与敌人之间的碰撞
            EnemyPk(maps, enemys);//检测敌人与地图之间的碰撞
            EnemyPk(plays, enemys);//检测玩家与敌人之间的碰撞
            EnemyPk(enemys, enemys);//检测敌人与敌人之间的碰撞
            BulletPk(enemys, files,true);//检测敌人与子弹之间的碰撞
            BulletPk(plays, files, false);//检测玩家与子弹之间的碰撞
            BulletPk(maps, files,false);//检测地图与子弹之间的碰撞

            gameTime++;//唯一的时间控制
            if (enemys.size() == 0) {
                return true;
            }
            for (int i = 0; i < plays.size(); i++) {
                if (!plays.get(i).isLive()) {
                    return false;
                }
            }
            try {
                sleep(10);//默认理解为 1秒刷新100次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @说明：切换游戏关卡
     */
    private boolean gameOver(boolean isWin) {
        em.clearEm();
        String gameoverUrl = "image/gameover.png";
        ImageIcon gameoverIcon = new ImageIcon(gameoverUrl);


        if (isWin) {
            if (this.IndexOfMap != 5) {
                IndexOfMap++;
                JLabel label = new JLabel("恭喜你，过关了！\n您的得分：" + gs.getScore());
                JDialog dialog = new JDialog((JFrame)null, "提示", false);
                dialog.getContentPane().add(label);
                dialog.setSize(200, 100);
                dialog.setLocationRelativeTo(null);

                dialog.setVisible(true); // 显示对话框

                // 一段时间后隐藏对话框
                try {
                    sleep(5000);
                    dialog.setVisible(false);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return true;
            } else {
                ElementObj obj = new GameOver(150, 10, gameoverIcon.getIconWidth(), gameoverIcon.getIconHeight(), gameoverIcon,gs.getScore());
                em.addElement(obj, GameElement.UI);
                return false;
            }
        } else {
            ElementObj obj = new GameOver(150, 10, gameoverIcon.getIconWidth(), gameoverIcon.getIconHeight(), gameoverIcon,gs.getScore());
            em.addElement(obj, GameElement.UI);
            return false;
        }
    }

    //	游戏元素自动化方法
    public void moveAndUpdate(Map<GameElement, List<ElementObj>> all, long gameTime) {
//		GameElement.values();//隐藏方法  返回值是一个数组,数组的顺序就是定义枚举的顺序
        for (GameElement ge : GameElement.values()) {
            List<ElementObj> list = all.get(ge);
//			编写这样直接操作集合数据的代码建议不要使用迭代器。
//			for(int i=0;i<list.size();i++) {
            for (int i = list.size() - 1; i >= 0; i--) {
                ElementObj obj = list.get(i);//读取为基类
                if (!obj.isLive()) {//如果死亡
//					list.remove(i--);  //可以使用这样的方式
//					启动一个死亡方法(方法中可以做事情例如:死亡动画 ,掉装备)
                    obj.die();//需要大家自己补充
                    list.remove(i);
                    continue;
                }
                obj.model(gameTime);//调用的模板方法 不是move
            }
        }
    }

    /**
     * @param objects
     * @param bullets
     * @说明：子弹与其他物体的碰撞检测方法
     */
    public void BulletPk(List<ElementObj> objects, List<ElementObj> bullets, boolean isEnemy) {
        for (int i = 0; i < bullets.size(); i++) {
            ElementObj bullet = bullets.get(i);

            for (int j = 0; j < objects.size(); j++) {//子弹与物体碰撞检测
                ElementObj object = objects.get(j);
                if (bullet.pk(object)) {

                    if (object.beAttack(bullet)) {
//                        bullet.die();
                        object.setLive(false);
                        bullet.setLive(false);
                        if (isEnemy){
                            gs.updateScore(1);
                        }
                        return;
                    }

                }
            }
        }
    }

    /**
     * @param objects
     * @param enemys
     * @说明：敌人与其他物体的碰撞检测方法
     */
    private void EnemyPk(List<ElementObj> objects, List<ElementObj> enemys) {
        for (int i = 0; i < enemys.size(); i++) {
            ElementObj enemy = enemys.get(i);

            for (int j = 0; j < objects.size(); j++) {
                ElementObj object = objects.get(j);
                if (enemy.pk(object) && enemy != object) {
                    enemy.goBack();
                }
            }
        }
    }

    /**
     * @param objects
     * @param plays
     * @说明：玩家与其他物体的碰撞检测方法
     */
    private void PlayPk(List<ElementObj> objects, List<ElementObj> plays) {
        for (int i = 0; i < plays.size(); i++) {
            ElementObj play = plays.get(i);

            for (int j = 0; j < objects.size(); j++) {
                ElementObj object = objects.get(j);
                if (play.pk(object)) {
                    play.goBack();
                }
            }
        }
    }

}





