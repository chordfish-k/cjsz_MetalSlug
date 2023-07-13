package com.scnu.geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * 多边形类
 * @author LSR
 */
public class Polygon {
    protected List<Vector2> points = null;
    protected Vector2 center = new Vector2();
    private List<List<Vector2>> forceOnPoints = null;

    /**
     * 创建多边形
     * @param center 多边形的中心点，如果用于移动物体的碰撞体，应将改变中心点以改变
     *               整个多边形的位置
     */
    public Polygon(Vector2 center) {
        this.points = new ArrayList<>();
        this.center = center;
        this.forceOnPoints = new ArrayList<>();
    }

    /**
     * 向该多边形添加一个点，用相对于中心点的坐标表示
     * @param p
     */
    public void addPoint(Vector2 p) {
        this.points.add(p);
        this.forceOnPoints.add(new ArrayList<>());
    }

    /**
     * 返回该多边形的所有点
     * @return
     */
    public List<Vector2> getPoints() {
        return this.points;
    }

    /**
     * 清除多边形的所有点
     */
    public void clear() {
        this.points.clear();
        this.forceOnPoints.clear();
    }

    public Vector2 getCenter() {
        return center;
    }

    public void setCenter(Vector2 center) {
        Vector2 oldCenter =  this.center.clone();
        this.center = center;
        Vector2 d = center.sub(oldCenter);

        for(int i=0; i<getPoints().size(); i++) {
            Vector2 p = getPoints().get(i);
            p.set(p.add(d));
            //System.out.println(p.y);
        }
        //System.out.println(getPoints().get(0).y);
    }

    /**
     * 将此形状根据中心顺时针旋转某个角度
     * @param ang
     */
    public void rotateBy(float ang) {
        float arc = ang * (float)Math.PI / 180f;
        for (Vector2 p : points) {
            Vector2 m = p.sub(this.center);
            float cos = (float)Math.cos(arc);
            float sin = (float)Math.sin(arc);
            Vector2 v = new Vector2(
                    m.x * cos - m.y * sin,
                    m.y * cos + m.x * sin);
            p.set(v.add(this.center));
        }
    }

    /**
     * 检测一个点是否在该多边形内
     * @param p 二维点
     * @return 检测结果
     */
    public boolean testPoint(Vector2 p) {
        Vector2 o = this.center.clone();
        //System.out.println("p: " + p);
        //System.out.println("===");
        for (int i=0; i<points.size(); i++) {
            Vector2 a = points.get(i);
            Vector2 b = points.get((i+1) % points.size());
            Vector2 p2a = a.sub(p);
            Vector2 p2b = b.sub(p);
            // 叉乘
            float re = p2b.x * p2a.y - p2b.y * p2a.x;
            if (re < 0) {
                return false;
            }
        }
        return true;
    }

    public boolean testPolygon(Polygon polygon) {
        for (Vector2 p : polygon.getPoints()) {
            if (testPoint(p)) {
                return true;
            }
        }
        for (Vector2 p : getPoints()) {
            if (polygon.testPoint(p)) {
                return true;
            }
        }
        return false;
    }

    public Polygon clone() {
        Polygon p = new Polygon(this.center);
        for(Vector2 point : this.points) {
            p.addPoint(point.clone());
        }
        return p;
    }

    public int countOfPoints() {
        return this.points.size();
    }

    public Vector2 calcJoinForce() {
        Vector2 jf = new Vector2();
        for (List<Vector2> li : forceOnPoints) {
            for (Vector2 f : li) {
                jf.add(f);
            }
        }
        return jf;
    }

    public float calcAngularForce() {
        Vector2 o = this.center.clone();
        // 施加在这个点上的力(垂直于点到中心的连线)
        float force = 0f;

        int count = 0;
        for (List<Vector2> li : forceOnPoints) {
            // 从中心到顶点的向量
            Vector2 o2jf = points.get(count).sub(o);
            // 每个顶点的合力
            Vector2 jf = new Vector2();
            for (Vector2 f : li) {
                jf.add(f);
            }

            // 叉乘取符号
            float sig = (jf.x * o2jf.y - jf.y * o2jf.x) > 0 ? 1 : -1;
            // 力矩
            float fr = jf.projectOn(o2jf).norm() * o2jf.norm();

            force += sig * fr;

            count++;
        }
        return force;
    }
    /**
     * 该形状在某向量上投影的长度
     * @param dir 投影方向
     * @return 投影长度
     */
    public float projectOn(Vector2 dir) {
        float maxNorm = 0f;
        for (Vector2 a : points) {
            for (Vector2 b : points) {
                if (a.equal(b))
                    continue;
                float tmp = a.sub(b).projectOn(dir).norm();
                System.out.println("> "+tmp + ", " + dir);
                maxNorm = Math.max(maxNorm, tmp);
            }
        }
        return maxNorm;
    }

    /**
     * 将该形状向某个方向拉伸拉伸
     * @param dir 拉伸方向和增长比率
     * @return 拉伸后的形状
     */
    public Polygon scaleToward(Vector2 dir) {
        float l = projectOn(dir);
        float k = dir.norm() / l; // 缩放因子
        System.out.println(dir + ", " + l + ", " + k);

//        List<Vector2> list = new ArrayList<>(points);
//        //根据各向量在dir上投影长度进行排序
//        list.sort((a, b) -> {
//            float normA = a.sub(this.center).projectOn(dir).norm();
//            float normB = b.sub(this.center).projectOn(dir).norm();
//
//
//            if (normA == normB)
//                return 0;
//            if (normA > normB)
//                return 1;
//            return -1;
//        });

        for (Vector2 p : points) {

            Vector2 v = p.sub(this.center);
//            System.out.println(p + ",");
            Vector2 vp = v.projectOn(dir); // 与dir平行的分量，受缩放因子影响
            vp = vp.mul(k);
            Vector2 ndir = v.sub(dir);
            Vector2 vn = v.projectOn(ndir); // 与dir垂直的分量，不受缩放因子影响
            v = vp.add(vn).add(this.center);
//            System.out.println(v);
            p.set(v);
        }
        return this;
    }

}
