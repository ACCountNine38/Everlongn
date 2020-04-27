package com.everlongn.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.assets.Images;
import com.everlongn.states.StateManager;

import static com.everlongn.utils.Constants.PPM;

public class ControlCenter extends ApplicationAdapter {

	public static boolean DEBUG = true;
	public static final float SCALE = 1f;

	/*
	  Box2D world, where you put the physial body into
	  handles physics and calculations
	 */
	public static OrthographicCamera camera;

	private SpriteBatch batch;
	private StateManager stateManager;

	@Override
	public void create () {
		batch = new SpriteBatch();
		Images.init();

		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		TextManager.batch = batch;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width/SCALE, height/SCALE);
		stateManager = new StateManager(this);
	}

	// note (0, 0) is now at bottom left not top left
	@Override
	public void render () {
		stateManager.tick(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// allows the use of alpha channel
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		batch.begin(); // begins drawing process
		stateManager.render();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		stateManager.resize((int)(width/SCALE), (int)(height/SCALE));
	}

	// keeps the game stay efficient, clears the unneeded resources and etc
	@Override
	public void dispose () {
		stateManager.dispose();
		batch.dispose();
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}
}
