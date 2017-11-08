package com.game.launcher.screens;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.game.engine.Game;

import javazoom.spi.mpeg.sampled.convert.DecodedMpegAudioInputStream;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;

public class Mp3Screen extends Screen{
	
	// Vals
	private int columns = 0, rows = 0, scale=10;
	private double[] noiseGrid;
	
	@Override
	public void create(ScreenManager screenManager) {
		this.screenManager = screenManager;
		
		columns = screenManager.game().getWidth() / scale;
		rows = screenManager.game().getHeight() / scale;
		noiseGrid = new double[columns];
		
		try {
			file = new File(filename);
			in = AudioSystem.getAudioInputStream(file);
			baseFormat = in.getFormat();
			decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 
											baseFormat.getSampleRate(),
											16,
											baseFormat.getChannels(),
											baseFormat.getChannels() * 2,
											baseFormat.getSampleRate(),
											false);
			
			din = AudioSystem.getAudioInputStream(decodedFormat, in);
			streemy = new DecodedMpegAudioInputStream(decodedFormat, din);
			streemy.execute();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("length of buffer: "+len);
		
		encode();
	}
	
	// Obj
	private String filename = "E:/Audio/Music/my music/Techno/Genre/DnB/Krewella - Come & Get It.mp3";
	
	File file;
	AudioInputStream in;
	AudioInputStream din = null;
	AudioFormat baseFormat;
	AudioFormat decodedFormat;
	DecodedMpegAudioInputStream streemy;
	
	private int len, index = 0;
	
	@Override
	public void update() {
////		thread.run();
//		float[] equalizer;
//		try {
//		    // DecodedMpegAudioInputStream properties
//		    if (din instanceof javazoom.spi.PropertiesContainer) {
//		    }
//			Map properties = ((javazoom.spi.PropertiesContainer) din).properties(); 
//			
//			equalizer = (float[]) properties.get("mp3.equalizer");
//			equalizer[0] = 0.5f;
//			equalizer[31] = 0.25f;
//			
////			din.read();
//			streemy.read();
//			
//		} catch(Exception e){
//			e.printStackTrace();
//		}
		
		for(int i=0; i < columns-1; i++) {
			noiseGrid[i] = noiseGrid[i+1];
		}
//		noiseGrid[columns-1] = vals.get(index);
		
//		if(vals.get(index) != 0)
//			System.out.println("val is: "+vals.get(index));
		
		index ++;
	}
	
	private void encode() {
		
		try {
			FFmpeg ffmpeg = new FFmpeg("E:/Audio/Music/my music/Techno/Genre/DnB/");
			FFprobe ffprobe = new FFprobe("E:/Audio/Music/my music/Techno/Genre/DnB/");
	
			FFmpegBuilder builder = new FFmpegBuilder()
	
			  .setInput("Krewella - Come & Get It.mp3")     // Filename, or a FFmpegProbeResult
			  .overrideOutputFiles(true) // Override the output if it exists
	
			  .addOutput("output.mp4")   // Filename for the destination
			    .setFormat("wav")        // Format is inferred from filename, or can be set
	//		    .setTargetSize(250_000)  // Aim for a 250KB file
	//
	//		    .disableSubtitle()       // No subtiles
	//
	//		    .setAudioChannels(1)         // Mono audio
	//		    .setAudioCodec("aac")        // using the aac codec
	//		    .setAudioSampleRate(48_000)  // at 48KHz
	//		    .setAudioBitRate(32768)      // at 32 kbit/s
	//
	//		    .setVideoCodec("libx264")     // Video using x264
	//		    .setVideoFrameRate(24, 1)     // at 24 frames per second
	//		    .setVideoResolution(640, 480) // at 640x480 resolution
	//
	//		    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
			    .done();
	
			FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
	
			// Run a one-pass encode
			executor.createJob(builder).run();
			
			FFmpegProbeResult probeResult = ffprobe.probe("Krewella - Come & Get It.mp3");
	
			FFmpegFormat format = probeResult.getFormat();
			System.out.format("%nFile: '%s' ; Format: '%s' ; Duration: %.3fs", 
				format.filename, 
				format.format_long_name,
				format.duration
			);
	
			FFmpegStream stream = probeResult.getStreams().get(0);
			System.out.format("%nCodec: '%s' ; Width: %dpx ; Height: %dpx",
				stream.codec_long_name,
				stream.width,
				stream.height
			);
	
			FFmpegProbeResult in = ffprobe.probe("Krewella - Come & Get It.mp3");
	
			FFmpegJob job = executor.createJob(builder, new ProgressListener() {
	
				// Using the FFmpegProbeResult determine the duration of the input
				final double duration_ns = in.getFormat().duration * TimeUnit.SECONDS.toNanos(1);
	
				@Override
				public void progress(Progress progress) {
					double percentage = progress.out_time_ns / duration_ns;
	
					// Print out interesting information about the progress
					System.out.println(String.format(
						"[%.0f%%] status:%s frame:%d time:%s ms fps:%.0f speed:%.2fx",
						percentage * 100,
						progress.status,
						progress.frame,
						FFmpegUtils.toTimecode(progress.out_time_ns, TimeUnit.NANOSECONDS),
						progress.speed
					));
				}
			});
	
			job.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Game getGame(){
		return screenManager.game;
	}

	@Override
	public void draw(Graphics2D g) {
		Color greyish = new Color(25,25,25);
		g.setColor(greyish);
		g.fillRect(0, 0, screenManager.game().getWidth(), screenManager.game().getHeight());	// Sets background
		g.setColor(Color.darkGray);
		g.setStroke(new BasicStroke(0.1f));
		//g.translate(0, screenManager.game().getHeight()/2);
		//g.rotate(Math.PI/4, screenManager.game().getWidth() / 2, screenManager.game().getHeight() / 2);
		
		for(int j=0; j < rows; j++){
			for(int i=0; i < columns; i++){
				g.drawRect(i*scale, j*scale, scale, scale);
				//g.drawLine(i*scale, j*scale, j*scale, i*scale);
			}
		}
		
		g.setColor(Color.white);
		g.setStroke(new BasicStroke(1f));
		
		for(int i=0; i < columns-1; i++){
			g.drawLine(i*scale, (int)noiseGrid[i], (i*scale)+scale, (int)noiseGrid[i+1]);
		}
	}

}
