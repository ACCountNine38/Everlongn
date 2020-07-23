package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Sounds {
    public static float ambientPercentage = 1f, sfxPercentage = 1f;
    // music
    public static Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/dreamscape.mp3"));
    public static Music gameAmbient = Gdx.audio.newMusic(Gdx.files.internal("music/dreamAmbient.mp3"));

    // player sfx
    public static Sound[] steps = new Sound[3];

    public static Sound jump = Gdx.audio.newSound(Gdx.files.internal("audio/player/land.mp3"));
    public static Sound land = Gdx.audio.newSound(Gdx.files.internal("audio/player/land.mp3"));

    // sfx arrays
    public static Sound[] spider = new Sound[3];
    public static Sound[] basicImpact = new Sound[3];

    // arcane sfx
    public static Sound arcaneCaster = Gdx.audio.newSound(Gdx.files.internal("audio/arcane/arcaneCaster.mp3"));
    public static Sound arcaneReflection = Gdx.audio.newSound(Gdx.files.internal("audio/arcane/arcaneReflection.mp3"));
    public static Sound eruptionCast = Gdx.audio.newSound(Gdx.files.internal("audio/arcane/eruptionCast.mp3"));
    public static Sound eruptionLand = Gdx.audio.newSound(Gdx.files.internal("audio/arcane/eruptionLand.mp3"));
    public static Sound shadowCast = Gdx.audio.newSound(Gdx.files.internal("audio/arcane/shadowCast.mp3"));
    public static Sound shadowShift = Gdx.audio.newSound(Gdx.files.internal("audio/arcane/shadowShift.mp3"));
    public static Sound arcaneEscort = Gdx.audio.newSound(Gdx.files.internal("audio/arcane/arcaneEscort.mp3"));
    public static Sound arcaneRebound = Gdx.audio.newSound(Gdx.files.internal("audio/arcane/arcaneRebound.mp3"));
    public static Sound arcaneDevastation = Gdx.audio.newSound(Gdx.files.internal("audio/arcane/arcaneDevastation.mp3"));

    public static Sound bounce = Gdx.audio.newSound(Gdx.files.internal("audio/arcane/bounce.mp3"));
    public static Sound reflect = Gdx.audio.newSound(Gdx.files.internal("audio/arcane/reflect.mp3"));

    // melee sfx
    public static Sound bladeSwing1 = Gdx.audio.newSound(Gdx.files.internal("audio/melee/bladeSwing1.mp3"));
    public static Sound bladeSwing2 = Gdx.audio.newSound(Gdx.files.internal("audio/melee/bladeSwing2.mp3"));
    public static Sound bladeSwing3 = Gdx.audio.newSound(Gdx.files.internal("audio/melee/bladeSwing3.mp3"));
    public static Sound bladeSwing4 = Gdx.audio.newSound(Gdx.files.internal("audio/melee/bladeSwing4.mp3")); // basic blade

    public static Sound swordSwing1 = Gdx.audio.newSound(Gdx.files.internal("audio/melee/swordSwing1.mp3"));
    public static Sound swordSwing2 = Gdx.audio.newSound(Gdx.files.internal("audio/melee/swordSwing2.mp3"));
    public static Sound swordSwing3 = Gdx.audio.newSound(Gdx.files.internal("audio/melee/swordSwing3.mp3"));
    public static Sound swordSwing4 = Gdx.audio.newSound(Gdx.files.internal("audio/melee/swordSwing4.mp3")); // basic sword

    // throwing sfx
    public static Sound shurikenThrow = Gdx.audio.newSound(Gdx.files.internal("audio/throwing/shurikenThrow.mp3"));
    public static Sound shurikenLand = Gdx.audio.newSound(Gdx.files.internal("audio/throwing/shurikenLand.mp3"));
    public static Sound shurikenSlice = Gdx.audio.newSound(Gdx.files.internal("audio/throwing/slice.mp3"));
    public static Sound ninjaStarThrow = Gdx.audio.newSound(Gdx.files.internal("audio/throwing/ninjaStarThrow.mp3"));
    public static Sound ninjaStarLand = Gdx.audio.newSound(Gdx.files.internal("audio/throwing/ninjaStarLand.mp3"));
    public static Sound throwWeapon = Gdx.audio.newSound(Gdx.files.internal("audio/throwing/throwWeapon.mp3"));
    public static Sound throwKnifeLand = Gdx.audio.newSound(Gdx.files.internal("audio/throwing/throwKnifeLand.mp3"));
    public static Sound daggerLand = Gdx.audio.newSound(Gdx.files.internal("audio/throwing/daggerLand.mp3"));
    public static Sound bomb = Gdx.audio.newSound(Gdx.files.internal("audio/throwing/bomb.mp3"));

    // wood
    public static Sound chop1 = Gdx.audio.newSound(Gdx.files.internal("audio/wood/chop1.mp3"));
    public static Sound chop2 = Gdx.audio.newSound(Gdx.files.internal("audio/wood/chop2.mp3"));
    public static Sound chop3 = Gdx.audio.newSound(Gdx.files.internal("audio/wood/chop3.mp3"));
    public static Sound treeChop = Gdx.audio.newSound(Gdx.files.internal("audio/wood/treeChop.mp3"));
    public static Sound treeFall = Gdx.audio.newSound(Gdx.files.internal("audio/wood/treeFall.mp3"));

    // utils
    public static Sound buttonHover = Gdx.audio.newSound(Gdx.files.internal("audio/util/hover.mp3"));
    public static Sound buttonClick = Gdx.audio.newSound(Gdx.files.internal("audio/util/click.mp3"));

    public static void init() {
        steps[0] = Gdx.audio.newSound(Gdx.files.internal("audio/player/step1.mp3"));
        steps[1] = Gdx.audio.newSound(Gdx.files.internal("audio/player/step2.mp3"));
        steps[2] = Gdx.audio.newSound(Gdx.files.internal("audio/player/step3.mp3"));

        spider[0] = Gdx.audio.newSound(Gdx.files.internal("audio/entities/spider1.mp3"));
        spider[1] = Gdx.audio.newSound(Gdx.files.internal("audio/entities/spider2.mp3"));
        spider[2] = Gdx.audio.newSound(Gdx.files.internal("audio/entities/spider3.mp3"));

        basicImpact[0] = Gdx.audio.newSound(Gdx.files.internal("audio/melee/impact1.mp3"));
        basicImpact[1] = Gdx.audio.newSound(Gdx.files.internal("audio/melee/impact2.mp3"));
        basicImpact[2] = Gdx.audio.newSound(Gdx.files.internal("audio/melee/impact3.mp3"));
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

    public static void playMusic(Music music, float mod) {
        music.setVolume(ambientPercentage*mod);
        music.play();
    }
}
