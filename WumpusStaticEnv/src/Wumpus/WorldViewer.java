package Wumpus;


import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;



public class WorldViewer {

	public WorldViewer() {

	
	
	JFrame Frame = new JFrame();
	JPanel 	WorldPanel = new JPanel(),
			ButtonPanel = new JPanel();

	JButton BLearn = new JButton("Learn"),
			BTest = new JButton("Test"),
			BGenerate = new JButton("GenerateWorl");
		
	
	ButtonPanel.add(BGenerate);
	ButtonPanel.add(BLearn);
	ButtonPanel.add(BTest);
	

	WorldPanel.setBackground(Color.GREEN);
	ButtonPanel.setBackground(Color.GRAY);
	Frame.getContentPane().setLayout(new GridLayout(1,2));
	Frame.getContentPane().add(WorldPanel);
	Frame.getContentPane().add(ButtonPanel);//,BorderLayout.ENDPAGE);



	Frame.add(WorldPanel);
	Frame.add(ButtonPanel);
	
	//BorderLayout bl = new BorderLayout();
	//ButtonPanel.add(BLearn);
	//ButtonPanel.add(BLearn);

	Frame.setVisible(true);

	Container c = new Container();
	
	
	Frame.setJMenuBar(new DemoMenu());
	Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	Frame.pack();
	Frame.setVisible(true);
	Frame.setSize(800,700);
	
	}
	
	public class DemoMenu extends JMenuBar {

		public DemoMenu() {
			super();
			initializeMenu();
		}

		private void initializeMenu() {
			JMenu menu_file = new JMenu("File");
			JMenuItem menu_gen = new JMenuItem("Generate new World");
			JMenuItem menu_learn = new JMenuItem("Learn");
			JMenuItem menu_test = new JMenuItem("Test");
			JMenuItem menu_exit = new JMenuItem("Exit");
			menu_exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					System.exit(0);
				}
			});
			menu_file.add(menu_gen);
			menu_file.add(menu_learn);
			menu_file.add(menu_test);
			menu_file.add(menu_exit);
			add(menu_file);

			JMenu menu_edit = new JMenu("Option");
			JMenuItem menu_edit_copy = new JMenuItem("Copy");
			JMenuItem menu_edit_cut = new JMenuItem("Cut");
			JMenuItem menu_edit_paste = new JMenuItem("Paste");
			menu_edit.add(menu_edit_copy);
			menu_edit.add(menu_edit_cut);
			menu_edit.add(menu_edit_paste);
			add(menu_edit);
		}


		}

	}
