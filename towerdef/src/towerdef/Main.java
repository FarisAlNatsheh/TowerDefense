package towerdef;
//LASER SOUND
//GUI CraftPix.net 2D Game Assets


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

//ADD BACKGROUND FOR TURRET SELECTION
//ADD MORE TURRETS
//FIX RANGE
@SuppressWarnings("serial")
public class Main extends JFrame{

	int mouseX, mouseY;
	//Mouse location on screen
	static AudioInputStream audio;
	static Clip clip;
	static FloatControl volume;
	static double volumeEff = -10;
	static double vol = -10;
	boolean song = false;
	//Audio variables

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

	static int playerHealth = 100, playerMoney = 123456789, wave = 1;
	//Game stats

	static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	static ArrayList<Tower> towers = new ArrayList<Tower>();
	static ArrayList<BloodSpot> bloodSpots = new ArrayList<BloodSpot>();
	//Entity lists

	static TextBuilder builder = new TextBuilder();
	//for drawing strings with sprite font

	static BufferedImage[] enemyTexture, enemyTexture2;
	static BufferedImage projectileImg, blood, bgImg, towerImg, bloodAnim, buttonImg, menuBack, selection;
	static BufferedImage[][] textures;
	//textures

	ArrayList<Dimension> placedTowers = new ArrayList<Dimension>();
	int selectedTower;
	//To save the locations of already placed towers ( checked to avoid adding multiple towers in the same spot)


	long startTime;
	int delay , targetTPS = 70;
	//delay before each frame (how often should the program tick)

	Menu menu;
	int menuSwitch = 3; //Menu switch
	//1 game
	//2 death
	//3 menu

