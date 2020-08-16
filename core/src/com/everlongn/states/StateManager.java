package com.everlongn.states;

import com.badlogic.gdx.Gdx;
import com.everlongn.game.ControlCenter;

import java.awt.event.KeyEvent;
import java.util.Stack;

public class StateManager {
    private final ControlCenter c;
    public static Stack<State> states;

    public enum CurrentState {
        SPLASH,
        MENU_STATE,
        WORLD_SELECTION_STATE,
        WORLD_CREATION_STATE,
        GAME_STATE,
        ERROR_LOADING_STATE
    }

    public StateManager(ControlCenter c) {
        this.c = c;
        this.states = new Stack<State>();
        this.setState(CurrentState.SPLASH); // initial state
    }

    public void tick(float delta) {
        states.peek().tick(delta);
    }

    public void render() {
        states.peek().render();
    }

    public void dispose() {
        for(State s: states) {
            s.dispose();
        }
    }

    public ControlCenter getC() {
        return c;
    }

    public void resize(int w, int h) {
        states.peek().resize(w, h);
    }

    public void setState(CurrentState state) {
        if(states.size() >= 1) {
            states.pop().dispose();
        }
        states.push(getState(state));
    }

    private State getState(CurrentState state) {
        switch(state) {
            case SPLASH: return new SplashState(this);
            case MENU_STATE: return new MenuState(this);
            case WORLD_SELECTION_STATE: return new WorldSelectionState(this);
            case WORLD_CREATION_STATE: return new WorldCreationState(this);
            case GAME_STATE: return new GameState(this);
            case ERROR_LOADING_STATE: return new ErrorLoadingState(this);
        }
        return null;
    }
}
