package com.everlongn.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.everlongn.assets.Images;
import com.everlongn.assets.Sounds;
import com.everlongn.items.Arcane;
import com.everlongn.items.Melee;
import com.everlongn.items.Throwing;
import com.everlongn.items.TileItem;
import com.everlongn.states.StateManager;
import com.everlongn.utils.TextManager;

import static com.everlongn.utils.Constants.PPM;

public class ControlCenter extends ApplicationAdapter {

	public static Vector3 camStartingPosition;
	public static boolean DEBUG, DEBUG_RENDER;
	public static float SCALE = 1f;
	public static int width, height;

	public static Pixmap cursor1, emptyCursor, aimCursor, attackCursor, handCursor;

	/*
	  Box2D world, where you put the physial body into
	  handles physics and calculations
	 */
	public static OrthographicCamera camera;

	private SpriteBatch batch;
	private StateManager stateManager;

	public static float delta;

	public ControlCenter(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		TextManager.batch = batch;
		Images.init();
		Sounds.init();
		Arcane.init();
		Melee.init();
		Throwing.init();
		TileItem.init();

		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, width/SCALE, height/SCALE);
		camStartingPosition = camera.position;
		stateManager = new StateManager(this);

		cursor1 = new Pixmap(Gdx.files.internal("UI/Cursor.png"));
		emptyCursor = new Pixmap(Gdx.files.internal("UI/emptyCursor.png"));
		aimCursor = new Pixmap(Gdx.files.internal("UI/aimCursor.png"));
		attackCursor = new Pixmap(Gdx.files.internal("UI/swordCursor.png"));
		handCursor = new Pixmap(Gdx.files.internal("UI/handCursor.png"));
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursor1, 0, 0));
	}

	// note (0, 0) is now at bottom left not top left
	@Override
	public void render () {
		delta = Gdx.graphics.getDeltaTime();
		stateManager.tick(delta);
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// allows the use of alpha channel
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		//batch.begin(); // begins drawing process
		stateManager.render();
		//batch.end();
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
