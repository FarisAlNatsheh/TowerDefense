package towerdef;

import java.awt.Graphics;

public class Button extends Component{
	int x,y,size;
	String s;
	
	public Button(int x, int y, String s, int size) {
		this.x = x;
		this.y = y;
		this.s = s;
		this.size = size;
	}
	
	public void draw(Graphics g){
		g.drawImage(Main.buttonImg,x,y,size*(s.length()+2),size+Main.tileWidth/2,null);
		Main.builder.drawString(g, s, x+size, y+Main.tileWidth/4, size);
	}
	
}
