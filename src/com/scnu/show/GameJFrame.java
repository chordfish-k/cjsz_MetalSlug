package com.scnu.show;

import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * 游戏窗体 <p>
 * 主要实现：关闭，显示，最大最小化 <p>
 * 功能说明：需要嵌入面板，启动主线程等 <p>
 * 窗体说明：swing awt 窗体大小 记录用户上次窗体样式设置 <p>
 * @author LSR
 */
public class GameJFrame extends JFrame {
    public static int SIZE_W = 900;
    public static int SIZE_H = 600;
    public static int INFO_H = 30;
    public static int CAMERA_X = 0;
    public static int CAMERA_Y = 0;
    public static String TITLE = "Metal Slug RELOAD";

    private JPanel panel = null; // 正在显示的面板
    private KeyListener keyListener = null; // 键盘监听器
    private MouseMotionListener mouseMotionListener = null; // 鼠标动作监听器
    private MouseListener mouseListener = null; // 鼠标按键监听器
    private Thread thread = null; // 游戏主线程

    public GameJFrame() {
        init();
    }

    /**
     * 窗体初始化
     */
    public void init() {
        this.setSize(SIZE_W, SIZE_H + INFO_H);
        this.setTitle(TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); // 居中显示
    }

    /**
     * 启动主线程
     */
    public void start() {
        if(panel != null) {
            this.add(panel);
        }
        if(keyListener != null) {
            this.addKeyListener(keyListener);
        }
        if(mouseListener != null) {
            this.addMouseListener(mouseListener);
        }
        if (mouseMotionListener != null) {
            this.addMouseMotionListener(mouseMotionListener);
        }
        if(thread != null) {
            thread.start(); // 启动线程
        }

        // 显示界面
        this.setVisible(true);

        // 如果panel继承了Runnable接口
        if(this.panel instanceof Runnable) {
            // 建立刷新线程
            new Thread((Runnable) this.panel).start();
        }
    }


    public void addButton() {
//        this.setLayout(...);
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public void setKeyListener(KeyListener keyListener) {
        this.keyListener = keyListener;
    }

    public void setMouseMotionListener(MouseMotionListener mouseMotionListener) {
        this.mouseMotionListener = mouseMotionListener;
    }

    public void setMouseListener(MouseListener mouseListener) {
        this.mouseListener = mouseListener;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Thread getThread() {
        return thread;
    }
}
