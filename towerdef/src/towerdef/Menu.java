package towerdef;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Menu {

	BufferedImage[] bldTexture;
	public int anim = 0;
	public Menu() {
		initializeTexture();
	}
	void death(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if(Main.tick % 10 == 0)
			anim++;
		if(anim == 16)
			anim = 0;
		g2.setColor(new Color(255,0,0,70));
		g2.fillRect(0, 0, Main.winWidth, Main.winHeight);
		g2.drawImage(bldTexture[anim], 0, 0, Main.winWidth, Main.winHeight,null);
		Main.builder.drawString(g2, "YOU DIED", Main.winWidth/2-400, Main.winHeight/2-50, 100);
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
}
