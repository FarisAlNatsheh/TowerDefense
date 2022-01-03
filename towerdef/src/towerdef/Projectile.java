package towerdef;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class Projectile extends Entity{
	public double x,y,mapX, mapY;
	Enemy target;
	double speedX = Main.tileWidth/2, speedY= Main.tileHeight/2;
	double initialXdif;
	double initialYdif;
	double angle;
	int pierce;
	int type;
	int width = Main.tileHeight/2;
	int height = Main.tileWidth/6;
	int damage;
	AudioInputStream stream;
	AudioFormat format;
	DataLine.Info info;
	Clip clip;
	public Projectile(double x, double y, Enemy target, int pierce, int type, int damage) {
		this.x = x;
		this.y = y;
		this.target = target;
		this.type = type;
		this.damage = damage;
		initialXdif = (target.X)*Main.tileWidth+target.animX+Main.tileWidth/2 - x;
		initialYdif = (target.Y)*Main.tileHeight+target.animY+Main.tileHeight/2 - y;
		angle = Math.atan((double)initialYdif/initialXdif);

		//Calcualtes the angle between projectile and targeted enemy
		this.pierce = pierce;
		if(type == 0) {
			speedX = Main.tileWidth/2;
			speedY= Main.tileHeight/2;
			play(new File("Sound/laser4.wav")); //credit dklon
		}
		else if(type == 1) {
			speedX = Main.tileWidth/16;
			speedY= Main.tileHeight/16;
			play(new File("Sound/tir.wav")); //credit dklon
		}
		else if(type == 2) {
			speedX = Main.tileWidth;
			speedY= Main.tileHeight;
			play(new File("Sound/Laser Shot.wav"));
		}
		//speedX = 0;
		//speedY = 0;

	}
	public void play(File file) {
		FloatControl volume;
		try {
			stream = AudioSystem.getAudioInputStream(file);
			format = stream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.addLineListener(new LineListener() {
				public void update(LineEvent event) {
					if (event.getType() == LineEvent.Type.STOP)
						clip.close();
				}
			});
			clip.open(stream);
			volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue((float) Main.volumeEff);
			clip.start();
		} catch (Exception e) { }
	}
	public void move() {
		//initialXdif = target.X*Main.tileWidth+target.animX - x;
		//initialYdif = target.Y*Main.tileHeight+target.animY - y;
		//angle = Math.atan((double)initialYdif/initialXdif); 
		//use for auto-aim bullets
		double sin = Math.sin(angle) * speedX;
		double cosine = Math.cos(angle) * speedY;
		if(Main.tick%20 == 0) {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(initialYdif < 0) {
			y -= Math.abs(sin);
		}
		else {
			y += Math.abs(sin);
		}
		if(initialXdif < 0) {
			x -= Math.abs(cosine);
		}
		else {
			x += Math.abs(cosine);
		}
		//Move based on angle
	}
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(angle , x,y- height/2);
		g2.drawImage(Main.projectileImg,(int)x-width/2, (int)y- height/2, width, height,null);
		g2.rotate(angle * -1, x,y- height/2);

		
	}
	//Draw projectile based on rotation
}
