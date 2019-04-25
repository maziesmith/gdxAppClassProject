package com.mygdx.game.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.mygdx.game.GdxSpaceGameT1;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import sun.applet.Main;

public class DesktopLauncher extends JFrame {
//    public static void main(String[] arg) {
//        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//        new LwjglApplication(new GdxSpaceGameT1(), config);
//    }

	//16:9 Landscape aspect ratio
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int CELL_WIDTH = 200;
	private static final int CANVAS_WIDTH = WIDTH - CELL_WIDTH;

	// AWT = Abstract Window Toolkit
	// enables us to embed libgdx app/game into java desktop app
	private LwjglAWTCanvas lwjglAWTCanvas;

	public DesktopLauncher() throws HeadlessException {
		init();
	}

	private void init() {
		launchSample("com.mygdx.game.GdxSpaceGameT1");

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				if (lwjglAWTCanvas != null) {
					//stop will call our dispose and stop libgdx application
					lwjglAWTCanvas.stop();
				}
			}
		});

		setTitle(DesktopLauncher.class.getSimpleName());
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		//tell windwo (jframe) to resize and layout our components
		pack();
		setVisible(true);


	}

	private void launchSample(String name) {
		System.out.println("launching sample name = " + name);

		Container container = getContentPane();

		if (lwjglAWTCanvas != null) {
			lwjglAWTCanvas.stop();
			container.remove(lwjglAWTCanvas.getCanvas());
		}

		ApplicationListener sample;

		try {
			//get class object by name
			Class<ApplicationListener> clazz = ClassReflection.forName(name);

			//create new instance of our sample class
			sample = ClassReflection.newInstance(clazz);
		} catch (Exception e) {
			throw new RuntimeException("Cannot create sample with name = " + name, e);
		}

		lwjglAWTCanvas = new LwjglAWTCanvas(sample);
		lwjglAWTCanvas.getCanvas().setSize(CANVAS_WIDTH, HEIGHT);
		container.add(lwjglAWTCanvas.getCanvas(), BorderLayout.CENTER);

		pack();
	}

	public static void main(String[] args) {
		// must be used to run our jframe properly
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new DesktopLauncher();
			}
		});
	}
}

