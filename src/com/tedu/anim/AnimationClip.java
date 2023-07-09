package com.tedu.anim;

import com.tedu.manager.GameLoad;
import com.tedu.show.GameJFrame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class AnimationClip {
    public List<String> frameNames = new ArrayList<>();
    private long lastTime = 0;
    public int frameSpan = 4;
    public int index = 0;

    public int size() {
        return frameNames.size();
    }

    public ImageIcon nextFrame(long currentTime) {
        if (currentTime > lastTime + frameSpan) {
            lastTime = currentTime;

            if (index < size()-1)
                index++;
            else
                reset();
        }

        return GameLoad.imgMap.get(frameNames.get(index));
    }

    public void reset() {
        index = 0;
    }
}
