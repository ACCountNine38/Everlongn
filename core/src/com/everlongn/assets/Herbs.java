package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Herbs {
    public static Texture grass0, grass1, grass2;
    public static Texture treeSprite, treeStemSprite, treeRootsSprite, aerogelSprite, diagonalAerogelSprite;

    public static TextureRegion treeStem, treeRoots, tree1, aerogel[], diagonalAerogel[];

    public static void init() {
        grass0 = new Texture(Gdx.files.internal("herbs/Grass0.png"));
        grass1 = new Texture(Gdx.files.internal("herbs/Grass1.png"));
        grass2 = new Texture(Gdx.files.internal("herbs/Grass2.png"));
        treeSprite = new Texture(Gdx.files.internal("herbs/tree1.png"));
        tree1 = new TextureRegion(treeSprite, 0, 0, treeSprite.getWidth(), treeSprite.getHeight());

        treeStemSprite = new Texture(Gdx.files.internal("herbs/treeStem.png"),true);
        treeStemSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        treeStem = new TextureRegion(treeStemSprite, 0, 0, treeStemSprite.getWidth(), treeStemSprite.getHeight());
        treeRootsSprite = new Texture(Gdx.files.internal("herbs/treeRoots.png"),true);
        treeRootsSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        treeRoots = new TextureRegion(treeRootsSprite, 0, 0, treeRootsSprite.getWidth(), treeRootsSprite.getHeight());

        aerogelSprite = new Texture(Gdx.files.internal("herbs/aerogel.png"), true);
        aerogelSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        aerogel = new TextureRegion[4];
        aerogel[0] = new TextureRegion(aerogelSprite, 0, 0, aerogelSprite.getWidth(), aerogelSprite.getHeight());
        aerogel[1] = new TextureRegion(aerogelSprite, 0, 0, aerogelSprite.getWidth(), aerogelSprite.getHeight());
        aerogel[1].flip(true, false);
        aerogel[2] = new TextureRegion(aerogelSprite, 0, 0, aerogelSprite.getWidth(), aerogelSprite.getHeight());
        aerogel[2].flip(false, true);
        aerogel[3] = new TextureRegion(aerogelSprite, 0, 0, aerogelSprite.getWidth(), aerogelSprite.getHeight());
        aerogel[3].flip(true, true);

        diagonalAerogelSprite = new Texture(Gdx.files.internal("herbs/aerogelDiagonal.png"), true);
        diagonalAerogelSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        diagonalAerogel = new TextureRegion[4];
        diagonalAerogel[0] = new TextureRegion(diagonalAerogelSprite, 0, 0, diagonalAerogelSprite.getWidth(), diagonalAerogelSprite.getHeight());
        diagonalAerogel[1] = new TextureRegion(diagonalAerogelSprite, 0, 0, diagonalAerogelSprite.getWidth(), diagonalAerogelSprite.getHeight());
        diagonalAerogel[1].flip(true, false);
        diagonalAerogel[2] = new TextureRegion(diagonalAerogelSprite, 0, 0, diagonalAerogelSprite.getWidth(), diagonalAerogelSprite.getHeight());
        diagonalAerogel[2].flip(false, true);
        diagonalAerogel[3] = new TextureRegion(diagonalAerogelSprite, 0, 0, diagonalAerogelSprite.getWidth(), diagonalAerogelSprite.getHeight());
        diagonalAerogel[3].flip(true, true);
    }
}
