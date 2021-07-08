package com.felipepepe.entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.felipepepe.main.Game;
import com.felipepepe.main.Sound;
import com.felipepepe.world.Camera;
import com.felipepepe.world.World;

/* O player extende entity, então sabemos que todas as características de entity valem pro player.
 * E o super constrói o player.  */

public class Player extends Entity {

	public int life = 3;
	
	public boolean right, up, left, down;
	
	public BufferedImage[] sprites;
	public BufferedImage[] intangibleSprites; 
	
	/* Last dir guarda a última direção:
	 * 1 - Direita
	 * -1 - Esquerda
	 * 2 - Cima
	 * -2 - Baixo*/
	public int lastDir = 1;
	
	
	// Váriaveis da animação:
	private int curAnimFrames = 0;
	// maxAnimFrames vai determinar a velocidade da animação. Se for 3, por exemplo, a cada 3 frames mudará a sprite.
	private int maxAnimFrames = 3;
	private int index = 0;
	private int maxIndex = 7;
	
	
	/* PlayerIntangible: O jogador vai ficar intangível por um tempo após levar dano. */
	public boolean playerIntangible;
	private int curIntangibleFrame = 0;
	private int maxIntangibleFrames = 120;
	
	
	
	/* --------------------------- Método construtor do player: -------------------------------------- */
	
	
	
	public Player(int x, int y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);

		sprites = new BufferedImage[8];
		intangibleSprites = new BufferedImage[8];
		
		for (int i = 0; i < sprites.length; i++) {
			sprites[i] = Game.spritesheet.getSprite(0 + (16*i), 0, 16, 16);
		}
		for (int i = 0; i < sprites.length; i++) {
			intangibleSprites[i] = Game.spritesheet.getSprite(0 + (16*i), 16, 16, 16);
		}
		
	}
	
	
	
	
	/* --------------------------- Método para pegar a fruta: ------------------------------*/
	
	
	public void verifyCatchFruit() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if (current instanceof Fruit) {
				if (Entity.isColliding(this, current)) {
					Sound.pickupEffect.play();
					Game.entities.remove(i);
					Game.fruitsEaten++;
					Game.totalFruitsEaten++;
					return;
				}
			}
		}
	}
	
	
	
	/* --------------------------- Método para ficar intangível: ------------------------------*/
	
	
	public void intangibleTimeout() {
		
		curIntangibleFrame++;
		if (curIntangibleFrame > maxIntangibleFrames) {
			
			curIntangibleFrame = 0;
			
			playerIntangible = false;
		}
	}
	
	
	
	
	/* --------------------------- Método tick com a lógica do player: ------------------------------*/
	
	
	
	public void tick() {
		
		depth = 1;
		
		// Movimentação do personagem:
		if (right && World.isFree((int) (x + speed), this.getY(), this.z)) {
			x += speed;
			lastDir = 1;
		}
			
		else if (left && World.isFree((int) (x - speed), this.getY(), this.z)) {
			x -= speed;
			lastDir = -1;
		}
			
		if(up && World.isFree(this.getX(), (int) (y - speed), this.z)) {
			y -= speed;
			lastDir = 2;
		}
			
		else if (down && World.isFree(this.getX(), (int) (y + speed), this.z)) {
			y += speed;
			lastDir = -2;
		}
		
		if (playerIntangible) {
			intangibleTimeout();
		}
		
		
		verifyCatchFruit();
		
		
		// Animação do Pac-Man:
		
		curAnimFrames++;
		if (curAnimFrames == maxAnimFrames) {
			curAnimFrames = 0;
			index++;
			if (index > maxIndex) {
				index = 0;
			}
		}
		
		if (life <= 0) {
			Game.gameState = Game.GAME_STATE_GAME_OVER;
		}
		
		
		
		
	}
	
	
	
	/* --------------------------- Método render do player: ------------------------------*/

	
	public void render(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		//Se a última direção foi pra direita:
		if (lastDir == 1) {
			// Não precisa rotacionar.
		}
		//Se a última direção foi pra esquerda:
		else if (lastDir == -1) {
			g2.rotate(Math.toRadians(180), this.getX() + 8, this.getY() + 8);		
		}
		//Se a última direção foi pra cima:
		else if (lastDir == 2) {
			g2.rotate(Math.toRadians(270), this.getX() + 8, this.getY() + 8);
		}
		//Se a última direção foi pra baixo:
		else if (lastDir == -2) {
			g2.rotate(Math.toRadians(90), this.getX() + 8, this.getY() + 8);
		}
		
		if (playerIntangible == true)
			g2.drawImage(intangibleSprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		else
			g2.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
	}

}
