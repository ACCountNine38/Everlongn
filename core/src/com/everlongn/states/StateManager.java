package com.everlongn.states;

import com.everlongn.game.ControlCenter;

import java.util.Stack;

public class StateManager {
    private final ControlCenter c;
    private Stack<State> states;

    public enum CurrentState {
        SPLASH,
        MENUSTATE,
        GAMESTATE
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
            case MENUSTATE: return new MenuState(this);
            case GAMESTATE: return new GameState(this);
        }
        return null;
    }
}
