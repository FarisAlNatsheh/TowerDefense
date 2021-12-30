package towerdef;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class Menu {

	BufferedImage[] bldTexture;
	public int anim = 0;
	public static int size=Main.tileWidth*2;
	static Button[] components = new Button[4];
	static Button[] settingsComps = new Button[7];
	int vol = 50, volumeEff = 50;
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
		g2.setColor(new Color(255,0,0,70));
		g2.fillRect(0, 0, Main.winWidth, Main.winHeight);
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
		Main.builder.drawString(g, "Game Settings", Main.winWidth/2- Main.tileWidth*6, Main.winHeight/2-Main.tileHeight*3, Main.tileWidth);

		for(int i =0; i < 6; i++)
			settingsComps[i].draw(g);
		if(Main.game) {
			settingsComps[6].draw(g);
		}
		Main.builder.drawString(g, Integer.toString(vol), Main.winWidth/2- Main.tileWidth, Main.winHeight/2+ Main.tileHeight/2, Main.tileWidth);
		Main.builder.drawString(g, Integer.toString(volumeEff), Main.winWidth/2- Main.tileWidth, Main.winHeight/2+ Main.tileHeight/2+ Main.tileHeight*2, Main.tileWidth);
		Main.builder.drawString(g, "Music: ", Main.winWidth/2- Main.tileWidth*10, Main.winHeight/2+ Main.tileHeight/2, Main.tileWidth);
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
	
	public void setUpComponents() {
		components[0] = (new Button(Main.winWidth/2- Main.tileWidth*4, Main.winHeight/2- Main.tileHeight,"Start", Main.tileWidth));
		components[1] = (new Button(Main.winWidth/2- Main.tileWidth*4, Main.winHeight/2+ Main.tileHeight/2,"Settings", Main.tileWidth));
		components[2] = (new Button(Main.winWidth/2- Main.tileWidth*4, Main.winHeight/2+ 2* Main.tileHeight,"Quit", Main.tileWidth));
		components[3] = new Button(Main.winWidth/2-2*size, Main.winHeight/2+Main.tileHeight*2, "Restart", size/2);
		
		settingsComps[0] = (new Button(Main.winWidth/2- Main.tileWidth*6, Main.winHeight/2- Main.tileHeight,"Fullscreen", Main.tileWidth));
		settingsComps[1] = (new Button(Main.winWidth/2- Main.tileWidth*4, Main.winHeight/2+ Main.tileHeight/2,"+", Main.tileWidth));
		settingsComps[2] = (new Button(Main.winWidth/2+ Main.tileWidth*2, Main.winHeight/2+ Main.tileHeight/2,"-", Main.tileWidth));
		settingsComps[3] = (new Button(Main.winWidth/2- Main.tileWidth*4, Main.winHeight/2+ Main.tileHeight/2+ Main.tileHeight*2,"+", Main.tileWidth));
		settingsComps[4] = (new Button(Main.winWidth/2+ Main.tileWidth*2, Main.winHeight/2+ Main.tileHeight/2+ Main.tileHeight*2,"-", Main.tileWidth));
		settingsComps[5] = new Button(Main.winWidth/2-2*size+Main.tileWidth, Main.winHeight/2+Main.tileHeight*4, "Back", size/2);
		settingsComps[6] = new Button(0,0, "back to menu", size/3);

	}
}