	int towerSelection = 0;
	static int barLength = 31;
	boolean fullscreen;
	static boolean game;
	StatWindow stats;
	Button pause;
	public void reAdjust(){
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
		}
		catch(Exception e1) {}
	}
	public Main() {
		pause = new Button(tileWidth/4,winHeight-tileHeight*2,"Pause",tileWidth/4);
		music("Menu.wav");
		//Re-adjusting variables depending on screen size
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				//System.out.println("Resized to " + e.getComponent().getSize());
				winWidth = getWidth();
				winHeight = getHeight();
				reAdjust();
				try {
					stats.adjust();
				}
				catch(Exception e1) {}
			}
		});

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {

				for(int i = 0; i < Menu.components.length; i++) {
					Button k = Menu.components[i];
					if(e.getX() > k.x &&
							e.getX() <= k.x + k.size*(k.s.length()+2) &&
							e.getY()-barLength > k.y &&
							e.getY()-barLength <= k.y+k.size) {
						if(menuSwitch == 3)
							switch(i) {
							case 0:
								clip.stop();
								music("Songgame.wav");
								menuSwitch = 1; 
								return;
							case 1:menuSwitch =  4;break;
							case 2:
								System.exit(1);break;
							}
						else if(menuSwitch == 2) {
							if(i == 3) {
								resetGame();
								return;
							}
						}
					}

				}
				for(int i = 0; i < Menu.settingsComps.length; i++) {
					Button k = Menu.settingsComps[i];
					if(e.getX() > k.x &&
							e.getX() <= k.x + k.size*(k.s.length()+2) &&
							e.getY()-barLength > k.y &&
							e.getY()-barLength <= k.y+k.size) {
						if(menuSwitch == 4)
							switch(i) {
							case 0:
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
								break;
							case 1:if(menu.vol < 100)menu.vol++;break;
							case 2:if(menu.vol > 0)menu.vol--;break;
							case 3:if(menu.volumeEff < 100)menu.volumeEff++;break;
							case 4:if(menu.volumeEff > 0)menu.volumeEff--;break;
							case 5:
								vol =-80 + menu.vol/100.0 * 86;
								volumeEff =-80 +menu.volumeEff/100.0 * 86;
								
								if(!game) {
									clip.stop();
									music("Menu.wav");
									menuSwitch = 3;
								}
								else{
									clip.stop();
									if(playerHealth > 50)
										music("Songgame.wav");
									else
										music("intense.wav");
									menuSwitch = 1;
								}
								break;
							case 6:
								if(game) {
									resetGame();
									clip.stop();
									music("Menu.wav");
									game = false;
									menuSwitch = 3;
								}
								break;
							}
						else if(menuSwitch == 2) {
							if(i == 3) {
								resetGame();
								return;
							}
						}
					}

				}
				if(menuSwitch == 1) {
					if(e.getX() > pause.x &&
							e.getX() <= pause.x + pause.size*(pause.s.length()+2) &&
							e.getY()-barLength > pause.y &&
							e.getY()-barLength <= pause.y+pause.size) {
						menuSwitch = 4;
						
						winWidth = getWidth();
						winHeight = getHeight();
						reAdjust();
					}
					for(int i =0; i < towers.size(); i++) {
						if(mouseX/tileWidth == towers.get(i).mapX && mouseY/tileHeight == towers.get(i).mapY) {selectedTower = i+1;break;}
						selectedTower = 0;
					}
					stats.click(e.getX(), e.getY(), selectedTower);
					if(e.getButton() == MouseEvent.BUTTON3) {towerSelection = 0;}
					int mouseX= e.getX()/tileWidth;
					int mouseY= (e.getY()-barLength)/tileHeight;
					if(e.getX() > winWidth && e.getY()-barLength < tileHeight*2) {
						towerSelection = 1;
					}
					for(Dimension k : placedTowers) {
						if(k.width == mouseX && k.height == mouseY)
							return;
					}
					try {
						if(map[mouseX][mouseY] != 1 && map[mouseX][mouseY] != 2 && towerSelection == 1 && playerMoney >= 50) {
							Tower tower = new Tower(mouseX, mouseY);
							towers.add(tower);
							placedTowers.add(new Dimension(mouseX, mouseY));
							playerMoney-=50;
							towerSelection = 0;
						}
						else
							return;
					}
					catch(Exception e1) {};
				}


			}
			public void mouseReleased(MouseEvent e) {
				int mouseX= e.getX()/tileWidth;
				int mouseY= (e.getY()-barLength)/tileHeight;
				if(e.getX() > winWidth && e.getY()-barLength < tileHeight*2) {
					towerSelection = 1;
				}
				for(Dimension k : placedTowers) {
					if(k.width == mouseX && k.height == mouseY)
						return;
				}
				try {
					if(map[mouseX][mouseY] != 1 && map[mouseX][mouseY] != 2 && towerSelection == 1 && playerMoney >= 50) {
						Tower tower = new Tower(mouseX, mouseY);
						towers.add(tower);
						placedTowers.add(new Dimension(mouseX, mouseY));
						towerSelection = 0;
						playerMoney-=50;
					}
					else
						towerSelection = 0;
				}
				catch(Exception e1) {};
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

		JPanel game;
		//enemies.add(new Enemy(3,0,3,200));
		stats = new StatWindow(13,7);

		game = new JPanel() {
			public void paint(Graphics g) {
				//Game loop
				startTime = System.currentTimeMillis();
				try {Thread.sleep(delay);} 
				catch (InterruptedException e) {}

				repaint();
				drawGrid(g);
				drawEnts(g);
				checkCollisions();
				drawSelection(g);
				//Drawing entities and blocks
				pause.draw(g);
				tick++;
				tps++;

				switch(menuSwitch) {
				case 1:
					game();
					break;
				case 2:
					winWidth = getWidth()-tileWidth*4;
					winHeight = getHeight();
					reAdjust();
					menu.death(g);
					break;
				case 3:
					menu.mainMenu(g);
					break;
				case 4:
					menu.settings(g);
					break;
				}


				if(song) {
					Graphics2D g2 = (Graphics2D)g;
					g2.setColor(new Color(255,0,0,30));
					g2.fillRect(0, 0, winWidth+tileWidth*4, winHeight);
				}

				//TPS counter
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

	public void resetGame() {
		song = false;
		clip.stop();
		music("Songgame.wav");
		menuSwitch = 1;
		playerHealth= 100;
		playerMoney = 100;
		wave = 1;
		enemies = new ArrayList<Enemy>();
		towers = new ArrayList<Tower>();
		bloodSpots = new ArrayList<BloodSpot>();
		placedTowers = new ArrayList<Dimension>();
	}
	public void game() {
		//Run each tick
		game = true;
		winWidth = getWidth()-tileWidth*4;
		winHeight = getHeight();
		reAdjust();
		checkCollisions();
		if(playerHealth <= 50) {
			if(!song) {
				clip.stop();
				clip.stop();
				music("intense.wav");
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
				if(towers.get(k).projectiles.get(i).x < 0 || towers.get(k).projectiles.get(i).y < 0)
					towers.get(k).projectiles.remove(i);
			}
		}
		//Move projectiles and remove them if they go offscreen

		if(tick % 40 == 0) {
			enemies.add(new Enemy(3,0,3, rand(1,2)));
		}
		//Spawn enemies randomly between a certain tick interval

		if(enemies.size() > 0 && towers.size()>0)
			for(Tower k : towers)
				k.fire();
		//Fire towers

		if(tick % 1000 == 0) {
			if(bloodSpots.size() > 0)
				bloodSpots = new ArrayList<BloodSpot>();
		}
		//Clear blood spots every interval
	}

	public int rand(int min, int max) {
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
									bloodSpots.add(new BloodSpot(enemies.get(i).X,enemies.get(i).Y,enemies.get(i).animX,enemies.get(i).animY));
									enemies.remove(i);
									playerMoney+=10;
								}
								else {
									enemies.get(i).health-= towers.get(k).projectiles.get(j).damage;
									if(enemies.get(i).health <= 1) { 
										bloodSpots.add(new BloodSpot(enemies.get(i).X,enemies.get(i).Y,enemies.get(i).animX,enemies.get(i).animY));
										enemies.remove(i);
										playerMoney+=10;
									}
								}
								if(towers.get(k).projectiles.get(j).pierce <= 1)
									towers.get(k).projectiles.remove(j);
								else 
									towers.get(k).projectiles.get(j).pierce--;

								towers.get(k).incCount();;
							}
						//Check the collisions between the midpoints of the projectile and the enemy

					}
				if(map[enemies.get(i).X][enemies.get(i).Y] == 2) {
					playerHealth-= enemies.get(i).damage;
					enemies.remove(i);
				} 
				//remove enemies if they go to the marked spot
			}
		}
		catch(Exception c) {

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
		for(Entity k : enemies) {
			k.draw(g);
		}
		for(Entity k : towers) {
			k.draw(g);
		}
		if(selectedTower != 0)
			towers.get(selectedTower-1).drawRange((Graphics2D)g, true);
		//Draw all entities
		builder.drawString(g, new StringBuilder("Health:").append(playerHealth).toString() , 0, 0, tileWidth/2);
		builder.drawString(g, new StringBuilder("Money:").append(playerMoney).toString() , 10, tileHeight/2, tileWidth/2);
		builder.drawString(g, new StringBuilder("Wave:").append(wave).toString() , 10, tileHeight, tileWidth/2);

		//Draw text
		if(stats.visible)
			stats.draw(g);
	}

	public void initializeTextures() {
		BufferedImage sheet = null;
		try {
			sheet = ImageIO.read(new File("texturess.png"));
			projectileImg = ImageIO.read(new File("laser.png"));
			blood = ImageIO.read(new File("blood.png"));
			bgImg = ImageIO.read(new File("background.png"));
			towerImg = ImageIO.read(new File("cannon2.png"));
			bloodAnim = ImageIO.read(new File("bloodAnim.png"));
			buttonImg = ImageIO.read(new File("button.png"));
			menuBack = ImageIO.read(new File("bg.jpg"));
			selection = ImageIO.read(new File("gradient.png"));
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
			sheet = ImageIO.read(new File("spritesheet.png"));
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
			sheet = ImageIO.read(new File("enemy2.png"));
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

	public void drawSelection(Graphics g) {
		Graphics2D g2= (Graphics2D) g;
		g.drawImage(selection, winWidth, 0, tileWidth*4,winHeight,null);
		g.drawImage(towerImg, winWidth+tileWidth,0 ,tileWidth*2, tileHeight*2,null );

		if(towerSelection == 1) {
			g.drawImage(towerImg, mouseX, mouseY,tileWidth, tileHeight,null );
			g.setColor(new Color(255,255,255,50));
			g.fillOval((int)((mouseX/tileWidth-Tower.defaultRange)*tileWidth), (int)((mouseY/tileHeight-Tower.defaultRange)*Main.tileHeight), (int) ((Tower.defaultRange*2+1)*Main.tileWidth), (int)((Tower.defaultRange*2+1)*Main.tileHeight));
		}
	}
	public static void music(String songName) { // grabs random a random .wav file and plays the song continuously, until the player changes it

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
	public static void soundEffect(String songName) { // grabs random a random .wav file and plays the song continuously, until the player changes it

		try {
			audio = AudioSystem.getAudioInputStream(new File(songName));
			clip = AudioSystem.getClip();
			clip.open(audio);
			volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue((float) volumeEff);
			clip.start();
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	public static void main(String args[]) {new Main();}

}
