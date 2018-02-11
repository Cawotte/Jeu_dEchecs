package echec;

public abstract class TourReineFou extends Piece {

    public TourReineFou(Couleur couleur) {
        super(couleur);
    }

    /**
     * Utile pour Tour, Fou & Reine.
     * Sert à calculer les coup possibles en ligne et diagonales du pion en position (a,n), en fonction d'une direction définie
     * par ax et ny. ( ax = 1 : droite, ax = -1 : gauche, ny = 1 : haut, ect...
     */
    public void coupDiagonalEtLignes(int a, int n, int ax, int ny) {
        boolean obstacle=false;
        a+=ax;
        n+=ny;
        while (inBound(a, n) && !obstacle) {
            if (contenuCase(a, n) == null)
                ListeDeCoups.add(new Position(a, n));
            else {
                obstacle = true;
                if (contenuCase(a, n).getCouleur() != couleur)
                    ListeDeCoups.add(new Position(a, n));
            }
            a+=ax;
            n+=ny;
        }
    }

}
