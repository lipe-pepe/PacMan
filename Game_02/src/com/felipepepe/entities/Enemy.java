package com.felipepepe.entities;

import java.awt.image.BufferedImage;
import java.util.Random;

import com.felipepepe.main.Game;
import com.felipepepe.main.Sound;
import com.felipepepe.world.AStar;
import com.felipepepe.world.Vector2i;
//import com.felipepepe.world.Camera;


public class Enemy extends Entity{

	
	public boolean ghostMode = false;
	public int ghostFrames = 0;
	//Para restringir o valor que queremos no rand, contamos a diferença do mínimo e depois adicionamos o mínimo:
	//Aqui, ele gera um valor entre 3 e 5 segundos.
	public int nextTime = Entity.rand.nextInt(60*5 - 60*3) + 60*3;
	
	
	// Váriaveis de animção:
	private int curAnimFrame = 0;
	private int maxAnimFrames = 30;
	protected int index = 0;
	private int maxIndex = 1;
	
	
	/* ----------------------------------- Método construtor: -------------------------------------------*/

	
	
	public Enemy(int x, int y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		
	}
	
	
	
	
	/* ------------------------------ Método tick com a lógica do enemy: -------------------------------*/
	
	
	
	
	public void tick() {
			
		depth = 0;
		
		if (ghostMode == false) {
		
			// Algoritmo A* do enemy:
				
			if (path == null || path.size() == 0) {
				
				Vector2i start = new Vector2i((int) (x/16), (int) (y/16));
				Vector2i end = new Vector2i((int) (Game.player.x/16), (int) (Game.player.y/16));
				
				path = AStar.findPath(Game.world, start, end);
			}
			
			if (new Random().nextInt(100) < 90) {
				followPath(path);
			}
			if (new Random().nextInt(100) < 5) {
				Vector2i start = new Vector2i((int) (x/16), (int) (y/16));
				Vector2i end = new Vector2i((int) (Game.player.x/16), (int) (Game.player.y/16));
				
				path = AStar.findPath(Game.world, start, end);
			}
		}
		
		ghostFrames++;
		// A cada x segundos, o ghost mode ativa/desativa.
		if (ghostFrames == nextTime) {
			
			nextTime = Entity.rand.nextInt(60*7 - 60*3) + 60*3;
			ghostFrames = 0;
			
			ghostMode = !ghostMode;
			
		}
		
		/* Animação do inimigo:
		 * Vai ser feita dentro de classes separadas Toby, Kevin e Roxy (os nomes dos fantasmas)
		 *  que utilizam as variáveis da classe pai Enemy */
		if (ghostMode == true) {
			curAnimFrame++;
			if (curAnimFrame == maxAnimFrames) {
				curAnimFrame = 0;
				index++;
				if (index > maxIndex) {
					index = 0;
				}
			}
			
		}
		
		/* Dano ao jogador: 
		 * Se o inimigo estiver em modo fantasma, não levamos dano.*/		
		if (Entity.isColliding(this, Game.player)) {
			
			if (Game.player.playerIntangible == false) {
				
				if (this.ghostMode == false) {
					Sound.hurtEffect.play();
					Game.player.life--;
					Game.player.playerIntangible = true;
				}
			}
				
		}		
		
	}
		
	
}
