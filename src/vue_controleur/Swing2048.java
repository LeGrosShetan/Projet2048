package vue_controleur;

import modele.Case;
import modele.Direction;
import modele.Jeu;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class Swing2048 extends JFrame implements Observer {
    private static final int PIXEL_PER_SQUARE = 60;
    // tableau de cases : i, j -> case graphique
    private JLabel score,texte,vicdef;
    private JPanel[][] tabC;
    private JLabel[][] tabV;
    private Jeu jeu;
    private boolean flag = false;


    public Swing2048(Jeu _jeu) {
        jeu = _jeu;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(jeu.getSize() * PIXEL_PER_SQUARE, jeu.getSize() * PIXEL_PER_SQUARE);
        tabC = new JPanel[jeu.getSize()][jeu.getSize()];
        tabV = new JLabel[jeu.getSize()][jeu.getSize()];


        JPanel contentPane = new JPanel(new GridLayout(jeu.getSize()-1,jeu.getSize()-2));

        for (int i = 1; i < jeu.getSize()-1; i++) {
            for (int j = 1; j < jeu.getSize()-1; j++) {
                Border border = BorderFactory.createLineBorder(Color.darkGray, 5);
                tabC[i][j] = new JPanel();
                tabV[i][j] = new JLabel();
                
                
                tabV[i][j].setForeground(Color.black);
                tabC[i][j].setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                tabC[i][j].setBorder(border);
                
                tabC[i][j].add(tabV[i][j], gbc);

                contentPane.add(tabC[i][j]);

            }
        }
        
        texte = new JLabel("score :");
        score = new JLabel("0");
        vicdef = new JLabel("");
        
        contentPane.add(texte);
        contentPane.add(score);
        contentPane.add(vicdef);
        
        
        setContentPane(contentPane);
        
        if(!flag) {
            ajouterEcouteurClavier();
            flag = true;
        }
        
        rafraichir();

    }




    /**
     * Correspond à la fonctionnalité de Vue : affiche les données du modèle
     */
    private void rafraichir()  {

        SwingUtilities.invokeLater(new Runnable() { // demande au processus graphique de réaliser le traitement
            @Override
            public void run() {
                for (int i = 1; i < jeu.getSize()-1; i++) {
                    for (int j = 1; j < jeu.getSize()-1; j++) {
                        Case c = jeu.getCase(i, j);

                        if (c == null) {

                            tabV[i][j].setText("");

                        } else {
                        	
                            tabV[i][j].setText(c.getValeur() + "");
                        }


                    }
                }
                colorize();
                System.out.println(jeu.getScore());
                String scr = String.valueOf(jeu.getScore());
                score.setText(scr);

                String vcdf = "en cours ...";
                if(jeu.testVictoire()) {
                	vcdf = "Gagn� !";
                }
                else if(jeu.testDefaite()) {
                	vcdf = "Perdu !";
                }
                vicdef.setText(vcdf);
            }
            
        });


    }
    
    private void colorize()  {

        SwingUtilities.invokeLater(new Runnable() { // demande au processus graphique de réaliser le traitement
            @Override
            public void run() {
                for (int i = 1; i < jeu.getSize()-1; i++) {
                    for (int j = 1; j < jeu.getSize()-1; j++) {
                        Case c = jeu.getCase(i, j);

                        if (c != null) {
                        	switch(c.getValeur()) {
                        	case 2:
                        		tabC[i][j].setBackground(new Color(238, 228, 218));
                        		break;
                        	case 4:
                        		tabC[i][j].setBackground(new Color(237, 224, 200));
                        		break;
                        	case 8:
                        		tabC[i][j].setBackground(new Color(242, 177, 121));
                        		break;
                        	case 16:
                        		tabC[i][j].setBackground(new Color(245, 149, 99));
                        		break;
                        	case 32:
                        		tabC[i][j].setBackground(new Color(246, 124, 95));
                        		break;
                        	case 64:
                        		tabC[i][j].setBackground(new Color(246, 94, 59));
                        		break;
                        	case 128:
                        		tabC[i][j].setBackground(new Color(237, 207, 114));
                        		break;
                        	case 256:
                        		tabC[i][j].setBackground(new Color(237, 204, 97));
                        		break;
                        	case 512:
                        		tabC[i][j].setBackground(new Color(237, 200, 80));
                        		break;
                        	case 1024:
                        		tabC[i][j].setBackground(new Color(237, 197, 63));
                        		break;
                        	case 2048:
                        		tabC[i][j].setBackground(new Color(237, 194, 46));
                        		break;
                        	}
                        }else {
                        	tabC[i][j].setBackground(new Color(204, 192, 179));
                        }
                    }
                }
            }
        });


    }

    /**
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements, et déclenche des traitements sur le modèle
     */
    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
            	if(jeu.getStatus() == false) {
            		switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
	                    case KeyEvent.VK_LEFT : jeu.move(Direction.haut); break;
	                    case KeyEvent.VK_RIGHT : jeu.move(Direction.bas); break;
	                    case KeyEvent.VK_DOWN : jeu.move(Direction.droite); break;
	                    case KeyEvent.VK_UP : jeu.move(Direction.gauche); break;
	                }
            	}
            }
        });
    }


    @Override
    public void update(Observable o, Object arg) {
        rafraichir();
    }
}