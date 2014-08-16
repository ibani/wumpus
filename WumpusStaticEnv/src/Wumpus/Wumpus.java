package Wumpus;

import java.util.ArrayList;
import java.util.HashMap;

public class Wumpus {
	
	private  int gridSize=5,
				 action = 0,
	 			 nbIteration = 100000;
	private  double alpha = 0.2,
					gamma = 0.9;
	ArrayList<String> planAction;
	ArrayList<Integer> planActionInt;
	boolean exist;
	WumpusWorld  world;//= new WumpusWorld(gridSize);
		
		
	Etat initialState;  //   à changer (4,0) position du depart de l'agent
	Etat etatActuel;
	Etat etatPrecedent;
		
	HashMap<Etat, double[][]> etatAction;
	
		
	Wumpus(WumpusWorld  map)
	{
		world = map;
		gridSize = world.gridSize;
		
		initialState = new Etat(gridSize - 1, 0, true, false);
		etatAction = new HashMap<Etat, double[][]> ();
	}
	
	public void Learn(){
	//world.affiche();
		System.out.println("Or : " + world.getGoldLocation());
		
		world.affiche();
		etatAction = new HashMap<Etat, double[][]> ();
		etatAction.put(initialState, GenerateActionTab(initialState));
		System.out.println("Debut de la phase d'apprentissage");
		etatActuel = initialState;
		for (int i = 0; i < nbIteration; i++)
		{
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
				etatActuel = initialState;
				world.liveWumpus = true; // On oublie pas de remettre le wumpus
			}
		}
		System.out.println("Fin de la phase d'apprentissage");
		System.out.println(" Nombre d etats visites : " + etatAction.size());
		
		}
	
		public void Test()
		{
		planAction = new ArrayList<String>();
		planActionInt = new ArrayList<Integer>();
			
		System.out.println("Debut de la phase de test");
		etatActuel = initialState;
		world.liveWumpus = true; // On oublie pas de remettre le wumpus
		int maxIter = 0;
		System.out.print("Etat(" + etatActuel.x +", " + etatActuel.y + ", " + etatActuel.existFleche + ", " + etatActuel.ramasseOr + ") action : ");
		while(!Win(etatActuel))
		{
			for(Etat mapKey : etatAction.keySet())
			{
				if(mapKey.Equalite(etatActuel) == true)
				{
					ArrayList maxIndex = IndexMax(etatAction.get(mapKey));
					action = Integer.parseInt((String) maxIndex.get((int)(Math.random()*maxIndex.size())).toString());
					System.out.println(maxIndex + "->" + action);
					printAction(action);
				}
			}
			etatPrecedent = etatActuel;
			etatActuel = doAction(etatPrecedent, action);
			System.out.print("Etat(" + etatActuel.x +", " + etatActuel.y + ", " + etatActuel.existFleche + ", " + etatActuel.ramasseOr + ") action : ");
			if(IsDead(etatActuel) == true)
			{
				System.out.println("Mort");
				break;
			}
			if(Win(etatActuel))
			{
				System.out.println("Gagne");
			}
			maxIter += 1;
			if(maxIter > 100)
				break;
		}
		System.out.println("Fin de la phase de test");
		for(int i = 0; i<planAction.size();i++)
			System.out.println(planAction.get(i));
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
	if(courant.x == gridSize-1 && courant.y == 0 && courant.ramasseOr == true)
	{
		//System.out.print("Gagne ");
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
		return 1;
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
	
	return tabAction;
}
	
	public  Etat doAction(Etat etatPrecedent, int action)
	{
		Etat etatSuivant;

		etatSuivant = new Etat(etatPrecedent.x, etatPrecedent.y, etatPrecedent.existFleche, etatPrecedent.ramasseOr);
		switch(action)
		{
		case 0 :
			etatSuivant.x = etatSuivant.x - 1;
			break;
		case 1 :
			etatSuivant.y = etatSuivant.y + 1;
			break;
		case 2 :
			etatSuivant.x = etatSuivant.x + 1;
			break;
		case 3 :
			etatSuivant.y = etatSuivant.y - 1;
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
			case 0 : System.out.println("En Haut")  ;planAction.add("up"); 			break;
			case 1 : System.out.println("A Droite")  ;planAction.add("rigth");		break;
			case 2 : System.out.println("En Bas")  ;planAction.add("down"); 		break;
			case 3 : System.out.println("A Gauche")  ;planAction.add("left"); 		break;
			case 4 : System.out.println("Fleche  Haut");planAction.add("Aup");		break;
			case 5 : System.out.println("Fleche  Droite");planAction.add("Arigth");	break;
			case 6 : System.out.println("Fleche  Bas")  ;planAction.add("Adown"); 	break;
			case 7 : System.out.println("Fleche  Gauche");planAction.add("Aleft");	break;
			case 8 : System.out.println("Rammse Or");	planAction.add("take"); 	break;	
		}
		planActionInt.add(action); 	
	}
}
