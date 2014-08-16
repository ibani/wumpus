package Wumpus;

public class Etat {
	public int x,
				y;
	boolean existFleche,
			ramasseOr;
	
	public Etat(int x, int y, boolean existFleche, boolean ramasseOr)
	{
		this.x = x;
		this.y= y;
		this.existFleche= existFleche;
		this.ramasseOr = ramasseOr;
		
	}
	
	public Etat()
	{
	}
	
	public boolean Equalite(Etat etat)
	{
		if(this.x == etat.x && this.y == etat.y && this.existFleche == etat.existFleche && this.ramasseOr == etat.ramasseOr)
			return true;
		else
			return false;
	}
	
	public void  AfficheEtat()
	{
			System.out.println("Etat(" + x +", " + y + ", " + existFleche + ", " + ramasseOr + ")");
	}		
}


