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
	private double x,y,mapX, mapY;
	private Enemy target;
	private double speedX = Main.tileWidth/2, speedY= Main.tileHeight/2;
	private double initialXdif;
	private double initialYdif;
	private double angle;
	private int pierce;
	private int type;
	private int width = Main.tileHeight/2;
	private int height = Main.tileWidth/6;
	private int damage;
	private AudioInputStream stream;
	private AudioFormat format;
	private DataLine.Info info;
	private Clip clip;
	private boolean left, doubleShot;
	public Projectile(double x, double y, Enemy target, int pierce, int type, int damage, boolean left, boolean doubleShot) {
		this.doubleShot = doubleShot;
		this.left = left;
		this.x = x;
		this.y = y;
		this.target = target;
		this.type = type;
		this.damage = damage;
		initialXdif = (target.X)*Main.tileWidth+target.animX+Main.tileWidth/2 - x;
		initialYdif = (target.Y)*Main.tileHeight+target.animY+Main.tileHeight/2 - y;
		angle = Math.atan((double)initialYdif/initialXdif);

		//Calcualtes the angle between projectile and targeted enemy
		this.setPierce(pierce);
		if(type == 0) {
			speedX = Main.tileWidth/2;
			speedY= Main.tileHeight/2;
			play(new File("Sound/laser4.wav")); 
		}
		else if(type == 1) {
			speedX = Main.tileWidth/16;
			speedY= Main.tileHeight/16;
			play(new File("Sound/tir.wav")); 
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
			y = getY() - Math.abs(sin);
		}
		else {
			y = getY() + Math.abs(sin);
		}
		if(initialXdif < 0) {
			x = getX() - Math.abs(cosine);
		}
		else {
			x = getX() + Math.abs(cosine);
		}
		//Move based on angle
	}
	public void draw(Graphics g) {
		if(doubleShot) {
			if(left) {
				Graphics2D g2 = (Graphics2D) g;
				g2.rotate(angle , getX()+Main.tileWidth/4,getY()- getHeight()/2);
				g2.drawImage(Main.projectileImg,(int)getX()+Main.tileWidth/4-getWidth()/2, (int)getY()- getHeight()/2, getWidth(), getHeight(),null);
				g2.rotate(angle * -1, getX()+Main.tileWidth/4,getY()- getHeight()/2);
			}
			else {
				Graphics2D g2 = (Graphics2D) g;
				g2.rotate(angle , getX()-Main.tileWidth/4,getY()- getHeight()/2);
				g2.drawImage(Main.projectileImg,(int)getX()-Main.tileWidth/4-getWidth()/2, (int)getY()- getHeight()/2, getWidth(), getHeight(),null);
				g2.rotate(angle * -1, getX()-Main.tileWidth/4,getY()- getHeight()/2);
			}
			return;
		}
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(angle , getX(),getY()- getHeight()/2);
		g2.drawImage(Main.projectileImg,(int)getX()-getWidth()/2, (int)getY()- getHeight()/2, getWidth(), getHeight(),null);
		g2.rotate(angle * -1, getX(),getY()- getHeight()/2);


	}
	//Draw projectile based on rotation
	public double getY() {
		return y;
	}
	public double getX() {
		return x;
	}
	public int getDamage() {
		return damage;
	}
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
	public int getPierce() {
		return pierce;
	}
	public void setPierce(int pierce) {
		this.pierce = pierce;
	}
}
