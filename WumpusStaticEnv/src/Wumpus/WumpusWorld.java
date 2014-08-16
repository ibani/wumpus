package Wumpus;

import java.util.*;

public class WumpusWorld {
	
	public int gridSize;
	public static String[][] map;
	int nbPit= 2;
	public boolean liveWumpus = true;
	public ArrayList <Location> occuped = new ArrayList<Location>(); 	

	
	public WumpusWorld(int gridSize){
		this.gridSize=gridSize;	
		 
		map = new String[gridSize][gridSize];
		for(int i =0; i<gridSize;i++)
			for(int j =0; j<gridSize;j++)
				map[i][j]="";
			
		GenerateAgent();
		GenerateWumpus();
		liveWumpus = true;
		for(int i=0; i< nbPit; i++)
			GeneratePit();		
		
		GenerateGold();
				
	}
	private void GenerateAgent(){
		 occuped.add(new Location(gridSize-1, 0));		
		 map[gridSize-1][0] = "A";
		// interdir les cases adjacentes à la position de l'agent au départ; 
		occuped.add(new Location(gridSize-2, 0));
		occuped.add(new Location(gridSize-2, 1));
		occuped.add(new Location(gridSize-1, 1));
		}
	
	private void GeneratePit(){
		
		 int xPit = (int) (Math.random() * (gridSize));         // entre [0 et size grid -1]
		 int yPit = (int) (Math.random() * (gridSize));
		 
		 while(exist(occuped, new Location(xPit, yPit))){
			 xPit = (int) (Math.random() * (gridSize));         
			 yPit = (int) (Math.random() * (gridSize));			 
		 }
		 map[xPit][yPit] += "P";
		 occuped.add(new Location(xPit, yPit));
		 
		
		 if(xPit == gridSize-1 ) 		map[xPit-1][yPit] += "B";
		 else if(xPit == 0 ) 			map[xPit+1][yPit] +="B";
		 else if(xPit>0 && xPit < gridSize-1){
			 map[xPit-1][yPit] += "B";
			 map[xPit+1][yPit] += "B";			 
		 }
		 if(yPit == gridSize-1 ) 		map[xPit][yPit-1] += "B";	 
		 else if(yPit == 0 ) 			map[xPit][yPit+1] += "B";
		 else if (yPit>0 && yPit <gridSize-1){
			 map[xPit][yPit-1] +="B";
			 map[xPit][yPit+1] += "B";
		 }	 	
	}	
	
	private void GenerateWumpus(){	
		int xWumpus = (int) (Math.random() * (gridSize));
		int yWumpus = (int) (Math.random() * (gridSize));
		while(exist(occuped, new Location(xWumpus, yWumpus))){			
			xWumpus = (int) (Math.random() * (gridSize));
			yWumpus = (int) (Math.random() * (gridSize));
		}			
			 
		map[xWumpus][yWumpus] += "W";
		occuped.add(new Location(xWumpus, yWumpus));
		
		if(xWumpus == gridSize-1 ) 			map[xWumpus-1][yWumpus] += "O";
		 else if(xWumpus == 0 ) 			map[xWumpus+1][yWumpus] += "O";
		 else if(xWumpus>0 && xWumpus < gridSize-1){
			 map[xWumpus-1][yWumpus] += "O";
			 map[xWumpus+1][yWumpus] += "O";			 
		 }
		 if(yWumpus == gridSize-1 ) 		map[xWumpus][yWumpus-1] += "O";	 
		 else if(yWumpus == 0 ) 			map[xWumpus][yWumpus+1] += "O";
		 else if (yWumpus>0 && yWumpus <gridSize-1){
			 map[xWumpus][yWumpus-1] += "O";
			 map[xWumpus][yWumpus+1] += "O";
		 }	 				
	}	
	
	private void GenerateGold(){	
		int xGold = (int) (Math.random() * (gridSize));
		int yGold = (int) (Math.random() * (gridSize));
		while(exist(occuped, new Location(xGold, yGold))){			
			xGold = (int) (Math.random() * (gridSize));
			yGold = (int) (Math.random() * (gridSize));
		}	
			 
		map[xGold][yGold] += "G";
		occuped.add(new Location(xGold, yGold));	
	
	}
 

	public void KillWumpus() {
		/*
		map[xWumpus][yWumpus] -= 2;
		//if(xPit == gridSize)
			
		if(xWumpus-1 < gridSize && xWumpus -1  >=0)
			map[xWumpus-1][yWumpus] -= 32;  // il y'a courant d'air 
		if(xWumpus + 1 < gridSize && xWumpus +1  >=0)
			map[xWumpus+1][yWumpus] -= 32;  // il y'a courant d'air X+1, Y
		if(yWumpus - 1 < gridSize && yWumpus -1  >=0)
			map[xWumpus][yWumpus-1] -= 32;
		if(yPit2 + 1 < gridSize && yPit2 +1  >=0)
			map[xWumpus][yWumpus+1] -= 32;
	*/		
		liveWumpus = false;
	}
	
	
public  void affiche(){
		
		for(int i = 0 ; i<gridSize;i++){
			for(int j = 0 ; j<gridSize;j++)
				System.out.print(map[i][j]+ "\t ");
				System.out.println();
		}		
	}

public Location getWumpusLocation(){
	return occuped.get(4);
}
	

public ArrayList<Location> getPitLocation(){
	ArrayList<Location> loc = new ArrayList<Location> ();
	for(int i=0; i<nbPit;i++)
		loc.add(occuped.get(5+i));
	return loc;
}


public Location getGoldLocation(){
	return occuped.get(occuped.size()-1);
}
private boolean exist(ArrayList<Location> a, Location loc){
	
	for (int i =0; i<a.size();i++){		
		if(a.get(i).x ==  loc.x && a.get(i).y ==  loc.y)			
			return true;	
		}
	return false;
	}
}