package com.scnu.anim;

import com.scnu.element.ElementObj;
import com.scnu.manager.GameLoad;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动画帧序列组
 * @author LSR
 */
public class AnimationClip {
    private class AnimationRecord {
        int index = 0;
        long lastTime = 0;
    }
    public List<String> frameNames = new ArrayList<>();
    private int frameSpan = 4;
    private final Map<ElementObj, AnimationRecord> indexList = new HashMap();

    public int size() {
        return frameNames.size();
    }

    public SpriteImg nextFrame(long currentTime, ElementObj obj) {
        // 如果超一定时间，则复原
        if (currentTime > indexList.get(obj).lastTime + getTotalTime()) {
            reset(obj);
        }
        int index = indexList.get(obj).index;
        if (currentTime > indexList.get(obj).lastTime + frameSpan) {
            indexList.get(obj).lastTime = currentTime;


            if (index < size()-1) {
                index++;
                indexList.get(obj).index = index;
            } else
                reset(obj);
        }

        return GameLoad.imgMap.get(frameNames.get(index));
    }

    public void reset(ElementObj obj) {
        indexList.get(obj).index = 0;
    }

    public void setFrameSpan(int frameSpan) {
        this.frameSpan = frameSpan;
    }

    public int getTotalTime() {
        return frameSpan * frameNames.size();
    }

    public int getIndex(ElementObj obj) {
        if (indexList.containsKey(obj)) {
            return indexList.get(obj).index;
        }
        return 0;
    }

    public void requireAnime(ElementObj obj) {
        if (!indexList.containsKey(obj)) {
            indexList.put(obj, new AnimationRecord());
        }
    }
}
