package com.felipepepe.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.felipepepe.entities.Enemy;
import com.felipepepe.entities.Entity;
import com.felipepepe.entities.Fruit;
import com.felipepepe.entities.Kevin;
import com.felipepepe.entities.Player;
import com.felipepepe.entities.Roxy;
import com.felipepepe.entities.Toby;
import com.felipepepe.main.Game;
import com.felipepepe.main.Sound;

public class World {

	// Esse array é melhor ser simples ao invés de multidimensional por uma questão de performance:
	public static Tile[] tiles;
	
	public static int WIDTH;
	public static int HEIGHT; 
	
	public static final int TILE_SIZE = 16;
	
	
	
	
	public World(String path) {
		
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth()*map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth()*map.getHeight()];
			
			// Esse é um método próprio para 
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			
			for (int xx = 0; xx < map.getWidth(); xx++) {
				
				for (int yy = 0; yy < map.getHeight(); yy++) {
					
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					
					//Sempre por padrão o tile é o chão:
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					
					if (pixelAtual == 0xFF000000) {
						// Chão
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					}
					else if (pixelAtual == 0xFFFFFFFF) {
						// Parede
						if (Game.runthrough == 1)
							tiles[xx + (yy * WIDTH)] = new WallTile(xx*16, yy*16, Tile.TILE_BLUE_WALL);
						else if (Game.runthrough == 2)
							tiles[xx + (yy * WIDTH)] = new WallTile(xx*16, yy*16, Tile.TILE_PURPLE_WALL);
						else if (Game.runthrough == 3)
							tiles[xx + (yy * WIDTH)] = new WallTile(xx*16, yy*16, Tile.TILE_GREEN_WALL);
					}
					else if (pixelAtual == 0xFF0026FF) {
						// Player
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					}
					else if (pixelAtual == 0xFFFFD800) {
						// Fruta
						Fruit fruit = new Fruit(xx*16, yy*16, 16, 16, 0, Entity.fruits_Sprites[Entity.rand.nextInt(3)]);
						Game.entities.add(fruit);
						Game.fruitsAmount++;
					}
					else if (pixelAtual == 0xFF00FFFF) {
						// Fantasma 1 - Toby
						Enemy enemy = new Toby(xx*16, yy*16, 16, 16, 1, Entity.TOBY_SPRITE);
						Game.entities.add(enemy);		
					}
					else if (pixelAtual == 0xFF4CFF00) {
						// Fantasma 2 - Kevin
						Enemy enemy = new Kevin(xx*16, yy*16, 16, 16, 1, Entity.KEVIN_SPRITE);
						Game.entities.add(enemy);
					}
					else if (pixelAtual == 0xFFB200FF) {
						// Fantasma 3 - Roxy
							Enemy enemy = new Roxy(xx*16, yy*16, 16, 16, 1, Entity.ROXY_SPRITE);
							Game.entities.add(enemy);
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	/*------------------------ Método pra restartar o jogo: -----------------------------------*/
	
	
	
	
	public static void restartGame(String level) {
		Sound.nextLevelEffect.play();
		Game.entities.clear();
		Game.player = new Player(0, 0, 16, 16, 2, Game.spritesheet.getSprite(0, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.fruitsEaten = 0;
		Game.fruitsAmount = 0;
		Game.world = new World("/" + level);
		
		return;
	}
	
	
	public static boolean isFree(int xnext, int ynext, int zplayer) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + TILE_SIZE - 1) / TILE_SIZE;
		
		int x4 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (ynext + TILE_SIZE - 1) / TILE_SIZE;
		
		/* Esse tile do vetor tiles é uma instancia de parede (WallTile)?
		 * Se sim, retorna falso. IsFree = false (tá livre = falso)		 */
		if (!((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile))) {
			return true;
		}
		
		if (zplayer > 0) {
			return true;
		}
		return false;
	}
	
	
	
	/* --------------------------------- Método para renderizar o mundo: -----------------------------------*/
	
	
	
	public void render(Graphics g) {
		
		/* Variáveis para otimização do jogo:
		 * O mundo só vai carregar o que estiver sendo visto.
		 * Xstart é o primeiro tile que é renderizado. Ele está na posição da câmera dividida
		 * pelo tamanho da tile. 
		 * Fazer >> 4 é a mesma coisa que dividir por 16, mas bem mais rápido*/
		
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for (int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				
				/* Uma hora a câmera vai ficar negativa, para centralizar o jogador. Mas não tem
				 * indice negativo e isso vai dar um erro. Resolvemos isso com a seguinte verificação: */
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					//Continua e não renderiza nada:
					continue;
				}
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
				
			}
		}
	}	
	
	
}
