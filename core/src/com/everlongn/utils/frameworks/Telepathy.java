package com.everlongn.utils.frameworks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.everlongn.assets.UI;
import com.everlongn.states.GameState;
import com.everlongn.utils.TextManager;

import java.util.ArrayList;

public class Telepathy {
    public static boolean focused;
    public int x, y, width, height;
    public String currentText = "";
    public static char commandSymbol = '/';

    public InputListener input;

    public static ArrayList<Message> messages = new ArrayList<Message>();

    public Telepathy(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void tick() {
        for(int i = 0; i < messages.size(); i++) {
            messages.get(i).tick();
        }
        if(!focused) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && !GameState.options.active) {
                focused = true;
            }
        } else {
            checkKeysTyped();
            if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                if(!currentText.equals("")) {
                    messages.add(new Message(x, y + height / 2, height, currentText, currentText.charAt(0) == commandSymbol, Color.WHITE));
                    currentText = "";
                }
                focused = false;
            } else if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                focused = false;
                currentText = "";
            }
        }
    }

    public void checkKeysTyped() {
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.Q))
                currentText += "Q";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.W))
                currentText += "W";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.E))
                currentText += "E";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.R))
                currentText += "R";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.T))
                currentText += "T";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.Y))
                currentText += "Y";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.U))
                currentText += "U";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.I))
                currentText += "I";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.O))
                currentText += "O";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.P))
                currentText += "P";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.A))
                currentText += "A";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.S))
                currentText += "S";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.D))
                currentText += "D";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.F))
                currentText += "F";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.G))
                currentText += "G";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.H))
                currentText += "H";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.J))
                currentText += "J";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.K))
                currentText += "K";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.L))
                currentText += "L";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.Z))
                currentText += "Z";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.X))
                currentText += "X";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.C))
                currentText += "C";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.V))
                currentText += "V";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.B))
                currentText += "B";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.N))
                currentText += "N";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.M))
                currentText += "M";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
                currentText += " ";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.COMMA))
                currentText += "<";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.PERIOD))
                currentText += ">";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.SLASH))
                currentText += "?";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS))
                currentText += "_";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS))
                currentText += "+";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.SEMICOLON))
                currentText += ":";

            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0))
                currentText += ")";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1))
                currentText += "!";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2))
                currentText += "@";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3))
                currentText += "#";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4))
                currentText += "$";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5))
                currentText += "%";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6))
                currentText += "^";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7))
                currentText += "&";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8))
                currentText += "*";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9))
                currentText += "(";
        } else {
            if(Gdx.input.isKeyJustPressed(Input.Keys.Q))
                currentText += "q";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.W))
                currentText += "w";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.E))
                currentText += "e";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.R))
                currentText += "r";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.T))
                currentText += "t";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.Y))
                currentText += "y";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.U))
                currentText += "u";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.I))
                currentText += "i";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.O))
                currentText += "o";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.P))
                currentText += "p";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.A))
                currentText += "a";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.S))
                currentText += "s";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.D))
                currentText += "d";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.F))
                currentText += "f";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.G))
                currentText += "g";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.H))
                currentText += "h";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.J))
                currentText += "j";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.K))
                currentText += "k";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.L))
                currentText += "l";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.Z))
                currentText += "z";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.X))
                currentText += "x";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.C))
                currentText += "c";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.V))
                currentText += "v";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.B))
                currentText += "b";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.N))
                currentText += "n";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.M))
                currentText += "m";
            else if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
                currentText += " ";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.COMMA))
                currentText += ",";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.PERIOD))
                currentText += ".";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.SLASH))
                currentText += "/";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS))
                currentText += "-";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS))
                currentText += "=";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.SEMICOLON))
                currentText += ";";

            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0))
                currentText += "0";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1))
                currentText += "1";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2))
                currentText += "2";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3))
                currentText += "3";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4))
                currentText += "4";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5))
                currentText += "5";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6))
                currentText += "6";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7))
                currentText += "7";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8))
                currentText += "8";
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9))
                currentText += "9";
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.DEL)) {
            if(currentText.length() > 0) {
                currentText = currentText.substring(0, currentText.length()-1);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for(int i = 0; i < messages.size(); i++) {
            messages.get(i).render(batch);
        }
        if(focused) {
            batch.draw(UI.worldSelect, x, y, width, height);
            TextManager.draw(currentText, x + 10, y + height/2 + 8, Color.WHITE, 1f, false);
        }
    }
}
