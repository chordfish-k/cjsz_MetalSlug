package com.scnu.manager;

import com.scnu.element.ElementObj;
import com.scnu.element.RootObj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 元素管理器 <p>
 * 单例类，用List存储所有的元素，并提供方法给予View和Controller获取数据
 * @author LSR
 */
public class ElementManager {
    // 实现单例
    private static ElementManager EM = null;
    // 根节点
    public static RootObj eleRoot = null;

    // 资源锁
    private boolean locked = false;

    /**
     * 用于获取ElementManager单例
     * @return ElementManager唯一单例
     */
    public static ElementManager getManager() {
        if (EM == null) {
            EM = new ElementManager();
        }
        return EM;
    }
    // 构造函数私有化
    private ElementManager(){
        init(); // 实例化方法
    }


    /**
     * 初始化方法 <p>
     * 用于将来可能出现的功能扩展，要重写init方法准备的
     */
    public void init() {
        gameElements = new HashMap<>();

        // 将每种元素集合都放入map中
        for(ElementType type : ElementType.values()) {
            gameElements.put(type, new ArrayList<>());
        }
    }

    private Map<ElementType, List<ElementObj>> gameElements;

    /**
     * 获取本管理器所有元素
     * @return 元素Map集合
     */
    public Map<ElementType, List<ElementObj>> getGameElements() {
        return gameElements;
    }

    /**
     * 向管理器添加元素
     * @param obj 要添加的元素
     * @param type 元素类型
     */
    public void addElement(ElementObj obj, ElementType type) {
        gameElements.get(type).add(obj);
        obj.setElementType(type);
        obj.onCreate();
    }

    /**
     * 获取某一类型的元素列表
     * @param type 元素类型
     * @return 该类型的元素列表
     */
    public List<ElementObj> getElementsByType(ElementType type){
        return gameElements.get(type);
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void cleanAll() {
        for (ElementType type : gameElements.keySet()) {
            gameElements.get(type).clear();
        }
    }
}


