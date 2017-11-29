package com.engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.math.Vector3;
import com.noise.OpenSimplexNoise;
import com.test.GridRendering;

public class GridThread extends Thread {
	
	private int gridMinZ = -25;
	private int gridMaxZ = -25;
	private int gridMinX = 25;
	private int gridMaxX = 25;
	
	private int gridTotalZ = 50;
	private int gridTotalX = 50;
	
	
	private OpenSimplexNoise noise;
	
	private int scale = 1;	// These should have defaults incase something goes wrong
	
	private double[][] displayArray;
	
	private Color color;
	
	public GridThread(int gridMinZ, int gridMaxZ, int gridMinX, int gridMaxX, int scale, double[][] displayArray, Color color){
		this.gridMaxZ = gridMaxZ;
		this.gridMinZ = gridMinZ;
		this.gridMaxX = gridMaxX;
		this.gridMinX = gridMinX;
		
		gridTotalZ = (Math.abs(gridMinZ) * Math.abs(gridMaxZ));
		gridTotalX = (Math.abs(gridMinX) * Math.abs(gridMaxX));
		
		displayArray = new double[gridTotalZ][gridTotalX];;
		
		this.scale = scale;
		this.color = color;
		
		float zoff = GridRendering.acceleration;
		for (int z = 0; z < gridTotalZ; z += scale) {
			float xoff = 0;
			for (int x = 0; x < gridTotalX; x += scale) {
				displayArray[z][x] = (noise.eval(xoff, zoff)*GridRendering.size);
				xoff += GridRendering.offIncr;
			}
			zoff += GridRendering.offIncr;
		}
	}
	
	@Override
	public void run() {
		
	}
	
	Vector3 p1,p2,p3,p4;
	
	public void update(float offIncr, float size, float acceleration) {
		float xoff = 0;
		for (int x = 0; x < gridTotalX; x += scale) {
			displayArray[0][x] = (noise.eval(xoff, acceleration)*size);
			xoff += offIncr;
		}
		for (int z = 0; z < gridTotalZ; z += scale) {
			for (int x = 0; x < gridTotalX; x += scale) {
				displayArray[z][x] = displayArray[z - scale][x];
			}
		}
	}
	
	public void render(MeshPartBuilder builder, float offIncr, float size, float acceleration) {

		builder.setColor(color);
		
		float zoff = acceleration;
		for(int z = gridMinZ; z < gridMaxZ; z += scale){
			float xoff = 0;
			for(int x = gridMinX; x < gridMaxX; x += scale){
				builder.rect(new VertexInfo().setPos(x, (float) (displayArray[z][x]), z),
						new VertexInfo().setPos(x, (float) (noise.eval(xoff, zoff+offIncr)*size), z+scale),
						new VertexInfo().setPos(x+scale, (float) (noise.eval(xoff+offIncr, zoff)*size), z),
						new VertexInfo().setPos(x+scale, (float) (noise.eval(xoff+offIncr, zoff+offIncr)*size), z+scale));
				
				xoff += offIncr;
			}
			zoff += offIncr;
		}
	}
}
