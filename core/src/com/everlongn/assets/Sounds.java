package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Sounds {
    public static float ambientPercentage, sfxPercentage;
    // music
    public static Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/dreamscape.mp3"));
    public static Music gameAmbient = Gdx.audio.newMusic(Gdx.files.internal("music/dreamscape.mp3"));

    // player sfx
    public static Sound[] steps = new Sound[3];

    public static Sound jump = Gdx.audio.newSound(Gdx.files.internal("audio/player/land.mp3"));
    public static Sound land = Gdx.audio.newSound(Gdx.files.internal("audio/player/land.mp3"));

    // entities sfx
    public static Sound[] spider = new Sound[3];

    // arcane sfx
    public static Sound arcaneCaster = Gdx.audio.newSound(Gdx.files.internal("audio/arcane/arcaneCaster.mp3"));
    public static Sound arcaneReflection = Gdx.audio.newSound(Gdx.files.internal("audio/arcane/arcaneReflection.mp3"));
    public static Sound eruptionCast = Gdx.audio.newSound(Gdx.files.internal("audio/arcane/eruptionCast.mp3"));
    public static Sound eruptionLand = Gdx.audio.newSound(Gdx.files.internal("audio/arcane/eruptionLand.mp3"));
    public static Sound shadowCast = Gdx.audio.newSound(Gdx.files.internal("audio/arcane/shadowCast.mp3"));
    public static Sound shadowShift = Gdx.audio.newSound(Gdx.files.internal("audio/arcane/shadowShift.mp3"));
    public static Sound arcaneEscort = Gdx.audio.newSound(Gdx.files.internal("audio/arcane/arcaneEscort.mp3"));

    // melee sfx
    public static Sound bladeSwing = Gdx.audio.newSound(Gdx.files.internal("audio/melee/bladeSwing.mp3"));

    public static void init() {
        steps[0] = Gdx.audio.newSound(Gdx.files.internal("audio/player/step1.mp3"));
        steps[1] = Gdx.audio.newSound(Gdx.files.internal("audio/player/step2.mp3"));
        steps[2] = Gdx.audio.newSound(Gdx.files.internal("audio/player/step3.mp3"));

        spider[0] = Gdx.audio.newSound(Gdx.files.internal("audio/entities/spider1.mp3"));
        spider[1] = Gdx.audio.newSound(Gdx.files.internal("audio/entities/spider2.mp3"));
        spider[2] = Gdx.audio.newSound(Gdx.files.internal("audio/entities/spider3.mp3"));
    }

    public static void playSound(Sound sound) {
        sound.play(sfxPercentage);
    }

    public static void playSound(Sound sound, float mod) {
        sound.play(sfxPercentage*mod);
    }

    public static void playMusic(Music music) {
        music.setVolume(ambientPercentage);
        music.play();
    }
}
