package com.felipepepe.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.felipepepe.world.World;

public class Menu {

	
	public boolean previous, next, enter;
	

	public String[] menuOptions = {"jogar","ranking","sair"};
	public String[] pauseOptions = {"resumir","sair"};
	public String[] gameOverOptions = {"recomeçar","sair"};
	
	public int curOption;
	public int maxOption;
	
	
	
	
	/*------------------------------- Método tick com a lógica dos menus ----------------------------*/
	
	
	public void tick() {
		
		
		if (previous == true) {
			previous = false;
			curOption--;
			if (curOption < 0)
				curOption = maxOption;
		}
		
		if (next == true) {
			next = false;
			curOption++;
			if (curOption > maxOption)
				curOption = 0;
		}
		
		
			
		// --- MENU ---:
		
		if (Game.gameState == Game.GAME_STATE_MENU) {
			
			maxOption = menuOptions.length -1;
			
			if (enter == true) {
				enter = false;
				
				if (menuOptions[curOption] == "jogar") {
					Game.gameState = Game.GAME_STATE_NORMAL;
				}
				if (menuOptions[curOption] == "ranking") {
					// Mostrar ranking
				}
				if (menuOptions[curOption] == "sair") {
					System.exit(1);
				}
			}
		}
		
		// --- PAUSE ---:
		
		else if (Game.gameState == Game.GAME_STATE_PAUSED) {
			
			maxOption = pauseOptions.length -1;
			
			if (enter == true) {
				enter = false;
				if (pauseOptions[curOption] == "resumir") {
					Game.gameState = Game.GAME_STATE_NORMAL;
				}
				if (pauseOptions[curOption] == "sair") {
					System.exit(1);
				}
			}
		}
		
		// --- GAME OVER ---:
		
		else if (Game.gameState == Game.GAME_STATE_GAME_OVER) {
				
			maxOption = gameOverOptions.length -1;
			
			if (enter == true) {
				enter = false;
				if (gameOverOptions[curOption] == "recomeçar") {
					Game.totalFruitsEaten = 0;
					Game.CUR_LEVEL = 1;
					World.restartGame("level1.png");
					Game.gameState = Game.GAME_STATE_NORMAL;
				}
				
				if (gameOverOptions[curOption] == "sair") {
					System.exit(1);
				}
			}
		}
			
	}
	
	
	
	/*---------------------------- Método render: ----------------------------------------------*/
	
	
	
	public void render(Graphics g) {
		
		
		/* ---- MENU ---- */
		
		if (Game.gameState == Game.GAME_STATE_MENU) {
			
			g.setColor(Color.black);
			g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
			
			g.setColor(new Color(0xFFF000));
			g.setFont(Game.titleFont.deriveFont(90f));
			g.drawString("PAC-MAN", Game.WIDTH / 2 - 40, 240);
			
			/* Jogar: */
			
			if (menuOptions[curOption] == "jogar") {
				g.setColor(new Color(0xFFF466));
				g.setFont(Game.gameFont.deriveFont(75f));
				g.drawString("Jogar", 284, 370);
			}
			else {
				g.setColor(Color.white);
				g.setFont(Game.gameFont.deriveFont(60f));
				g.drawString("Jogar", 300, 370);
			}
			
			/* Ranking: */
			
			if (menuOptions[curOption] == "ranking") {
				g.setColor(Color.gray);
				g.setFont(Game.gameFont.deriveFont(75f));
				g.drawString("Ranking", 244, 490);
			}
			else {
				g.setColor(Color.gray);
				g.setFont(Game.gameFont.deriveFont(60f));
				g.drawString("Ranking", 270, 490);
			}
			
			/* Sair: */
			
			if (menuOptions[curOption] == "sair") {
				g.setColor(new Color(0xFFF466));
				g.setFont(Game.gameFont.deriveFont(75f));
				g.drawString("Sair", 304, 610);
			}
			else {
				g.setColor(Color.white);
				g.setFont(Game.gameFont.deriveFont(60f));
				g.drawString("Sair", 320, 610);
			}
		
		}
		
		/* ---- PAUSE ---- */
		
		else if (Game.gameState == Game.GAME_STATE_PAUSED) {
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0, 0, 0, 230));
			g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
			
			g.setColor(Color.white);
			g.setFont(Game.gameFont.deriveFont(90f));
			g.drawString("PAUSADO", 160, 260);
			
			/* Resumir: */
			
			if (pauseOptions[curOption] == "resumir") {
				g.setColor(new Color(0xFFF466));
				g.setFont(Game.gameFont.deriveFont(75f));
				g.drawString("Resumir", 240, 420);
			}
			else {
				g.setColor(Color.white);
				g.setFont(Game.gameFont.deriveFont(60f));
				g.drawString("Resumir", 260, 420);
			}
			
			/* Sair: */
			
			if (pauseOptions[curOption] == "sair") {
				g.setColor(new Color(0xFFF466));
				g.setFont(Game.gameFont.deriveFont(75f));
				g.drawString("Sair", 300, 550);
			}
			else {
				g.setColor(Color.white);
				g.setFont(Game.gameFont.deriveFont(60f));
				g.drawString("Sair", 310, 550);
			}
		
		}
		
		/* ---- GAME OVER ---- */
		
		else if (Game.gameState == Game.GAME_STATE_GAME_OVER) {
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0, 0, 0, 230));
			g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
			
			g.setColor(Color.white);
			g.setFont(Game.gameFont.deriveFont(90f));
			g.drawString("GAME OVER", 130, 230);
			
			g.setFont(Game.gameFont.deriveFont(30f));
			g.drawString("PONTUAÇÃO:", 280, 320);
			
			g.setColor(new Color(0xFFF000));
			g.setFont(Game.gameFont.deriveFont(200f));
			g.drawString(Integer.toString(Game.totalFruitsEaten), Game.WIDTH*Game.SCALE / 2 - 120, 510);
			
			
			/* Recomeçar: */
			
			if (gameOverOptions[curOption] == "recomeçar") {
				g.setColor(new Color(0xFFF466));
				g.setFont(Game.gameFont.deriveFont(60f));
				g.drawString("Recomeçar", 120, 650);
			}
			else {
				g.setColor(Color.white);
				g.setFont(Game.gameFont.deriveFont(50f));
				g.drawString("Recomeçar", 150, 650);
			}
			
			/* Sair: */
			
			if (gameOverOptions[curOption] == "sair") {
				g.setColor(new Color(0xFFF466));
				g.setFont(Game.gameFont.deriveFont(60f));
				g.drawString("Sair", 480, 650);
			}
			else {
				g.setColor(Color.white);
				g.setFont(Game.gameFont.deriveFont(50f));
				g.drawString("Sair", 500, 650);
			}
		
		}
		
	}
	
}
