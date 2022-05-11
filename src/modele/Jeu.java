package modele;

import java.util.HashMap;
import java.util.Observable;
import java.util.Random;

public class Jeu extends Observable {

    private Case[][] tabCases;
    private static Random rnd = new Random(4);
    private HashMap<Case, Position> map = new HashMap<Case, Position>();

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

    public int getSize() {
        return tabCases.length;
    }

    public Case getCase(int i, int j) {
        return tabCases[i][j];
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
    		if(getVoisin(d, c).getValeur() == -1 || getVoisin(d, c).getValeur() != c.getValeur() || c.isFusion() == true) {
        		return;
        	}
    	}
		Position p;
		Position posC = map.get(c);
		Case voisin = getVoisin(d, c);
		if(voisin == null) {
			p = getPosVoisin(d,c);
		}else {
			p = map.get(getVoisin(d, c));
			
    		if(voisin.getValeur() == c.getValeur()) {
    			c.setValeur(c.getValeur() + voisin.getValeur());
    			c.setFusion(true);
    		}
    		
    		map.remove(voisin);
    		voisin = null;
    		
		}
		
		tabCases[p.getX()][p.getY()] = c;
		System.out.println("la case " + posC.getX()  + ", " + posC.getY() + " de valeur " + c.getValeur()/2 +" va en " + p.getX() + ", " + p.getY());		
		
		tabCases[posC.getX()][posC.getY()] = null;
		
		posC = p;
		
		move(d,c);
    }
    
    public void move(Direction d) {
    	new Thread() { // permet de libérer le processus graphique ou de la console
			public void run() {

				Case c;
				
				switch(d) {
	    		case haut : 
	    			for (int i = 1; i < tabCases.length - 1; i++) {
	                    for (int j = 1; j < tabCases.length - 1; j++) {
	                    	c = getCase(j,i);
							
	                    	move(d,c);
	                    }
	                }
	    			break;
	    		case bas : 
	    			for (int i = tabCases.length - 2; i >= 1; i--) {
	                    for (int j = 1; j < tabCases.length - 1; j++) {
	                    	c = getCase(j,i);
							
	                    	move(d,c);
	                    }
	                }
					break;
	    		case gauche : 
	    			for (int i = 1; i < tabCases.length - 1; i++) {
	                    for (int j = 1; j < tabCases.length - 1; j++) {
	                    	c = getCase(i,j);
							
	                    	move(d,c);
	                    }
	                }
					break;
	    		case droite : 
	    			for (int i = 1; i < tabCases.length - 1; i++) {
	                    for (int j = tabCases.length - 2; j >= 1; j--) {
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
                        r = rnd.nextInt(3);
                        
                        switch (r) {
                            case 0:
                                tabCases[i][j] = null;
                                break;
                            case 1:
                            	Case c = new Case(2);
                                tabCases[i][j] = c;
                                Position p = new Position(i,j);
                                map.put(c, p);
                                break;
                            case 2:
                            	Case c1 = new Case(4);
                                tabCases[i][j] = c1;
                                Position p1 = new Position(i,j);
                                map.put(c1, p1);
                                break;
                        }
                    }
                }
                
                setChanged();
                notifyObservers();
            }

        }.start();

    }

}
