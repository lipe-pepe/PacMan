package com.felipepepe.world;

public class Vector2i {

	// Essa classe é pra guardar posições para o funcionamento do algoritmo A*.
	
	public int x,y;
	
	
	
	/*-------- Método construtor: --------*/
	
	
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	
	/*--------- Método para comparar duas posições: --------*/
	
	
	public boolean equals(Object object) {
		Vector2i vec = (Vector2i) object;
		if (vec.x == this.x && vec.y == this.y) {
			return true;
		}
		return false;
	}
	
	
	
	
}
