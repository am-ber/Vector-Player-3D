package com.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class DrawBoxTest implements InputProcessor {
	
	private PerspectiveCamera camera;		// Will display what is rendered
	private ModelBatch modelBatch;			// Will tell opengl what to render
	private ModelBuilder modelBuilder;		// Will be what constructs the model on the scene
	private Model box;						// Will be the generic model each instance inherits
	private ModelInstance modelInstance;	// Will hold information on each model
	private Environment environment;
	
	public void create(){
		camera = new PerspectiveCamera(80,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		camera.position.set(0f,0f,3f);		// Set the camera a few units back
		
		camera.lookAt(0f,0f,0f);			// Look at origin
		
		camera.near = 0.1f;					// This is how near we can see
		camera.far = 300f;					// This is how far we can see
		
		modelBatch = new ModelBatch();
		modelBuilder = new ModelBuilder();
		
		box = modelBuilder.createBox(2f, 2f, 2f,
				new Material(ColorAttribute.createDiffuse(Color.FOREST)),
				VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);
		
		modelInstance = new ModelInstance(box,0,0,0);
		
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight,0.8f,0.8f,0.8f,1f));
	}
	public void render () {
		camera.update();
		modelBatch.begin(camera);
		modelBatch.render(modelInstance);
		modelBatch.end();
	}
	@Override
	public boolean keyDown(int keycode) {
		
		if(keycode == Input.Keys.LEFT)
			camera.rotateAround(new Vector3(0f, 0f, 0f), new Vector3(0f,1f,0f), 8f);

		if(keycode == Input.Keys.RIGHT)
			camera.rotateAround(new Vector3(0f, 0f, 0f), new Vector3(0f,1f,0f), -8f);
		
		return true;
	}
	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
