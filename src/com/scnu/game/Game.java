package com.scnu.game;

import com.scnu.controller.GameThread;
import com.scnu.geometry.Vector2;
import com.scnu.show.GameJFrame;

/**
 * 游戏类，单例，为元素和组件等提供静态方法以操作游戏进程
 */
public class Game {
    private static Game game = null;

    public static Game getInstance() {
        if (game == null) {
            game = new Game();
        }
        return game;
    }

    private Game() {
        init();
    }

    private void init() {
    }

    private GameJFrame gameJFrame = null;
    private Vector2 mapSize = Vector2.ZERO;

    public GameJFrame getGameJFrame() {
        return gameJFrame;
    }

    public void setGameJFrame(GameJFrame gameJFrame) {
        this.gameJFrame = gameJFrame;
    }

    /**
     * 结束游戏进程，结算当前关卡
     */
    public void finishGameRun() {
        ((GameThread)this.gameJFrame.getThread())
                .finishGameRun();
    }

    public int getGameLevel() {
        if (this.gameJFrame != null)
            return ((GameThread)this.gameJFrame.getThread()).getLevelNum();
        return 1;
    }

    public void setMapSize(Vector2 mapSize) {
        this.mapSize = mapSize;
    }

    public Vector2 getMapSize() {
        return mapSize;
    }
}
