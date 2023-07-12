package com.tedu.element;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

public class Play extends ElementObj /* implements Comparable<Play>*/ {
    /**
     * �ƶ�����:
     * 1.������  ���  ����ö������ʹ��; һ��ֻ���ƶ�һ������
     * 2.˫����  ���� �� ����    ���booleanֵʹ�� ���磺 true������ false Ϊ�� ��������
     * ��Ҫ����һ������ȷ���Ƿ��·����
     * Լ��    0 ������   1������   2������
     * 3.4����  �������Ҷ�����  boolean���ʹ��  true �����ƶ� false ���ƶ�
     * ͬʱ���Ϻ��� ��ô�죿�󰴵Ļ������Ȱ���
     * ˵��������3�ַ�ʽ �Ǵ����д���ж���ʽ ��һ��
     * ˵������Ϸ�зǳ�����ж����������ʹ�� �ж����ԣ��ܶ�״ֵ̬Ҳʹ���ж�����
     * ��״̬ ����ʹ��map<����,boolean>;set<�ж�����> �ж���������ʱ��
     *
     * @���� 1.ͼƬҪ��ȡ���ڴ��У� ������  ��ʱ����ʽ���ֶ���д�洢���ڴ���
     * 2.ʲôʱ������޸�ͼƬ(��ΪͼƬ���ڸ����е����Դ洢)
     * 3.ͼƬӦ��ʹ��ʲô���Ͻ��д洢
     */
    private boolean left = false; //��
    private boolean up = false;   //��
    private boolean right = false;//��
    private boolean down = false; //��
    private int step = 0;//�ڼ��� 0��ʾ��ֹ
    private int moveSpeed = 15; //����
    private int hp = 10;    //Ѫ��
    private int bulletNum = 1;//�ӵ���
    private int bulletTime = 20; //�����ӵ����
    private long atkTime = 0;    //��һ�ο����ʱ��
    private long walkTime = 0;//��һ�����ߵ�ʱ��


    //	����ר��������¼��ǰ��������ķ���,Ĭ��Ϊ��up
    private String fx = "up";
    private boolean pkType = false;//����״̬ true ����  falseֹͣ

    public Play() {
    }

    public Play(int x, int y, int w, int h, ImageIcon icon) {
        super(x, y, w, h, icon);
    }

    //���⻰: ��ʱ�ķ��������� �����ã�Ҳ�ܹ��ã���Ϊ�㲻��jdk�ײ�ʹ��
    @Override
    public ElementObj createElement(String str) {
        String[] split = str.split(",");
        this.setX(Integer.parseInt(split[0]));
        this.setY(Integer.parseInt(split[1]));
        ImageIcon icon2 = GameLoad.imgMap.get(split[2]);
        this.setW(icon2.getIconWidth());
        this.setH(icon2.getIconHeight());
        this.setIcon(icon2);
        return this;
    }


    /**
     * ��������е�1��˼�룺 �����Լ��������Լ���
     */
    @Override
    public void showElement(Graphics g) {
//		�滭ͼƬ
        g.drawImage(this.getIcon().getImage(),
                this.getX(), this.getY(),
                this.getW(), this.getH(), null);
    }

    /*
     * @˵�� ��д������ ��д��Ҫ�󣺷������� �Ͳ����������� ����͸���ķ���һ��
     * @�ص� ������������Ҫ�ı�״ֵ̬
     */
    @Override   // ע�� ͨ��������ƣ�Ϊ����߷����������� ��ӵ�ע��(�൱�����֤�ж�)
    public void keyClick(boolean bl, int key) { //ֻ�а��»�����_�ŕ� �����������
//		System.out.println("���ԣ�"+key);
        if (bl) {//����
            switch (key) {  //��ô�Ż� �������˼��;�� �����������������û�취����һ��
                case 37:
                    this.down = false;
                    this.up = false;
                    this.right = false;
                    this.left = true;
                    this.fx = "left";
                    break;
                case 38:
                    this.right = false;
                    this.left = false;
                    this.down = false;
                    this.up = true;
                    this.fx = "up";
                    break;
                case 39:
                    this.down = false;
                    this.up = false;
                    this.left = false;
                    this.right = true;
                    this.fx = "right";
                    break;
                case 40:
                    this.right = false;
                    this.left = false;
                    this.up = false;
                    this.down = true;
                    this.fx = "down";
                    break;
                case 32:
                    this.pkType = true;
                    break;//��������״̬
            }
        } else {
            switch (key) {
                case 37:
                    this.left = false;
                    break;
                case 38:
                    this.up = false;
                    break;
                case 39:
                    this.right = false;
                    break;
                case 40:
                    this.down = false;
                    break;
                case 32:
                    this.pkType = false;
                    break;//�رչ���״̬
            }
            //a a
        }
    }


