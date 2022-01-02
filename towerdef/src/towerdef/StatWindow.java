package towerdef;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class StatWindow extends Component{
	int x,y;
	int width = Main.tileWidth*5;
	int height = Main.tileHeight*4;
	BufferedImage texture;
	Button sell, upgrade, hitCount;
	boolean visible = false;
	int selectedTower;
	public void initializeTextures() {
		try {
			texture = ImageIO.read(new File("gradient.png"));
			//texture = texture.getSubimage(0, 0, 384, 570);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void adjust() {
		width = Main.tileWidth*5;
		height = Main.tileHeight*4;
		sell = new Button((x-1)*Main.tileWidth+Main.tileWidth/2,(y+3)*Main.tileHeight,"Sell", Main.tileWidth/3);
		upgrade = new Button((x-1)*Main.tileWidth,(y+1)*Main.tileHeight,"Upgrade", Main.tileWidth/3);

	}
	public StatWindow(int x, int y) {
		this.x = x;
		this.y = y;
		initializeTextures();
		sell = new Button((x-1)*Main.tileWidth+Main.tileWidth/2,(y+3)*Main.tileHeight,"Sell", Main.tileWidth/3);
		upgrade = new Button((x-1)*Main.tileWidth,(y+1)*Main.tileHeight,"Upgrade", Main.tileWidth/3);

	}
	public void draw(Graphics g) {
		g.setColor(new Color(20,20,70));
		g.fillRect(x*Main.tileWidth, y*Main.tileHeight, width, height);
		g.drawImage(texture,x*Main.tileWidth, y*Main.tileHeight, width, height, null);
		upgrade.draw(g);
		sell.draw(g);
		Main.builder.drawString(g, "Price: "+ Main.towers.get(selectedTower-1).price/2, (x+1)*Main.tileWidth,(y+2)*Main.tileHeight,Main.tileWidth/5);
		Main.builder.drawString(g, "Damage: "+ Main.towers.get(selectedTower-1).hitCount, (x+1)*Main.tileWidth,(y+2)*Main.tileHeight+Main.tileHeight/2,Main.tileWidth/5);

	}
	public void click(int mouseX, int mouseY, int selectedTower) {
		if(selectedTower > 0) {
			visible = true;
			this.selectedTower = selectedTower;
		}
		else
			visible = false;
		if(mouseX > sell.x &&
				mouseX <= sell.x + sell.size*(sell.s.length()+2) &&
				mouseY-Main.barLength > sell.y &&
				mouseY-Main.barLength <= sell.y+sell.size+Main.tileWidth/4) {
			Main.playerMoney += Main.towers.get(this.selectedTower-1).price/2;
			Main.towers.remove(this.selectedTower-1);
			Main.placedTowers.remove(this.selectedTower-1);
		}
		if(mouseX > upgrade.x &&
				mouseX <= upgrade.x + upgrade.size*(upgrade.s.length()+2) &&
				mouseY-Main.barLength > upgrade.y &&
				mouseY-Main.barLength <= upgrade.y+upgrade.size+Main.tileWidth/4 && Main.playerMoney >= 25) {
			if(Main.towers.get(this.selectedTower-1).speed > 1)
				Main.towers.get(this.selectedTower-1).speed--;
			Main.towers.get(this.selectedTower-1).range++;
			Main.playerMoney-=	Main.towers.get(this.selectedTower-1).price/2;
			Main.towers.get(this.selectedTower-1).price+= Main.towers.get(this.selectedTower-1).price;
		}

	}
	public void animate(int mouseX, int mouseY) {
		if(mouseX > sell.x &&
				mouseX <= sell.x + sell.size*(sell.s.length()+2) &&
				mouseY-Main.barLength > sell.y &&
				mouseY-Main.barLength <= sell.y+sell.size+Main.tileWidth/4) {
			sell.hold();
		
		}
		if(mouseX > upgrade.x &&
				mouseX <= upgrade.x + upgrade.size*(upgrade.s.length()+2) &&
				mouseY-Main.barLength > upgrade.y &&
				mouseY-Main.barLength <= upgrade.y+upgrade.size+Main.tileWidth/4 && Main.playerMoney >= 25) {
			upgrade.hold();
		
		}
	}
	public void release(int mouseX, int mouseY) {
		if(mouseX > sell.x &&
				mouseX <= sell.x + sell.size*(sell.s.length()+2) &&
				mouseY-Main.barLength > sell.y &&
				mouseY-Main.barLength <= sell.y+sell.size+Main.tileWidth/4) {
			sell.release();
		
		}
		if(mouseX > upgrade.x &&
				mouseX <= upgrade.x + upgrade.size*(upgrade.s.length()+2) &&
				mouseY-Main.barLength > upgrade.y &&
				mouseY-Main.barLength <= upgrade.y+upgrade.size+Main.tileWidth/4 && Main.playerMoney >= 25) {
			upgrade.release();
		
		}
	}

}
