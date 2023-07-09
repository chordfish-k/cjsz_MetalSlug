package com.tedu.game;

import com.tedu.controller.GameListener;
import com.tedu.controller.GameThread;
import com.tedu.show.GameJFrame;
import com.tedu.show.GameMainJPanel;

import java.awt.*;

/**
 * 游戏入口类
 */
public class GameStart {

    /**
     * 程序唯一入口
     */
    public static void main(String[] args) {
        GameJFrame gj = new GameJFrame();
        // 实例化面板
        GameMainJPanel mainJPanel = new GameMainJPanel()
                                        .setRefreshSleep(10);
        // 实例化监听器
        GameListener listener = new GameListener();
        // 实例化主线程
        GameThread thread = new GameThread()
                                .setGameRunFrameSleep(15);
        // 注入
        gj.setPanel(mainJPanel);
        gj.setKeyListener(listener);
        gj.setThread(thread);
        gj.start(); // 显示窗体

        Game.getInstance().setGameJFrame(gj);
    }
}
