package towerdef;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;



public class Menu {

	BufferedImage[] bldTexture;
	public int anim = 0;
	public static int size=Main.tileWidth*2;
	Button[] components = new Button[5];
	Button[] settingsComps = new Button[7];
	int vol = 70, volumeEff = 70;
	
	public Menu() {
		initializeTexture();
		setUpComponents();
	}
	void death(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if(Main.tick % 10 == 0)
			anim++;
		if(anim == 16)
			anim = 0;
		g2.drawImage(bldTexture[anim], 0, 0, Main.winWidth+Main.tileWidth*4, Main.winHeight,null);
		Main.builder.drawString(g2, "YOU DIED", (Main.winWidth+Main.tileWidth*4)/2-4*size, Main.winHeight/2-50, size);
		components[3].draw(g);
	}
	void mainMenu(Graphics g) {
		g.drawImage(Main.menuBack,0,0,Main.winWidth, Main.winHeight, null);
		//Main.builder.drawString(g, "MINDMATE", Main.winWidth/2- Main.tileWidth*4, Main.winHeight/2-Main.tileHeight*4, Main.tileWidth);
		Main.builder.drawString(g, "TOWER DEFENSE", Main.winWidth/2- Main.tileWidth*6, Main.winHeight/2-Main.tileHeight*3, Main.tileWidth);
		for(int i =0; i < 3; i++)
			components[i].draw(g);
		components[4].draw(g);
	}
	void settings(Graphics g) {
		g.drawImage(Main.menuBack,0,0,Main.winWidth, Main.winHeight, null);
		Main.builder.drawString(g, "Game Settings", Main.winWidth/2- Main.tileWidth*6, Main.winHeight/2-Main.tileHeight*5, Main.tileWidth);

		for(int i =0; i < 6; i++)
			settingsComps[i].draw(g);
		if(Main.game) {
			settingsComps[6].draw(g);
		}
		Main.builder.drawString(g, Integer.toString(vol), Main.winWidth/2- Main.tileWidth/2, Main.winHeight/2- Main.tileHeight/3, Main.tileWidth);
		Main.builder.drawString(g, Integer.toString(volumeEff), Main.winWidth/2- Main.tileWidth/2, Main.winHeight/2+ Main.tileHeight/2+ Main.tileHeight*2- Main.tileHeight/4, Main.tileWidth);
		Main.builder.drawString(g, "Music: ", Main.winWidth/2- Main.tileWidth*10, Main.winHeight/2+ Main.tileHeight/2- Main.tileHeight/3, Main.tileWidth);
		Main.builder.drawString(g, "Sound: ", Main.winWidth/2- Main.tileWidth*10, Main.winHeight/2+ Main.tileHeight/2+ Main.tileHeight/2+ Main.tileHeight*3/2, Main.tileWidth);

	}
	public void initializeTexture() {
		BufferedImage sheet = null;
		sheet = Main.bloodAnim;
		int sheetWidth = sheet.getWidth(null)/16;
		bldTexture = new BufferedImage[sheetWidth];
		for(int i = 0; i < 16; i++) {
			bldTexture[i] = sheet.getSubimage(i*430,  0, 430,sheet.getHeight());
		}
	}
	public void readjust() {
		size=Main.tileWidth*2;
		setUpComponents();
	}
	public void menuClick(int mouseX, int mouseY) {
		for(int i = 0; i < components.length; i++) {
			Button k = components[i];
			if(mouseX > k.x &&
					mouseX <= k.x + k.size*(k.s.length()+2) &&
					mouseY-Main.barLength > k.y &&
					mouseY-Main.barLength <= k.y+k.size+Main.tileWidth/4) {

				switch(i) {
				case 0:
					Main.clip.stop();
					Main.music("Sound/Songgame.wav");
					Main.menuSwitch = 1;
					Main.start = true;
					return;
				case 1:Main.menuSwitch =  4;break;
				case 2:
					System.exit(1);break;
				case 4:
				       JOptionPane.showMessageDialog(null, 
				                "drakzlin Game space background \r\n"+
				                "Glimmervoid Menu space background \r\n"+
				                "Orange turret laser fire by farfadet46\r\n"+
				                "\"Titan battlecruiser\" by kanadaj licensed CC-BY 3.0: https://opengameart.org/content/titan-battlecruiser Credit Skorpio for the kit and Kanadaj for the design either on the start-screen, menu, end-screen or credits screen.\r\n"+
				                "\"Gun Mine.\" by Xavier4321 licensed CC-BY 3.0: https://opengameart.org/content/gun-mine The original pic's are from Scorpio's Construction Kit2. Check it out here (http://opengameart.org/content/space-ship-construction-kit)\r\n" + 
				                "\"Parts2 ART Space Marine.\" by Xavier4321 licensed CC-BY 3.0: https://opengameart.org/content/parts2-art-space-marine Made with Wuditog's Parts2 App (https://www.arrall.com/part2art/). Parts from Scorpio (http://opengameart.org/content/space-ship-construction-kit) Great work. ;)\r\n" + 
				                "\"96 x 96 Space Ship\" by phobi licensed CC-BY 3.0: https://opengameart.org/content/96-x-96-space-ship \r\n"+
				                "\"Laser Shot\" by Mobeyee Sounds licensed CC-BY 4.0: https://opengameart.org/content/laser-shot-0 Link To mobeyee.com\r\n"+
				                "\"Laser Fire\" by dklon licensed CC-BY 3.0: https://opengameart.org/content/laser-fire \r\n"+
				                "\"Various Spaceship Models\" by Sypher Zent licensed CC-BY-SA 3.0: https://opengameart.org/content/various-spaceship-models Original artwork by Skorpio. Ship model design by SypherZent. \r\n"+
				                "\"Spaceship Boss\" by MattBolere licensed CC-BY-SA 4.0: https://opengameart.org/content/spaceship-boss Credit to Skorpio for the ariginal assets https://opengameart.org/users/skorpio\r\n" +
				                "\"Space Shooter Game User Interface\" by CraftPix.net 2D Game Assets licensed OGA-BY 3.0: https://opengameart.org/content/space-shooter-game-user-interface \r\n"+
				                "\"Boxy Bold Font\" by Clint Bellanger & split by cemkalyoncu",
				                "Credits",
				                JOptionPane.INFORMATION_MESSAGE);
					break;
				}

			}

		}

	}
	public void menuAnimate(int mouseX, int mouseY) {
		for(int i = 0; i < components.length; i++) {
			Button k = components[i];
			if(mouseX > k.x &&
					mouseX <= k.x + k.size*(k.s.length()+2) &&
					mouseY-Main.barLength > k.y &&
					mouseY-Main.barLength <= k.y+k.size+Main.tileWidth/4) {
				k.hold();

			}

		}
	}

