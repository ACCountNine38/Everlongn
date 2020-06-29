package com.everlongn.utils.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.assets.Sounds;
import com.everlongn.assets.UI;
import com.everlongn.game.ControlCenter;

public class TextArea extends UIComponent {
    public boolean focused, numbersOnly;
    public String currentText = "";
    public int limit;
    public boolean center, soundCanPlay;

    public TextArea(float x, float y, float width,
                    String text, boolean clickable, BitmapFont font, boolean numbersOnly, int limit, boolean center) {
        super(x, y, text, clickable, font);
        currentText = text;
        this.numbersOnly = numbersOnly;
        this.width = width;
        this.limit = limit;
        this.center = center;
    }

    @Override
    public void tick() {
        if (Gdx.input.getX() >= x && Gdx.input.getX() < x + width &&
                Gdx.input.getY() >= ControlCenter.height-y-height - 20 && Gdx.input.getY() < ControlCenter.height - y) {
            hover = true;
            if(soundCanPlay) {
                Sounds.playSound(Sounds.buttonHover);
                soundCanPlay = false;
            }
        } else {
            hover = false;
            soundCanPlay = true;
        }

        if(focused) {
            if(currentText.length() < limit) {
                checkInput();
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.DEL)) {
                if(currentText.length() > 0) {
                    currentText = currentText.substring(0, currentText.length()-1);
                }
            }
        }
    }

    public void checkInput() {
        if(!numbersOnly) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                currentText += " ";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                currentText += "Q";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                currentText += "W";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                currentText += "E";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                currentText += "R";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
                currentText += "T";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
                currentText += "Y";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
                currentText += "U";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                currentText += "I";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
                currentText += "O";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                currentText += "P";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                currentText += "A";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                currentText += "S";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                currentText += "D";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                currentText += "F";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
                currentText += "G";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
                currentText += "H";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
                currentText += "J";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
                currentText += "K";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
                currentText += "L";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                currentText += "Z";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                currentText += "X";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                currentText += "C";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
                currentText += "V";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                currentText += "B";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
                currentText += "N";
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
                currentText += "M";
            } else if(Gdx.input.isKeyJustPressed(Input.Keys.APOSTROPHE)) {
                currentText+="'";
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            currentText+="1";
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            currentText+="2";
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            currentText+="3";
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            currentText+="4";
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            currentText+="5";
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
            currentText+="6";
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
            currentText+="7";
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) {
            currentText+="8";
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) {
            currentText+="9";
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            currentText+="0";
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if(!focused && !hover) {
            batch.draw(UI.worldSelect, x, y, width, height + 20);
        } else {
            batch.draw(UI.worldSelected, x, y, width, height + 20);
        }
        font.setColor(Color.WHITE);
        if(center) {
            layout.setText(font, currentText);
            font.draw(batch, currentText, x + width/2 - layout.width/2, y + height + 10);
        } else {
            font.draw(batch, currentText, x + 10, y + height + 10);
        }
    }

    @Override
    public void onClick() {

    }
}
