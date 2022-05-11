package modele;

public class Case {
    private int valeur;
    private boolean fusion;

    public Case(int _valeur) {
        valeur = _valeur;
        fusion = false;
    }

    public int getValeur() {
        return valeur;
    }
    
    public void setValeur(int valeur) {
    	this.valeur = valeur;
    }

	public boolean isFusion() {
		return fusion;
	}

	public void setFusion(boolean fusion) {
		this.fusion = fusion;
	}

}
