package com.scnu.manager;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class UIManager {
    // 实现单例
    private static UIManager UM = null;

    private Map<String, JComponent> allUI;


    /**
     * 用于获取UIManager单例
     * @return UIManager唯一单例
     */
    public static UIManager getManager() {
        if (UM == null) {
            UM = new UIManager();
        }
        return UM;
    }
    // 构造函数私有化
    private UIManager(){
        init(); // 实例化方法
    }

    private void init() {
        allUI = new HashMap<>();
    }

    public Map<String, JComponent> getAllUI() {
        return allUI;
    }

    public void addUI(String name, JComponent ui) {
        allUI.put(name, ui);
    }

    public JComponent getUI(String name) {
        return allUI.get(name);
    }
}
