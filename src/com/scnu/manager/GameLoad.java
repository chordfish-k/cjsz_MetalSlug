package com.scnu.manager;

import com.scnu.anim.AnimationClip;
import com.scnu.anim.SpriteImg;
import com.scnu.element.ElementObj;
import com.scnu.element.RootObj;
import com.scnu.element.map.BarrierObj;
import com.scnu.geometry.Vector2;
import com.scnu.show.GameJFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

/**
 * 加载器，读取配置文件的工具类
 * 提供一系列static方法
 * @author LSR
 */
public class GameLoad {
    // 资源管理器
    private static final ElementManager em = ElementManager.getManager();
    // 图片集合
    public static Map<String, SpriteImg> imgMap = new HashMap<>();
    // 动画集合
    public static Map<String, AnimationClip >aniMap = new HashMap<>();
    // 元素集合
    public static Map<String, Class<?>> objMap = new HashMap<>();
    // 碰撞检查集合
    public static Map<ElementType, List<ElementType>> colMap = new HashMap<>();

    // 读取配置文件的类
    private static final Properties pro = new Properties();

    public static void loadRoot() {
        em.eleRoot = new RootObj().create("");
        em.addElement(em.eleRoot, ElementType.DEFAULT);
    }
    public static void loadBackground() {
        ElementObj back = createElementByName("background");
        em.eleRoot.addChild(back);
        em.addElement(back, ElementType.BACK);
    }