	public void deathClick(int mouseX, int mouseY) {
		if(mouseX > components[3].x &&
				mouseX <= components[3].x + components[3].size*(components[3].s.length()+2) &&
				mouseY-Main.barLength > components[3].y &&
				mouseY-Main.barLength <= components[3].y+components[3].size+Main.tileWidth/4) {
			Main.resetGame();
			return;
		}
		
		
	}
	public void settingsClick(int mouseX, int mouseY) {
		for(int i = 0; i < settingsComps.length; i++) {
			Button k = settingsComps[i];
			if(mouseX > k.x &&
					mouseX <= k.x + k.size*(k.s.length()+2) &&
					mouseY-Main.barLength > k.y &&
					mouseY-Main.barLength <= k.y+k.size+Main.tileWidth/4) {
				if(Main.menuSwitch == 4)
					switch(i) {
					case 0:
						Main.toggleFullscreen = true;
						break;
					case 1:if(vol < 100)vol++;break;
					case 2:if(vol > 0)vol--;break;
					case 3:if(volumeEff < 100)volumeEff++;break;
					case 4:if(volumeEff > 0)volumeEff--;break;
					case 5:
						Main.vol =-80 + vol/100.0 * 86;
						Main.volumeEff =-80 +volumeEff/100.0 * 86;
						
						if(!Main.game) {
							Main.clip.stop();
							Main.music("Sound/Menu.wav");
							Main.menuSwitch = 3;
						}
						else{
							Main.clip.stop();
						
							if(Main.playerHealth > 50)
								Main.music("Sound/Songgame.wav");
							else
								Main.music("Sound/intense.wav");
							if(Boss.playing) {
								Main.clip.stop();
								Main.music("Sound/boss song.wav");
							}
							Main.menuSwitch = 1;
							Main.start = true;
							Main.reAdjust = 0;
							
						}
						break;
					case 6:
						if(Main.game) {
							Main.resetGame();
							Main.clip.stop();
							Main.music("Sound/Menu.wav");
							Main.game = false;
							Main.menuSwitch = 3;
						}
						break;
					}
				else if(Main.menuSwitch == 2) {
					if(i == 3) {
						Main.resetGame();
						Main.menuSwitch = 1;
						Main.start = true;
						return;
					}
				}
			}

		}
	}
	public void settingsAnimate(int mouseX, int mouseY) {
		for(int i = 0; i < settingsComps.length; i++) {
			Button k = settingsComps[i];
			if(mouseX > k.x &&
					mouseX <= k.x + k.size*(k.s.length()+2) &&
					mouseY-Main.barLength > k.y &&
					mouseY-Main.barLength <= k.y+k.size+Main.tileWidth/4) {
				k.hold();
			}

		}
	}
	public void release(int mouseX, int mouseY) {
		for(int i = 0; i < settingsComps.length; i++) {
			Button k = settingsComps[i];
			if(mouseX > k.x &&
					mouseX <= k.x + k.size*(k.s.length()+2) &&
					mouseY-Main.barLength > k.y &&
					mouseY-Main.barLength <= k.y+k.size+Main.tileWidth/4) {
				k.release();
			}

		}
		for(int i = 0; i < components.length; i++) {
			Button k = components[i];
			if(mouseX > k.x &&
					mouseX <= k.x + k.size*(k.s.length()+2) &&
					mouseY-Main.barLength > k.y &&
					mouseY-Main.barLength <= k.y+k.size+Main.tileWidth/4) {
				k.release();

			}

		}
	}
	public void gameClick(int x, int y, MouseEvent e) {
		
		for(int i =0; i < Main.towers.size(); i++) {
			if(Main.mouseX/Main.tileWidth == Main.towers.get(i).mapX && Main.mouseY/Main.tileHeight == Main.towers.get(i).mapY) {Main.selectedTower = i+1;break;}
			Main.selectedTower = 0;
		}

		if(e.getButton() == MouseEvent.BUTTON3) {Main.towerSelection = 0;}
		/////////////////////////////////////////////////////////////////////
		Main.placeTower(x, y);
	}
	public void setUpComponents() {
		components[0] = (new Button(Main.winWidth/2- Main.tileWidth*4, Main.winHeight/2- Main.tileHeight,"Start", Main.tileWidth));
		components[1] = (new Button(Main.winWidth/2- Main.tileWidth*4, Main.winHeight/2+ Main.tileHeight,"Settings", Main.tileWidth));
		components[2] = (new Button(Main.winWidth/2- Main.tileWidth*4, Main.winHeight/2+ 3*Main.tileHeight,"Quit", Main.tileWidth));
		components[3] = new Button(Main.winWidth/2-2*size, Main.winHeight/2+Main.tileHeight*2, "Restart", size/2);
		components[4] = new Button(0,0, "Credits", size/8);
		
		settingsComps[0] = (new Button(Main.winWidth/2- Main.tileWidth*6, Main.winHeight/2- Main.tileHeight*3,"Fullscreen", Main.tileWidth));
		settingsComps[1] = (new Button(Main.winWidth/2- Main.tileWidth*4, Main.winHeight/2- Main.tileHeight/2,"+", Main.tileWidth));
		settingsComps[2] = (new Button(Main.winWidth/2+ Main.tileWidth*2, Main.winHeight/2- Main.tileHeight/2,"-", Main.tileWidth));
		settingsComps[3] = (new Button(Main.winWidth/2- Main.tileWidth*4, Main.winHeight/2+ Main.tileHeight*2,"+", Main.tileWidth));
		settingsComps[4] = (new Button(Main.winWidth/2+ Main.tileWidth*2, Main.winHeight/2+ Main.tileHeight*2,"-", Main.tileWidth));
		settingsComps[5] = new Button(Main.winWidth/2-2*size+Main.tileWidth, Main.winHeight/2+Main.tileHeight*4, "Back", size/2);
		settingsComps[6] = new Button(0,0, "back to menu", size/3);

	}
}
