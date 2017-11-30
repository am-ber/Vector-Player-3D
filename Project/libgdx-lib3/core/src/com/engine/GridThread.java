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
	
	public GridThread(int gridMinZ, int gridMaxZ, int gridMinX, int gridMaxX, int scale, Color color){
		this.gridMaxZ = gridMaxZ;
		this.gridMinZ = gridMinZ;
		this.gridMaxX = gridMaxX;
		this.gridMinX = gridMinX;
		
		gridTotalZ = (Math.abs(gridMaxZ - gridMinZ) / scale);
		gridTotalX = (Math.abs(gridMaxX - gridMinX) / scale);
		
		displayArray = new double[gridTotalZ][gridTotalX];;
		
		this.scale = scale;
		this.color = color;
		
		noise = new OpenSimplexNoise();
		
		float zoff = GridRendering.acceleration;
		for (int z = 0; z < gridTotalZ; z += 1) {
			float xoff = 0;
			for (int x = 0; x < gridTotalX; x += 1) {
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
	
	public float update(float xoff, float offIncr, float size, float acceleration) {
		for (int z = gridTotalZ-1; z >= 1; z --) {
			for (int x = gridTotalX-1; x >= 0; x --) {
				displayArray[z][x] = displayArray[z - 1][x];
			}
		}
		for (int x = 0; x < gridTotalX - 1; x += 1) {
			displayArray[0][x] = (noise.eval(xoff, acceleration)*size);
			xoff += offIncr;
		}
		return xoff;
	}
	
	public void render(MeshPartBuilder builder, float size, float acceleration) {

		builder.setColor(color);
		int zS=0;
		for(int z = gridMinZ; z < gridMaxZ-1; z += scale){
			int xS=0;
			for(int x = gridMinX; x < gridMaxX-1; x += scale){
				builder.rect(new VertexInfo().setPos(x, (float) (displayArray[zS][xS]), z),
						new VertexInfo().setPos(x, (float) (displayArray[zS+1][xS]), z+scale),
						new VertexInfo().setPos(x+scale, (float) (displayArray[zS][xS+1]), z),
						new VertexInfo().setPos(x+scale, (float) (displayArray[zS+1][xS+1]), z+scale));
				xS++;
			}
			zS++;
		}
	}
}