    /**
     * 传入地图id加载地图
     * @param mapId 地图文件id
     */
    public static void LoadMap(int mapId) {
        String mapName = "com/scnu/text/" + mapId + ".map";
        // 用io流获取文件对象，加载包内文件
        ClassLoader classLoader = GameLoad.class.getClassLoader(); // 类加载器
        InputStream maps = classLoader.getResourceAsStream(mapName);
        if (maps == null) {
            System.out.println("配置文件读取异常，清重新安装");
            return;
        }
        try {
            pro.load(maps);
            Enumeration<?> names = pro.propertyNames();
            while (names.hasMoreElements()) {
                String key = names.nextElement().toString();
                String[] arrs = pro.getProperty(key).split(";");
                if (key.equals("ENEMY")) {
                    // 后续改为动态加载+对象池
                    for (String arr : arrs) {
                        ElementObj enemy = createElementByName("enemy", arr + "," + "left");
                        em.addElement(enemy, ElementType.ENEMY);
                        ElementManager.eleRoot.addChild(enemy);
                    }
                    continue;
                }
//                for (String arr : arrs) {
//                    ElementObj element = new MapObj().create(key + "," + arr);
//                    em.addElement(element, ElementType.MAP);
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        pro.clear();

        //创建地图边界
        int barrierWidth = 10;
        int offsetH = -40;
        int offsetW = -10;
        BarrierObj left  = new BarrierObj().setRect(
                new Rectangle(-barrierWidth,0, barrierWidth, GameJFrame.SIZE_H));
        BarrierObj right = new BarrierObj().setRect(
                new Rectangle(GameJFrame.SIZE_W + offsetW,0, barrierWidth, GameJFrame.SIZE_H));
        BarrierObj up    = new BarrierObj().setRect(
                new Rectangle(0,-barrierWidth, GameJFrame.SIZE_W, barrierWidth));
        BarrierObj down  = new BarrierObj().setRect(
                new Rectangle(0, GameJFrame.SIZE_H + offsetH, GameJFrame.SIZE_W, barrierWidth));

        em.addElement(left,ElementType.MAP);
        em.addElement(right,ElementType.MAP);
        em.addElement(up,ElementType.MAP);
        em.addElement(down,ElementType.MAP);
    }

    /**
     * 加载图片资源
     */
    public static void loadImage() {
        String fileName = "com/scnu/text/GameData.re";
        // 用io流获取文件对象，加载包内文件
        ClassLoader classLoader = GameLoad.class.getClassLoader(); // 类加载器
        InputStream ist = classLoader.getResourceAsStream(fileName);
        if (ist == null) {
            System.out.println("配置文件读取异常，清重新安装");
            return;
        }
        try {
            pro.clear();
            pro.load(ist);
            Set<Object> set = pro.keySet();
            for (Object o : set) {

                String path = "";
                Vector2 center = new Vector2();

                String[] split = pro.getProperty(o.toString()).split("[|]");
                path = split[0];

                if (split.length > 1) {
                    String[] s = split[1].split(",");
                    center.x = Integer.parseInt(s[0]);
                    center.y = Integer.parseInt(s[1]);
                }

                // 存入map中
                ImageIcon ic = new ImageIcon(path);

                imgMap.put(o.toString(), new SpriteImg(ic, center));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        pro.clear();
    }

    /**
     * 加载动画资源
     */
    public static void loadAnim() {
        String fileName = "com/scnu/text/Animation.re";
        // 用io流获取文件对象，加载包内文件
        ClassLoader classLoader = GameLoad.class.getClassLoader(); // 类加载器
        InputStream ist = classLoader.getResourceAsStream(fileName);
        if (ist == null) {
            System.out.println("配置文件读取异常，清重新安装");
            return;
        }
        try {
            pro.clear();
            pro.load(ist);
            Set<Object> set = pro.keySet();
            for (Object o : set) {
                AnimationClip ac = new AnimationClip();
                String[] sp = pro.getProperty(o.toString()).split("[|]");
                ac.frameNames.addAll(Arrays.asList(sp[0].split(";")));
                if (sp.length > 1)
                    ac.setFrameSpan(Integer.parseInt(sp[1]));
                // 存入map中
                aniMap.put(o.toString(), ac);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        pro.clear();
    }

    public static void loadCollision() {
        String fileName = "com/scnu/text/Collision.re";

        ClassLoader classLoader = GameLoad.class.getClassLoader(); // 类加载器
        InputStream ist = classLoader.getResourceAsStream(fileName);
        if (ist == null) {
            System.out.println("配置文件读取异常，清重新安装");
            return;
        }
        try {
            for (ElementType type : em.getGameElements().keySet()) {
                colMap.put(type, new ArrayList<>());
            }

            pro.load(ist);
            Set<Object> set = pro.keySet();
            for (Object o : set) {
                ElementType key = ElementType.valueOf(o.toString().toUpperCase());

                String[] colList = pro.getProperty(o.toString()).split(",");
                for (String col : colList) {
                    ElementType type = ElementType.valueOf(col.toUpperCase());
                    colMap.get(key).add(type);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        pro.clear();
    }

    public static void loadElement() {
        String fileName = "com/scnu/text/Elements.re";

        ClassLoader classLoader = GameLoad.class.getClassLoader(); // 类加载器
        InputStream ist = classLoader.getResourceAsStream(fileName);
        if (ist == null) {
            System.out.println("配置文件读取异常，清重新安装");
            return;
        }
        try {
            pro.load(ist);
            Set<Object> set = pro.keySet();
            for (Object o : set) {
                String packageName = pro.getProperty(o.toString());
                // 使用反射的方式直接根据名字获取这个类
                Class<?> cForName = Class.forName(packageName);
                objMap.put(o.toString(), cForName);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        pro.clear();
    }

    public static void loadPlayer() {
        // 创建元素
        ElementObj obj = createElementByName("hero", "450,520");

        if (obj != null) {
            em.addElement(obj, ElementType.PLAYER);
            em.eleRoot.addChild(obj);
        }
    }

    public static void loadEnemies(List<Vector2> posList) {
//        Random ran = new Random();

//        for (Vector2 p : posList) {
//            ElementObj enemy = createElementByName("enemy", p.x+","+p.y+","+"up");
//            em.addElement(enemy, ElementType.ENEMY);
//        }
    }


    public static ElementObj createElementByName(String name) {
        return createElementByName(name, "");
    }

    /**
     * 根据类名创建元素类对象
     * @param name 类名（在配置文件中记录）
     * @param data 调用该元素类的create()方法要用的字符串数据
     * @return 该元素类的新对象
     */
    public static ElementObj createElementByName(String name, String data) {
        ElementObj obj = null;
        try {
            Object newIns = objMap.get(name).newInstance();
            if (newIns instanceof ElementObj) {
                obj = ((ElementObj) newIns).create(data);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
