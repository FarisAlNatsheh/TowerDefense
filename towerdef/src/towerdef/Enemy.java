package towerdef;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Enemy extends Entity{
	public int X, Y, dir, health, textureDir = 10, maxHealth, damage;
	public double  animX, animY, anim, speedX, speedY;
	public BufferedImage texture = Main.enemyTexture[0];
	int type;
	static double value = 5;
	//Texture & Movement flags directions

	//0 left 9
	//1 up 8
	//2 right 11
	//3 down 10
	public void setEnemy() {
		switch(type) {
		case 1:
			health = 200;
			health+= health * WaveHandler.healthAddition;
			maxHealth = health;
			damage = 10;
			break;
		case 2:
			health = 50;
			health+= health * WaveHandler.healthAddition;
			maxHealth = health;
			damage = 40;
			break;
		case 3:
			health = 5000;
			maxHealth = health;
			damage = 1000;
			break;
		}
	}
	public void readjust() {
		switch(type) {
		case 1:
			speedX= Main.tileWidth/30.0;
			speedY = Main.tileHeight/30.0;
			break;
		case 2:
			speedX= Main.tileWidth/20.0;
			speedY =Main.tileHeight/20.0;
			break;
		case 3:
			speedX= Main.tileWidth/90.0;
			speedY =Main.tileHeight/90.0;
			break;
		}
	}

	public Enemy(int startX, int startY, int startDir, int type) {
		this.X = startX;
		this.Y = startY;
		this.dir = startDir;
		this.type = type;
		setEnemy();
	}

	public void checkSurr() {
		switch(dir) {
		case 0: 	
			if(Main.map[X-1][Y] == 0) {
				if(Main.map[X][Y+1] == 0) {
					dir = 1;
					textureDir = 8;
					break;
				}

				if(Main.map[X][Y-1] == 0) {
					dir = 3;
					textureDir = 10;
					break;
				}
				dir = 2;
				textureDir = 11;
			}
			break;
		case 1: 
			if(Main.map[X][Y-1] == 0) {
				if(Main.map[X+1][Y] == 0) {
					dir = 0;
					textureDir = 9; 
					break;
				}
				if(Main.map[X-1][Y] == 0) {
					dir = 2;
					textureDir = 11;
					break;
				}
				dir = 3;
				textureDir = 10;
			}
			break;
		case 2: 
			if(Main.map[X+1][Y] == 0) {
				if(Main.map[X][Y+1] == 0) {
					dir = 1;
					textureDir = 8;
					break;
				}
				if(Main.map[X][Y-1] == 0) {
					dir = 3;
					textureDir = 10;
					break;
				}
				dir = 0;
				textureDir = 11;
			}
			break;
		case 3: 
			if(Main.map[X][Y+1] == 0) {
				if(Main.map[X+1][Y] == 0){
					dir = 0;
					textureDir = 9;
					break;
				}
				if(Main.map[X-1][Y] == 0) {
					dir = 2;
					textureDir = 11;
					break;
				}
				dir = 1;
				textureDir = 8;
			}
			break;
		}

	}
	//Rotate and change texture based on surroundings
	public void move() {
		boolean dec = false;
		if(dir == 3) { 
			dec = false;
			animY+= speedY;
		}
		else if(dir == 2) {
			dec = false;
			animX+= speedX;
		}
		else if(dir == 0) {
			dec = true;
			animX-= speedX;
		}

		else if(dir == 1) {
			dec = true;
			animY-= speedY;
		}

		if(!dec) {
			if(animX >= Main.tileWidth) {
				animX = 0;
				X++;
			}
			if(animY >= Main.tileHeight) {
				animY = 0;
				Y++;
			}
		}
		else {
			if(animX <= Main.tileWidth*-1) {
				animX = 0;
				X--;
			}
			if(animY <= Main.tileHeight*-1) {
				animY = 0;
				Y--;
			}
		}
	}
	//Updates location of the enemy on the map

	public void run() {

		
		if(type == 1) {
			double animSpeed = 0.3;
			anim+= animSpeed;
			if(anim >= 20)
				anim = 0;
			texture = Main.enemyTexture[(int)anim];
		}
		else if(type == 2){
			double animSpeed = 0.2;
			anim+= animSpeed;
			if(anim > 3)
				anim = 0;
			texture = Main.enemyTexture2[(int)anim];
		}
		else if(type == 3){
			double animSpeed = 0.2;
			anim+= animSpeed;
			if(anim > 3)
				anim = 0;
			texture = Main.bossTexture;
		}
		//Runs animation

		
		//texture = Main.towerImg;
		if(X < Main.gridWidth-1 && Y < Main.gridHeight-1) {
			checkSurr();
			move();
		}
	}
	//Run enemy methods

	public void draw(Graphics g) {
		double angle = 0;
		if(dir == 0)
			angle = Math.PI;
		else if(dir == 1)
			angle = Math.PI/-2;
		else if(dir == 2)
			angle = 0;
		else if(dir == 3)
			angle = Math.PI/2;
		if(type == 2)
			angle+= Math.PI/-2;
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(angle, X*Main.tileWidth+animX+Main.tileWidth/2, Y*Main.tileHeight+animY+ Main.tileHeight/2);
		g.drawImage(texture, Main.tileWidth*X+(int)animX , Main.tileHeight*Y+(int)animY, Main.tileWidth, Main.tileHeight, null);
		g2.rotate((angle)*-1, X*Main.tileWidth+animX+Main.tileWidth/2, Y*Main.tileHeight+animY+ Main.tileHeight/2);

		g.setColor(new Color(0,0,0));
		g.fillRect(Main.tileWidth*X+(int)animX + Main.tileWidth/3 , Main.tileHeight*Y+(int)animY + Main.tileHeight, Main.tileWidth/3, 5);
		g.setColor(new Color(120,0,0));
		g.fillRect(Main.tileWidth*X+(int)animX + Main.tileWidth/3 , Main.tileHeight*Y+(int)animY + Main.tileHeight, (int)(Main.tileWidth/3*(1.0*health/maxHealth)), 5);
		g.setColor(new Color(0,0,0));
	}
	//Draw enemies on screen
}
