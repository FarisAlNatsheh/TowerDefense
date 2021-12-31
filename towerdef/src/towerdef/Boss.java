package towerdef;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Boss extends Enemy{
	static boolean playing;
	int bossH;
	public Boss(int startX, int startY, int startDir, int health) {
		super(startX, startY, startDir, 3);
		bossH = health;
		setEnemy();
	}
	public void setEnemy() {
			health = bossH;
			maxHealth = health;
			damage = 1000;
	}
	public void draw(Graphics g) {
		if(!playing) {
			Main.clip.stop();
			Main.music("boss song.wav");
			playing = true;
		}
		double angle = 0;
		if(dir == 0)
			angle = Math.PI;
		else if(dir == 1)
			angle = Math.PI/-2;
		else if(dir == 2)
			angle = 0;
		else if(dir == 3)
			angle = Math.PI/2;
		
		angle+= Math.PI/-2;
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(angle, X*Main.tileWidth+animX+Main.tileWidth/2, Y*Main.tileHeight+animY+ Main.tileHeight/2);
		g.drawImage(texture, Main.tileWidth*X+(int)animX - Main.tileWidth*3, Main.tileHeight*Y+(int)animY- Main.tileHeight*5, Main.tileWidth*6, Main.tileHeight*9, null);
		g2.rotate((angle)*-1, X*Main.tileWidth+animX+Main.tileWidth/2, Y*Main.tileHeight+animY+ Main.tileHeight/2);

		g.setColor(new Color(0,0,0));
		g.fillRect(Main.winWidth/2 - Main.tileWidth*4 , Main.winHeight-Main.tileHeight*2, Main.tileWidth*8, Main.tileHeight/2);
		g.setColor(new Color(120,0,0));
		g.fillRect(Main.winWidth/2 - Main.tileWidth*4 , Main.winHeight-Main.tileHeight*2, (int)(Main.tileWidth*8*(1.0*health/maxHealth)), Main.tileHeight/2);
		g.setColor(new Color(0,0,0));
	}
}
