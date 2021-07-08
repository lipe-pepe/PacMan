package com.felipepepe.world;

public class Vector2i {

	// Essa classe � pra guardar posi��es para o funcionamento do algoritmo A*.
	
	public int x,y;
	
	
	
	/*-------- M�todo construtor: --------*/
	
	
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	
	/*--------- M�todo para comparar duas posi��es: --------*/
	
	
	public boolean equals(Object object) {
		Vector2i vec = (Vector2i) object;
		if (vec.x == this.x && vec.y == this.y) {
			return true;
		}
		return false;
	}
	
	
	
	
}
