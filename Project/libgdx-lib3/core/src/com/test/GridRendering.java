package com.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.engine.GridThread;
import com.noise.OpenSimplexNoise;


public class GridRendering {
	
	private PerspectiveCamera camera;			// Will display what is rendered
	private ModelBatch modelBatch;				// Will tell opengl what to render
	private Model gridModel;					// Will be the generic model each instance inherits
	private ModelInstance gridInstance;
	
	private OpenSimplexNoise noise;
	
	private Color gridLineColor;
	private Color gridLineColor2;
	private Color gridLineColor3;
	private Color gridLineColor4;
	
	private ModelBuilder modelBuilder = new ModelBuilder();
	
	private MeshPartBuilder builder;
	private MeshPartBuilder builder2;
	private MeshPartBuilder builder3;
	private MeshPartBuilder builder4;
	
	private GridThread gThread;
	private GridThread gThread2;
	private GridThread gThread3;
	private GridThread gThread4;
	
	public Array<ModelInstance> instances = new Array<ModelInstance>();
	
	public void create(PerspectiveCamera camera){
		this.camera = camera;
		modelBatch = new ModelBatch();
		noise = new OpenSimplexNoise();
		
		sound = false;
		
		gridLineColor = new Color(Color.LIGHT_GRAY);	// Thread 1
		gridLineColor2 = new Color(Color.VIOLET);		// Thread 2
		gridLineColor3 = new Color(Color.CYAN);			// Thread 3
		gridLineColor4 = new Color(Color.GREEN);		// Thread 3
		
		gThread = new GridThread(gridMin, gridMax, gridMin, gridMin/2, scale, gridLineColor);
		gThread2 = new GridThread(gridMin, gridMax, gridMin/2, 0, scale, gridLineColor2);
		gThread3 = new GridThread(gridMin, gridMax, 0, gridMax/2, scale, gridLineColor3);
		gThread4 = new GridThread(gridMin, gridMax, gridMax/2, gridMax, scale, gridLineColor4);
		
		gThread.start();
		gThread2.start();
		gThread3.start();
		gThread4.start();
	}
	
	public void render(){
		render(instances);
	}
	
	private boolean sound;
	
	// Variables
	public static float acceleration = 0;
	public static float accelerationIncre = 0.03f;
	public static float size = 1.9f;
	public static float offIncr = 0.25f;
	
	private final int gridMin = -22;		// DO NOT HAVE OVER 65 VALUES
	private final int gridMax = 22;			// DO NOT HAVE OVER 65 VALUES
	private final int scale = 1;
	private float xoff = acceleration;
	
	public void update(){
		xoff = gThread.update(xoff, offIncr, size, acceleration);
		xoff = gThread2.update(xoff, offIncr, size, acceleration);
		xoff = gThread3.update(xoff, offIncr, size, acceleration);
		xoff = gThread4.update(xoff, offIncr, size, acceleration);
	}
	
	public float getNextSoundBytes(){
		return 0.1f;	// Reads from sound buffer and grabs next points
	}
	/*
	 * We would have some sound object that we would read data from
	 */
	
	private void render(Array<ModelInstance> instances) {
		
		modelBatch.begin(camera);
		
		checkKeys();
		
		modelBuilder.begin();
		
		builder = modelBuilder.part("gridpart1", GL20.GL_LINES, Usage.Position | Usage.ColorUnpacked, new Material());
		builder2 = modelBuilder.part("gridpart2", GL20.GL_LINES, Usage.Position | Usage.ColorUnpacked, new Material());
		builder3 = modelBuilder.part("gridpart3", GL20.GL_LINES, Usage.Position | Usage.ColorUnpacked, new Material());
		builder4 = modelBuilder.part("gridpart4", GL20.GL_LINES, Usage.Position | Usage.ColorUnpacked, new Material());
		
		acceleration -= accelerationIncre;
		
//		zoff = acceleration;
//		for(int z = gridMin; z < gridMax; z += scale){
//			xoff = 0;
//			for(int x = gridMin; x < gridMax; x += scale){
//				builder.rect(new VertexInfo().setPos(x, (float) (noise.eval(xoff, zoff)*size), z),
//						new VertexInfo().setPos(x, (float) (noise.eval(xoff, zoff+offIncr)*size), z+scale),
//						new VertexInfo().setPos(x+scale, (float) (noise.eval(xoff+offIncr, zoff)*size), z),
//						new VertexInfo().setPos(x+scale, (float) (noise.eval(xoff+offIncr, zoff+offIncr)*size), z+scale));
//				
//				xoff += offIncr;
//			}
//			zoff += offIncr;
//		}
		
		gThread.render(builder, size, acceleration);
		gThread2.render(builder2, size, acceleration);
		gThread3.render(builder3, size, acceleration);
		gThread4.render(builder4, size, acceleration);
		
//		try {
//			gThread.join();
//			gThread2.join();
//			gThread3.join();
//			gThread4.join();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		gridModel = modelBuilder.end();
		
		gridInstance = new ModelInstance(gridModel);
		
		modelBatch.render(gridInstance);
		modelBatch.end();
	}
	
	public void checkKeys() {
		if(Gdx.input.isKeyPressed(Input.Keys.HOME)) {
			accelerationIncre = 0.03f;
			size = 1.9f;
			offIncr = 0.25f;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
			camera.rotateAround(new Vector3(0f, 0f, 0f), new Vector3(0f, 1f, 0f), 4f);
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			camera.rotateAround(new Vector3(0f, 0f, 0f), new Vector3(0f, 1f, 0f), -4f);
		if(Gdx.input.isKeyPressed(Input.Keys.UP))
			camera.rotateAround(new Vector3(0f, 0f, 0f), new Vector3(1f, 0f, 0f), 4f);
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
			camera.rotateAround(new Vector3(0f, 0f, 0f), new Vector3(1f, 0f, 0f), -4f);
		
		if(Gdx.input.isKeyPressed(Input.Keys.PLUS))
			accelerationIncre += 0.01;
		if(Gdx.input.isKeyPressed(Input.Keys.EQUALS))
			accelerationIncre += 0.01;
		if(Gdx.input.isKeyPressed(Input.Keys.MINUS))
			accelerationIncre -= 0.01;
		
		if(Gdx.input.isKeyPressed(Input.Keys.Q))
			size += 0.1f;
		if(Gdx.input.isKeyPressed(Input.Keys.A))
			size -= 0.1f;
		
		if(Gdx.input.isKeyPressed(Input.Keys.W))
			offIncr += 0.01f;
		if(Gdx.input.isKeyPressed(Input.Keys.S))
			offIncr -= 0.01f;
	}
	
	public void dispose () {
		modelBatch.dispose();
		gridModel.dispose();
		gridModel = null;
	}
}
