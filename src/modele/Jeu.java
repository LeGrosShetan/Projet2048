package modele;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Stream;

public class Jeu extends Observable {

    private Case[][] tabCases;
    private static Random rnd = new Random(4);
    private HashMap<Case, Position> map = new HashMap<Case, Position>();
    private int score = 0;
    private int caseMax = 0;
    private boolean aFini = false;

    public Jeu(int size) {
        tabCases = new Case[size+2][size+2];
        for (int i = 0; i < tabCases.length; i++) {
        	Case c1 = new Case(-1);
        	Case c2 = new Case(-1);

        	tabCases[i][0]  = c1;
        	tabCases[i][tabCases.length - 1]  = c2;
        	Position p1 = new Position(i,0);
        	Position p2 = new Position(i,tabCases.length - 1);
            map.put(c1, p1);
            map.put(c2, p2);
        	
        }
        for (int j = 1; j < tabCases.length - 1; j++) {
        	Case c1 = new Case(-1);
        	Case c2 = new Case(-1);

        	tabCases[0][j]  = c1;
        	tabCases[tabCases.length - 1][j]  = c2;
        	Position p1 = new Position(0,j);
        	Position p2 = new Position(tabCases.length - 1,j);
            map.put(c1, p1);
            map.put(c2, p2);
    }
        rnd();
    }

    public boolean getStatus() {
    	return aFini;
    }
    
    public int getSize() {
        return tabCases.length;
    }

    public Case getCase(int i, int j) {
        return tabCases[i][j];
    }
    
    public int getScore() {
    	return this.score;
    }
    
    public void setScore(int newScore) {
    	this.score = newScore;
    }

    public Case getVoisin(Direction d, Case c) {
    	Position p = map.get(c);
    	
    	int x = p.getX();
    	int y = p.getY();
    	
    	switch(d) {
    		case haut : y--;
    			break;
    		case bas : y++;
				break;
    		case gauche : x--;
				break;
    		case droite : x++;
				break;
    	}
    	
    	return tabCases[x][y];
    }
    
    public Position getPosVoisin(Direction d, Case c) {
    	Position p = map.get(c);
    	
    	int x = p.getX();
    	int y = p.getY();
    	
    	switch(d) {
    		case haut : y--;
    			break;
    		case bas : y++;
				break;
    		case gauche : x--;
				break;
    		case droite : x++;
				break;
    	}
    	
    	return new Position(x,y);
    }
    
    public void move(Direction d, Case c) {
    	if(c == null || c.isFusion() == true) {
    		 return;
    	}
    	if(getVoisin(d, c) != null) {
    		if(getVoisin(d, c).getValeur() == -1 || getVoisin(d, c).getValeur() != c.getValeur() || getVoisin(d, c).isFusion() == true) {
        		return;
        	}
    	}
		Position p;
		Position posC = map.get(c);
		Case voisin = getVoisin(d, c);
		if(voisin == null) {
			p = getPosVoisin(d,c);
		}else {
			p = getPosVoisin(d,c);
			//p = map.get(getVoisin(d, c));
			
    		if(voisin.getValeur() == c.getValeur()) {
    			c.setValeur(c.getValeur() + voisin.getValeur());
    			
    			setScore(getScore() + c.getValeur());
    			c.setFusion(true);
    			if(caseMax < c.getValeur()) {
                	caseMax = c.getValeur();
                }
    			testVictoire();
    		}
    		
    		map.remove(voisin);
    		voisin = null;
    		
		}
		
		tabCases[p.getX()][p.getY()] = c;
		System.out.println("la case " + posC.getX()  + ", " + posC.getY() + " de valeur " + c.getValeur()/2 +" va en " + p.getX() + ", " + p.getY());		
		
		tabCases[posC.getX()][posC.getY()] = null;
		
		map.put(c, p);
		
		System.out.println(map.get(c).getX()+ " " + map.get(c).getY());
		
		move(d,c);
    }
    
    public void move(Direction d) {
    	new Thread() { // permet de libérer le processus graphique ou de la console
			public void run() {

				Case c;
				Case newC;
				switch(d) {
	    		case haut : 
	    			for (int i = 1; i < tabCases.length - 1; i++) {
	    				for (int j = 1; j < tabCases.length - 1; j++) {
	                    
	                    	c = getCase(i,j);
							
	                    	move(d,c);
	                    }
	                }
	    			break;
	    		case bas : 
	    			for (int i = 1; i < tabCases.length - 1; i++) {
	    				for (int j = tabCases.length - 2; j >= 1; j--) {
	                    
	                    	c = getCase(i,j);
							
	                    	move(d,c);
	                    }
	                }
					break;
	    		case gauche : 
	    			for (int j = 1; j < tabCases.length - 1; j++) {
	                    for (int i = 1; i < tabCases.length - 1; i++) {
	                    	c = getCase(i,j);
							
	                    	move(d,c);
	                    }
	                }
					break;
	    		case droite : 
	    			for (int j = 1; j < tabCases.length - 1; j++) {
	                    for (int i = tabCases.length - 2; i >= 1; i--) {
	                    	c = getCase(i,j);
							
	                    	move(d,c);
	                    }
	                }
					break;
				
				}
                
				for (int i = 1; i < tabCases.length - 1; i++) {
                    for (int j = 1; j < tabCases.length - 1; j++) {
                    	c = getCase(i,j);
                    	if(c != null) {
                    		c.setFusion(false);
                    	}
                    }
				}
				ajouteCase();
                testDefaite();
                setChanged();
                notifyObservers();
            }

        }.start();
    }

