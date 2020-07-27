package com.everlongn.utils;

public class ScreenShake {
    public float duration;
    public int force;
    public boolean fade;

    public ScreenShake(int force, float duration) {
        this.duration = duration;
        this.force = force;
    }

    public ScreenShake(int force, float duration, boolean fade) {
        this.force = force;
        this.duration = duration;
        this.fade = fade;
    }
}
