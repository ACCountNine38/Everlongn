package com.everlongn.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.everlongn.game.ControlCenter;

import java.awt.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

		config.width = (int)dimension.getWidth();
		config.height = (int)dimension.getHeight();
		config.resizable = false;

		config.addIcon("UI/icon128.png", Files.FileType.Internal);
		config.addIcon("UI/icon32.png", Files.FileType.Internal);
		config.addIcon("UI/icon16.png", Files.FileType.Internal);

		config.fullscreen = true;

		new LwjglApplication(new ControlCenter(config.width, config.height), config);
	}
}
