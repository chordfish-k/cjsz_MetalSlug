package com.tedu.element;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

/**
 * @author renjj
 * @˵�� ����ӵ��࣬�����ʵ�����������Ҷ�����úʹ���
 * @����Ŀ������� 1.�̳���Ԫ�ػ���;��дshow����
 * 2.��������ѡ������д�����������磺move��
 * 3.˼���������������е�����
 */
public class PlayFile extends ElementObj {
    private int attack;//������
    private int moveNum = 3;//�ƶ��ٶ�ֵ
    private String fx;
    private String type;//����
    private String shoter;//�����

    //	ʣ�µĴ����չ; ������չ�������ӵ��� ���⣬�����ȵȡ�(��������Ҫ���ӵ�����)
    public PlayFile() {
    }//һ���յĹ��췽��

    private PlayFile(int x, int y, int w, int h, ImageIcon icon, String fx) {
        super(x, y, w, h, icon);
    }

    public PlayFile(String shoter) {
        this.shoter = shoter;
    }
    @Override   //{X:3,y:5,f:up}
    public ElementObj createElement(String str) {//�����ַ����Ĺ���
        String[] split = str.split(",");
        for (String str1 : split) {//X:3
            String[] split2 = str1.split(":");// 0�±� �� x,y,f   1�±���ֵ
            switch (split2[0]) {
                case "x":
                    this.setX(Integer.parseInt(split2[1]));
                    break;
                case "y":
                    this.setY(Integer.parseInt(split2[1]));
                    break;
                case "f":
                    this.fx = split2[1];
                    break;
            }
        }
        this.setW(10);
        this.setH(10);
        return this;
    }

    @Override
    public void showElement(Graphics g) {
        g.setColor(Color.red);// new Color(255,255,255)
        g.fillOval(this.getX(), this.getY(), this.getW(), this.getH());
    }

    @Override
    protected void move() {
        if (this.getX() < 0 || this.getX() > 900 ||
                this.getY() < 0 || this.getY() > 600) {
            this.setLive(false);
            return;
        }
        switch (this.fx) {
            case "up":
                this.setY(this.getY() - this.moveNum);
                break;
            case "left":
                this.setX(this.getX() - this.moveNum);
                break;
            case "right":
                this.setX(this.getX() + this.moveNum);
                break;
            case "down":
                this.setY(this.getY() + this.moveNum);
                break;
        }

    }

    /**
     * �����ӵ���˵��1.���߽�  2.��ײ  3.��ҷű���
     * ����ʽ���ǣ����ﵽ����������ʱ��ֻ���� �޸�����״̬�Ĳ�����
     */
    @Override
    public void die() {
//		ElementManager em=ElementManager.getManager();
//		ImageIcon icon=new ImageIcon("image/tank/play2/player2_up.png");
//		ElementObj obj=new Play(this.getX(),this.getY(),50,50,icon);//ʵ��������
////		��������뵽 Ԫ�ع�������
////		em.getElementsByKey(GameElement.PLAY).add(obj);
//		em.addElement(obj,GameElement.DIE);//ֱ�����
        this.setLive(false);
    }

//    /**�ӵ���װ*/
//	private long time=0;
//	protected void updateImage(long gameTime) {
//		if(gameTime-time>5) {
//			time=gameTime;//Ϊ�´α�װ��׼��
//			this.setW(this.getW()+2);
//			this.setH(this.getH()+2);
////			���ͼƬ��������
//		}
//	}

    public int getAttack() {
        return attack;
    }


    public String getShoter() {
        return shoter;
    }


    public void setShoter(String shoter) {
        this.shoter = shoter;
    }

}





