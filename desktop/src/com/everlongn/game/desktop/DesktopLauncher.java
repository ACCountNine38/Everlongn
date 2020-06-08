package com.everlongn.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.everlongn.game.ControlCenter;

import java.awt.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		//LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//new LwjglApplication(new ControlCenter(), config);

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		//config.width = 1280;
		//config.height = 800;
		config.width = (int)dimension.getWidth();
		config.height = (int)dimension.getHeight();
		config.fullscreen = true;

		new LwjglApplication(new ControlCenter(config.width, config.height), config);
	}
}
