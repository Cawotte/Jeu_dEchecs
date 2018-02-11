package echec;

public class Fou extends TourReineFou {

    public Fou(Couleur couleur) {
        super(couleur);
        super.name = "Fou";
        super.symbole = 'F';
    }

    public void coupsPossibles(int a, int n) {

        ListeDeCoups.clear(); //On réinitialise tout
        //On parcours chaque diagonale en partant du Fou, on ajoute les case vide à la liste des coups, et
        //on s'arrête quand on rencontre un obstacle.

        //Diagonale NO
        coupDiagonalEtLignes(a, n, -1, 1);
        //Diagonale NE
        coupDiagonalEtLignes(a, n, 1, 1);
        //Diagonale SE
        coupDiagonalEtLignes(a, n, 1, -1);
        //Diagonale NE
        coupDiagonalEtLignes(a, n, -1, -1);


    }

}
