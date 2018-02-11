package echec;


import static echec.Couleur.*; //Permet de faire reconnaitre BLANC/NOIR comme type directement

public class Tour extends TourReineFou {

    //Pour savoir si la Tour a déjà bougé, pour les roques.
    private boolean moved;

    //Constructeurs -----------
    public Tour(Couleur couleur) {
        super(couleur);
        super.name = "Tour";
        super.symbole = 'T';
        this.moved = false;
    }

    //Constructeur utilisé lors de la lecture de fichier (Lorsque moved est important)
    public Tour(Couleur couleur, Position pos) {
        super(couleur);
        super.name = "Tour";
        super.symbole = 'T';
        //On définit move en fonction de si la tour est sur une de ses positions initiales :
        if ( pos.getA() == 7 || pos.getA() == 0 && (pos.getN() == 0 && couleur == BLANC) || (pos.getN() == 7 && couleur == NOIR) )
            this.moved = false; //Elle est sur une position initiale, la tour n'a pas bougée.
        else
            this.moved = true; //Sinon elle a déjà bougé.
    }

    //Méthodes  --------------
    public void coupsPossibles(int a, int n) {
        ListeDeCoups.clear(); //On réinitialise tout

        //Ligne Nord
        coupDiagonalEtLignes(a, n, 0, 1);
        //Ligne Est
        coupDiagonalEtLignes(a, n, 1, 0);
        //Ligne Sud
        coupDiagonalEtLignes(a, n, 0, -1);
        //Ligne Ouest
        coupDiagonalEtLignes(a, n, -1, 0);

    }


    //Accesseurs --------------
    public boolean getMoved() {
        return moved;
    }
    public void setMoved(boolean bool) {
        this.moved = bool;
    }


}
