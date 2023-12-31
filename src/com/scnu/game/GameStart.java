package com.scnu.game;

import com.scnu.controller.GameListener;
import com.scnu.controller.GameThread;
import com.scnu.show.GameJFrame;
import com.scnu.show.GameMainJPanel;

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
