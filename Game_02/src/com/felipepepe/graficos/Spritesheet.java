package com.felipepepe.graficos;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Spritesheet {
	
	private BufferedImage spritesheet;
	
	public Spritesheet(String path) {	
	
		try {
			spritesheet = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getSprite(int x, int y, int width, int height) {
		// Pega a sprite determinada pelas coordenas x e y e pelo tamanho width e height:
		return spritesheet.getSubimage(x, y, width, height);
	}

}
