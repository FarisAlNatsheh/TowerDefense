package towerdef;
//GUI CraftPix.net 2D Game Assets buttons and windows
//Xavier4321 turret: The original pic's are from Scorpio's Construction Kit2. Check it out here (http://opengameart.org/content/space-ship-construction-kit)
//Xavier4321 bot Made with Wuditog's Parts2 App (https://www.arrall.com/part2art/). Parts from Scorpio (http://opengameart.org/content/space-ship-construction-kit)
//Credit Skorpio for the kit and Kanadaj for the design either on the start-screen, menu, end-screen or credits screen. titan turret
//credit dklon for laser sound 
//Link To mobeyee.com laser
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
//COMMENT
//ADD BACKGROUND FOR TURRET SELECTION
//ADD MORE TURRETS
//FIX RANGE
////
@SuppressWarnings("serial")
public class Main extends JFrame{
	static int reAdjust = 0;
	
	static int mouseX;
	static int mouseY;
	//Mouse location on screen
	static AudioInputStream audio;
	static Clip clip;
	static FloatControl volume;
	static double volumeEff = -10;
	static double vol = -10;
	static boolean song = false;
	//Audio variables
	static Clip effect;
	static int tick, tps;
	//Tick counter variables

	static int winHeight = 720;
	static int winWidth = 1280;
	static int gridHeight = 13;
	static int gridWidth = 21;
	static int tileHeight = winHeight/gridHeight;
	static int tileWidth = winWidth/gridWidth;
	static int[][] map = new int[gridWidth][gridHeight];
	//Rendering variables

	static int playerHealth = 100, wave = 1;
	static double playerMoney = 100;
	//Game stats

	static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	static ArrayList<Tower> towers = new ArrayList<Tower>();
	static ArrayList<BloodSpot> bloodSpots = new ArrayList<BloodSpot>();
	//Entity lists

	static TextBuilder builder = new TextBuilder();
	//for drawing strings with sprite font

	static BufferedImage[] enemyTexture, enemyTexture2;
	static BufferedImage projectileImg, blood, bgImg, bloodAnim, menuBack, selection, bossTexture;
	static BufferedImage[][] textures;
	//textures

	static ArrayList<Dimension> placedTowers = new ArrayList<Dimension>();
	static int selectedTower;
	//To save the locations of already placed towers ( checked to avoid adding multiple towers in the same spot)


	static long startTime, startTime2 = System.nanoTime();
	static int delay;

	static int targetTPS = 70;
	static boolean toggleFullscreen = false;
	//delay before each frame (how often should the program tick)

	Menu menu;
	WaveHandler waveHandler;
	static int menuSwitch = 3; //Menu switch
	//1 game
	//2 death
	//3 menu
	//4 settings
	
