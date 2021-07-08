package com.felipepepe.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.felipepepe.entities.Enemy;
import com.felipepepe.entities.Entity;
import com.felipepepe.entities.Player;
import com.felipepepe.graficos.Spritesheet;
import com.felipepepe.graficos.UI;
import com.felipepepe.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {


	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static final int HEIGHT_OFFSET = 30;
	public static final int HEIGHT = 240 + HEIGHT_OFFSET;
	public static final int SCALE = 3;
	
	private BufferedImage image;
	public static Player player;
	
	public static List<Entity> entities;
	
	// O spritesheet é estático para conseguirmos ter um acesso mais fácil a ele. 
	public static Spritesheet spritesheet;
	public static World world;
	
	public UI ui;
	
	public static int fruitsEaten = 0;
	public static int fruitsAmount = 0;
	public static int totalFruitsEaten = 0;
	
	
	// Váriaveis dos níveis:
	public static int CUR_LEVEL = 1, MAX_LEVEL = 12;
	// Game replay é para recomeçar os níveis com uma nova configuração: muda a cor da parede e os inimigos ficam mais rápidos.
	public static double runthrough = 1;
	public static int maxRunthroughs = 3;
	
	// Pra importar fontes:
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("04B_30__.TTF");
	public static Font titleFont;
	public InputStream stream2 = ClassLoader.getSystemClassLoader().getResourceAsStream("Pixellari.ttf");
	public static Font gameFont;
	
	// Constantes de game states, usadas para facilitar a comparação.
	public static final int GAME_STATE_MENU = 0;
	public static final int GAME_STATE_NORMAL = 1;
	public static final int GAME_STATE_PAUSED = 2;
	public static final int GAME_STATE_GAME_OVER = 3;
	
	public static int gameState = 0;
	
	public Menu menu;


	
	
	
	/* ------------------------------------ Método construtor: -----------------------------------*/
	
	
	
	public Game() {	

		Sound.music2.loop();
		
		// Adicionamos ouvidores de eventos e definimos que eles estão nessa classe (this):
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		// Fullscreen meio ruim:
		//setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();	
		//Image é onde renderizamos os graficos em cima:
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		
		/* Inicializando objetos:
		 * É importante inicializar os objetos na ordem certa! */
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, 2, spritesheet.getSprite(0, 0, 16, 16));
		entities = new ArrayList<Entity>();
		world = new World("/level1.png");
		ui = new UI();
		
		entities.add(player);
		
		// Fontes:
		try {
			titleFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(50f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		try {
			gameFont = Font.createFont(Font.TRUETYPE_FONT, stream2).deriveFont(50f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		menu = new Menu();
	
	}
	
	
	
	/* ------------------------------ Método para iniciar frame -----------------------------*/
	
	
	public void initFrame() {
		frame = new JFrame("Pac-Man");
		frame.add(this);
		//frame.setUndecorated(true);
		frame.setResizable(false);
		frame.pack();
		
		Image icone = null;
		try {
			icone = ImageIO.read(getClass().getResource("/icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.setIconImage(icone);
		
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	
	
	/*------------------------------------------------------------------------------------------*/
	
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		isRunning = true;
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	/*----------------------------------- MAIN ----------------------------------------*/
	
	
	public static void main(String[] args) {
		
		Game game = new Game();
		game.start();
	}

	
	
	/* ------------------------------- Método tick com a lógica do jogo: -----------------------------------*/
	
	
	
	public void tick() {
			
		if (gameState == GAME_STATE_NORMAL) {
			// Em cada tick do jogo(frame), dar o tick em todas as entidades:
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}		
		}
		else {
			menu.tick();
		}
		
		// Passando de nível, após coletar todas as frutas:
		if (Game.fruitsEaten == Game.fruitsAmount) {
			CUR_LEVEL++;
			if (CUR_LEVEL > MAX_LEVEL) {
				runthrough++;
				CUR_LEVEL = 1;
			}
			
			String newWorld = "level" + CUR_LEVEL + ".png";
			World.restartGame(newWorld);		
		}
		
		if (runthrough > maxRunthroughs) {
			runthrough = 1;
		}
	}
	
	
	
	/* ------------------------- Método render para renderizar o jogo: ----------------------------*/
	
	
	public void render() {
		//BufferStrategy é uma sequência de buffers que colocamos na tela para otimizar a renderização.
		BufferStrategy bs = this.getBufferStrategy();
		
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = image.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		/* Renderização do jogo */
		//Graphics2D g2 = (Graphics2D) g;
		
		world.render(g);
		
		//Manipulando a profundidade de cada entity:
		Collections.sort(entities, Entity.nodeSorter);
		
		// Em cada render do jogo(frame), dar o render de todas as entidades:
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		
		// Aqui renderizamos o jogo:
		
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		ui.render(g);

		if (gameState != GAME_STATE_NORMAL) {
			menu.render(g);
		}
		
		bs.show();
		
	}
	
	
	
	
	/*----------------------------------------------------------------------------------------------*/
	
	
	
	public void run() {
		
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		// Cálculo que fazemos pra pegar o momento certo de fazer o update do jogo:
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		
		requestFocus();
		
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			// Se tiver passado 1 segundo após a última vez que mostrou a mensagem:
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}
		
		stop();
		
	}

	
	
	
	/*----------------------------------------------------------------------------------------------*/
	
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		
	/* ------- Movimentação do Player -----------*/
		
		
		// Se a minha tecla da direita estiver pressionada.
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
			
			if (gameState == GAME_STATE_GAME_OVER)
				Sound.selectEffect.play();
				menu.previous = true;
		}
		
		// Se a minha tecla da esquerda estiver pressionada.
		else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;

			
			/* Navegação no menu: */
			if (gameState == GAME_STATE_GAME_OVER)
				Sound.selectEffect.play();
				menu.next = true;
		}
		
		/* O próximo if é um if ao invés de else if porque senão não poderíamos andar pra cima e
		 * pro lado ao mesmo tempo. Ficaria restrito a uma direção.	 */
		
		// Se a minha tecla de cima estiver pressionada.
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;

			
			/* Navegação no menu: */
			if (gameState == GAME_STATE_MENU || gameState == GAME_STATE_PAUSED)
				Sound.selectEffect.play();
				menu.previous = true;
			
		}
		// Se a minha tecla de baixo estiver pressionada.
		else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			
			/* Navegação no menu: */
			if (gameState == GAME_STATE_MENU || gameState == GAME_STATE_PAUSED)
				Sound.selectEffect.play();
				menu.next = true;
		}
		
		
		/* Navegação no menu: */
		
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			
			if (gameState != GAME_STATE_NORMAL)
				menu.enter = true;
		}
		
		
		/* Pausar: */
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			
			if (gameState == GAME_STATE_NORMAL)
				gameState = GAME_STATE_PAUSED;
		}
		
	
	}
	
		
	

	
	
	
	
	@Override
	public void keyReleased(KeyEvent e) {

	
	/* ------- Movimentação do Player -----------*/
		
	
		// Se a minha tecla da direita for solta.
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}
		// Se a minha tecla da esquerda for solta.
		else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		
		/* O próximo if é um if ao invés de else if porque senão não poderíamos andar pra cima e
		 * pro lado ao mesmo tempo. Ficaria restrito a uma direção.	 */
		
		// Se a minha tecla de cima for solta.
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		}
		// Se a minha tecla de baixo for solta.
		else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
	}

	
	
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

}

