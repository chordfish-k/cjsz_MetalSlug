package com.tedu.element;

import java.awt.Graphics;
import java.util.Random;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

public class Enemy extends ElementObj {
    private String fx = "up";//坦克默认朝向
    private int moveSpeed = 2;    //坦克移速
    private int hp = 1; //坦克血量
    private int bulletNum = 1;//坦克子弹数
    private int bulletTime = 100; //坦克发射子弹间隔
    private boolean left = false; //左
    private boolean up = true;   //上
    private boolean right = false;//右
    private boolean down = false; //下

    private String mapId;

    public Enemy() {
        //该方法不被使用，写出来，防止继承出错
    }

    public Enemy(int x, int y, int w, int h, ImageIcon icon) {
        super(x, y, w, h, icon);
    }


    @Override
    public void showElement(Graphics g) {
        g.drawImage(this.getIcon().getImage(),
                this.getX(), this.getY(),
                this.getW(), this.getH(), null);
    }

    @Override
    public ElementObj createElement(String str, String mapID) {
        this.mapId = mapID;
        String[] split = str.split(",");
        System.out.println(str);
        this.setX(new Integer(split[0]));
        this.setY(new Integer(split[1]));
        ImageIcon icon2 = GameLoad.imgMap_enemy.get(split[2]);
        this.setW(icon2.getIconWidth());
        this.setH(icon2.getIconHeight());
        this.setIcon(icon2);
        return this;
    }

    @Override
    public void move() {
        boolean move = false;
        if (this.left && this.getX() > 0) {
            this.setX(this.getX() - moveSpeed);
            move = true;
        }
        if (this.up && this.getY() > 0) {
            this.setY(this.getY() - moveSpeed);
            move = true;
        }
        if (this.right && this.getX() < GameJFrame.getGameX() - 20 - this.getW()) {  //坐标的跳转由大家来完成
            this.setX(this.getX() + moveSpeed);
            move = true;
        }
        if (this.down && this.getY() < GameJFrame.getGameY() - 30 - this.getH()) {
            this.setY(this.getY() + moveSpeed);
            move = true;
        }
        if (!move) {
            changeFx();
        }
    }

    /**
     * @说明：坦克改变方向
     */
    private void changeFx() {
        String tempFx = this.fx;
        do {
            int fxsIndex = 0;
            if (mapId.equals("map4")) {
                fxsIndex = new Random().nextInt(2);
            } else {
                fxsIndex = new Random().nextInt(4);
            }


            switch (fxsIndex) {
                case 0:
                    this.down = false;
                    this.up = false;
                    this.right = false;
                    this.left = true;
                    tempFx = "left";
                    break;
                case 1:
                    this.down = false;
                    this.up = false;
                    this.left = false;
                    this.right = true;
                    tempFx = "right";
                    break;
                case 2:
                    this.right = false;
                    this.left = false;
                    this.down = false;
                    this.up = true;
                    tempFx = "up";
                    break;
                case 3:
                    this.right = false;
                    this.left = false;
                    this.up = false;
                    this.down = true;
                    tempFx = "down";
                    break;
            }
        } while (tempFx.equals(this.fx));
        this.fx = tempFx;
    }

    protected void updateImage(long gameTime) {
        this.setIcon(GameLoad.imgMap_enemy.get(fx));
    }

    @Override
    protected void add(long gameTime) {
        if (gameTime % bulletTime >= bulletNum) return;

        // {x:3,y:5,f:up}
        ElementObj element = new PlayFile(ShoterName.ENEMY.toString()).createElement(this.toString());
        ElementManager.getManager().addElement(element, GameElement.PLAYFILE);
    }

    public String toString() {
        int x = this.getX();
        int y = this.getY();
        switch (this.fx) {
            case "up":
                x += 20;
                y -= 5;
                break;
            case "left":
                y += 20;
                x -= 5;
                break;
            case "right":
                x += 50;
                y += 20;
                break;
            case "down":
                y += 50;
                x += 20;
                break;
        }
        return "x:" + x + ",y:" + y + ",f:" + this.fx;
    }

    @Override
    public boolean beAttack(ElementObj bullet) {
        if (bullet instanceof PlayFile) {

            if (ShoterName.ENEMY.toString().equals(((PlayFile) bullet).getShoter())) return false;

            this.hp -= ((PlayFile) bullet).getAttack();

            if (hp <= 0) {
                this.die();
            }
            return true;
        }
        return false;
    }

    public void goBack() {//遇障后调转方向
        switch (this.fx) {
            case "left":
                this.setX(this.getX() + moveSpeed);
                break;
            case "up":
                this.setY(this.getY() + moveSpeed);
                break;
            case "down":
                this.setY(this.getY() - moveSpeed);
                break;
            case "right":
                this.setX(this.getX() - moveSpeed);
                break;
        }
        changeFx();
    }


}
