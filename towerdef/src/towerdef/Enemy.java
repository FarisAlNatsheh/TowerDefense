package towerdef;

import java.awt.image.BufferedImage;

public class Enemy {
	public int X, Y, dir, health, speed = 2, textureDir = 10, maxHealth, damage = 21;
	public double  animX, animY, anim, speedX= Main.tileWidth/30, speedY = Main.tileHeight/30;
	public BufferedImage texture = Main.enemyTexture[0][7];
	//0 left 9
	//1 up 8
	//2 right 11
	//3 down 10
	public Enemy(int startX, int startY, int startDir, int health) {
		this.X = startX;
		this.Y = startY;
		this.dir = startDir;
		this.health = health;
		maxHealth = health;
	
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
	public void run() {
		double animSpeed = 0.3;
		anim+= animSpeed;
		if(anim >= 8)
			anim = 0;
		
		
			
		texture = Main.enemyTexture[(int)anim][textureDir];
		if(X < Main.gridWidth-1 && Y < Main.gridHeight-1) {
			checkSurr();
			move();
		}
		
	}
}
