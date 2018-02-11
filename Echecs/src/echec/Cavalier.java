package echec;

public class Cavalier extends Piece {

    public Cavalier(Couleur couleur) {
        super(couleur);
        super.name = "Cavalier";
        super.symbole = 'C';
    }

    public void coupsPossibles(int a, int n) {
        ListeDeCoups.clear();
        //Pour chaque case "en L" par rapport au Cavalier, on vérifie si elle est vide ou avec une piece adverse.

        //On vérifie que pour chaque case autour du roi si elle est libre où a un ennemi.
        //T Nord
        if ( inBound(a+1,n+2) && caseVideOuEnnemi(a+1,n+2) )
            ListeDeCoups.add(new Position(a+1, n+2));
        if ( inBound(a-1,n+2) && caseVideOuEnnemi(a-1,n+2) )
            ListeDeCoups.add(new Position(a-1, n+2));

        //T Est
        if ( inBound(a+2,n+1) && caseVideOuEnnemi(a+2,n+1) )
            ListeDeCoups.add(new Position(a+2, n+1));
        if ( inBound(a+2,n-1) && caseVideOuEnnemi(a+2,n-1) )
            ListeDeCoups.add(new Position(a+2, n-1));

        //T SUD
        if ( inBound(a+1,n-2) && caseVideOuEnnemi(a+1,n-2) )
            ListeDeCoups.add(new Position(a+1, n-2));
        if ( inBound(a-1,n-2) && caseVideOuEnnemi(a-1,n-2) )
            ListeDeCoups.add(new Position(a-1, n-2));

        //T OUEST
        if ( inBound(a-2,n+1) && caseVideOuEnnemi(a-2,n+1) )
            ListeDeCoups.add(new Position(a-2, n+1));
        if ( inBound(a-2,n-1) && caseVideOuEnnemi(a-2,n-1) )
            ListeDeCoups.add(new Position(a-2, n-1));

    }
}
