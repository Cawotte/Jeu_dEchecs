package echec;

import java.util.ArrayList;


public abstract class Piece {

    //Attributs
    protected Couleur couleur;
    protected String name;
    protected char symbole;
    protected ArrayList<Position> ListeDeCoups = new ArrayList(); //Contient tout les coups possibles actuels pour la piece en question.
                                                                //Son utilisation et format varie selon la pièce.

    //Méthodes
    public Piece(Couleur coul) {
        this.couleur = coul;
    }

    /**
     * Prends en parametre les coordonnées a, n de la piece, et remplie l'ArrayList ListeDeCoups avec les positions des coups possibles de la piece.
     */
    public abstract void coupsPossibles(int a, int n);

    /**
     * Affiche dans la console tout les coups possibles de la piece sous le format AN. ( H1, D4, ect.. )
     */
    public void afficherCoupsPossibles() {
        if (listeCoupIsEmpty())
            System.out.println("Il n'y a aucun coup possible !");
        else {
            System.out.print("Liste de coups possibles : ");
            //On parcours la ListeDeCoups pour afficher tout les coups sous la forme "A1, B2, C5.. "
            for (int i = 0; i < ListeDeCoups.size(); i++) {
                System.out.print( ListeDeCoups.get(i).positionToCoordonnee() );
                System.out.print(" ");
            }
            System.out.println(); //retour chariot
        }

    }



    /**
     * Renvoie le contenu de la plateau[a][n]. Cette fonction sert surtout à rendre le code plus lisible.
     */
    public Piece contenuCase(int a, int n) {
        return Plateau.getInstance().contenuCase(a, n);
    }

    //Booléens
        //Renvoie vrai si a et n sont tout les deux des indices compris dans la taille du tableau (entre 0 et 7 inclus)
    public boolean inBound(int a, int n) {
        return (a<8 && a>=0 && n<8 && n>=0);
    }
        //Renvoie vrai si i est compris entre 0 et 7
    public boolean inBound(int i) {
        return (i<8 && i>=0);
    }
        //Renvoie vrai si plateau[a][n] est une case vide.
    public boolean caseVide(int a, int n) {
        return Plateau.getInstance().contenuCase(a, n) == null;
    }
        //Renvoie vrai si plateau[a][n] est une case avec une piece ennemie.
    public boolean caseEnnemi(int a, int n){ //Vérifier que la case n'est pas vide sinon erreur !
        return Plateau.getInstance().contenuCase(a,n).getCouleur() != couleur;
    }
        //Renvoie vrai si plateau[a][n] est une case vide ou avec une piece ennemie.
    public boolean caseVideOuEnnemi(int a, int n) {
        return caseVide(a,n) || caseEnnemi(a, n);
    }
        //Renvoie vrai si la liste des coups de la piece est vide : CàD qu'elle n'a aucun coup possible.
    public boolean listeCoupIsEmpty() {
        return ListeDeCoups.isEmpty();
    }

        //Renvoie vrai si ListeDeCoups contient des coordonnées équivalentes à pos.
    public boolean contientCoord(Position pos) {
        return ListeDeCoups.contains(pos);
    }


    //Accesseurs
    public char getSymbole() {
        return symbole;
    }
    public Couleur getCouleur() {
        return couleur;
    }
    public String getName() {
        return name;
    }
    public ArrayList getListeDeCoups() { return ListeDeCoups; }

}
