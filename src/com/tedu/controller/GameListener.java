package com.tedu.controller;

import com.tedu.element.ElementObj;
import com.tedu.manager.ElementManager;
import com.tedu.manager.ElementType;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 监听器类
 * @author LSR
 */
public class GameListener implements KeyListener {
    private ElementManager em = ElementManager.getManager();

    private Set<Integer> keySet = new HashSet<Integer>();

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // 拿到玩家集合
        List<ElementObj> players = em.getElementsByType(ElementType.PLAYER);
        // 使用集合判断按键是否曾被按下
        int key = e.getKeyCode();
        if(keySet.contains(key))
            return;
        keySet.add(key);

        for(ElementObj obj : players){
            obj.onKeyPressed(key);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        List<ElementObj> players = em.getElementsByType(ElementType.PLAYER);

        int key = e.getKeyCode();
        if(!keySet.contains(key))
            return;
        keySet.remove(key);

        for(ElementObj obj : players){
            obj.onKeyReleased(key);
        }
    }
}
