package Wumpus;

public class Perceptions
{
	int fenetre;
	int [][] perception;
	
	public Perceptions(int taille, int fenetre)
	{
		this.fenetre = fenetre;
		this.perception = new int[taille+2*fenetre][taille+2*fenetre];
		for(int i = 0; i < taille+2*fenetre; i++)
		{
			for(int j = 0; j < taille+2*fenetre; j++)
			{
				perception[i][j] = 0;
			}
		}
	}
	
	public void UpdatePerceptions(int Xmonde, int Ymonde, String percept)
	{
		perception[Xmonde+fenetre][Ymonde+fenetre] = NombreOcurence(percept, 'B')+NombreOcurence(percept, 'O');
	}
	
	public int[][] GetPerception(int Xmonde, int Ymonde)
	{
		int [][] ret;
		ret = new int[2*fenetre+1][2*fenetre+1];
		for(int i=0; i<2*fenetre+1; i++)
		{
			for(int j=0; j <2*fenetre+1; j++)
			{
				ret[i][j]=perception[Xmonde+i][Ymonde+j];
			}
		}
		return ret;
	}
	
	public int NombreOcurence(String str, char c)
	{
		int cnt = 0;
		for(int i=0; i < str.length(); i++)
		{
			if(str.charAt(i) == c)
				cnt += 1;
		}
		return cnt;
	}
	
}
