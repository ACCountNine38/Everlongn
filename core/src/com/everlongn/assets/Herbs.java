package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Herbs {
    public static Texture grass0, grass1, grass2;
    public static Texture treeSprite, treeStemSprite, treeRootsSprite;

    public static TextureRegion treeStem, treeRoots, tree1;

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
    }
}
