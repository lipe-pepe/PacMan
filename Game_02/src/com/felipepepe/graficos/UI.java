package com.felipepepe.graficos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.felipepepe.main.Game;


public class UI {
	
	public BufferedImage fruitsIcon = Game.spritesheet.getSprite(0, 144, 16, 16);
	public BufferedImage heartIcon = Game.spritesheet.getSprite(16, 144, 16, 16);
	
	private String levelString;
	
	public static final int UI_SCALE = 4;
	
	public void render(Graphics g) {
		
		
		/* --- Contador de frutas: --- */
		
		g.drawImage(fruitsIcon, 15, 730, 16*UI_SCALE, 16*UI_SCALE, null);
		g.setColor(Color.white);
		g.setFont(Game.gameFont.deriveFont(55f));
		g.drawString("x " + Game.fruitsEaten + "/" + Game.fruitsAmount, 90, 785);
		
		
		/* --- Nível: --- */
		
		g.setFont(Game.gameFont.deriveFont(20f));
		g.drawString("LEVEL", 345, 745);
		g.setFont(Game.gameFont.deriveFont(60f));
		
		if (Game.CUR_LEVEL < 10) 
			levelString = "0" + Game.CUR_LEVEL;
		else
			levelString = Integer.toString(Game.CUR_LEVEL);
		if (Game.runthrough == 2)
			levelString += "+";
		else if (Game.runthrough == 3) {
			levelString += "++";
		}
		g.drawString(levelString, 337, 794);
		
		
		/* --- Vida: --- */
		
		for (int i = 0; i < Game.player.life; i++) {
			g.drawImage(heartIcon, 645 - (i*75), 730, 16*UI_SCALE, 16*UI_SCALE, null);
		}
		
	}
	
	
}
