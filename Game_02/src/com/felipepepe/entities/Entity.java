package com.felipepepe.entities;

//import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.felipepepe.main.Game;
import com.felipepepe.world.Camera;
import com.felipepepe.world.Node;
import com.felipepepe.world.Vector2i;
import com.felipepepe.world.World;


public class Entity {
	
	public static BufferedImage APPLE_SPRITE = Game.spritesheet.getSprite(0, 112, 16, 16);
	public static BufferedImage ORANGE_SPRITE = Game.spritesheet.getSprite(16, 112, 16, 16);
	public static BufferedImage GRAPES_SPRITE = Game.spritesheet.getSprite(32, 112, 16, 16);
	public static BufferedImage[] fruits_Sprites = {APPLE_SPRITE, ORANGE_SPRITE, GRAPES_SPRITE};
	
	public static BufferedImage TOBY_SPRITE = Game.spritesheet.getSprite(0, 64, 16, 16);
	public static BufferedImage KEVIN_SPRITE = Game.spritesheet.getSprite(0, 80, 16, 16);
	public static BufferedImage ROXY_SPRITE = Game.spritesheet.getSprite(0, 96, 16, 16);
	
	// Colocamos x, y, width e height como protected para que as classes filhas possam acessá-los.
	protected double x;
	protected double y;
	protected int z;
	protected int width;
	protected int height;
	public double speed;
	
	protected List<Node> path;
	
	private BufferedImage sprite;
	
	public static int right_dir = 0, left_dir = 1; 
	public static int dir = right_dir;
	
	public static boolean moved = false;
	
	public int depth;
	
	public static Random rand = new Random();
	
	
	
	
	/*--------------------------------- Método construtor: ------------------------------------*/
	
	
	
	public Entity(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		//Estamos setando os valores da classe com base nos argumentos passados quando construída.
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
	}
	
	
	
	
	/*Os métodos a seguir são setters e getters. Eles pegam os valores necessários a partir de 
	 * um método. Getters e setters são usados em desenvolvimento de games. Seria mais fácil deixar x, y, 
	 * width e height públicos. Mas é melhor fazer de uma maneira protegida e profissional. */
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	
	public int getX() {
		return (int) this.x;
	}
	
	public int getY() {
		return (int) this.y;
	}
	
	public int getZ() {
		return (int) this.z;
	}
	
	public int getWidth() {
		return (this.width);
	}
	
	public int getHeight() {
		return (this.height);
	}
	
	
	
	/*----------------------------------------------------------------------------------------------------*/
	
	

	public void updateCamera() {
		/* Ao posicionar a câmera fazemos um clamp para impedí-la de mostrar o que está
		 * fora do mapa. Nesse caso, o player vai se movimentar sem a câmera.
		 * Multiplicamos o World.WIDTH e World.HEIGHT por 16 porque o World ta em formato de tiles,
		 * e assim conseguimos o valor em pixels. */
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
	}
	
	
	
	/*----------------------------------------------------------------------------------------------------*/
	
	
	//Esse método vai ordenar as entities pra renderizar na ordem certa.
	
	public static Comparator<Entity> nodeSorter = new Comparator<Entity>() {
		
		@Override
		public int compare(Entity e0, Entity e1) {
			if (e1.depth < e0.depth) {
				return +1;
			}
			if (e1.depth > e0.depth) {
				return  -1;
			}
			return 0;
		}
	};	
	
	
	
	/*--------------------------------------------------------------------------------------------------*/
	
	
	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX(), e1.getY(), e1.getWidth(), e1.getHeight());
		Rectangle e2Mask = new Rectangle(e2.getX(), e2.getY(), e2.getWidth(), e2.getHeight());
		
		if (e1Mask.intersects(e2Mask) && (e1.z == e2.z)) {
			return true;
		}
		return false;
	}
	
	
	/*-----------------------------------------------------------------------------------------------*/
	
	
	public double calculateDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	
	
	/*-------------------- Método implementando o algoritmo A* -------------------------------------*/
	
	
	
	public void followPath(List<Node> path) {
		
		if (path != null) {
			
			if (path.size() > 0) {
				Vector2i target = path.get(path.size() - 1).tile;
				//xprev = x;
				//yprev = y;
				moved = true;
				if (x < target.x * 16) {
					x ++;
					dir = right_dir;
				} 
				else if (x > target.x*16) {
					x --;
					dir = left_dir;
				}
				
				if (y < target.y*16) {
					y ++;
				}
				else if (y > target.y*16) {
					y --;
				}
				
				if (x == target.x * 16 && y == target.y * 16) {
					path.remove(path.size() - 1);
					moved = false;
				}
			}
		}
	}

	
	
	/* ---------------------------Método tick com a lógica de entidade: ------------------------------*/
	
	
	public void tick() {
		
	}
	
	
	
	
	
	/* ---------------------------- Método para renderizar a entidade: --------------------------*/
	
	public void render(Graphics g) {
		//Colocamos -Camera.x e -Camera-y para colocar o offset da câmera.
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
		
		//DEBUG DA MASK:
		//g.setColor(Color.blue);
		//g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, mwidth, mheight);
	}
}
