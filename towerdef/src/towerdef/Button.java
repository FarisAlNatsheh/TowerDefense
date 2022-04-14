package towerdef;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Button extends Component{
	private int x,y,size;
	private String s;
	private BufferedImage texture;
	
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
		g.drawImage(texture,getX(),getY(),getSize()*(getS().length()+2),getSize()+Main.tileWidth/2,null);
		Main.builder.drawString(g, getS(), getX()+getSize(), getY()+Main.tileWidth/4, getSize());
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
	public int getSize() {
		return size;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public String getS() {
		return s;
	}
}
