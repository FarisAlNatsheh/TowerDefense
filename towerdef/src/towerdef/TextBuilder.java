package towerdef;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TextBuilder {
	BufferedImage[][] textures;
	BufferedImage[] letters = new BufferedImage[26];
	BufferedImage[] numbers = new BufferedImage[10];

	public TextBuilder() {
		BufferedImage sheet = null;
		try {
			sheet = ImageIO.read(new File("font2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int scale = 16;
		int sheetHeight = sheet.getHeight(null)/16;
		int sheetWidth = sheet.getWidth(null)/18;
		textures = new BufferedImage[sheetWidth][sheetHeight];
		for(int i = 0; i < sheetWidth; i++) {
			for(int i2 = 0; i2 < sheetHeight; i2++) {
				textures[i][i2] = sheet.getSubimage(i * 18,  i2 * 16, 18,16 );
			}
		}
		setFont2();

	}
	public void setFont() {
		//letters[0]= textures[3][2];

		//Setting numbers
		int count = 0;
		for(int j = 2; j < 4; j++) {
			if(j == 2) {
				for(int i = 3; i < 15; i++) {
					letters[count] = textures[i][j];
					count++;
				}
			}
			else if(j == 3){
				for(int i = 0; i < 15; i++) {
					if(i == 14)
						break;
					letters[count] = textures[i][j];
					count++;

				}
			}
		}
		//Setting numbers
		for(int i = 1; i < 11; i++) {
			numbers[i-1] = textures[i][1];
		}

	}
	public void setFont2() {
		for(int i = 0; i < 7; i++) 
			letters[i] = textures[i][3];
		letters[7] = textures[8][3];
		for(int i = 0; i < 9; i++) 
			letters[i+8] = textures[i][4];
		for(int i = 1; i < 9; i++) 
			letters[i+16] = textures[i][5];
		numbers[0] = textures[8][0];
		for(int i = 0; i < 3; i++) 
			numbers[i+1] = textures[i][1];
		for(int i = 4; i < 9; i++) 
			numbers[i] = textures[i][1];
		numbers[9] = textures[0][2];
		
	}
	public void drawString(Graphics g, String s, int x, int y, int size) {
		char n = 0;
		for(int i =0;i < s.length();i++) {
			n = s.charAt(i);
			if(Character.isAlphabetic(n))
				g.drawImage(letters[Character.toLowerCase(n)-'a'],x+ i*(size),y,size,size,null);
			else {
				
				if(n == ' ')
					continue;
				if(n == ':')
					g.drawImage(textures[1][2],x+ i*(size),y,size,size,null);
				if(n == '+')
					g.drawImage(textures[3][0],x+ i*(size),y,size,size,null);
				if(n == '-')
					g.drawImage(textures[5][0],x+ i*(size),y,size,size,null);

				if(Character.isDigit(n))
					g.drawImage(numbers[Character.toLowerCase(s.charAt(i))-'0'],x+ i*(size),y,size,size,null);
			}
		}

	}
}
