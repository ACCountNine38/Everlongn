package com.everlongn.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Animation {
    public Texture[] textures;
    public float update, timer;
    public boolean looping;

    public int currentIndex;

    public Animation(Texture[] textures, float update, boolean looping) {
        this.textures = textures;
        this.update = update;
        this.looping = looping;
    }

    public void tick(float delta) {
        timer += delta;
        if(timer == update) {
            currentIndex++;
            if(currentIndex >= textures.length) {
                if(looping)
                    currentIndex = 0;
                else
                    currentIndex = textures.length - 1;
            }
            timer = 0;
        }
    }

    public Texture getFrame() {
        return textures[currentIndex];
    }
}
