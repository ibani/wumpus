package Wumpus;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Object;

public class Wumpus
{	
	int tailleMonde;
	int nbIteration;
	double alpha;
	double gamma;
	int rayon;

	WumpusWorld  world;

	Etat initialState;
	Etat etatActuel;
	Etat etatPrecedent;
	private  int action = 0;
	Perceptions perceptions;
	
	HashMap<Etat, double[][]> etatAction;
	
		
	Wumpus(int taille, int iteration, double a, double g, int perception)
	{
		tailleMonde = taille;
		nbIteration = iteration;
		alpha = a;
		gamma =	g;
		rayon = perception;
		etatAction = new HashMap<Etat, double[][]> ();
	}

	public long Learn()
	{
		boolean exist;
		long start = System.currentTimeMillis();
		
		world = new WumpusWorld(tailleMonde);
		perceptions = new Perceptions(tailleMonde, rayon);
		perceptions.UpdatePerceptions(tailleMonde - 1, 0, world.map[tailleMonde - 1][0]);
		etatActuel = new Etat(tailleMonde - 1, 0, true, false, 2*rayon+1); etatActuel.perceptions = perceptions.GetPerception(tailleMonde - 1, 0);
		etatAction.put(etatActuel, GenerateActionTab(etatActuel));
			
		for (int i = 0; i < nbIteration; i++)
		{
			System.out.print(i + "/" + nbIteration + '\r');
			// On choisit une action
			for(Etat mapKey : etatAction.keySet())
			{
				if(mapKey.Equalite(etatActuel) == true)
				{
					ArrayList maxIndex = IndexMax(etatAction.get(mapKey));
					Object  s =  maxIndex.get((int)(Math.random()*maxIndex.size()));
					action = Integer.parseInt(s.toString());				
				}
			}
			
			// On effectue l'action choisit et on change d'etat
			etatPrecedent = etatActuel;
			etatActuel = doAction(etatPrecedent, action);
			exist = false;
			for(Etat mapKey : etatAction.keySet())
			{
				if(mapKey.Equalite(etatActuel) == true)
					exist = true;			
			}
			if(exist == false)			
				etatAction.put(etatActuel, GenerateActionTab(etatActuel));
			// On applique l'algorithme de Q-Learning
			QLearning(alpha, gamma, etatPrecedent, etatActuel, action, etatAction);
			
			//si l'agent meure ou si il a gagne on le remet dans l'etat initial
			if(IsDead(etatActuel) || Win(etatActuel))
			{
				world = new WumpusWorld(tailleMonde);
				perceptions = new Perceptions(tailleMonde, rayon);
				perceptions.UpdatePerceptions(tailleMonde - 1, 0, world.map[tailleMonde - 1][0]);
				etatActuel = new Etat(tailleMonde - 1, 0, true, false, 2*rayon+1); etatActuel.perceptions = perceptions.GetPerception(tailleMonde - 1, 0);
				exist = false;
				for(Etat mapKey : etatAction.keySet())
				{
					if(mapKey.Equalite(etatActuel) == true)
						exist = true;			
				}
				if(exist == false)			
					etatAction.put(etatActuel, GenerateActionTab(etatActuel));
			}
		}
		return (System.currentTimeMillis() - start) / 1000;
	}
	