    //	@Override
//	public int compareTo(Play o) {
//		return 0;
//	}
    @Override
    public void move() {
        if (this.left && this.getX() > 0) {
            this.setX(this.getX() - 1);
        }
        if (this.up && this.getY() > 0) {
            this.setY(this.getY() - 1);
        }
        if (this.right && this.getX() < 900 - this.getW()) {  //�������ת�ɴ�������
            this.setX(this.getX() + 1);
        }
        if (this.down && this.getY() < 600 - this.getH()) {
            this.setY(this.getY() + 1);
        }
    }

    @Override
    protected void updateImage(long gameTime) {
//		ImageIcon icon=GameLoad.imgMap.get(fx);
//		System.out.println(icon.getIconHeight());//�õ�ͼƬ�ĸ߶�
//		����߶���С�ڵ���0 ��˵��������ͼƬ·��������
        if (gameTime - walkTime % 10 > 1
                && gameTime - walkTime < 5) {//���Ʋ�Ƶ
            return;
        }

        if (!GameLoad.imgMap.containsKey(fx)) {
            return; // ���û�ж�Ӧ�ļ�ֵ������ͼƬ���²���
        }

        if (this.step > 4) {//4��һѭ��
            this.step = 1;
        }
        String url = GameLoad.imgMap.get(fx) + Integer.toString(step) + ".png";
//		System.out.println("url:"+url);
        this.setIcon(GameLoad.imgMap.get(fx));
        if (this.step != 0) {//����ʱ
            this.step++;
            this.walkTime = gameTime;
        }
    }

    public void goBack() {//���Ϻ������ֹͣ�˶�
        switch (this.fx) {
            case "left":
                this.setX(this.getX() + moveSpeed);
                left = false;
                break;
            case "up":
                this.setY(this.getY() + moveSpeed);
                up = false;
                break;
            case "down":
                this.setY(this.getY() - moveSpeed);
                down = false;
                break;
            case "right":
                this.setX(this.getX() - moveSpeed);
                right = false;
                break;
        }
    }

    /**
     * @�������⣺1.������д�ķ����ķ������η��Ƿ�����޸ģ� 2.���������add�����Ƿ�����Զ��׳��쳣?
     * @��д����1.��д�����ķ������ƺͷ���ֵ ����͸����һ��
     * 2.��д�ķ����Ĵ�������������У�����͸����һ��
     * 3.��д�ķ����������η� ֻ�� �ȸ���ĸ��ӿ���
     * �ȷ�˵������ķ������ܱ����ģ�����������Ҫ�ڷ��������
     * ����ֱ������̳У���д��super.���෽����public����
     * 4.��д�ķ����׳����쳣 �����Աȸ�����ӿ�
     * �ӵ������ ��Ҫ���� �����ߵ�����λ�ã������ߵķ���  �������Ա任�ӵ�(˼������ô����)
     */
    private long filetime = 0;

    //	filefime �ʹ����ʱ�� gameTime ���бȽϣ���ֵ�Ȳ������㣬�����ӵ����
//	������ƴ��� �Լ�д
    @Override   //����ӵ�
    public void add(long gameTime) {//����ʱ��Ϳ��Խ��п���
        if (!this.pkType) return;//���Ƿ���״̬��renturn
        if (gameTime % bulletTime >= bulletNum
                && gameTime - atkTime < bulletTime) return;
        atkTime = gameTime;

        // {x:3,y:5,f:up}
        ElementObj element = new PlayFile(ShoterName.PLAYER.toString()).createElement(this.toString());
        ElementManager.getManager().addElement(element, GameElement.PLAYFILE);
    }

    @Override
    public String toString() {// ������͵����ֱ��ʹ��toString�������Լ�����һ������
        int x = this.getX();
        int y = this.getY();
        int fix = 5;
        switch (this.fx) {
            case "up":
                x += this.getIcon().getIconWidth() / 2 - fix;
                y -= fix;
                break;
            case "left":
                y += this.getIcon().getIconHeight() / 2 - fix;
                x -= fix;
                break;
            case "right":
                x += this.getIcon().getIconWidth();
                y += this.getIcon().getIconHeight() / 2 - fix;
                break;
            case "down":
                y += this.getIcon().getIconHeight();
                x += this.getIcon().getIconWidth() / 2 - fix;
                break;
        }
        return "x:" + x + ",y:" + y + ",f:" + this.fx;
    }

    @Override
    public boolean beAttack(ElementObj bullet) {
        if (bullet instanceof PlayFile) {

            if (ShoterName.PLAYER.toString().equals(((PlayFile) bullet).getShoter())) return false;

            this.hp -= ((PlayFile) bullet).getAttack();

            if (hp <= 0) {
                this.die();
            }
            return true;
        }
        return false;
    }

    public int getHp() {
        return hp;
    }
}

//try {
//Class<?> forName = Class.forName("com.tedu.....");
//ElementObj element = forName.newInstance().createElement("");
//} catch (InstantiationException e) {
//e.printStackTrace();
//} catch (IllegalAccessException e) {
//e.printStackTrace();
//} //�Ժ�Ŀ��ѧϰ�л�����
//// ������㷵�ض����ʵ�壬����ʼ������
//catch (ClassNotFoundException e) {
//			e.printStackTrace();
//}





