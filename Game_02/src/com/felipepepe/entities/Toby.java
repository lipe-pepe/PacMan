package com.felipepepe.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.felipepepe.main.Game;
import com.felipepepe.world.Camera;

public class Toby extends Enemy{

	public BufferedImage[] ghostModeSprites;
	
	public Toby(int x, int y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		
		ghostModeSprites = new BufferedImage[2];
		
		ghostModeSprites[0] = Game.spritesheet.getSprite(16, 64, 16, 16);
		ghostModeSprites[1] = Game.spritesheet.getSprite(32, 64, 16, 16);
		
	}
	

	
	/* ----------------------------- Método render para renderizar o inimigo: --------------------------------*/
	
	
	public void render(Graphics g) {
		if (ghostMode == false) {
			g.drawImage(Entity.TOBY_SPRITE, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		else {
			g.drawImage(ghostModeSprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		
	}

}
