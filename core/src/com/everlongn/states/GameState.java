package com.everlongn.states;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.everlongn.world.WorldGenerator;

public class GameState extends State {
    private WorldGenerator world;

    public GameState(StateManager stateManager) {
        super(stateManager);
        world = new WorldGenerator(c, "core//res//worlds//tempWorld.png");

    }

    @Override
    public void tick(float delta) {
        world.tick();
    }

    @Override
    public void render() {
        world.render(batch);
    }

    @Override
    public void dispose() {
        world.dispose();
    }

}
