package echec;

import static echec.Couleur.*; //Permet de faire reconnaitre BLANC/NOIR comme type directement

public class Pion extends Piece {



    //Constructeurs  --------------
    public Pion(Couleur couleur) {
        super(couleur);
        super.name = "Pion";
        super.symbole = 'P';
    }



    //Méthodes  --------------
    public void coupsPossibles(int a, int n) {
        int m;
        ListeDeCoups.clear();

        //Permet de généraliser une partie du code. La seule différence entre les camps noir et blanc et la direction dans laquelle les pions avancent
        //, en associant cet increment à une variable égale à -1/+1 on divise par deux la quantité de code suivant.
        if (couleur == BLANC) m = 1;
        else m = -1;

        //On peut jouer sur la case devant si elle est vide.
        if (inBound(n+m) && caseVide(a, n+m) )
            ListeDeCoups.add(new Position(a, n+m));
            //On peut jouer sur les cases en diagonales devant si la case est sur le plateau, qu'elle n'est pas vide, et que la couleur de la piece est differente. :
        if (inBound(a-1,n+m) && !caseVide(a-1, n+m) && caseEnnemi(a-1, n+m) )
            ListeDeCoups.add(new Position(a-1, n+m));
        if (inBound(a+1,n+m) && !caseVide(a+1, n+m) && caseEnnemi(a+1, n+m) )
            ListeDeCoups.add(new Position(a+1, n+m));

        //On vérifie si le pion peut avancer de deux cases, si oui, on ajoute le coup
        /*
        if (!aSprinte && caseVide(a, n+(2*m)) )
            ListeDeCoups.add(new Position(a, n+(2*m)));*/
        //On vérifie si le pion peut avancer de deux cases, pour ça on a juste à vérifier si il sont encore sur leur ligne de
        //départ et si la case deux pas en face est libre.
        if ( ((couleur == BLANC && n == 1) || (couleur == NOIR && n == 6)) && caseVide(a,n+m) && caseVide(a, n+(2*m)) )
            ListeDeCoups.add(new Position(a, n+(2*m)));

        //On vérifie si le pion peut prendre 'En Passant' un pion.
        //C'est possible si il y a une autre piece à côté de lui qui est un pion de la couleur adverse et qui viens de aSprinte (aSprinte ==1)
        if ( enPassantPossible(a-1, n) )
            ListeDeCoups.add(new Position(a-1, n+m)); //En passant gauche
        if ( enPassantPossible(a+1, n) )
            ListeDeCoups.add(new Position(a+1, n+m)); //En passant droite

    }

    //Booléens

    /**
     * Vérifie si le pion en case [a][n] peut être pris en passant par ce pion (this).
     */
    public boolean enPassantPossible(int a, int n) {
        Position positionPionSensibleEnPassant = Plateau.getInstance().getPosImportantes("EnPassant");

        return inBound(a, n) && positionPionSensibleEnPassant != null && contenuCase(a, n) instanceof Pion && contenuCase(a,n).getCouleur() != this.couleur &&
                positionPionSensibleEnPassant.equals(new Position(a,n));
    }

}
