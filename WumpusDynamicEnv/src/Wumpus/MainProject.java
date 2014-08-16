package Wumpus;

public class MainProject
{
	public static  void main(String[] args)
	{	
		int reussite = 0;
		int tailleMonde = 4;
		int nbIteration = 10000;
		double alpha    = 0.1;
		double gamma    = 0.9;
		int rayonPerception = 2;
		Wumpus wumpus = new Wumpus(tailleMonde, nbIteration, alpha, gamma, rayonPerception);
		
		wumpus.Learn();
		for(int j = 0; j < 1000; j++)
		{
			if(wumpus.Test())
				reussite += 1;
		}
		System.out.println("Pourcentage réussite : " + reussite/10);
	}
}
