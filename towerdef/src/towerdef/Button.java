package towerdef;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Button extends Component{
	int x,y,size;
	String s;
	BufferedImage texture;
	public Button(int x, int y, String s, int size) {
		this.x = x;
		this.y = y;
		this.s = s;
		this.size = size;
		initializeTexture();
	}
	public void initializeTexture() {
		try {
			texture = ImageIO.read(new File("Textures/button.png"));
	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g){
		g.drawImage(texture,x,y,size*(s.length()+2),size+Main.tileWidth/2,null);
		Main.builder.drawString(g, s, x+size, y+Main.tileWidth/4, size);
	}
	public void hold() {
			try {
			texture = ImageIO.read(new File("Textures/buttonP.png"));
	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void release() {
			try {
			texture = ImageIO.read(new File("Textures/button.png"));
	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
