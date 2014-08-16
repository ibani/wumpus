package Wumpus;

public class Location {
	
	int x,y;
	public Location (int x, int y){
		this.x=x;
		this.y = y;
	}
	
	
	public boolean equals(Location x, Location y){
		return(x.x == y.x && x.y == y.y);
	}
	
	public String toString(){
		return "location = ("+x+ " ; "+ y +")";
	}
	
}
