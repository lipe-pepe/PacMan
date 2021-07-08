package com.felipepepe.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.felipepepe.main.Game;

public class Tile {
	
	// Botamos os tiles estáticos para serem inicializados apenas uma vez.
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 128, 16, 16);
	public static BufferedImage TILE_BLUE_WALL = Game.spritesheet.getSprite(16, 128, 16, 16);
	public static BufferedImage TILE_PURPLE_WALL = Game.spritesheet.getSprite(32, 128, 16, 16);
	public static BufferedImage TILE_GREEN_WALL = Game.spritesheet.getSprite(48, 128, 16, 16);
	
	private BufferedImage sprite;
	private int x,y;
	
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	/* Método para renderizar o tile: */
	
	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}
	
	
	

}