    public void rnd() {
        new Thread() { // permet de libérer le processus graphique ou de la console
			public void run() {
                int r;
                for (int i = 1; i < tabCases.length - 1; i++) {
                    for (int j = 1; j < tabCases.length - 1; j++) {
                    	r = (int) (Math.random() * (3 - 0 ));
                        
                        switch (r) {
                            case 0:
                                tabCases[i][j] = null;
                                break;
                            case 1:
                            	Case c = new Case(2);
                                tabCases[i][j] = c;
                                Position p = new Position(i,j);
                                map.put(c, p);
                                if(caseMax < 2) {
                                	caseMax = 2;
                                }
                                break;
                            case 2:
                            	Case c1 = new Case(4);
                                tabCases[i][j] = c1;
                                Position p1 = new Position(i,j);
                                map.put(c1, p1);
                                if(caseMax < 4) {
                                	caseMax = 4;
                                }
                                break;
                        }
                    }
                }
                
                setChanged();
                notifyObservers();
            }

        }.start();

    }
    
    public void ajouteCase() {
    	int x,y,r;
    	do {
    		x = (int) (1 + Math.random() * (5 - 1));
    		y = (int) (1 + Math.random() * (5 - 1));
    	}while(tabCases[x][y] != null);
    	
    	r = (int) (Math.random() * (2 - 0 ));
        
        switch (r) {
            case 0:
            	Case c = new Case(2);
            	tabCases[x][y] = c;
                Position p = new Position(x,y);
                map.put(c, p);
                break;

            case 1:
            	Case c1 = new Case(4);
            	tabCases[x][y] = c1;
                Position p1 = new Position(x,y);
                map.put(c1, p1);
                break;
        }
    }

    public Boolean testDefaite() {
    	Case c;
    	int dirBloquees;
    	int nbCasesBloquees = 0;
		for (int i = 1; i < tabCases.length - 1; i++) {
			for (int j = 1; j < tabCases.length - 1; j++) {
		    
		    	c = getCase(i,j);
				
		    	if(c == null) {
		    		return false;
		    	}
		    	if(c.isFusion() != true) {
		           	 for (Direction d : Direction.values()) {
		           		 if(getVoisin(d, c) == null) {
		           			 return false;
		           		 }
		           		 if(getVoisin(d, c) != null) {
		           			 if(getVoisin(d, c).getValeur() == c.getValeur() && getVoisin(d, c).isFusion() == false) {
		           				 return false;
		           			 }
		           		 }
		           	 }
		    	}
			}
		}
    	System.out.println("t'as perdu !");
    	aFini = true;
    	sauvegarde();
    	return true;
    }
    
    public Boolean testVictoire() {
    	if(caseMax < 2048) {
    		return false;
    	}
    	System.out.println("t'as gagn� !");
    	aFini = true;
    	sauvegarde();
    	return true;
    }
    
    public void sauvegarde() {
    	String[] tabUsers = new String[4];
    	int[]    tabScore = new int[4];
    	try {
    		int indiceLigne = 1;
    		int lignesEcrites = 0;
			BufferedReader reader = new BufferedReader(new FileReader("score.txt"));
			Stream<String> lignes = reader.lines();
			List<String> listeLignes = lignes.toList();
			boolean estEntre = false;
			
			while (indiceLigne <= listeLignes.size() && lignesEcrites < 4) {
				String lineText = listeLignes.get(indiceLigne-1);
				
				if(estEntre == false && score > Integer.parseInt(lineText.split(" ")[1])) {
					System.out.println("Entrez votre nom d'utilisateur :");
					Scanner newUser = new Scanner(System.in);
					tabUsers[lignesEcrites] = newUser.nextLine();
					tabScore[lignesEcrites] = score;
					estEntre = true;
				}
				else {
					tabUsers[lignesEcrites] = lineText.split(" ")[0];
					tabScore[lignesEcrites] = Integer.parseInt(lineText.split(" ")[1]);
					
					System.out.println(tabUsers[indiceLigne-1] + " : " + tabScore[indiceLigne-1]);
					
					indiceLigne++;
				}
				lignesEcrites++;
			}
			
			if(estEntre == false && lignesEcrites < 4) {
				System.out.println("Entrez votre nom d'utilisateur :");
				Scanner newUser = new Scanner(System.in);
				tabUsers[indiceLigne-1] = newUser.nextLine();
				tabScore[indiceLigne-1] = score;
				estEntre = true;
			}
			
			reader.close();
			
			FileWriter writer = new FileWriter("score.txt");
			
			for(int i = 0; i < 4; i++) {
				if(i < tabScore.length) {
					String ligne = tabUsers[i] + " " + String.valueOf(tabScore[i]) + '\n';
					writer.write(ligne);	
				}
			}
			
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    }
