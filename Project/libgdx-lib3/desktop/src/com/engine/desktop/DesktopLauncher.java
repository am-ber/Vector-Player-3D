package com.engine.desktop;

//import java.io.IOException;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
//import com.engine.JavaProcess;
import com.engine.MainCore;
/*
 * Created by Zachary Vanscoit
 * This is for CS220 Final Project in data structures at SIU university
 */

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "3-D Noise";
		config.width = 800;
		config.height = 600;
		new LwjglApplication(new MainCore(config.width, config.height), config);
		
//		try {
//			int res = JavaProcess.exec(BarLauncher.class); // Where the second window is shown
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
}
