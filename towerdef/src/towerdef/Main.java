package towerdef;


import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class Main extends JFrame{
	static int tick, tps, playerHealth = 100, playerMoney = 100;
	static int winHeight = 720;
	static int winWidth = 1280;
	static int gridHeight = 13;
	static int gridWidth = 21;
	static int tileHeight = winHeight/gridHeight;
	static int tileWidth = winWidth/gridWidth;
	static int[][] map = new int[gridWidth][gridHeight];
	long startTime = System.currentTimeMillis();
	static BufferedImage[][] enemyTexture;
	BufferedImage[][] textures;
	ArrayList<Dimension> placedTowers = new ArrayList<Dimension>();
	static BufferedImage projectileImg, blood, bgImg, towerImg, bloodAnim;
	static TextBuilder builder = new TextBuilder();
	ArrayList<Enemy> bloons = new ArrayList<Enemy>();
	ArrayList<Tower> towers = new ArrayList<Tower>();
	ArrayList<BloodSpot> bloodSpots = new ArrayList<BloodSpot>();
	ArrayList<Entity> entities = new ArrayList<Entity>();
	long time;
	int delay , targetTPS = 60;
	Menu menu;
	boolean stop;
	public Main() {
		initializeMap();
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				//System.out.println("Resized to " + e.getComponent().getSize());
				winWidth = getWidth();
				winHeight = getHeight();
				tileHeight = winHeight/gridHeight;
				tileWidth = winWidth/gridWidth;

				for(Tower k : towers) {
					k.width = Main.tileWidth;
					k.height = Main.tileHeight;
					k.x = k.mapX * Main.tileWidth+Main.tileWidth/2;
					k.y = k.mapY * Main.tileHeight+Main.tileHeight/2;
				}
				for(Enemy k : bloons) {
					k.speedX = tileWidth/30;
					k.speedY = tileHeight/30;
				}
				try {
					menu.readjust();
				}
				catch(Exception e1) {}
			}
		});
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int mouseX= e.getX()/tileWidth;
				int mouseY= (e.getY()-31)/tileHeight;
				for(Dimension k : placedTowers) {
					if(k.width == mouseX && k.height == mouseY)
						return;
				}
				try {
					if(map[mouseX][mouseY] != 1 && map[mouseX][mouseY] != 2) {
						towers.add(new Tower(mouseX, mouseY));
						placedTowers.add(new Dimension(mouseX, mouseY));
					}
				}
				catch(Exception e1) {};
			}
		});
		//towers.add(new Tower(6*tileWidth,2*tileHeight));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
		setSize(winWidth, winHeight);
		setLayout(new GridLayout());
		setLocationRelativeTo(null);       
		initializeTextures();
		menu = new Menu();
		JPanel game;
		//bloons.add(new Enemy(3,1,3));
		startTime = System.currentTimeMillis();

		game = new JPanel() {
			public void paint(Graphics g) {
				startTime = System.currentTimeMillis();
				try {Thread.sleep(delay);} 
				catch (InterruptedException e) {}
				repaint();
				drawGrid(g);
				drawEnts(g);
				checkCollisions();
				tick++;
				tps++;
				if(!stop) {
					game();
				}
				else {

					menu.death(g);
				}

				g.drawString(Double.toString(tps/((System.currentTimeMillis()-startTime)/1000.0) ), winWidth-140, 10); ;
				if(tps/((System.currentTimeMillis()-startTime)/1000.0) > targetTPS + 10)
					delay++;
				else if(tps/((System.currentTimeMillis()-startTime)/1000.0) < targetTPS- 10)
					if(delay != 0)
						delay--;
				tps = 0;
			} 
		};
		add(game);
		setVisible(true);
	}
	public void game() {
		checkCollisions();

		if(playerHealth <= 0) {
			playerHealth = 0;
			stop = true;
		}
		for(Enemy i: bloons) {
			i.run();
		}
		for(int k = 0; k < towers.size();k++) {
			for(int i = 0;i< towers.get(k).projectiles.size();i++) {
				towers.get(k).projectiles.get(i).move();
				if(towers.get(k).projectiles.get(i).x < 0 || towers.get(k).projectiles.get(i).y < 0)
					towers.get(k).projectiles.remove(i);
			}
		}


		if(tick % rand(1,100) == 0)
			bloons.add(new Enemy(3,0,3,400));
		if(bloons.size() > 0 && towers.size()>0)
			for(Tower k : towers)
				k.fire(bloons.get(0));

		if(tick % 1000 == 0) {
			if(bloodSpots.size() > 0)
				bloodSpots = new ArrayList<BloodSpot>();
			/*if(towers.size() > 0)
				for(Tower k: towers)
					if(k.projectiles.size() > 0)
						k.projectiles = new ArrayList<Projectile>();*/
		}

	}
	public int rand(int min, int max) {
		return (int) Math.floor(Math.random()*(max-min+1)+min);
	}
	public void checkCollisions() {

		try {
			for(int i = 0; i < bloons.size();i++) { 
				for(int k = 0; k < towers.size(); k++)
					for(int j = 0; j < towers.get(k).projectiles.size();j++) {
						Enemy obj = bloons.get(i);
						if(towers.get(k).projectiles.get(j).x >= obj.X*tileWidth+obj.animX-towers.get(k).projectiles.get(j).width && towers.get(k).projectiles.get(j).x <= obj.X*tileWidth+obj.animX+tileWidth)
							if(towers.get(k).projectiles.get(j).y >= obj.Y*tileHeight+obj.animY-towers.get(k).projectiles.get(j).height && towers.get(k).projectiles.get(j).y <= obj.Y*tileHeight+obj.animY+tileHeight) {
								if(bloons.get(i).health <= 1) { 
									bloodSpots.add(new BloodSpot(bloons.get(i).X,bloons.get(i).Y,bloons.get(i).animX,bloons.get(i).animY, rand(0,3)));
									bloons.remove(i);
									playerMoney+=10;
								}
								else {
									bloons.get(i).health-= towers.get(k).projectiles.get(j).damage;
									if(bloons.get(i).health <= 1) { 
										bloodSpots.add(new BloodSpot(bloons.get(i).X,bloons.get(i).Y,bloons.get(i).animX,bloons.get(i).animY, rand(0,3)));
										bloons.remove(i);
										playerMoney+=10;
									}
								}
								if(towers.get(k).projectiles.get(j).pierce <= 1)
									towers.get(k).projectiles.remove(j);
								else
									towers.get(k).projectiles.get(j).pierce--;

							}
					}
				if(map[bloons.get(i).X][bloons.get(i).Y] == 2) {
					playerHealth-= bloons.get(i).damage;
					bloons.remove(i);
				}
			}
		}
		catch(Exception c) {

		}
	} 
	public void drawGrid(Graphics g) {
		g.drawImage(bgImg,0,0 ,winWidth, winHeight,null);
		for(int i = 0; i < gridHeight; i++) {
			for(int j = 0; j < gridWidth; j++) {
				if(map[j][i] == 0) {
					g.drawImage(textures[9][1],tileWidth*j, tileHeight*i, tileWidth, tileHeight, null);
				}
				else if(map[j][i] == 1){
					g.drawImage(textures[9][17],tileWidth*j, tileHeight*i, tileWidth, tileHeight, null);
				}
				else if(map[j][i] == 2){
					g.drawImage(textures[3][16],tileWidth*j, tileHeight*i, tileWidth, tileHeight, null);
				}
			}
		}

	}
	public void initializeMap() {
		for(int i = 0; i < 3; i++) {
			map[3][i] = 1;
		}
		for(int i = 4; i < gridWidth-1; i++) {
			map[i][2] = 1;
			map[i][5] = 1;
		}
		for(int i = 3; i < (gridWidth)/2+1; i++) {
			map[i][8] = 1;
		}
		for(int i = 2; i < 5; i++) {
			map[gridWidth-2][i] = 1;
			map[3][i+3] = 1;

		}
		for(int i = 8; i < 11; i++) {
			map[(gridWidth)/2+1][i] = 1;
		}
		map[(gridWidth)/2+1][12] = 1;
		map[(gridWidth)/2+1][11] = 2;
	}
	public void drawEnts(Graphics g) {
		for(Entity k: bloodSpots) {
			k.draw(g);
		}
		for(Entity k : bloons) {
			k.draw(g);
		}
		for(Entity k : towers) {
			k.draw(g);
		}
		builder.drawString(g, new StringBuilder("Health:").append(playerHealth).toString() , 0, 0, tileWidth/2);
		builder.drawString(g, new StringBuilder("Money:").append(playerMoney).toString() , 10, tileWidth/2, tileWidth/2);
	}
	public void initializeTextures() {
		BufferedImage sheet = null;
		try {
			sheet = ImageIO.read(new File("textures.png"));
			projectileImg = ImageIO.read(new File("laser.png"));
			blood = ImageIO.read(new File("blood.png"));
			bgImg = ImageIO.read(new File("background.jpg"));
			towerImg = ImageIO.read(new File("tower.png"));
			bloodAnim = ImageIO.read(new File("bloodAnim.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int scale = 16;
		int sheetHeight = sheet.getHeight(null)/scale;
		int sheetWidth = sheet.getWidth(null)/scale;
		textures = new BufferedImage[sheetWidth][sheetHeight];
		for(int i = 0; i < sheetWidth; i++) {
			for(int i2 = 0; i2 < sheetHeight; i2++) {
				textures[i][i2] = sheet.getSubimage(i * scale,  i2 * scale, scale,scale );
			}
		}
		try {
			sheet = ImageIO.read(new File("zombie2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		scale = 64;
		sheetHeight = sheet.getHeight(null)/scale;
		sheetWidth = sheet.getWidth(null)/scale;
		enemyTexture = new BufferedImage[sheetWidth][sheetHeight];
		for(int i = 0; i < sheetWidth; i++) {
			for(int i2 = 0; i2 < sheetHeight; i2++) {
				enemyTexture[i][i2] = sheet.getSubimage(i * scale,  i2 * scale, scale,scale );
			}
		}




	}

	public static void main(String args[]) {new Main();}
}
