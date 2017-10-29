package com.engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.noise.OpenSimplexNoise;
import com.test.GridTest;

public class GridThread extends Thread {
	
	private int gridMinZ = -25;
	private int gridMaxZ = -25;
	private int gridMinX = 25;
	private int gridMaxX = 25;
	
	private float scale = 1;	// These should have defaults incase something goes wrong
	
	private Color color;
	
	private GridTest gridTest;
	
	public GridThread(int gridMinZ, int gridMaxZ, int gridMinX, int gridMaxX, float scale, GridTest gridTest, Color color){
		this.gridMaxZ = gridMaxZ;
		this.gridMinZ = gridMinZ;
		this.gridMaxX = gridMaxX;
		this.gridMinX = gridMinX;
		
		this.scale = scale;
		this.gridTest = gridTest;
		this.color = color;
	}
	
	@Override
	public void run() {
		
	}
	
	public void render(MeshPartBuilder builder, OpenSimplexNoise noise, float offIncr, float size, float acceleration, int zS, int xS){
//		float zoff = acceleration;
//		for(int z = gridMin; z < (Math.abs(gridMin)+gridMax); z += scale){
//			float xoff = 0;
//			for(int x = gridMin; x < (Math.abs(gridMin)+gridMax); x += scale){
//				zS = z+gridMin;
//				xS = x+gridMin;
//				builder.rect(new VertexInfo().setPos(xS, (float) (noise.eval(xoff, zoff)*size), zS),
//							new VertexInfo().setPos(xS, (float) (noise.eval(xoff, zoff+offIncr)*size), zS+scale),
//							new VertexInfo().setPos(xS+scale, (float) (noise.eval(xoff+offIncr, zoff)*size), zS),
//							new VertexInfo().setPos(xS+scale, (float) (noise.eval(xoff+offIncr, zoff+offIncr)*size), zS+scale) );
//				
//				xoff += offIncr;
//			}
//			zoff += offIncr;
//		}
		
		builder.setColor(color);
		
		float zoff = acceleration;
		for(int z = gridMinZ; z < gridMaxZ; z += scale){
			float xoff = 0;
			for(int x = gridMinX; x < gridMaxX; x += scale){
				zS = z;
				xS = x;
				
				gridTest.buildRect(xS, zS, scale, xoff, zoff);
				
				xoff += offIncr;
			}
			zoff += offIncr;
		}
	}
}
