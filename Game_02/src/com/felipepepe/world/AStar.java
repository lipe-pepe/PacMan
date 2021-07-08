package com.felipepepe.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AStar {

	public static double lastTime = System.currentTimeMillis();
	private static Comparator<Node> nodeSorter = new Comparator<Node>() {
		
		@Override
		public int compare(Node n0, Node n1) {
			if (n1.fCost < n0.fCost) {
				return + 1;
			}
			if (n1.fCost > n0.fCost) {
				return - 1;
			}
			return 0;
		}
	};
	
	
	public static boolean clear() {
		if (System.currentTimeMillis() - lastTime >= 1000) {
			return true;
		}
		return false;
	}
	
	
	
	
	/* ----------------- Método principal do algoritmo: -------------------------*/
	
	
	public static List<Node> findPath(World world, Vector2i start, Vector2i end) {
		
		lastTime = System.currentTimeMillis();
		
		// openlist: posições possiveis que o inimigo pode percorrer para chegar ao jogador.
		// closedlist: posições já verificadas e descartadas.
		List<Node> openList = new ArrayList<Node>();
		List<Node> closedList = new ArrayList<Node>();
		
		Node current = new Node(start, null, 0, getDistance(start, end));
		
		openList.add(current);
		
		while(openList.size() > 0) {
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			
			if (current.tile.equals(end)) {
				//Chegamos no ponto final!
				//Basta retornar o valor.
				List<Node> path = new ArrayList<Node>();
				while (current.parent != null) {
					path.add(current);
					current = current.parent;
				}
				openList.clear();
				closedList.clear();
				return path;				
			}
			
			// Se não deu certo no if, o caminho não dá certo. Podemos tirá-lo da lista aberta e botar na fechada.
			openList.remove(current);
			closedList.add(current);
			
			/* Esse for confere todas as posições em volta do jogador, incluindo a dele mesmo, pra ver se estão
			 * livres. A posição do jogador é a 4 e por isso, pulamos ela com o continue.
			 */
			for (int i = 0; i < 9; i++) {
				if (i == 4) {
					continue;
				}
				int x = current.tile.x;
				int y = current.tile.y;
				
				int xi = (i%3) - 1;
				int yi = (i/3) - 1;
				
				Tile tile = World.tiles[x + xi + ((y + yi)*World.WIDTH)];
				
				if (tile == null) {
					continue;
				}
				if (tile instanceof WallTile) {
					continue;
				}
				// Os ifs a seguir conferem os tiles das diagonais.
				if (i == 0) {
					Tile test = World.tiles[x+xi+1+((y+yi)*World.WIDTH)]; 
					Tile test2 = World.tiles[x+xi+((y+yi+1)*World.WIDTH)]; 
					if (test instanceof WallTile || test2 instanceof WallTile) {
						continue;
					}
				}
				else if (i == 2) {
					Tile test = World.tiles[x+xi-1+((y+yi)*World.WIDTH)]; 
					Tile test2 = World.tiles[x+xi+((y+yi+1)*World.WIDTH)]; 
					if (test instanceof WallTile || test2 instanceof WallTile) {
						continue;
					}
				}
				else if (i == 6) {
					Tile test = World.tiles[x+xi+1+((y+yi)*World.WIDTH)]; 
					Tile test2 = World.tiles[x+xi+((y+yi)*World.WIDTH)]; 
					if (test instanceof WallTile || test2 instanceof WallTile) {
						continue;
					}
				}
				else if (i == 8) {
					Tile test = World.tiles[x+xi+((y+yi-1)*World.WIDTH)]; 
					Tile test2 = World.tiles[x+xi-1+((y+yi)*World.WIDTH)]; 
					if (test instanceof WallTile || test2 instanceof WallTile) {
						continue;
					}
				}
				
				Vector2i a = new Vector2i(x + xi, y + yi);
				// gCost é o custo de quanto vai demorar pra ir de um tile pro outro.
				double gCost = current.gCost + getDistance(current.tile, a);
				double hCost = getDistance(a, end);
				
				Node node = new Node(a, current, gCost, hCost);
				
				/*Próximo if: verifica se esse node já foi adicionado à closed list e estamos vendo se o custo desse
				* node é maior ou igual ao atual. */
				if (vecInList(closedList, a) && gCost >= current.gCost) {
					continue;
				}
				
				if (!vecInList(openList, a)) {
					openList.add(node);
				}
				else if (gCost < current.gCost) {
					openList.remove(current);
					openList.add(node);
				}
				
				
			}
		}
		
		closedList.clear();
		return null;
		
	}
	
	
	
	/*----------------- Método para verificar se o vetor está na lista ----------------------------*/
	
	
	private static boolean vecInList(List<Node> list, Vector2i vector) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).tile.equals(vector)) {
				return true;
			}
		}
		return false;
	}
	
	
	
	
	/*----------------- Método para pegar a distancia entre dois pontos: --------------------------*/
	
	private static double getDistance(Vector2i tile, Vector2i goal) {
		double dx = tile.x - goal.x;
		double dy = tile.y - goal.y;
		
		return Math.sqrt((dx * dx) + (dy*dy));
	}
}