		public boolean Test()
		{
			boolean exist;
			world = new WumpusWorld(tailleMonde);
			perceptions = new Perceptions(tailleMonde, rayon);
			perceptions.UpdatePerceptions(tailleMonde - 1, 0, world.map[tailleMonde - 1][0]);
			etatActuel = new Etat(tailleMonde - 1, 0, true, false, 2*rayon+1); etatActuel.perceptions = perceptions.GetPerception(tailleMonde - 1, 0);
			int maxIter = 0;
			while(!Win(etatActuel))
			{
				exist = false;
				for(Etat mapKey : etatAction.keySet())
				{
					if(mapKey.Equalite(etatActuel) == true)
					{
						ArrayList maxIndex = IndexMax(etatAction.get(mapKey));
						//System.out.print(maxIndex + " ");
						action = Integer.parseInt((String) maxIndex.get((int)(Math.random()*maxIndex.size())).toString());
						//printAction(action);
						exist = true;
					}
				}
				if(exist == false)
				{
					double [][] etatAction;
					etatAction = GenerateActionTab(etatActuel);
					ArrayList maxIndex = IndexMax(etatAction);
					action = Integer.parseInt((String) maxIndex.get((int)(Math.random()*maxIndex.size())).toString());
				}
				etatPrecedent = etatActuel;
				etatActuel = doAction(etatPrecedent, action);
				if(IsDead(etatActuel) == true)
				{
					return false;
				}
				if(Win(etatActuel))
				{
					return true;
				}
				maxIter += 1;
				if(maxIter > 10000)
					return false;
			}
			return false;
	}
	
public  void QLearning(double alpha, double gamma, Etat precedent, Etat courant, int action, HashMap<Etat, double[][]> etatAction)
{
	double oldValue = 0;
	double R;
	double maxRecompense = 0;
	
	
	for(Etat mapKey : etatAction.keySet())
	{
		if(mapKey.Equalite(precedent) == true)
		{
			oldValue = etatAction.get(mapKey)[action][0];
		}
	}
	R = Recompense(courant);
	for(Etat mapKey : etatAction.keySet())
	{
		if(mapKey.Equalite(courant) == true)
		{
			maxRecompense = MaxArray(etatAction.get(mapKey));
		}
	}
	
	for(Etat mapKey : etatAction.keySet())
	{
		if(mapKey.Equalite(precedent) == true)
		{
			etatAction.get(mapKey)[action][0] = oldValue + alpha * (R + gamma * maxRecompense - oldValue);
		}
	}
}

public  boolean IsDead(Etat courant)
{
	Location locAgent = new Location(courant.x, courant.y);
	ArrayList<Location> pits = world.getPitLocation();

	if(world.liveWumpus == true)
	{	
		if(locAgent.x == world.getWumpusLocation().x && locAgent.y == world.getWumpusLocation().y)
		{
			//System.out.print("Mort ");
			return true;
		}
	}

	for(int i = 0; i < pits.size(); i++)
	{
		if(locAgent.x == pits.get(i).x && locAgent.y == pits.get(i).y)
		{
			//System.out.print("Mort ");
			return true;
		}
	}
	
	
	return false;
}

public  boolean Win(Etat courant)
{
	if(courant.x == tailleMonde-1 && courant.y == 0 && courant.ramasseOr == true)
	{
		return true;
	}
	else
		return false;
}

public  double Recompense(Etat courant)
{
	if(IsDead(courant) == true)
		return -100000;
	else if(Win(courant) == true)
		return 1000;
	else
		return 0;

}

public  double[][] GenerateActionTab(Etat etat)
{
	double[][] tabAction = new double[9][2];
	
	for(int i=0; i < 9; i++)
	{
		tabAction[i][0] = 0;
		tabAction[i][1] = 1;
	}
	
	if(etat.x <= 0)
	{
		tabAction[0][1] = 0;
		tabAction[4][1] = 0;
	}
	if(etat.x >= world.gridSize - 1)
	{
		tabAction[2][1] = 0;
		tabAction[6][1] = 0;
	}
	if(etat.y <= 0)
	{
		tabAction[3][1] = 0;
		tabAction[7][1] = 0;
	}
	if(etat.y >= world.gridSize - 1)
	{
		tabAction[1][1] = 0;
		tabAction[5][1] = 0;
	}
	if(etat.existFleche == false)
	{
		tabAction[4][1] = 0;
		tabAction[5][1] = 0;
		tabAction[6][1] = 0;
		tabAction[7][1] = 0;
	}
	if(etat.ramasseOr == true || !(etat.x == world.getGoldLocation().x && etat.y == world.getGoldLocation().y))
	{
		tabAction[8][1] = 0;
	}
	if(etat.ramasseOr == false && etat.x == world.getGoldLocation().x && etat.y == world.getGoldLocation().y)
	{
		tabAction[0][1] = 0;
		tabAction[1][1] = 0;
		tabAction[2][1] = 0;
		tabAction[3][1] = 0;
		tabAction[4][1] = 0;
		tabAction[5][1] = 0;
		tabAction[6][1] = 0;
		tabAction[7][1] = 0;
	}
	
	return tabAction;
}
	
