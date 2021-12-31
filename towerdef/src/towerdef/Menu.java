package towerdef;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;



public class Menu {

	BufferedImage[] bldTexture;
	public int anim = 0;
	public static int size=Main.tileWidth*2;
	Button[] components = new Button[4];
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
					Main.music("Songgame.wav");
					Main.menuSwitch = 1; 
					return;
				case 1:Main.menuSwitch =  4;break;
				case 2:
					System.exit(1);break;
				}

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
							Main.music("Menu.wav");
							Main.menuSwitch = 3;
						}
						else{
							Main.clip.stop();
							if(Main.playerHealth > 50)
								Main.music("Songgame.wav");
							else
								Main.music("intense.wav");
							Main.menuSwitch = 1;
						}
						break;
					case 6:
						if(Main.game) {
							Main.resetGame();
							Main.clip.stop();
							Main.music("Menu.wav");
							Main.game = false;
							Main.menuSwitch = 3;
						}
						break;
					}
				else if(Main.menuSwitch == 2) {
					if(i == 3) {
						Main.resetGame();
						Main.menuSwitch = 1;
						return;
					}
				}
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

		settingsComps[0] = (new Button(Main.winWidth/2- Main.tileWidth*6, Main.winHeight/2- Main.tileHeight*3,"Fullscreen", Main.tileWidth));
		settingsComps[1] = (new Button(Main.winWidth/2- Main.tileWidth*4, Main.winHeight/2- Main.tileHeight/2,"+", Main.tileWidth));
		settingsComps[2] = (new Button(Main.winWidth/2+ Main.tileWidth*2, Main.winHeight/2- Main.tileHeight/2,"-", Main.tileWidth));
		settingsComps[3] = (new Button(Main.winWidth/2- Main.tileWidth*4, Main.winHeight/2+ Main.tileHeight*2,"+", Main.tileWidth));
		settingsComps[4] = (new Button(Main.winWidth/2+ Main.tileWidth*2, Main.winHeight/2+ Main.tileHeight*2,"-", Main.tileWidth));
		settingsComps[5] = new Button(Main.winWidth/2-2*size+Main.tileWidth, Main.winHeight/2+Main.tileHeight*4, "Back", size/2);
		settingsComps[6] = new Button(0,0, "back to menu", size/3);

	}
}