	static int towerSelection = 0;
	static int barLength = 31;
	boolean fullscreen;
	static boolean game, adjust,start;
	static StatWindow stats;
	Button pause;
	int mouseXi, mouseYi;
	static double frames;
	static int FPS;
	public Main() {
		vol =-80 + 70/100.0 * 86;
		volumeEff =-80 +70/100.0 * 86;
		pause = new Button(tileWidth/4,winHeight-tileHeight*2,"Pause",tileWidth/4);
		music("Sound/Menu.wav");
		//Re-adjusting variables depending on screen size
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				//System.out.println("Resized to " + e.getComponent().getSize());
				winWidth = getWidth();
				winHeight = getHeight();
				reAdjust = 0;
				reAdjust();
				try {
					stats.adjust();
				}
				catch(Exception e1) {}
			}
		});
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				mouseXi = e.getX();
				mouseYi = e.getY();
				if(menuSwitch == 1) {
					menu.gameClick(mouseXi, mouseYi, e);
						stats.animate(mouseXi, mouseYi);
				}
				else if(menuSwitch == 2) 
					menu.deathClick(mouseXi, mouseYi);
				else if(menuSwitch == 3) {
					menu.menuAnimate(mouseXi, mouseYi);
				}
				else if(menuSwitch == 4)
					menu.settingsAnimate(mouseXi, mouseYi);
			}
			public void mouseReleased(MouseEvent e) {
				if(menuSwitch == 1) {
					pauseGame(mouseXi, mouseYi);
					menu.gameClick(mouseXi, mouseYi, e);
						stats.click(mouseXi, mouseYi, Main.selectedTower);
				}
				else if(menuSwitch == 2) 
					menu.deathClick(mouseXi, mouseYi);
				else if(menuSwitch == 3) {
					menu.menuClick(mouseXi, mouseYi);
				}
				else if(menuSwitch == 4) {
					menu.settingsClick(mouseXi, mouseYi);
				}
				if(toggleFullscreen) {
					setFullscreen();
					toggleFullscreen = false;
				}
				menu.release(mouseXi, mouseYi);
				placeTower(e.getX(), e.getY());
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				mouseX= e.getX()/tileWidth *tileWidth;
				mouseY= (e.getY()-barLength)/tileHeight *tileHeight;
			}
			public void mouseDragged(MouseEvent e) {
				mouseX= e.getX()/tileWidth *tileWidth;
				mouseY= (e.getY()-barLength)/tileHeight *tileHeight;
			}
		});
		//Window setup
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
		setSize(winWidth, winHeight);
		setLayout(new GridLayout());
		setLocationRelativeTo(null);

		//Set starting values
		initializeMap();
		initializeTextures();

		menu = new Menu();
		waveHandler = new WaveHandler(3,0,3);
		JPanel game;
		stats = new StatWindow(13,7);
		//enemies.add(new Boss(3,0,3));
	
		game = new JPanel() {
			public void paintComponent(Graphics g) {
				frames = 0;
				startTime = System.nanoTime();
				//try {Thread.sleep(10);}catch(Exception e) {};
				
				draw(g);
				frames++;
				//TPS counter
				g.setColor(Color.white);
				if(System.nanoTime()-startTime2 >= 500000000) {
					FPS = (int) (frames/(System.nanoTime()-startTime)*1000000000);
					startTime2 = System.nanoTime();
				}
				g.drawString(FPS+ " FPS", winWidth-140, 10);
				repaint();
			} 
		};
		game.setDoubleBuffered(false);
		add(game);
		
		setVisible(true);
	}

	public void draw(Graphics g) {

		//Game loop
		switch(menuSwitch) {
		case 1:
			g.setColor(Color.black);
			g.fillRect(0,0,winWidth,winHeight);
			drawGrid(g);
			drawEnts(g);
			drawSelection(g);
			pause.draw(g);
			//Drawing entities and blocks
			
			builder.drawString(g, new StringBuilder("Health:").append(playerHealth).toString() , 0, 0, tileWidth/2);
			builder.drawString(g, new StringBuilder("Money:").append((int)playerMoney).toString() , 10, tileHeight/2, tileWidth/2);
			builder.drawString(g, new StringBuilder("Wave:").append(wave).toString() , 10, tileHeight, tileWidth/2);

			//Draw text
			if(stats.visible)
				stats.draw(g);
			break;
		case 2:
			winWidth = getWidth()-tileWidth*4;
			winHeight = getHeight();
			menu.death(g);
			break;
		case 3:
			menu.mainMenu(g);
			builder.drawString(g, "Faris Al-Natsheh 2022" , 0, winHeight-tileHeight, tileHeight/4);
			break;
		case 4:
			menu.settings(g);
			builder.drawString(g, "Faris Al-Natsheh 2022" , 0, winHeight-tileHeight, tileHeight/4);
			break;
		}
		



		if(song) {
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(new Color(255,0,0,30));
			g2.fillRect(0, 0, winWidth+tileWidth*4, winHeight);
		}

		
		



	} 
	
	public void drawGrid(Graphics g) {

		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(bgImg,0,0 ,winWidth, winHeight,null);

		//Draw blocks based on values in the array
		//Ignores empty blocks for performance

		for(int i = 0; i < gridHeight; i++) {
			for(int j = 0; j < gridWidth; j++) {
				if(map[j][i] == 0) {
					//g.drawImage(textures[18][6],tileWidth*j, tileHeight*i, tileWidth, tileHeight, null);
				}
				else if(map[j][i] == 1){
					g.drawImage(textures[4][3],tileWidth*j, tileHeight*i, tileWidth, tileHeight, null);
				}
				else if(map[j][i] == 2){
					g.drawImage(textures[0][0],tileWidth*j, tileHeight*i, tileWidth, tileHeight, null);
				}
			}
		}



	}

	public void drawEnts(Graphics g) {
		for(Entity k: bloodSpots) {
			k.draw(g);
		}
		for(Entity k : towers) {
			k.draw(g);
		}
		for(int i =0; i < enemies.size();i++) {
			Enemy k = enemies.get(i);
			if(k != null)
				k.draw(g);
		}

		if(selectedTower != 0)
			towers.get(selectedTower-1).drawRange((Graphics2D)g, true);
		//Draw all entities

	}
	
	public void drawSelection(Graphics g) {
		g.drawImage(selection, winWidth, 0, tileWidth*4,winHeight,null);
		g.drawImage(Tower1.texture, winWidth+tileWidth,0 ,tileWidth*2, tileHeight*2,null );
		builder.drawString(g, "100" , winWidth+tileWidth,tileHeight*2 , tileHeight/2);
		g.drawImage(Tower2.texture, winWidth+tileWidth,tileHeight*3 ,tileWidth*2, tileHeight*2,null );
		builder.drawString(g, "350" , winWidth+tileWidth,tileHeight*5 , tileHeight/2);
		g.drawImage(Tower3.texture, winWidth+tileWidth,tileHeight*6 ,tileWidth*2, tileHeight*2,null );
		builder.drawString(g, "50" , winWidth+tileWidth,tileHeight*8 , tileHeight/2);
		if(towerSelection == 1) {
			g.drawImage(Tower1.texture, mouseX, mouseY,tileWidth, tileHeight,null );
			g.setColor(new Color(255,255,255,50));
			g.fillOval((int)((mouseX/tileWidth-Tower1.defaultRange)*tileWidth), (int)((mouseY/tileHeight-Tower1.defaultRange)*Main.tileHeight), (int) ((Tower1.defaultRange*2+1)*Main.tileWidth), (int)((Tower1.defaultRange*2+1)*Main.tileHeight));
		}
		else if(towerSelection == 2) {
			g.drawImage(Tower2.texture, mouseX, mouseY,tileWidth, tileHeight,null );
			g.setColor(new Color(255,255,255,50));
			g.fillOval((int)((mouseX/tileWidth-Tower2.defaultRange)*tileWidth), (int)((mouseY/tileHeight-Tower2.defaultRange)*Main.tileHeight), (int) ((Tower2.defaultRange*2+1)*Main.tileWidth), (int)((Tower2.defaultRange*2+1)*Main.tileHeight));
		}
		else if(towerSelection == 3) {
			g.drawImage(Tower3.texture, mouseX, mouseY,tileWidth, tileHeight,null );
			g.setColor(new Color(255,255,255,50));
			g.fillOval((int)((mouseX/tileWidth-Tower3.defaultRange)*tileWidth), (int)((mouseY/tileHeight-Tower3.defaultRange)*Main.tileHeight), (int) ((Tower3.defaultRange*2+1)*Main.tileWidth), (int)((Tower3.defaultRange*2+1)*Main.tileHeight));
		}
	}

	public void pauseGame(int x, int y) {
		if(x > pause.x &&
				x <= pause.x + pause.size*(pause.s.length()+2) &&
				y-barLength > pause.y &&
				y-barLength <= pause.y+pause.size+tileWidth/2) {
			menuSwitch = 4;
			winWidth = getWidth();
			winHeight = getHeight();
			reAdjust();
			Main.start = false;
		}	
	}

	public static void resetGame() {
		song = false;
		clip.stop();
		music("Sound/Songgame.wav");
		menuSwitch = 1;
		playerHealth= 100;
		playerMoney = 100;
		wave = 1;
		enemies = new ArrayList<Enemy>();
		towers = new ArrayList<Tower>();
		bloodSpots = new ArrayList<BloodSpot>();
		placedTowers = new ArrayList<Dimension>();
		WaveHandler.enemies = 1;
		WaveHandler.prevEnemies = 1;
	}

	public void game() {
		//Run each tick
		game = true;
		//reAdjust();
		if(tick%10 == 0) {
			if(reAdjust < 5) {
				reAdjust();
				reAdjust++;
			}
		}
		checkCollisions();
		if(playerHealth <= 50) {
			if(!song) {
				clip.stop();
				clip.stop();
				music("Sound/intense.wav");
				song = true;
			}
		}
		if(playerHealth <= 0) {
			playerHealth = 0;
			menuSwitch = 2;
		}


		for(Enemy i: enemies) {
			i.run();
		}
		//Move enemies

		for(int k = 0; k < towers.size();k++) {
			for(int i = 0;i< towers.get(k).projectiles.size();i++) {
				towers.get(k).projectiles.get(i).move();
				if(towers.get(k).projectiles.get(i).x < 0 || towers.get(k).projectiles.get(i).y < 0) {
					towers.get(k).projectiles.set(i, null);
					towers.get(k).projectiles.remove(i);
				}
			}
		}
		//Move projectiles and remove them if they go offscreen


		waveHandler.runRound();
		//Spawn enemies based on round pattern

		if(enemies.size() > 0 && towers.size()>0)
			for(Tower k : towers)
				k.fire();
		//Fire towers

		if(tick % 1000 == 0) {
			for(int i = 0; i < bloodSpots.size(); i++) {
				bloodSpots.set(i, null);
				bloodSpots.remove(i);
				i--;
			}
		}
		//Clear blood spots every interval
	}

	public static void placeTower(int x, int y) {
		int mouseX=x/tileWidth;
		int mouseY= (y-barLength)/tileHeight;
		if(x > winWidth && y-barLength < tileHeight*2) {
			towerSelection = 1;
		}
		if(x > winWidth && y-barLength > tileHeight*2  && y-barLength < tileHeight*5) {
			towerSelection = 2;
		}
		if(x > winWidth && y-barLength > tileHeight*6  && y-barLength < tileHeight*8) {
			towerSelection = 3;
		}
		for(Dimension k : placedTowers) {
			if(k.width == mouseX && k.height == mouseY)
				return;
		}
		try {
			if(map[mouseX][mouseY] != 1 && map[mouseX][mouseY] != 2  && towerSelection != 0) {
				Tower tower = null;
				if(towerSelection == 1)
					tower = new Tower1(mouseX, mouseY);
				if(towerSelection == 2)
					tower = new Tower2(mouseX, mouseY);
				if(towerSelection == 3)
					tower = new Tower3(mouseX, mouseY);
				if(tower.price <= playerMoney) {
					towers.add(tower);
					placedTowers.add(new Dimension(mouseX, mouseY));
					towerSelection = 0;
					playerMoney-=tower.price;
				}
			}
			else
				towerSelection = 0;
		}
		catch(Exception e1) {};
	}

	public void setFullscreen() {
		if(!fullscreen) {
			dispose();
			setUndecorated(true);
			setVisible(true);
			setExtendedState(JFrame.MAXIMIZED_BOTH );
			fullscreen = true;
			barLength = 0;
		}
		else {
			dispose();
			setUndecorated(false);
			setVisible(true);
			fullscreen = false;
			barLength = 31;
		}
	}
	
	@SuppressWarnings("static-access")
	
	public static int rand(int min, int max) {
		return (int) Math.floor(Math.random()*(max-min+1)+min);
	}

	public void checkCollisions() {
		
		try {
			for(int i = 0; i < enemies.size();i++) { 
				for(int k = 0; k < towers.size(); k++)
					for(int j = 0; j < towers.get(k).projectiles.size();j++) {
						Enemy obj = enemies.get(i);
						if(towers.get(k).projectiles.get(j).x >= obj.X*tileWidth+obj.animX-towers.get(k).projectiles.get(j).width && 
								towers.get(k).projectiles.get(j).x <= obj.X*tileWidth+obj.animX+tileWidth)
							if(towers.get(k).projectiles.get(j).y >= obj.Y*tileHeight+obj.animY-towers.get(k).projectiles.get(j).height && 
							towers.get(k).projectiles.get(j).y <= obj.Y*tileHeight+obj.animY+tileHeight) {
								if(enemies.get(i).health <= 1) { 
									//bloodSpots.add(new BloodSpot(enemies.get(i).X,enemies.get(i).Y,enemies.get(i).animX,enemies.get(i).animY));
									enemies.set(i, null);
									enemies.remove(i);
									Boss.playing = false;
									playerMoney+=Enemy.value;
								}
								else {
									enemies.get(i).health-= towers.get(k).projectiles.get(j).damage;
									if(enemies.get(i).health <= 1) { 
										//bloodSpots.add(new BloodSpot(enemies.get(i).X,enemies.get(i).Y,enemies.get(i).animX,enemies.get(i).animY));
										enemies.set(i, null);
										enemies.remove(i);
										Boss.playing = false;
										playerMoney+=Enemy.value;
									}
								}
								if(towers.get(k).projectiles.get(j).pierce <= 1) {
									towers.get(k).projectiles.set(j, null);
									towers.get(k).projectiles.remove(j);
								}
								else 
									towers.get(k).projectiles.get(j).pierce--;

								towers.get(k).incCount();;
							}
						//Check the collisions between the midpoints of the projectile and the enemy

						if(towers.get(k).projectiles.get(j).x > winWidth 
								|| towers.get(k).projectiles.get(j).y > winHeight
								|| towers.get(k).projectiles.get(j).x < 0
								|| towers.get(k).projectiles.get(j).y < 0) {
							towers.get(k).projectiles.set(j, null);
							towers.get(k).projectiles.remove(j);
						}
					}
				if(map[enemies.get(i).X][enemies.get(i).Y] == 2) {
					playerHealth-= enemies.get(i).damage;
					enemies.set(i, null);
					enemies.remove(i);
					Boss.playing = false;
				} 
				//remove enemies if they go to the marked spot
			}
		}
		catch(Exception c) {

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
		//map[3][0] = 0;
		map[(gridWidth)/2+1][12] = 1;
		map[(gridWidth)/2+1][11] = 2;
	}

	public void initializeTextures() {
		Tower1.initializeTexture();
		Tower2.initializeTexture();
		Tower3.initializeTexture();
		BufferedImage sheet = null;
		try {
			sheet = ImageIO.read(new File("Textures/texturess.png"));
			projectileImg = ImageIO.read(new File("Textures/laser.png"));
			blood = ImageIO.read(new File("Textures/blood.png"));
			bgImg = ImageIO.read(new File("Textures/background.png"));
			bloodAnim = ImageIO.read(new File("Textures/bloodAnim.png"));
			menuBack = ImageIO.read(new File("Textures/bg.jpg"));
			selection = ImageIO.read(new File("Textures/gradient.png"));
			bossTexture = ImageIO.read(new File("Textures/REDBOSS.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Import sprites that arent in sheets or are initialized in other classes
		int scale = 32;
		int sheetHeight = sheet.getHeight(null)/scale;
		int sheetWidth = sheet.getWidth(null)/scale;
		textures = new BufferedImage[sheetWidth][sheetHeight];
		for(int i = 0; i < sheetWidth; i++) {
			for(int i2 = 0; i2 < sheetHeight; i2++) {
				textures[i][i2] = sheet.getSubimage(i * scale,  i2 * scale, scale,scale );
			}
		}
		try {
			sheet = ImageIO.read(new File("Textures/spritesheet.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		scale = 189;
		sheetHeight = sheet.getHeight(null)/scale;
		sheetWidth = sheet.getWidth(null)/scale;
		enemyTexture = new BufferedImage[sheetWidth];
		for(int i = 0; i < sheetWidth; i++) {
			enemyTexture[i]= sheet.getSubimage(i * 195,  0, 195,189 );
		}
		try {
			sheet = ImageIO.read(new File("Textures/enemy2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheetWidth = sheet.getWidth(null)/96;
		enemyTexture2 = new BufferedImage[sheetWidth];

		for(int i = 0; i < sheetWidth; i++) {
			enemyTexture2[i]= sheet.getSubimage(i * 96,  0, 96,96 );
		}
		//Cuts spirtesheets based on size and saves them into arrays of buffered images




	}

	@SuppressWarnings("static-access")
	public static void music(String songName) {

		try {
			audio = AudioSystem.getAudioInputStream(new File(songName));
			clip = AudioSystem.getClip();
			clip.open(audio);
			clip.start();
			clip.loop(clip.LOOP_CONTINUOUSLY);

			volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue((float) vol);
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
	}

	public void reAdjust(){
		if(menuSwitch == 1) {
			winWidth = getWidth()-tileWidth*4;
			winHeight = getHeight();
		}
		tileHeight = winHeight/gridHeight;
		tileWidth = winWidth/gridWidth;
		pause = new Button(tileWidth/4,winHeight-tileHeight*2,"Pause",tileWidth/4);
		for(Tower k : towers) {
			k.width = Main.tileWidth;
			k.height = Main.tileHeight;
			k.x = k.mapX * Main.tileWidth+Main.tileWidth/2;
			k.y = k.mapY * Main.tileHeight+Main.tileHeight/2;
		}
		for(Enemy k : enemies) {
			k.readjust();
		}
		try {
			menu.readjust();
			stats.adjust();
		}
		catch(Exception e1) {}
		
	}


}
