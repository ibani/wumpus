package Wumpus;

import java.awt.Component;
import java.awt.Point;

public class MyThread extends Thread implements Runnable{

	Thread runner;
	Viewer viewer;
	public MyThread(Viewer viewer){
		this.viewer=viewer;		
		runner = new Thread();
	}
	
	
	public void run(int i) throws InterruptedException{
		runner.run();
		annimate();		
	}
	
	public final void sleep(){
		
		try {
			runner.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void Clik(int i){
		Point p = new Point(viewer.gridSize-1,0);
		
		Point nex = new Point(p.x-i,p.y+ i);
		viewer.MoveAgent(nex,true);
		Point p2 = new Point(nex.x+1,p.y+1);
		viewer.initCell(p);			
	}
	void annimate() throws InterruptedException{

		Component 	newC = null,
					oldC = null;
		
		Point currentLocAgent; // position actuelle de l'agent (currentAgentMove)
		DTimer timer;
		timer= new DTimer ();
		timer.startDTimer();
	//	for(int i=0; i< planAction.size();i++){//
			currentLocAgent =  viewer.currentAgentMove.get(viewer.currentAgentMove.size()-1);
			
			System.out.println("temps="+timer.getTime());
			switch(viewer.planAction.get(viewer.i)){
			
			case 0 : viewer.currentAgentMove.add(new Point(currentLocAgent.x -1, currentLocAgent.y)); ;break;
			case 1 : viewer.currentAgentMove.add(new Point(currentLocAgent.x , currentLocAgent.y+1)); break;
			case 2 : viewer.currentAgentMove.add(new Point(currentLocAgent.x +1, currentLocAgent.y)); break;
			case 3 : viewer.currentAgentMove.add(new Point(currentLocAgent.x , currentLocAgent.y-1)); break;
			case 4|5|6|7|8 :{System.out.println("tier fleche");}
			}
			
			
			Point nextLoc =viewer.currentAgentMove.get(viewer.currentAgentMove.size()-1);
			if(nextLoc != currentLocAgent){
				/*if(map[nextLoc.x][nextLoc.y].equals(""))
					newC = new Panneau(agentA);						
				else if(map[nextLoc.x][nextLoc.y].equals("B") || map[nextLoc.x][nextLoc.y].equals("BB"))
					newC = new Panneau(agentB);
				else if(map[nextLoc.x][nextLoc.y].equals("O"))
					newC = new Panneau(agentO);	
				else if(map[nextLoc.x][nextLoc.y].equals("OB") ||  map[nextLoc.x][nextLoc.y].equals("OBB"))
					newC = new Panneau(agentBO);
				else newC = new Panneau(wumpus);
			*/
				viewer.MoveAgent(nextLoc,true);
				viewer.initCell(currentLocAgent);
				//for(int k=0; k<999999999;k++)
					//;
				viewer.i++;
				
		/*	oldC = getComponent(currentLocAgent);
			System.out.println(timer.getTime() + ""+timer.isRunning());
			//if(timer.getTime()>=2){
			System.out.println("***************************"+currentLocAgent);
			panelDroite.remove(nextLoc.x *gridSize + nextLoc.y);
			panelDroite.add(newC, nextLoc.x *gridSize + nextLoc.y );
			
			panelDroite.remove(currentLocAgent.x *gridSize + currentLocAgent.y);
			panelDroite.add(oldC, currentLocAgent.x * gridSize+ currentLocAgent.y );
			*/
				viewer.RefrechWindow();
			
			//}
			
			
			
			
			
			}
			
			
		
	//	Thread.sleep(1200);
		
	
	
		//}
	}
	}
	

