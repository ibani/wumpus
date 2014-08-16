package Wumpus;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;



class Viewer extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int gridSize = 5;
	MyThread runner;
	WumpusWorld world;           
	boolean hasArrow=true;
	int i =0;
	Point Pinit = new Point(gridSize-1,0);
	
	Wumpus Wumpus;
	Dimension Dim = new Dimension(685,550);
	ArrayList<Integer> planAction=new ArrayList<Integer>();;
	JPanel tabPanel[][] = new JPanel[gridSize][gridSize];
	private Image agentA,agent, agentO, agentB, agentBO,agentOr, wumpus, pit, brezz, oder, ob, or, orO, orB, orOB	;
	String path = "src\\images\\";
	String[][] map;	
	ArrayList<Point> currentAgentMove;
	Color color, color2;
	JFrame fenetre1;
	JPanel panelGauche;
	JPanel panelDroite;
	
	//JScrollPane jsp;
	JSplitPane splitPane,splitPane2;
	JPanel panelboutton;
	JButton generate,learn,play,can; 
	JLabel label;
	JPanel panelgen;
		
	Viewer()//String [][]map)
	{
		//this.map =map;
		world = new WumpusWorld(gridSize);
		Wumpus = new Wumpus(world);
		runner = new MyThread(this);
		color = Color.yellow;
		color2 = Color.blue;
		currentAgentMove = new ArrayList<Point>();
		currentAgentMove.add(new Point(gridSize-1, 0));
		agent  = Toolkit.getDefaultToolkit().getImage(path+"agent.jpg");
		agentA  =Toolkit.getDefaultToolkit().getImage(path+"agentA.jpg");
		agentO = Toolkit.getDefaultToolkit().getImage(path+"agentO.jpg");
		agentB = Toolkit.getDefaultToolkit().getImage(path+"agentB.jpg");
		agentBO= Toolkit.getDefaultToolkit().getImage(path+"agentBO.jpg");
		agentOr= Toolkit.getDefaultToolkit().getImage(path+"agentOr.jpg");
		wumpus = Toolkit.getDefaultToolkit().getImage(path+"wumpus.jpg");
		pit    = Toolkit.getDefaultToolkit().getImage(path+"pit.jpg");
		brezz  = Toolkit.getDefaultToolkit().getImage(path+"brezz.jpeg");
		oder   = Toolkit.getDefaultToolkit().getImage(path+"oder.jpeg");
		ob     = Toolkit.getDefaultToolkit().getImage(path+"oB.jpg");		
		or 	   = Toolkit.getDefaultToolkit().getImage(path+"trésor.jpg");
		orO    = Toolkit.getDefaultToolkit().getImage(path+"orO.jpg");
		orB    = Toolkit.getDefaultToolkit().getImage(path+"orB.jpg");
		orOB   = Toolkit.getDefaultToolkit().getImage(path+"orOB.jpg");
		
				 		
		 
		 panelGauche = new JPanel(new BorderLayout());
		 panelGauche.setBackground(new Color(255, 255, 255));
		
		 panelGauche.setBorder(BorderFactory.createCompoundBorder(
                 BorderFactory.createTitledBorder("Contents"),
                 BorderFactory.createEmptyBorder(10,10,10,10)));
		  label = new JLabel();
		 panelGauche.add(label);
		 panelboutton = new JPanel(new BorderLayout());
		 panelboutton.setSize(50, 100);
		 generate = new JButton ("<html><body><u>G</u>enerate World</body></html>");
		 generate.setBounds(200, 15, 170, 25);
		 generate.addActionListener(this);
		 panelboutton.add(generate);
		 
		 learn = new JButton ("<html><body><u>L</u>earn</body></html>");
		 learn.setBounds(385, 15, 90, 25);
		 learn.addActionListener(this);
		 panelboutton.add(learn);
		 learn.addActionListener(this);
		 
		 play = new JButton ("<html><body><u>P</u>lay</body></html>");
		 play.setBounds(480, 15, 90, 25);
		 Icon icon = new ImageIcon(path+"play2.png");
		 play.setVerticalAlignment(SwingConstants.CENTER);  // of text and icon
		 play.setHorizontalAlignment(SwingConstants.LEFT); // of text and icon
	    // button.setHorizontalTextPosition(SwingConstants.LEFT); // of text relative to icon
	    // button.setVerticalTextPosition(SwingConstants.TOP);    // of text relative to icon
		 play.setVerticalTextPosition(SwingConstants.BOTTOM);
		 play.setHorizontalTextPosition(SwingConstants.RIGHT);
	
		 play.setIcon(icon);
		  
		 
	
		 play.addActionListener(this);
		 panelboutton.add(play);
		 
		 can = new JButton ("<html><body><u>S</u>top</body></html>");
		 can.setBounds(575, 15, 90, 25);
		 can.setEnabled(true);
		 can.addActionListener(this);
		 panelboutton.add(can);
		 can = new JButton ("<html><body><u></u></body></html>");
		 can.setBounds(575, 15, 0, 25);
		 can.setEnabled(false);
		 can.addActionListener(this);
		 panelboutton.add(can);
		 
	
		 panelDroite = null;
		 splitPane =null;
		 splitPane2 =null;
		 
		 panelDroite = new JPanel();		  
		 panelDroite.setBorder(BorderFactory.createCompoundBorder
						               (BorderFactory.createTitledBorder
						                ("Wumpus World"),
						               BorderFactory.createEmptyBorder(1,1,1,1)));          
		 panelDroite.setLayout(new GridLayout(gridSize,gridSize));
			  
			  //on crÃ©Ã© le splitPane avec une separation Horizontal (barre Ã  la vertical)
		 splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panelGauche,panelDroite);
			  //Place la barre de sÃ©paration a 200 px
		 splitPane.setDividerLocation(160);
			  //Permet de placer directement a gauche ou a droite la barre grace a un clic
		  splitPane.setOneTouchExpandable(true);
		  //splitPane.res
		  
		
		 generateWorld();
		 splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,splitPane,panelboutton);
		 
	  
		 
		 
		 fenetre1 =  new JFrame("Kill The Wumpus");
		 fenetre1.setBounds(150, 150, 685, 550); 	splitPane2.setDividerLocation(fenetre1.size().height); 
		 fenetre1.add(splitPane2);
		 fenetre1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 fenetre1.setResizable(true);
		 fenetre1.setMinimumSize(new Dimension(685,550));
		 fenetre1.setVisible(true);			
		 fenetre1.addComponentListener(new ComponentAdapter() {
		      public void componentResized(ComponentEvent evt) {
		          Component c = (Component) evt.getSource();
		          splitPane2.setDividerLocation(fenetre1.size().height-100);//(450);
		          
		        }});
		
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton)e.getSource();
		
		if(b.getText().contains("enerate")){			
			world= new WumpusWorld(gridSize);
			Wumpus = new Wumpus(world);
			System.out.println();
			for(int i =0; i<gridSize; i++)
				for(int j =0; j<gridSize; j++)
					System.out.print(world.map[i][j]);			
			generateWorld();
			fenetre1.setSize(fenetre1.getWidth()-1, fenetre1.getHeight());
			fenetre1.setSize(fenetre1.getWidth()+1, fenetre1.getHeight());
			world.affiche();
			}
		else if(b.getText().contains("earn")){
			System.out.println("Or1 : " + world.getGoldLocation());
			Wumpus.Learn();
			System.out.println("Or2 : " + world.getGoldLocation());
			planAction = new ArrayList<Integer>();
		}
		else if(b.getText().contains("lay")){
			
			Wumpus.Test();
			label.setSize(100, 500);
			planAction= Wumpus.planActionInt;
			String action ="", action2="<html><body>";
			for(int i=0;i<planAction.size();i++){
				switch(planAction.get(i)){
				case 0: action = "En Haut"; action2 += action+"<br>"; break;
				case 1: action="A Droite"; action2 += action+"<br>";break;
				case 2: action="En Bas"; action2 += action+"<br>";break;
				case 3: action="A Gauche"; action2 += action+"<br>";break;
				case 4: action="Flèche Haut"; action2 += action+"<br>";break;
				case 5: action="Flèche Droite"; action2 += action+"<br>";break;
				case 6: action="Flèche Bas"; action2 += action+"<br>";break;
				case 7: action="Flèche Gauche"; action2 += action+"<br>";break;
				case 8: action="Ramasser Or"; action2 += action+"<br>";break;
				
				default: System.out.println("Erreur Or"); break;
				
				}
				
				System.out.println(action);
				label.setText(action2+"</body></html>");
				
				
				
			}
			
		
		
		}
		
			  
			   
		
		else if(b.getText().contains("top")){
		Point p= new Point(gridSize-1,0);;
		
		for (int i=0; i<4;i++){		 
		
		Point nex = new Point(p.x-i,p.y+ i);
		MoveAgent(nex,true);
		Point p2 = new Point(nex.x+1,p.y+1);
		initCell(p);
		}
		
		
			
			/*
			Point suivant = Pinit;
			//System.out.println(planAction.size());
			//System.out.println("i           ="+i);
			
			int act = planAction.get(i);
			switch(act){
			case 0: suivant.x = Pinit.x-1; break;
			case 1: suivant.y = Pinit.y+1; break;
			case 2: suivant.x = Pinit.x+1; break;
			case 3: suivant.y = Pinit.y-1; break;
			default: suivant = Pinit; hasArrow = false;
			}
			//System.out.println(Pinit+ "  :"+ suivant);
			//if(act ==0 || act ==1 || act ==2|| act ==3){
			initCell(Pinit);	
			MoveAgent(suivant,hasArrow);				
			//initCell(Pinit);
			
			i++;
			Pinit = suivant;
			}
		
				fenetre1. repaint();
				panelGauche.repaint();System.out.println(System.currentTimeMillis());
				
				*/
			}
	
	/*
		Point currentLocAgent = new Point(4,0);
		Point nextLoc = new Point(3,0);
		
		Component oldC = getComponent(currentLocAgent);
		Component newC = new Panneau(agent);
		
		
		panelDroite.remove(nextLoc.x *gridSize + nextLoc.y);
		panelDroite.add(newC, nextLoc.x *gridSize + nextLoc.y );
		JPanel pn =new JPanel();
		pn.setBackground(Color.black);
	 	panelDroite.remove((currentLocAgent.x )*gridSize + currentLocAgent.y);
	 	System.out.println(currentLocAgent);
		panelDroite.add(oldC, currentLocAgent.x *gridSize + currentLocAgent.y );
		}
		fenetre1.setSize(fenetre1.getWidth()+1, fenetre1.getHeight());
		fenetre1.setSize(fenetre1.getWidth()-1, fenetre1.getHeight());
		panelDroite.repaint();
		}*/
		

	private void Clik(int i){
		Point p = new Point(gridSize-1,0);
		
		Point nex = new Point(p.x-i,p.y+ i);
		MoveAgent(nex,true);
		Point p2 = new Point(nex.x+1,p.y+1);
			initCell(p);			
	}

	public void generateWorld(){
		world = new WumpusWorld(gridSize);
		Wumpus = new Wumpus(world);
		map =WumpusWorld.map;
		world.affiche();
		panelDroite.removeAll();
		JComponent c = null ;
		for(int i =0; i<gridSize; i++)
			for(int j =0; j<gridSize; j++){
	
				if(map[i][j].contains("W") )					
					c = new Panneau(wumpus);
				
				else if(map[i][j].equals("A"))
					c = new Panneau(agentA);
				else if(map[i][j].equals("AB"))
					c = new Panneau(agentB);
				else if(map[i][j].equals("AO"))
					c = new Panneau(agentO);
				
				else if(map[i][j].contains("P"))
					c = new Panneau(pit);					
				
				else if(map[i][j].equals("B")|| map[i][j].equals("BB"))
					c = new Panneau(brezz);	
				else if(map[i][j].equals("OB")||map[i][j].equals("OBB"))
					c = new Panneau(ob);	
				
				else if(map[i][j].equals("O"))
					c = new Panneau(oder);			
								
				else if(map[i][j].equals("G"))
					c = new Panneau(or);		
				else if(map[i][j].equals("OG"))
					c = new Panneau(orO);
				else if(map[i][j].equals("BG") ||map[i][j].equals("BBG")  )
					c = new Panneau(orB);
				else if(map[i][j].equals("OBG"))
					c = new Panneau(orOB);				
				
				else{
					c = new JPanel();
					c.setBackground(color);
					((JComponent) c).setBorder(new BevelBorder(BevelBorder.RAISED));
				}
			
				panelDroite.add(c);			
				//panelDroite.add(c,i*gridSize+j);
				 
			}
		 panelDroite.repaint();
		 c.repaint();
		 panelDroite.repaint();
		  validate();
		  repaint();
	}
	
	void initCell(Point p){
	
		JComponent c = null ;
		int x = p.x,
			y= p.y;
		System.out.println(x+","+ y);
			if(map[x][y].contains("W") )					
				c = new Panneau(wumpus);		
		
			else if(map[x][y].equals("AB"))
				c = new Panneau(agentB);
			else if(map[x][y].equals("AO"))
				c = new Panneau(agentO);
			
			else if(map[x][y].contains("P"))
				c = new Panneau(pit);					
			
			else if(map[x][y].equals("B")|| map[x][y].equals("BB"))
				c = new Panneau(brezz);	
			else if(map[x][y].equals("OB")||map[x][y].equals("OBB"))
				c = new Panneau(ob);	
			
			else if(map[x][y].equals("O"))
				c = new Panneau(oder);			
							
			else if(map[x][y].equals("G"))
				c = new Panneau(or);		
			else if(map[x][y].equals("OG"))
				c = new Panneau(orO);
			else if(map[x][y].equals("BG") ||map[x][y].equals("BBG")  )
				c = new Panneau(orB);
			else if(map[x][y].equals("OBG"))
				c = new Panneau(orOB);				
			
			else if(map[x][y].equals("A")){
				
					c = new JPanel();
					c.setBackground(color);
					((JComponent) c).setBorder(new BevelBorder(BevelBorder.RAISED));
				}					
			else{
				c = new JPanel();
				c.setBackground(color);
				((JComponent) c).setBorder(new BevelBorder(BevelBorder.RAISED));
			}
			
			panelDroite.remove(x*gridSize+y);
			panelDroite.add(c,x*gridSize+y);			
			RefrechWindow();
	}
	
	void MoveAgent(Point p, boolean hasArrow){   //p: point to move it!!
		
		JComponent c = null ;
		int x = p.x,
			y= p.y;
	
			if(map[x][y].contains("W") )					
				{c = new Panneau(wumpus);
				System.out.println("Agent kiled by Wumpus");
				}
			else if(map[x][y].contains("") )					
				c = new Panneau(agent);
			
			
		///	else if(map[x][y].equals("AB"))
		//		c = new Panneau(agentB);
		//	else if(map[x][y].equals("AO"))
		//		c = new Panneau(agentO);
			
			else if(map[x][y].contains("P"))
			{		c = new Panneau(pit);
				System.out.println("Agent fall down in Pit");
			}
			else if(map[x][y].equals("B")|| map[x][y].equals("BB"))
				c = new Panneau(agentB);	
			else if(map[x][y].equals("OB")||map[x][y].equals("OBB"))
				c = new Panneau(agentBO);	
			
			else if(map[x][y].equals("O"))
				c = new Panneau(agentO);			
							
			else if(map[x][y].equals("G"))
				c = new Panneau(agentOr);		
			else if(map[x][y].equals("OG"))
				c = new Panneau(agentOr);
			else if(map[x][y].equals("BG") ||map[x][y].equals("BBG")  )
				c = new Panneau(agentOr);
			else if(map[x][y].equals("OBG"))
				c = new Panneau(agentOr);				
			
			else{
				c = new Panneau(wumpus);
				
			}
		
			
			panelDroite.remove(x*gridSize+y);
			panelDroite.add(c,x*gridSize+y);			
			RefrechWindow();
	}
		

	
	void annimate() throws InterruptedException{
		Component 	newC = null,
					oldC = null;
		
		Point currentLocAgent; // position actuelle de l'agent (currentAgentMove)
		DTimer timer;
		timer= new DTimer ();
		timer.startDTimer();
	//	for(int i=0; i< planAction.size();i++){//
			currentLocAgent =  currentAgentMove.get(currentAgentMove.size()-1);
			
			System.out.println("temps="+timer.getTime());
			switch(planAction.get(i)){
			/*case 0|4 : {				// aller vers le haut
			//	if(map[currentLocAgent.x -1][currentLocAgent.y]==""){  // prochaine case en haut vide
				//	newC = new Panneau(agentA);
					currentAgentMove.add(new Point(currentLocAgent.x -1, currentLocAgent.y));
					//	}
				else if(map[currentLocAgent.x -1][currentLocAgent.y]=="B")
					{newC = new Panneau(agentB);currentAgentMove.add(new Point(currentLocAgent.x -1, currentLocAgent.y));}
				else if(map[currentLocAgent.x -1][currentLocAgent.y]=="O")
				{newC = new Panneau(agentO);currentAgentMove.add(new Point(currentLocAgent.x -1, currentLocAgent.y));}
				else if(map[currentLocAgent.x -1][currentLocAgent.y].contains("BO"))
				{newC = new Panneau(agentBO);currentAgentMove.add(new Point(currentLocAgent.x -1, currentLocAgent.y));}	
			
				if(currentLocAgent == new Point(0,0)){
					JPanel p = new JPanel();
					p.setBackground(color);
					oldC = p;
				}
				else 
					oldC = panelDroite.getComponent(currentLocAgent.x*gridSize + currentLocAgent.y);			
				
				panelDroite.remove((currentLocAgent.x -1)*gridSize + currentLocAgent.y);
				panelDroite.add(newC , (currentLocAgent.x -1) *gridSize + currentLocAgent.y);
				
				panelDroite.remove((currentLocAgent.x )*gridSize + currentLocAgent.y);
				panelDroite.add(oldC , currentLocAgent.x *gridSize + currentLocAgent.y);
				
				break;
			}
			case 1|5 : {				// aller vers la droite
				if(map[currentLocAgent.x ][currentLocAgent.y+1]==""){  // prochaine case a droite
					newC = new Panneau(agentA);
					currentAgentMove.add(new Point(currentLocAgent.x , currentLocAgent.y+1));
					}
				else if(map[currentLocAgent.x ][currentLocAgent.y+1]=="B")
					{newC = new Panneau(agentB);currentAgentMove.add(new Point(currentLocAgent.x , currentLocAgent.y+1));}
				else if(map[currentLocAgent.x ][currentLocAgent.y+1]=="O")
				{newC = new Panneau(agentO);currentAgentMove.add(new Point(currentLocAgent.x , currentLocAgent.y+1));}
				else if(map[currentLocAgent.x ][currentLocAgent.y+1].contains("BO"))
				{newC = new Panneau(agentBO);currentAgentMove.add(new Point(currentLocAgent.x , currentLocAgent.y+1));}	
			
				if(currentLocAgent == new Point(0,0)){
					JPanel p = new JPanel();
					p.setBackground(color);
					oldC = p;
				}
				else
					oldC = panelDroite.getComponent(currentLocAgent.x*gridSize + currentLocAgent.y);			
				
				panelDroite.remove((currentLocAgent.x )*gridSize + currentLocAgent.y+1);
				panelDroite.add(newC , (currentLocAgent.x ) *gridSize + currentLocAgent.y+1);
				
				panelDroite.remove((currentLocAgent.x )*gridSize + currentLocAgent.y);
				panelDroite.add(oldC , currentLocAgent.x *gridSize + currentLocAgent.y);
				
				break;
			}
			case 2|6 : {				// aller vers le bas
				if(map[currentLocAgent.x +1][currentLocAgent.y]==""){  // prochaine case
					newC = new Panneau(agentA);
					currentAgentMove.add(new Point(currentLocAgent.x +1, currentLocAgent.y));
					}
				else if(map[currentLocAgent.x +1][currentLocAgent.y]=="B")
					{newC = new Panneau(agentB);currentAgentMove.add(new Point(currentLocAgent.x +1, currentLocAgent.y));}
				else if(map[currentLocAgent.x +1][currentLocAgent.y]=="O")
				{newC = new Panneau(agentO);currentAgentMove.add(new Point(currentLocAgent.x +1, currentLocAgent.y));}
				else if(map[currentLocAgent.x +1][currentLocAgent.y].contains("BO"))
				{newC = new Panneau(agentBO);currentAgentMove.add(new Point(currentLocAgent.x +1, currentLocAgent.y));}	
			
				if(currentLocAgent == new Point(0,0)){
					JPanel p = new JPanel();
					p.setBackground(color);
					oldC = p;
				}
				else 
					oldC = panelDroite.getComponent(currentLocAgent.x*gridSize + currentLocAgent.y);			
				
				panelDroite.remove((currentLocAgent.x +1)*gridSize + currentLocAgent.y);
				panelDroite.add(newC , (currentLocAgent.x +1) *gridSize + currentLocAgent.y);
				
				panelDroite.remove((currentLocAgent.x )*gridSize + currentLocAgent.y);
				panelDroite.add(oldC , currentLocAgent.x *gridSize + currentLocAgent.y);
				
				break;
			}
			case 3|7 : {				// aller vers la gauche
				if(map[currentLocAgent.x ][currentLocAgent.y-1]==""){  // prochaine case a droite
					newC = new Panneau(agentA);
					currentAgentMove.add(new Point(currentLocAgent.x , currentLocAgent.y-1));
					}
				else if(map[currentLocAgent.x ][currentLocAgent.y-1]=="B")
					{newC = new Panneau(agentB);currentAgentMove.add(new Point(currentLocAgent.x , currentLocAgent.y-1));}
				else if(map[currentLocAgent.x ][currentLocAgent.y-1]=="O")
				{newC = new Panneau(agentO);currentAgentMove.add(new Point(currentLocAgent.x , currentLocAgent.y-1));}
				else if(map[currentLocAgent.x ][currentLocAgent.y-1].contains("BO"))
				{newC = new Panneau(agentBO);currentAgentMove.add(new Point(currentLocAgent.x, currentLocAgent.y-1));}	
			
				if(currentLocAgent == new Point(0,0)){
					JPanel p = new JPanel();
					p.setBackground(color);
					oldC = p;
				}
				else
					oldC = panelDroite.getComponent(currentLocAgent.x*gridSize + currentLocAgent.y);			
				
				panelDroite.remove((currentLocAgent.x )*gridSize + currentLocAgent.y-1);
				panelDroite.add(newC , (currentLocAgent.x ) *gridSize + currentLocAgent.y-1);
				
				panelDroite.remove((currentLocAgent.x )*gridSize + currentLocAgent.y);
				panelDroite.add(oldC , currentLocAgent.x *gridSize + currentLocAgent.y);
				
				break;
			}
		//default : {}
		}*/
			
			case 0 : currentAgentMove.add(new Point(currentLocAgent.x -1, currentLocAgent.y)); ;break;
			case 1 : currentAgentMove.add(new Point(currentLocAgent.x , currentLocAgent.y+1)); break;
			case 2 : currentAgentMove.add(new Point(currentLocAgent.x +1, currentLocAgent.y)); break;
			case 3 : currentAgentMove.add(new Point(currentLocAgent.x , currentLocAgent.y-1)); break;
			case 4|5|6|7|8 :{System.out.println("tier fleche");}
			}
			
			
			Point nextLoc = currentAgentMove.get(currentAgentMove.size()-1);
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
				MoveAgent(nextLoc,true);
				initCell(currentLocAgent);
				//for(int k=0; k<999999999;k++)
					//;
				i++;
				
		/*	oldC = getComponent(currentLocAgent);
			System.out.println(timer.getTime() + ""+timer.isRunning());
			//if(timer.getTime()>=2){
			System.out.println("***************************"+currentLocAgent);
			panelDroite.remove(nextLoc.x *gridSize + nextLoc.y);
			panelDroite.add(newC, nextLoc.x *gridSize + nextLoc.y );
			
			panelDroite.remove(currentLocAgent.x *gridSize + currentLocAgent.y);
			panelDroite.add(oldC, currentLocAgent.x * gridSize+ currentLocAgent.y );
			*/
			RefrechWindow();
			
			//}
			
			
			
			
			
			}
			
			
		
	//	Thread.sleep(1200);
		
	
	
		//}
	}
	
	JComponent getComponent(Point p){
		JComponent c = null;
		if( p.x==gridSize-1 && p.y ==0 ){
			c = new JPanel();
			c.setBackground(color);
			((JComponent) c).setBorder(new BevelBorder(BevelBorder.RAISED));
		}		
		if(map[p.x][p.y].equals("")){
			c = new JPanel();
			c.setBackground(color);
			c.setBorder(new BevelBorder(BevelBorder.RAISED));				
			}
		else if(map[p.x][p.y].equals("B")|| map[p.x][p.y].equals("BB"))
			c = new Panneau(brezz);	
		else if(map[p.x][p.y].equals("OB")||map[p.x][p.y].equals("OBB"))
			c = new Panneau(ob);	
		
		else if(map[p.x][p.y].equals("O"))
			c = new Panneau(oder);			
						
		else if(map[p.x][p.y].equals("G"))
			c = new Panneau(or);		
		else if(map[p.x][p.y].equals("OG"))
			c = new Panneau(orO);
		else if(map[p.x][p.y].equals("BG") ||map[p.x][p.y].equals("BBG")  )
			c = new Panneau(orB);
		else if(map[p.x][p.y].equals("OBG"))
			c = new Panneau(orOB);
		else c = new JPanel();
		
		return c;
	}
	
	public class Panneau extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Image img;
		
		public Panneau(Image img)
		{
			this.img=img;	
		}
		  public void paintComponent(Graphics g){		   
			  g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);                
		  }	
		  
		  public void setImage(Image img){//, Graphics g){
			  this.img=img;
			  Graphics g = this.getGraphics();
			  paintComponent( g);//g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);    
			  revalidate();
			  repaint();
		  }		  
		}
	void RefrechWindow(){
		fenetre1.setSize(fenetre1.getWidth()-1, fenetre1.getHeight());
		fenetre1.setSize(fenetre1.getWidth()+1, fenetre1.getHeight());
		panelDroite.repaint();
			
	}
	
	public class Annimate extends Thread {

		   Annimate ( ) {
		     //....initialisations éventuelles du constructeur de MaClasse
		     this .start( ) ; 
		   }
		   public void run ( ) {
		   //....actions du thread secondaire ici
		   for ( int i1=1; i1<100; i1++)
		            System.out.println(">>> i1 = "+i1);
		   }
		}
	

	static class PieIcon extends JButton{
		    Image icon;
		
		    public PieIcon(Image img) {
		      icon = img;
		    }
		
		    public int getIconWidth() {
		      return 20;
		    }
		
		    public int getIconHeight() {
		      return 20;
		    }
		
		    public void paintComponent(Graphics g){
		    	g.drawImage(icon, 0,0,  this.getWidth(), this.getHeight(), this);          	    	
		    }
	}
		  
			
	
	
}
