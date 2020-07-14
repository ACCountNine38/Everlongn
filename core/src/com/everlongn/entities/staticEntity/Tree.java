package com.everlongn.entities.staticEntity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.everlongn.assets.Herbs;
import com.everlongn.assets.Tiles;
import com.everlongn.entities.EntityManager;
import com.everlongn.entities.Player;
import com.everlongn.entities.StaticEntity;
import com.everlongn.game.ControlCenter;
import com.everlongn.items.Inventory;
import com.everlongn.items.Item;
import com.everlongn.states.GameState;
import com.everlongn.tiles.Tile;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

import static com.everlongn.utils.Constants.PPM;

public class Tree extends StaticEntity {
    public boolean leftShift, rightShift, reset, fallen, chopped;
    public float currentAngle, fallSpeed = 0.05f, treeAlpha = 1f, impactForce;
    public int direction;

    public Tree(float x, float y, int height) {
        super(x, y, 50, height, 5);

        resetHealth(3000);
        resistance = 10;
    }

    @Override
    public void tick() {
        if(!fallen) {
            if (leftShift) {
                currentAngle -= impactForce/4;
                if (currentAngle <= -impactForce) {
                    reset = true;
                    leftShift = false;
                }
            } else if (rightShift) {
                currentAngle += impactForce/4;
                if (currentAngle >= impactForce) {
                    reset = true;
                    rightShift = false;
                }
            } else if (reset) {
                if (currentAngle < 0) {
                    currentAngle += 0.5;
                } else if (currentAngle > 0) {
                    currentAngle -= 0.5;
                } else {
                    reset = false;
                }
            }
        } else if(!chopped) {
            fallSpeed += 0.03;
            if(fallSpeed > 1)
                fallSpeed = 1;
            if(leftShift) {
                currentAngle -= fallSpeed;
                if (currentAngle <= -105) {
                    for(int i = 0; i < Math.random()*5; i++)
                        EntityManager.items.add(Item.log.createNew((int)(Math.random()*(x * PPM - Tile.TILESIZE * 2 - Tile.TILESIZE / 2)), y * PPM - Tile.TILESIZE * 2 + 100, 1, (float) Math.random() * 300 - 150, (float) Math.random() * 50 + 100));
                    chopped = true;
                }
            } else if(rightShift) {
                currentAngle += fallSpeed;

                if (currentAngle >= 105) {
                    for(int i = 0; i < Math.random()*5; i++)
                        EntityManager.items.add(Item.log.createNew((int)(Math.random()*(x * PPM - Tile.TILESIZE * 2 - Tile.TILESIZE / 2)), y * PPM - Tile.TILESIZE * 2 + 100, 1, (float) Math.random() * 300 - 150, (float) Math.random() * 50 + 100));
                    chopped = true;
                }
            }

            for(int i = 0; i < GameState.chunks.length; i++) {
                for (int j = 0; j < GameState.chunks[i].length; j++) {
                    if (i >= Player.currentChunkX - 2 && i <= Player.currentChunkX + 2 &&
                            j >= Player.currentChunkY - 2 && j <= Player.currentChunkY + 2) {
                            for (int x = i * GameState.chunkSize; x < i * GameState.chunkSize + GameState.chunkSize; x++) {
                                for (int y = j * GameState.chunkSize; y < j * GameState.chunkSize + GameState.chunkSize; y++) {
                                    if (GameState.tiles[x][y] != null && GameState.tiles[x][y].getBound().overlaps(getFallBound(direction))) {
                                        for(int k = 0; k < Math.random()*5; k++)
                                            EntityManager.items.add(Item.log.createNew((int)(Math.random()*(x * PPM - Tile.TILESIZE * 2 - Tile.TILESIZE / 2)), y * PPM - Tile.TILESIZE * 2 + 100, 1, (float) Math.random() * 300 - 150, (float) Math.random() * 50 + 100));
                                        chopped = true;
                                    }
                                }
                            }
                        }

                }
            }
        }
    }

    @Override
    public Rectangle getBound() {
        return new Rectangle(x * PPM - Tile.TILESIZE/4, y * PPM - Tile.TILESIZE*2, Tile.TILESIZE, height*Tile.TILESIZE);
    }

    public Rectangle getFallBound(int direction) {
        if(direction == 0) {
            return new Rectangle(x * PPM - Tile.TILESIZE/4 - (float)Math.sin(Math.toRadians(Math.abs(currentAngle))) * (height * Tile.TILESIZE * 2 / 3), y * PPM - Tile.TILESIZE*2 + (float)Math.cos(Math.toRadians(Math.abs(currentAngle))) * (height * Tile.TILESIZE * 2 / 3),
                    (float)Math.sin(Math.toRadians(Math.abs(currentAngle))) * (height * Tile.TILESIZE * 2 / 3), Tile.TILESIZE/2);
        } else {
            return new Rectangle(x * PPM - Tile.TILESIZE/4, y * PPM - Tile.TILESIZE*2 + (float)Math.cos(Math.toRadians(Math.abs(currentAngle))) * (height * Tile.TILESIZE * 2 / 3),
                    (float)Math.sin(Math.toRadians(Math.abs(currentAngle))) * (height * Tile.TILESIZE * 2 / 3), Tile.TILESIZE/2);
        }
    }

    public void impact(int damage, int direction) {
        hurt(damage, GameState.difficulty);
        impactForce = damage/50;
        if(direction == 0) {
            rightShift = true;
        } else {
            leftShift = true;
        }
        if(health <= 2000 && !fallen && !chopped) {
            this.direction = direction;
            fallen = true;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if(!fallen) {
            batch.draw(Herbs.tree1,
                    x * PPM - Tile.TILESIZE * 2 - Tile.TILESIZE / 2, y * PPM - Tile.TILESIZE * 2,
                    5 * Tile.TILESIZE / 2,
                    0,
                    5 * Tile.TILESIZE, height * Tile.TILESIZE,
                    1f, 1f, currentAngle);
        } else {
            batch.draw(Herbs.treeRoots, x * PPM - Tile.TILESIZE * 2 - Tile.TILESIZE / 2, y * PPM - Tile.TILESIZE * 2, 5 * Tile.TILESIZE, height * Tile.TILESIZE);

            if(!chopped) {
                batch.draw(Herbs.treeStem,
                        x * PPM - Tile.TILESIZE * 2 - Tile.TILESIZE / 2, y * PPM - Tile.TILESIZE * 2,
                        5 * Tile.TILESIZE / 2,
                        height * Tile.TILESIZE * 1 / 3,
                        5 * Tile.TILESIZE, height * Tile.TILESIZE,
                        1f, 1f, currentAngle);
            } else if(treeAlpha > 0){
                batch.draw(Tiles.blackTile, getFallBound(direction).x, getFallBound(direction).y, getFallBound(direction).width, getFallBound(direction).height);
                treeAlpha -= 0.01;
                if(treeAlpha <= 0)
                    treeAlpha = 0;
                batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, treeAlpha);
                batch.draw(Herbs.treeStem,
                        x * PPM - Tile.TILESIZE * 2 - Tile.TILESIZE / 2, y * PPM - Tile.TILESIZE * 2,
                        5 * Tile.TILESIZE / 2,
                        height * Tile.TILESIZE * 1 / 3,
                        5 * Tile.TILESIZE, height * Tile.TILESIZE,
                        1f, 1f, currentAngle);
                batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1);
            }
        }
        batch.end();
    }

    @Override
    public void finish() {

    }
}
