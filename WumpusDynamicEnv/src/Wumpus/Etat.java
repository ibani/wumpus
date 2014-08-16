package Wumpus;

import java.util.Arrays;

public class Etat {
	public int x,
				y;
	public boolean existFleche,
			ramasseOr;
	public int[][] perceptions;
	
	public Etat(int x, int y, boolean existFleche, boolean ramasseOr, int tailleperception)
	{
		this.x = x;
		this.y= y;
		this.existFleche= existFleche;
		this.ramasseOr = ramasseOr;
		
	}
	
	public boolean Equalite(Etat etat)
	{
		if(this.x == etat.x && this.y == etat.y && this.existFleche == etat.existFleche && this.ramasseOr == etat.ramasseOr)
		{
			return Arrays.deepEquals(this.perceptions, etat.perceptions);					
		}
		else
		{
			return false;
		}
	}
}


