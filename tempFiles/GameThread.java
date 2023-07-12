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
 * @˵�� ��Ϸ�����̣߳����ڿ�����Ϸ���أ���Ϸ�ؿ�����Ϸ����ʱ�Զ���
 * ��Ϸ�ж�����Ϸ��ͼ�л� ��Դ�ͷź����¶�ȡ������
 * @�̳� ʹ�ü̳еķ�ʽʵ�ֶ��߳�(һ�㽨��ʹ�ýӿ�ʵ��)
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
    public void run() {//��Ϸ��run����  ���߳�
        boolean contain = true;
        while (contain) { //��չ,���Խ�true��Ϊһ���������ڿ��ƽ���
//		��Ϸ��ʼǰ   ����������������Ϸ��Դ(������Դ)
            gameLoad(IndexOfMap);
//		��Ϸ����ʱ   ��Ϸ������
            boolean isWin = gameRun();
//		��Ϸ��������  ��Ϸ��Դ����(������Դ)
            contain = gameOver(isWin);
            if (!contain) {
                try {
                    sleep(5000);
                    System.out.println("��Ϸ����");
                    System.exit(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * ��Ϸ�ļ���
     */
    private void gameLoad(int IndexOfMap) {
        GameLoad.loadImg(); //����ͼƬ
        GameLoad.MapLoad(IndexOfMap);//���Ա�Ϊ ������ÿһ�����¼���  ���ص�ͼ
//		��������
        GameLoad.loadPlay();//Ҳ���Դ���������������2��
//		���ص���NPC��

//		ȫ��������ɣ���Ϸ����
    }

    /**
     * @˵�� ��Ϸ����ʱ
     * @����˵�� ��Ϸ��������Ҫ�������飺1.�Զ�����ҵ��ƶ�����ײ������
     * 2.��Ԫ�ص�����(NPC��������ֵ���)
     * 3.��ͣ�ȵȡ���������
     * ��ʵ�����ǵ��ƶ�
     */
    private long gameTime = 0L; //��¼����ʼ���е�ʱ��

    private boolean gameRun() {
//		long gameTime=0L;//��int���;Ϳ�����
        while (true) {// Ԥ����չ   true���Ա�Ϊ���������ڿ��ƹܹؿ�������
            Map<GameElement, List<ElementObj>> all = em.getGameElements();
            List<ElementObj> enemys = em.getElementsByKey(GameElement.ENEMY);
            List<ElementObj> files = em.getElementsByKey(GameElement.PLAYFILE);
            List<ElementObj> plays = em.getElementsByKey(GameElement.PLAY);
            List<ElementObj> maps = em.getElementsByKey(GameElement.MAPS);
            moveAndUpdate(all, gameTime);//	��ϷԪ���Զ�������
//			ElementPK(enemys,files);
//			ElementPK(files,maps);

            PlayPk(maps, plays);//����������ͼ֮�����ײ
            PlayPk(enemys, plays);//������������֮�����ײ
            EnemyPk(maps, enemys);//���������ͼ֮�����ײ
            EnemyPk(plays, enemys);//�����������֮�����ײ
            EnemyPk(enemys, enemys);//�����������֮�����ײ
            BulletPk(enemys, files,true);//���������ӵ�֮�����ײ
            BulletPk(plays, files, false);//���������ӵ�֮�����ײ
            BulletPk(maps, files,false);//����ͼ���ӵ�֮�����ײ

            gameTime++;//Ψһ��ʱ�����
            if (enemys.size() == 0) {
                return true;
            }
            for (int i = 0; i < plays.size(); i++) {
                if (!plays.get(i).isLive()) {
                    return false;
                }
            }
            try {
                sleep(10);//Ĭ�����Ϊ 1��ˢ��100��
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @˵�����л���Ϸ�ؿ�
     */
    private boolean gameOver(boolean isWin) {
        em.clearEm();
        String gameoverUrl = "image/gameover.png";
        ImageIcon gameoverIcon = new ImageIcon(gameoverUrl);


        if (isWin) {
            if (this.IndexOfMap != 5) {
                IndexOfMap++;
                JLabel label = new JLabel("��ϲ�㣬�����ˣ�\n���ĵ÷֣�" + gs.getScore());
                JDialog dialog = new JDialog((JFrame)null, "��ʾ", false);
                dialog.getContentPane().add(label);
                dialog.setSize(200, 100);
                dialog.setLocationRelativeTo(null);

                dialog.setVisible(true); // ��ʾ�Ի���

                // һ��ʱ������ضԻ���
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

    //	��ϷԪ���Զ�������
    public void moveAndUpdate(Map<GameElement, List<ElementObj>> all, long gameTime) {
//		GameElement.values();//���ط���  ����ֵ��һ������,�����˳����Ƕ���ö�ٵ�˳��
        for (GameElement ge : GameElement.values()) {
            List<ElementObj> list = all.get(ge);
//			��д����ֱ�Ӳ����������ݵĴ��뽨�鲻Ҫʹ�õ�������
//			for(int i=0;i<list.size();i++) {
            for (int i = list.size() - 1; i >= 0; i--) {
                ElementObj obj = list.get(i);//��ȡΪ����
                if (!obj.isLive()) {//�������
//					list.remove(i--);  //����ʹ�������ķ�ʽ
//					����һ����������(�����п�������������:�������� ,��װ��)
                    obj.die();//��Ҫ����Լ�����
                    list.remove(i);
                    continue;
                }
                obj.model(gameTime);//���õ�ģ�巽�� ����move
            }
        }
    }

    /**
     * @param objects
     * @param bullets
     * @˵�����ӵ��������������ײ��ⷽ��
     */
    public void BulletPk(List<ElementObj> objects, List<ElementObj> bullets, boolean isEnemy) {
        for (int i = 0; i < bullets.size(); i++) {
            ElementObj bullet = bullets.get(i);

            for (int j = 0; j < objects.size(); j++) {//�ӵ���������ײ���
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
     * @˵���������������������ײ��ⷽ��
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
     * @˵��������������������ײ��ⷽ��
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





