package echec;

public class Reine extends TourReineFou {


    public Reine(Couleur couleur) {
        super(couleur);
        super.name = "Reine";
        super.symbole = 'Q';
    }

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