	public  Etat doAction(Etat etatPrecedent, int action)
	{
		Etat etatSuivant;
		boolean [][] percept;

		etatSuivant = new Etat(etatPrecedent.x, etatPrecedent.y, etatPrecedent.existFleche, etatPrecedent.ramasseOr, 2*rayon+1);
		switch(action)
		{
		case 0 :
			etatSuivant.x = etatSuivant.x - 1;
			perceptions.UpdatePerceptions(etatSuivant.x, etatSuivant.y, world.map[etatSuivant.x][etatSuivant.y]);
			etatSuivant.perceptions = perceptions.GetPerception(etatSuivant.x, etatSuivant.y);
			break;
		case 1 :
			etatSuivant.y = etatSuivant.y + 1;
			perceptions.UpdatePerceptions(etatSuivant.x, etatSuivant.y, world.map[etatSuivant.x][etatSuivant.y]);
			etatSuivant.perceptions = perceptions.GetPerception(etatSuivant.x, etatSuivant.y);	
			break;
		case 2 :
			etatSuivant.x = etatSuivant.x + 1;
			perceptions.UpdatePerceptions(etatSuivant.x, etatSuivant.y, world.map[etatSuivant.x][etatSuivant.y]);
			etatSuivant.perceptions = perceptions.GetPerception(etatSuivant.x, etatSuivant.y);
			break;
		case 3 :
			etatSuivant.y = etatSuivant.y - 1;
			perceptions.UpdatePerceptions(etatSuivant.x, etatSuivant.y, world.map[etatSuivant.x][etatSuivant.y]);
			etatSuivant.perceptions = perceptions.GetPerception(etatSuivant.x, etatSuivant.y);	
			break;
		case 4 :
			etatSuivant.existFleche = false;
			if(etatSuivant.y==world.getWumpusLocation().y &&  (etatSuivant.x-1 ) == world.getWumpusLocation().x)
				world.KillWumpus();
			break;
		case 5 :
			etatSuivant.existFleche = false;
			if(etatSuivant.x==world.getWumpusLocation().x &&  (etatSuivant.y + 1) == world.getWumpusLocation().y)
				world.KillWumpus();
			break;
		case 6 :
			etatSuivant.existFleche = false;
			if(etatSuivant.y==world.getWumpusLocation().y &&  (etatSuivant.x + 1) == world.getWumpusLocation().x)
				world.KillWumpus();
			break;
		case 7 :
			etatSuivant.existFleche = false;
			if(etatSuivant.x==world.getWumpusLocation().x &&  (etatSuivant.y - 1) == world.getWumpusLocation().y)
				world.KillWumpus();
			break;
		case 8 :
			if(etatSuivant.x==world.getGoldLocation().x && etatSuivant.y==world.getGoldLocation().y)
				etatSuivant.ramasseOr = true;
			break;
		}
		
		return etatSuivant;
	}

	
	public  ArrayList IndexMax(double [][] tab)
	{
		int max;
		int i;
		ArrayList tabMax = new ArrayList();
		
		max = 0;
		for(i = 0; i < tab.length; i++)
		{
			if(tab[i][1] != 0)
			{
				max = i;
			}
		}
		for(i = 0; i < tab.length; i++)
		{
			if(tab[i][1] != 0 && tab[max][0] < tab[i][0])
			{
				max = i;
			}
		}
		for(i = 0; i < tab.length; i++)
		{
			if(tab[i][1] != 0 && tab[max][0] == tab[i][0])
			{
				tabMax.add(i);
			}
		}
		
		return tabMax;	
	}
	
	public double MaxArray(double [][] tab)
	{
		double max = tab[0][0];
		for(int i=0; i < tab.length; i++)
		{
			if(tab[i][0] > max && tab[i][1] != 0)
				max = tab[i][0];
		}
		return max;
	}
	
	public void printAction(int action)
	{
		switch(action)
		{
			case 0 : System.out.println("En Haut"); break;
			case 1 : System.out.println("A Droite"); break;
			case 2 : System.out.println("En Bas"); break;
			case 3 : System.out.println("A Gauche"); break;
			case 4 : System.out.println("Fleche  Haut"); break;
			case 5 : System.out.println("Fleche  Droite"); break;
			case 6 : System.out.println("Fleche  Bas"); break;
			case 7 : System.out.println("Fleche  Gauche"); break;
			case 8 : System.out.println("Rammse Or"); break;	
		}
	}
}
