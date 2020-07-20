package com.everlongn.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.assets.Herbs;
import com.everlongn.assets.Tiles;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class EarthTile extends Tile {
    private Sprite grass = new Sprite(Herbs.grass1);
    private boolean rotate = false;

    private int slantType = 0;

    public int treePlanted = -1;

    public EarthTile(int x, int y) {
        super(Tiles.earthTile, x, y, 1, true, true);
        slantType = (int)(Math.random()*2);
        if((int)(Math.random()*2) == 0) {
            grass.flip(true, false);
        }
    }

    @Override()
    public void tick() {
        checkAdjacent();
        if(numAdjacent == 4) {
            currentTexture = Tiles.earthTile;
            if (currentType != 0) {
                if(body != null) {
                    GameState.world.destroyBody(body);
                    body = null;
                }
                //body = Tool.createTile(x * Tile.TILESIZE - TILESIZE / 2, y * Tile.TILESIZE - TILESIZE / 2, 4, true, true, true, true,
                //        Constants.BIT_TILE, (short)(Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_PARTICLE | Constants.BIT_PROJECTILE), (short)0, this);
                currentType = 0;
            }
        } else if(numAdjacent == 3) {
            if(!left) {
                currentTexture = Tiles.earthTileExpose1[0];
            } else if(!right) {
                currentTexture = Tiles.earthTileExpose1[1];
            } else {
                currentTexture = Tiles.earthTile;
            }
            if (currentType != 1) {
                if(body != null) {
                    GameState.world.destroyBody(body);
                    body = null;
                }
                body = Tool.createTile(x * Tile.TILESIZE - TILESIZE / 2, y * Tile.TILESIZE - TILESIZE / 2, 4, true, true, true, true,
                        Constants.BIT_TILE, (short)(Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_PARTICLE | Constants.BIT_PROJECTILE), (short)0, this);
                currentType = 1;
            }
        } else if(numAdjacent == 2) {
            if (down && right) {
                if(slantType == 0) {
                    currentTexture = Tiles.earthTileExpose2[0];
                }  else {
                    currentTexture = Tiles.earthTileExpose2[4];
                }
                if (currentType != 2) {
                    if(body != null) {
                        GameState.world.destroyBody(body);
                        body = null;
                    }
                    body = Tool.createTile(x * Tile.TILESIZE - TILESIZE / 2, y * Tile.TILESIZE - TILESIZE / 2, 2, false, true, false, true,
                            Constants.BIT_TILE, (short)(Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_PARTICLE | Constants.BIT_PROJECTILE), (short)0, this);
                    currentType = 2;
                }
            } else if (down && left) {
                if(slantType == 0) {
                    currentTexture = Tiles.earthTileExpose2[1];
                } else {
                    currentTexture = Tiles.earthTileExpose2[5];
                }
                if (currentType != 3) {
                    if(body != null) {
                        GameState.world.destroyBody(body);
                        body = null;
                    }
                    body = Tool.createTile(x * Tile.TILESIZE - TILESIZE / 2, y * Tile.TILESIZE - TILESIZE / 2, 2, true, false, false, true,
                            Constants.BIT_TILE, (short)(Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_PARTICLE | Constants.BIT_PROJECTILE), (short)0, this);
                    currentType = 3;
                }
            } else if (up && right) {
                if(slantType == 0) {
                    currentTexture = Tiles.earthTileExpose2[2];
                } else {
                    currentTexture = Tiles.earthTileExpose2[6];
                }
                if (currentType != 4) {
                    if(body != null) {
                        GameState.world.destroyBody(body);
                        body = null;
                    }
                    body = Tool.createTile(x * Tile.TILESIZE - TILESIZE / 2, y * Tile.TILESIZE - TILESIZE / 2, 2, false, true, true, false,
                            Constants.BIT_TILE, (short)(Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_PARTICLE | Constants.BIT_PROJECTILE), (short)0, this);
                    currentType = 4;
                }
            } else if (up && left) {
                if(slantType == 0) {
                    currentTexture = Tiles.earthTileExpose2[3];
                } else {
                    currentTexture = Tiles.earthTileExpose2[7];
                }
                if (currentType != 5) {
                    if(body != null) {
                        GameState.world.destroyBody(body);
                        body = null;
                    }
                    body = Tool.createTile(x * Tile.TILESIZE - TILESIZE / 2, y * Tile.TILESIZE - TILESIZE / 2, 2, true, false, true, false,
                            Constants.BIT_TILE, (short)(Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_PARTICLE | Constants.BIT_PROJECTILE), (short)0, this);
                    currentType = 5;
                }
            } else if (up && down) {
                currentTexture = Tiles.earthTileExpose2[8];
                if (currentType != 1) {
                    if(body != null) {
                        GameState.world.destroyBody(body);
                        body = null;
                    }
                    body = Tool.createTile(x * Tile.TILESIZE - TILESIZE / 2, y * Tile.TILESIZE - TILESIZE / 2, 4, true, true, true, true,
                            Constants.BIT_TILE, (short)(Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_PARTICLE | Constants.BIT_PROJECTILE), (short)0, this);
                    currentType = 1;
                }
            } else {
                currentTexture = Tiles.earthTile;
                if (currentType != 1) {
                    if(body != null) {
                        GameState.world.destroyBody(body);
                        body = null;
                    }
                    body = Tool.createTile(x * Tile.TILESIZE - TILESIZE / 2, y * Tile.TILESIZE - TILESIZE / 2, 4, true, true, true, true,
                            Constants.BIT_TILE, (short)(Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_PARTICLE | Constants.BIT_PROJECTILE), (short)0, this);
                    currentType = 1;
                }
            }

        } else if(numAdjacent == 1) {
            if(right) {
                currentTexture = Tiles.earthTileExpose3[0];
                if (currentType != 6) {
                    if(body != null) {
                        GameState.world.destroyBody(body);
                        body = null;
                    }
                    body = Tool.createTile(x * Tile.TILESIZE - TILESIZE / 2, y * Tile.TILESIZE - TILESIZE / 2, 1, false, true, false, false,
                            Constants.BIT_TILE, (short)(Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_PARTICLE | Constants.BIT_PROJECTILE), (short)0, this);
                    currentType = 6;
                }
            } else if(left) {
                currentTexture = Tiles.earthTileExpose3[1];
                if (currentType != 7) {
                    if(body != null) {
                        GameState.world.destroyBody(body);
                        body = null;
                    }
                    body = Tool.createTile(x * Tile.TILESIZE - TILESIZE / 2, y * Tile.TILESIZE - TILESIZE / 2, 1, true, false, false, false,
                            Constants.BIT_TILE, (short)(Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_PARTICLE | Constants.BIT_PROJECTILE), (short)0, this);
                    currentType = 7;
                }
            } else if(down) {
                currentTexture = Tiles.earthTileExpose3[2];
                if (currentType != 8) {
                    if(body != null) {
                        GameState.world.destroyBody(body);
                        body = null;
                    }
                    body = Tool.createTile(x * Tile.TILESIZE - TILESIZE / 2, y * Tile.TILESIZE - TILESIZE / 2, 1, false, false, false, true,
                            Constants.BIT_TILE, (short)(Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_PARTICLE | Constants.BIT_PROJECTILE), (short)0, this);
                    currentType = 8;
                }
            } else if(up) {
                currentTexture = Tiles.earthTileExpose3[3];
                if (currentType != 9) {
                    if(body != null) {
                        GameState.world.destroyBody(body);
                        body = null;
                    }
                    body = Tool.createTile(x * Tile.TILESIZE - TILESIZE / 2, y * Tile.TILESIZE - TILESIZE / 2, 1, false, false, true, false,
                            Constants.BIT_TILE, (short)(Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_PARTICLE | Constants.BIT_PROJECTILE), (short)0, this);
                    currentType = 9;
                }
            }
        } else if(numAdjacent == 0){
            currentTexture = Tiles.earthTileExpose4;
            if (currentType != 10) {
                if(body != null) {
                    GameState.world.destroyBody(body);
                    body = null;
                }
                body = Tool.createTile(x * Tile.TILESIZE - TILESIZE / 2, y * Tile.TILESIZE - TILESIZE / 2, 0, false, false, false, false,
                        Constants.BIT_TILE, (short)(Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_PARTICLE | Constants.BIT_PROJECTILE), (short)0, this);
                currentType = 10;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if(digged) {
            alpha -= 0.05;
            if(alpha <= 0)
                alpha = 0;
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, alpha);
        }
        if(currentTexture != null) {
            batch.draw(currentTexture, x * Tile.TILESIZE - TILESIZE / 2, y * Tile.TILESIZE - TILESIZE / 2, TILESIZE, TILESIZE);
        }

        if(y+1 < GameState.worldHeight && GameState.tiles[x][y+1] == null && numAdjacent == 3) {
            if(rotate)
                batch.draw(grass, x*Tile.TILESIZE - TILESIZE/2, y*Tile.TILESIZE - TILESIZE/2 + Tile.TILESIZE, TILESIZE, TILESIZE);
            else
                batch.draw(grass, x*Tile.TILESIZE - TILESIZE/2, y*Tile.TILESIZE - TILESIZE/2 + Tile.TILESIZE, TILESIZE, TILESIZE);
        }
        if(digged) {
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1);
        }
        batch.end();
    }
}
